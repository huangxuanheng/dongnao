package edu.dongnao.rental.house.provider.service.search;

import static edu.dongnao.rental.house.provider.config.KafkaConfig.INDEX_TOPIC;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

import org.apache.dubbo.config.annotation.Service;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeAction;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeRequestBuilder;
import org.elasticsearch.action.admin.indices.analyze.AnalyzeResponse;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.geo.GeoPoint;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.VersionType;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.DeleteByQueryRequestBuilder;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.AggregationBuilder;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.elasticsearch.search.sort.SortOrder;
import org.elasticsearch.search.suggest.Suggest;
import org.elasticsearch.search.suggest.SuggestBuilder;
import org.elasticsearch.search.suggest.SuggestBuilders;
import org.elasticsearch.search.suggest.completion.CompletionSuggestion;
import org.elasticsearch.search.suggest.completion.CompletionSuggestionBuilder;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.primitives.Longs;

import edu.dongnao.rental.house.api.IAddressService;
import edu.dongnao.rental.house.api.ISearchService;
import edu.dongnao.rental.house.domain.AreaLevel;
import edu.dongnao.rental.house.domain.BaiduMapLocation;
import edu.dongnao.rental.house.domain.HouseBucketDTO;
import edu.dongnao.rental.house.domain.HouseHighlightDTO;
import edu.dongnao.rental.house.domain.HouseSort;
import edu.dongnao.rental.house.form.MapSearch;
import edu.dongnao.rental.house.form.RentSearch;
import edu.dongnao.rental.house.provider.entity.House;
import edu.dongnao.rental.house.provider.entity.HouseDetail;
import edu.dongnao.rental.house.provider.entity.HouseTag;
import edu.dongnao.rental.house.provider.entity.SupportAddress;
import edu.dongnao.rental.house.provider.repository.HouseDetailRepository;
import edu.dongnao.rental.house.provider.repository.HouseRepository;
import edu.dongnao.rental.house.provider.repository.HouseTagRepository;
import edu.dongnao.rental.house.provider.repository.SupportAddressRepository;
import edu.dongnao.rental.lang.ServiceMultiResult;
import edu.dongnao.rental.lang.ServiceResult;

/**
 * ElasticSearch全文检索服务实现类
 */
@Service(protocol = "dubbo")
public class SearchServiceImpl implements ISearchService {
    private static final Logger logger = LoggerFactory.getLogger(ISearchService.class);
    
    @Value("${elasticsearch.index_name:renting}")
    private static final String INDEX_NAME = "renting";

    @Value("${elasticsearch.index_type:house}")
    private static final String INDEX_TYPE = "house";

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseDetailRepository houseDetailRepository;

    @Autowired
    private HouseTagRepository tagRepository;
    
    @Autowired
    private SupportAddressRepository supportAddressRepository;
    
    @Autowired
    private IAddressService addressService;

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private TransportClient esClient;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    
    /**==============================MQ异步索引数据操作===============================*/
    /*
     * 实现房源索引
     */
    @Override
    public void index(Long houseId) {
    	// 索引构建耗时，而且es的索引能力有限，使用mq进行异步构建索引
        this.indexAsynByMQ(houseId, 0);
    }
    
    /**
     * 通过mq进行异步索引操作。
     * @param houseId
     * @param retry
     */
    private void indexAsynByMQ(Long houseId, int retry) {
        if (retry > HouseIndexMessage.MAX_RETRY) {
            logger.error("Retry index times over 3 for house: " + houseId + " Please check it!");
            return;
        }

        HouseIndexMessage message = new HouseIndexMessage(houseId, HouseIndexMessage.INDEX, retry);
        try {
            kafkaTemplate.send(INDEX_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("Json encode error for " + message);
        }

    }
    
    /**==============================处理MQ消息，进行索引数据===============================*/
    @KafkaListener(topics = INDEX_TOPIC)
    private void handleMessage(String content) {
        try {
            HouseIndexMessage message = objectMapper.readValue(content, HouseIndexMessage.class);

            switch (message.getOperation()) {
                case HouseIndexMessage.INDEX:
                    this.createOrUpdateIndex(message);
                    break;
                case HouseIndexMessage.REMOVE:
                    this.removeIndex(message);
                    break;
                default:
                    logger.warn("Not support message content " + content);
                    break;
            }
        } catch (Exception e) {
            logger.error("Cannot handle message for " + content, e);
            throw new IllegalArgumentException(e);
        }
    }
    
    private void createOrUpdateIndex(HouseIndexMessage message) {
        Long houseId = message.getHouseId();

        Optional<House> houseOpt = houseRepository.findById(houseId);
        if (! houseOpt.isPresent()) {
            logger.error("Index house {} dose not exist!", houseId);
            this.indexAsynByMQ(houseId, message.getRetry() + 1);
            return;
        }
        
        House house = houseOpt.get();
        HouseIndexTemplate indexTemplate = new HouseIndexTemplate();
        modelMapper.map(house, indexTemplate);

        HouseDetail detail = houseDetailRepository.findByHouseId(houseId);
        if (detail == null) {
            // TODO 异常情况
        }
        
        // 获取坐标地址
        SupportAddress city = supportAddressRepository.findByEnNameAndLevel(house.getCityEnName(), AreaLevel.CITY.getValue());

        SupportAddress region = supportAddressRepository.findByEnNameAndLevel(house.getRegionEnName(), AreaLevel.REGION.getValue());

        String address = city.getCnName() + region.getCnName() + house.getStreet() + house.getDistrict() + detail.getDetailAddress();
        ServiceResult<BaiduMapLocation> location = addressService.getBaiduMapLocation(city.getCnName(), address);
        if (location.isSuccess()) {
        	indexTemplate.setLocation(location.getResult());
        }

        modelMapper.map(detail, indexTemplate);

        List<HouseTag> tags = tagRepository.findAllByHouseId(houseId);
        if (tags != null && !tags.isEmpty()) {
            List<String> tagStrings = new ArrayList<>();
            tags.forEach(houseTag -> tagStrings.add(houseTag.getName()));
            indexTemplate.setTags(tagStrings);
        }
        
        boolean success;
        /*
        // 更新搜索时自动补全的词汇
        if (!updateSuggest(indexTemplate)) { return; }
        // 直接使用houseId作为ElasticSearch的_id，通过version进行索引的方式
        String id = String.valueOf(houseId);
        long version = house.getLastUpdateTime().getTime();
        success = indexData(id, indexTemplate, version);
        */
        
        // 通过查询判断的方式，分别进行操作
        
        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME).setTypes(INDEX_TYPE)
                .setQuery(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId));

        logger.debug(requestBuilder.toString());
        SearchResponse searchResponse = requestBuilder.get();

        long totalHit = searchResponse.getHits().getTotalHits();
        if (totalHit == 0) {
        	success = create(indexTemplate);
        } else if (totalHit == 1) {
            String esId = searchResponse.getHits().getAt(0).getId();
            success = update(esId, indexTemplate);
        } else {
        	success = deleteAndCreate(totalHit, indexTemplate);
        }
        
        // 上传百度的LBS地址信息
        if(location.getResult() != null) {
        	ServiceResult<?> serviceResult = addressService.lbsUpload(location.getResult(), house.getStreet() + house.getDistrict(),
        			city.getCnName() + region.getCnName() + house.getStreet() + house.getDistrict(),
        			message.getHouseId(), house.getPrice(), house.getArea());
        	
        	if (!success || !serviceResult.isSuccess()) {
        		this.index(message.getHouseId(), message.getRetry() + 1);
        	} else {
        		logger.debug("Index success with house " + houseId);
        		
        	}
        }
    }
    
    private void index(Long houseId, int retry) {
        if (retry > HouseIndexMessage.MAX_RETRY) {
            logger.error("Retry index times over 3 for house: " + houseId + " Please check it!");
            return;
        }

        HouseIndexMessage message = new HouseIndexMessage(houseId, HouseIndexMessage.INDEX, retry);
        try {
            kafkaTemplate.send(INDEX_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("Json encode error for " + message);
        }
    }
    
    private boolean indexData(String id, Object data, long version) {
    	// es中存在id的数据，根据version策略进行处理，比esversion大的数据运行覆盖
    	// 不存在id的数据，则直接新增
    	try {
			IndexRequestBuilder indexBuilder = esClient.prepareIndex(INDEX_NAME, INDEX_TYPE, id)
					.setVersion(version).setVersionType(VersionType.EXTERNAL_GTE);
			indexBuilder.setSource(objectMapper.writeValueAsBytes(data), XContentType.JSON);
			IndexResponse response = indexBuilder.get();
			logger.debug("data index to elasticsearch, dsl:"+indexBuilder.request());
			if(RestStatus.CREATED == response.status() || RestStatus.OK == response.status()) {
				logger.info("index success");
			}else {
				logger.error("index fail, "+response.status());
			}
		} catch (Exception e) {
			logger.error("Error to index house " + id, e);
			return false;
		}
    	return true;
    }

    private void removeIndex(HouseIndexMessage message) {
        Long houseId = message.getHouseId();
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, houseId))
                .source(INDEX_NAME);

        logger.debug("Delete by query for house: " + builder);

        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        logger.debug("Delete total " + deleted);
        
        ServiceResult<?> serviceResult = addressService.removeLbs(houseId);
        if (!serviceResult.isSuccess() || deleted <= 0) {
            logger.warn("Did not remove data from es for response: " + response);
            // 重新加入消息队列
            this.remove(houseId, message.getRetry() + 1);
        }
    }
    
    private void remove(Long houseId, int retry) {
        if (retry > HouseIndexMessage.MAX_RETRY) {
            logger.error("Retry remove times over 3 for house: " + houseId + " Please check it!");
            return;
        }

        HouseIndexMessage message = new HouseIndexMessage(houseId, HouseIndexMessage.REMOVE, retry);
        try {
            this.kafkaTemplate.send(INDEX_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("Cannot encode json for " + message, e);
        }
    }
    
    private boolean create(HouseIndexTemplate indexTemplate) {
        if (!updateSuggest(indexTemplate)) {
            return false;
        }

        try {
            IndexResponse response = this.esClient.prepareIndex(INDEX_NAME, INDEX_TYPE)
                    .setSource(objectMapper.writeValueAsBytes(indexTemplate), XContentType.JSON).get();

            logger.debug("Create index with house: " + indexTemplate.getHouseId());
            if (response.status() == RestStatus.CREATED) {
                return true;
            } else {
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error("Error to index house " + indexTemplate.getHouseId(), e);
            return false;
        }
    }

    private boolean update(String esId, HouseIndexTemplate indexTemplate) {
        if (!updateSuggest(indexTemplate)) {
            return false;
        }

        try {
            UpdateResponse response = this.esClient.prepareUpdate(INDEX_NAME, INDEX_TYPE, esId).setDoc(objectMapper.writeValueAsBytes(indexTemplate), XContentType.JSON).get();

            logger.debug("Update index with house: " + indexTemplate.getHouseId());
            if (response.status() == RestStatus.OK) {
                return true;
            } else {
                return false;
            }
        } catch (JsonProcessingException e) {
            logger.error("Error to index house " + indexTemplate.getHouseId(), e);
            return false;
        }
    }

    private boolean deleteAndCreate(long totalHit, HouseIndexTemplate indexTemplate) {
        DeleteByQueryRequestBuilder builder = DeleteByQueryAction.INSTANCE
                .newRequestBuilder(esClient)
                .filter(QueryBuilders.termQuery(HouseIndexKey.HOUSE_ID, indexTemplate.getHouseId()))
                .source(INDEX_NAME);

        logger.debug("Delete by query for house: " + builder);

        BulkByScrollResponse response = builder.get();
        long deleted = response.getDeleted();
        if (deleted != totalHit) {
            logger.warn("Need delete {}, but {} was deleted!", totalHit, deleted);
            return false;
        } else {
            return create(indexTemplate);
        }
    }
    
    /**==============================移除索引数据===============================*/
    @Override
    public void remove(Long houseId) {
        this.removeIndexData(houseId, 0);
    }
    
    private void removeIndexData(Long houseId, int retry) {
        if (retry > HouseIndexMessage.MAX_RETRY) {
            logger.error("Retry remove times over 3 for house: " + houseId + " Please check it!");
            return;
        }
        // 通过MQ消息进行处理
        HouseIndexMessage message = new HouseIndexMessage(houseId, HouseIndexMessage.REMOVE, retry);
        try {
            this.kafkaTemplate.send(INDEX_TOPIC, objectMapper.writeValueAsString(message));
        } catch (JsonProcessingException e) {
            logger.error("Cannot encode json for " + message, e);
        }
    }
    
    /**==============================检索数据操作===============================*/
    @Override
    public ServiceMultiResult<HouseHighlightDTO> highlightQuery(RentSearch rentSearch) {
    	SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE).setFetchSource(HouseIndexKey.HOUSE_ID, null);
    	HouseIndexSearchBuilder.buildSearchRequest(requestBuilder, rentSearch);
    	
    	// 构建高亮显示对象
    	HighlightBuilder highlightBuilder = SearchSourceBuilder.highlight();
    	highlightBuilder	// 设置能够进行高亮显示的字段
	    	.field(HouseIndexKey.TITLE)
	    	.field(HouseIndexKey.DESCRIPTION)
	    	.field(HouseIndexKey.LAYOUT_DESC)
	    	.field(HouseIndexKey.ROUND_SERVICE)
	    	.field(HouseIndexKey.TRAFFIC);
    	// 设置关键字前后标签，这里使用label进行红色标记
    	highlightBuilder.preTags("<label style=\"color:red;\">").postTags("</label>");
    	requestBuilder.highlighter(highlightBuilder);
    	
    	logger.debug(requestBuilder.toString());
    	
        List<HouseHighlightDTO> templates = new ArrayList<>();
        SearchResponse response = requestBuilder.get();
        if (response.status() != RestStatus.OK) {
            logger.warn("Search status is no ok for " + requestBuilder);
            return new ServiceMultiResult<>(0, templates);
        }

        for (SearchHit hit : response.getHits()) {
        	logger.debug(hit.getSourceAsString());
        	Map<String, Object> sourceMap = hit.getSourceAsMap();
        	// 用高亮标签，替换查询到的对应字段内容
            Map<String, HighlightField> highlightFieldMap = hit.getHighlightFields();
            Iterator<Entry<String, HighlightField>> iterator = highlightFieldMap.entrySet().iterator();
            while(iterator.hasNext()) { Entry<String, HighlightField> entry = iterator.next();
            	String highlightText = entry.getValue().fragments()[0].string();
            	sourceMap.put(entry.getKey(), highlightText);
            }
            
            // map转java对象
            HouseHighlightDTO dto = modelMapper.map(sourceMap, HouseHighlightDTO.class);
            templates.add(dto);
        }

        return new ServiceMultiResult<>(response.getHits().totalHits, templates);
    }

    @Override
    public ServiceMultiResult<Long> query(RentSearch rentSearch) {
    	SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE);
    	HouseIndexSearchBuilder.buildSearchRequest(requestBuilder, rentSearch);
    	requestBuilder.setFetchSource(HouseIndexKey.HOUSE_ID, null);
    	
        logger.debug(requestBuilder.toString());
        List<Long> houseIds = new ArrayList<>();
        SearchResponse response = requestBuilder.get();
        if (response.status() != RestStatus.OK) {
            logger.warn("Search status is no ok for " + requestBuilder);
            return new ServiceMultiResult<>(0, houseIds);
        }

        for (SearchHit hit : response.getHits()) {
        	logger.debug(hit.getSourceAsString());
            houseIds.add(Longs.tryParse(String.valueOf(hit.getSourceAsMap().get(HouseIndexKey.HOUSE_ID))));
        }

        return new ServiceMultiResult<>(response.getHits().totalHits, houseIds);
    }

    /**==============================分词联想操作===============================*/
    @Override
    public ServiceResult<List<String>> suggest(String prefix) {
    	// 构建一个从suggest字段匹配prefix的suggest，一次查询5条记录
        CompletionSuggestionBuilder suggestion = SuggestBuilders
        		.completionSuggestion("suggest").prefix(prefix).size(5);
        
        // 定义一个autocomplete名字的suggestBuilder，方面后面获取结果
        SuggestBuilder suggestBuilder = new SuggestBuilder();
        suggestBuilder.addSuggestion("autocomplete", suggestion);

        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .suggest(suggestBuilder);
        logger.debug(requestBuilder.toString());

        SearchResponse response = requestBuilder.get();
        Suggest suggest = response.getSuggest();
        if (suggest == null) {
            return ServiceResult.of(new ArrayList<>());
        }
        // 从前面定义的名字中获得结果
        Suggest.Suggestion<?> result = suggest.getSuggestion("autocomplete");

        int maxSuggest = 0;		// 允许的最大建议词条数
        Set<String> suggestSet = new HashSet<>();

        for (Object term : result.getEntries()) {
            if (term instanceof CompletionSuggestion.Entry) {
                CompletionSuggestion.Entry item = (CompletionSuggestion.Entry) term;

                if (item.getOptions().isEmpty()) {
                    continue;
                }
                
                // 添加联想词条，并去重
                for (CompletionSuggestion.Entry.Option option : item.getOptions()) {
                    String tip = option.getText().string();
                    if (suggestSet.contains(tip)) {
                        continue;
                    }
                    suggestSet.add(tip);
                    maxSuggest++;
                }
            }
            // 超过5条，跳出
            if (maxSuggest > 5) {
                break;
            }
        }
        List<String> suggests = Lists.newArrayList(suggestSet.toArray(new String[]{}));
        return ServiceResult.of(suggests);
    }
    
    private boolean updateSuggest(HouseIndexTemplate indexTemplate) {
    	
        List<String> textList = new ArrayList<String>();
        if(!Strings.isNullOrEmpty(indexTemplate.getTitle())) {
        	textList.add(indexTemplate.getTitle());
        }
        if(!Strings.isNullOrEmpty(indexTemplate.getLayoutDesc())) {
        	textList.add(indexTemplate.getLayoutDesc());
        }
        if(!Strings.isNullOrEmpty(indexTemplate.getRoundService())) {
        	textList.add(indexTemplate.getRoundService());
        }
        if(!Strings.isNullOrEmpty(indexTemplate.getDescription())) {
        	textList.add(indexTemplate.getDescription());
        }
        if(!Strings.isNullOrEmpty(indexTemplate.getSubwayLineName())) {
        	textList.add(indexTemplate.getSubwayLineName());
        }
        if(!Strings.isNullOrEmpty(indexTemplate.getSubwayStationName())) {
        	textList.add(indexTemplate.getSubwayStationName());
        }
        String[] textArray = new String[textList.size()];
        textList.toArray(textArray);
        // 构建联想词条，从标题、布局、周边服务、描述、地铁信息中构建
        AnalyzeRequestBuilder requestBuilder = new AnalyzeRequestBuilder(
                this.esClient, AnalyzeAction.INSTANCE, INDEX_NAME, textArray);
        
        // 使用ik_smart分词器进行构建
        requestBuilder.setAnalyzer("ik_smart");

        AnalyzeResponse response = requestBuilder.get();
        List<AnalyzeResponse.AnalyzeToken> tokens = response.getTokens();
        if (tokens == null) {
            logger.warn("Can not analyze token for house: " + indexTemplate.getHouseId());
            return false;
        }

        List<HouseSuggest> suggests = new ArrayList<>();
        for (AnalyzeResponse.AnalyzeToken token : tokens) {
            // 排序数字类型 & 小于2个字符的分词结果
            if ("<NUM>".equals(token.getType()) || token.getTerm().length() < 2) {
                continue;
            }

            HouseSuggest suggest = new HouseSuggest();
            suggest.setInput(token.getTerm());
            suggests.add(suggest);
        }

        // 定制化小区自动补全
        HouseSuggest suggest = new HouseSuggest();
        suggest.setInput(indexTemplate.getDistrict());
        suggests.add(suggest);
        
        indexTemplate.setSuggest(suggests);
        return true;
    }

    @Override
    public ServiceResult<Long> aggregateDistrictHouse(String cityEnName, String regionEnName, String district) {

        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery()
                .filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName))
                .filter(QueryBuilders.termQuery(HouseIndexKey.REGION_EN_NAME, regionEnName))
                .filter(QueryBuilders.termQuery(HouseIndexKey.DISTRICT, district));

        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(boolQuery)
                .addAggregation(
                        AggregationBuilders.terms(HouseIndexKey.AGG_DISTRICT)
                        .field(HouseIndexKey.DISTRICT)
                ).setSize(0);

        logger.debug(requestBuilder.toString());

        SearchResponse response = requestBuilder.get();
        if (response.status() == RestStatus.OK) {
            Terms terms = response.getAggregations().get(HouseIndexKey.AGG_DISTRICT);
            if (terms.getBuckets() != null && !terms.getBuckets().isEmpty()) {
                return ServiceResult.of(terms.getBucketByKey(district).getDocCount());
            }
        } else {
            logger.warn("Failed to Aggregate for " + HouseIndexKey.AGG_DISTRICT);

        }
        return ServiceResult.of(0L);
    }

    @Override
    public ServiceMultiResult<HouseBucketDTO> mapAggregate(String cityEnName) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName));

        AggregationBuilder aggBuilder = AggregationBuilders.terms(HouseIndexKey.AGG_REGION)
                .field(HouseIndexKey.REGION_EN_NAME);
        SearchRequestBuilder requestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(boolQuery)
                .addAggregation(aggBuilder);

        logger.debug(requestBuilder.toString());

        SearchResponse response = requestBuilder.get();
        List<HouseBucketDTO> buckets = new ArrayList<>();
        if (response.status() != RestStatus.OK) {
            logger.warn("Aggregate status is not ok for " + requestBuilder);
            return new ServiceMultiResult<>(0, buckets);
        }

        Terms terms = response.getAggregations().get(HouseIndexKey.AGG_REGION);
        for (Terms.Bucket bucket : terms.getBuckets()) {
            buckets.add(new HouseBucketDTO(bucket.getKeyAsString(), bucket.getDocCount()));
        }

        return new ServiceMultiResult<>(response.getHits().getTotalHits(), buckets);
    }

    @Override
    public ServiceMultiResult<Long> mapQuery(String cityEnName, String orderBy,
                                             String orderDirection,
                                             int start,
                                             int size) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, cityEnName));

        SearchRequestBuilder searchRequestBuilder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(boolQuery)
                .addSort(HouseSort.getSortKey(orderBy), SortOrder.fromString(orderDirection))
                .setFrom(start)
                .setSize(size);

        List<Long> houseIds = new ArrayList<>();
        SearchResponse response = searchRequestBuilder.get();
        if (response.status() != RestStatus.OK) {
            logger.warn("Search status is not ok for " + searchRequestBuilder);
            return new ServiceMultiResult<>(0, houseIds);
        }

        for (SearchHit hit : response.getHits()) {
            houseIds.add(Longs.tryParse(String.valueOf(hit.getSourceAsMap().get(HouseIndexKey.HOUSE_ID))));
        }
        return new ServiceMultiResult<>(response.getHits().getTotalHits(), houseIds);
    }

    @Override
    public ServiceMultiResult<Long> mapQuery(MapSearch mapSearch) {
        BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();
        boolQuery.filter(QueryBuilders.termQuery(HouseIndexKey.CITY_EN_NAME, mapSearch.getCityEnName()));

        boolQuery.filter(
            QueryBuilders.geoBoundingBoxQuery("location")
                .setCorners(
                        new GeoPoint(mapSearch.getLeftLatitude(), mapSearch.getLeftLongitude()),
                        new GeoPoint(mapSearch.getRightLatitude(), mapSearch.getRightLongitude())
                ));

        SearchRequestBuilder builder = this.esClient.prepareSearch(INDEX_NAME)
                .setTypes(INDEX_TYPE)
                .setQuery(boolQuery)
                .addSort(HouseSort.getSortKey(mapSearch.getOrderBy()),
                        SortOrder.fromString(mapSearch.getOrderDirection()))
                .setFrom(mapSearch.getStart())
                .setSize(mapSearch.getSize());

        List<Long> houseIds = new ArrayList<>();
        SearchResponse response = builder.get();
        if (RestStatus.OK != response.status()) {
            logger.warn("Search status is not ok for " + builder);
            return new ServiceMultiResult<>(0, houseIds);
        }

        for (SearchHit hit : response.getHits()) {
            houseIds.add(Longs.tryParse(String.valueOf(hit.getSourceAsMap().get(HouseIndexKey.HOUSE_ID))));
        }
        return new ServiceMultiResult<>(response.getHits().getTotalHits(), houseIds);
    }

}
