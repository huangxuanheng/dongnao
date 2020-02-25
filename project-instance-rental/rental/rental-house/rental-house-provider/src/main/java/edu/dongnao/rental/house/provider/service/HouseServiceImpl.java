package edu.dongnao.rental.house.provider.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.persistence.criteria.Predicate;

import org.apache.dubbo.config.annotation.Service;
import org.apache.logging.log4j.util.Strings;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.transaction.annotation.Transactional;

import com.google.common.collect.Maps;

import edu.dongnao.rental.house.api.IHouseService;
import edu.dongnao.rental.house.api.IQiNiuService;
import edu.dongnao.rental.house.api.ISearchService;
import edu.dongnao.rental.house.domain.HouseDTO;
import edu.dongnao.rental.house.domain.HouseDetailDTO;
import edu.dongnao.rental.house.domain.HouseHighlightDTO;
import edu.dongnao.rental.house.domain.HousePictureDTO;
import edu.dongnao.rental.house.domain.HouseSort;
import edu.dongnao.rental.house.domain.HouseStatus;
import edu.dongnao.rental.house.domain.HouseSubscribeDTO;
import edu.dongnao.rental.house.domain.HouseSubscribeStatus;
import edu.dongnao.rental.house.form.DatatableSearch;
import edu.dongnao.rental.house.form.HouseForm;
import edu.dongnao.rental.house.form.MapSearch;
import edu.dongnao.rental.house.form.PhotoForm;
import edu.dongnao.rental.house.form.RentSearch;
import edu.dongnao.rental.house.provider.entity.House;
import edu.dongnao.rental.house.provider.entity.HouseDetail;
import edu.dongnao.rental.house.provider.entity.HousePicture;
import edu.dongnao.rental.house.provider.entity.HouseSubscribe;
import edu.dongnao.rental.house.provider.entity.HouseTag;
import edu.dongnao.rental.house.provider.entity.Subway;
import edu.dongnao.rental.house.provider.entity.SubwayStation;
import edu.dongnao.rental.house.provider.repository.HouseDetailRepository;
import edu.dongnao.rental.house.provider.repository.HousePictureRepository;
import edu.dongnao.rental.house.provider.repository.HouseRepository;
import edu.dongnao.rental.house.provider.repository.HouseSubscribeRespository;
import edu.dongnao.rental.house.provider.repository.HouseTagRepository;
import edu.dongnao.rental.house.provider.repository.SubwayRepository;
import edu.dongnao.rental.house.provider.repository.SubwayStationRepository;
import edu.dongnao.rental.lang.ApiResponse;
import edu.dongnao.rental.lang.ServiceMultiResult;
import edu.dongnao.rental.lang.ServiceResult;

/**
 * 房源基本信息服务类
 */
@Service(protocol = "dubbo")
public class HouseServiceImpl implements IHouseService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private HouseRepository houseRepository;

    @Autowired
    private HouseDetailRepository houseDetailRepository;

    @Autowired
    private HousePictureRepository housePictureRepository;

    @Autowired
    private HouseTagRepository houseTagRepository;

    @Autowired
    private SubwayRepository subwayRepository;

    @Autowired
    private SubwayStationRepository subwayStationRepository;

    @Autowired
    private HouseSubscribeRespository subscribeRespository;

    @Autowired
    private IQiNiuService qiNiuService;

    @Value("${qiniu.cdn.prefix}")
    private String cdnPrefix;
    
    @Autowired
    private ISearchService searchService;

    @Override
    public ServiceResult<HouseDTO> save(HouseForm houseForm) {
        HouseDetail detail = new HouseDetail();
        ServiceResult<HouseDTO> subwayValidtionResult = wrapperDetailInfo(detail, houseForm);
        if (subwayValidtionResult != null) {
            return subwayValidtionResult;
        }

        House house = new House();
        modelMapper.map(houseForm, house);

        Date now = new Date();
        house.setCreateTime(now);
        house.setLastUpdateTime(now);
        house.setAdminId(houseForm.getAdminId());
        house = houseRepository.save(house);

        detail.setHouseId(house.getId());
        detail = houseDetailRepository.save(detail);

        List<HousePicture> pictures = generatePictures(houseForm, house.getId());
        Iterable<HousePicture> housePictures = housePictureRepository.saveAll(pictures);

        HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
        HouseDetailDTO houseDetailDTO = modelMapper.map(detail, HouseDetailDTO.class);

        houseDTO.setHouseDetail(houseDetailDTO);

        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        housePictures.forEach(housePicture -> pictureDTOS.add(modelMapper.map(housePicture, HousePictureDTO.class)));
        houseDTO.setPictures(pictureDTOS);
        houseDTO.setCover(this.cdnPrefix + houseDTO.getCover());

        List<String> tags = houseForm.getTags();
        if (tags != null && !tags.isEmpty()) {
            List<HouseTag> houseTags = new ArrayList<>();
            for (String tag : tags) {
                houseTags.add(new HouseTag(house.getId(), tag));
            }
            houseTagRepository.saveAll(houseTags);
            houseDTO.setTags(tags);
        }

        return new ServiceResult<HouseDTO>(true, null, houseDTO);
    }

    @Override
    @Transactional
    public ServiceResult<HouseDTO> update(HouseForm houseForm) {
        Optional<House> houseOpt = this.houseRepository.findById(houseForm.getId());
        if (! houseOpt.isPresent()) {
            return ServiceResult.notFound();
        }
        House house = houseOpt.get();
        HouseDetail detail = this.houseDetailRepository.findByHouseId(house.getId());
        if (detail == null) {
            return ServiceResult.notFound();
        }

        ServiceResult<HouseDTO> wrapperResult = wrapperDetailInfo(detail, houseForm);
        if (wrapperResult != null) {
            return wrapperResult;
        }

        houseDetailRepository.save(detail);

        List<HousePicture> pictures = generatePictures(houseForm, houseForm.getId());
        housePictureRepository.saveAll(pictures);

        if (houseForm.getCover() == null) {
            houseForm.setCover(house.getCover());
        }

        modelMapper.map(houseForm, house);
        house.setLastUpdateTime(new Date());
        houseRepository.save(house);

        return ServiceResult.success();
    }

    @Override
    public ServiceMultiResult<HouseDTO> adminQuery(DatatableSearch searchBody, Long userId) {
        List<HouseDTO> houseDTOS = new ArrayList<>();
        
        Sort sort = Sort.by(Sort.Direction.fromString(searchBody.getDirection()), searchBody.getOrderBy());
        int page = searchBody.getStart() / searchBody.getLength();

        Pageable pageable = PageRequest.of(page, searchBody.getLength(), sort);

        Specification<House> specification = (root, query, cb) -> {
            Predicate predicate = cb.equal(root.get("adminId"), userId);
            predicate = cb.and(predicate, cb.notEqual(root.get("status"), HouseStatus.DELETED.getValue()));

            if (searchBody.getCity() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("cityEnName"), searchBody.getCity()));
            }

            if (searchBody.getStatus() != null) {
                predicate = cb.and(predicate, cb.equal(root.get("status"), searchBody.getStatus()));
            }

            if (searchBody.getCreateTimeMin() != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMin()));
            }

            if (searchBody.getCreateTimeMax() != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("createTime"), searchBody.getCreateTimeMax()));
            }

            if (searchBody.getTitle() != null) {
                predicate = cb.and(predicate, cb.like(root.get("title"), "%" + searchBody.getTitle() + "%"));
            }

            return predicate;
        };

        Page<House> houses = houseRepository.findAll(specification, pageable);
        houses.forEach(house -> {
            HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(this.cdnPrefix + house.getCover());
            houseDTOS.add(houseDTO);
        });

        return new ServiceMultiResult<>(houses.getTotalElements(), houseDTOS);
    }

    @Override
    public ServiceResult<HouseDTO> findCompleteOne(Long id, Long userId) {
        Optional<House> houseOpt = houseRepository.findById(id);
        if (! houseOpt.isPresent()) {
            return ServiceResult.notFound();
        }
        
        House house = houseOpt.get();

        HouseDetail detail = houseDetailRepository.findByHouseId(id);
        List<HousePicture> pictures = housePictureRepository.findAllByHouseId(id);

        HouseDetailDTO detailDTO = modelMapper.map(detail, HouseDetailDTO.class);
        List<HousePictureDTO> pictureDTOS = new ArrayList<>();
        for (HousePicture picture : pictures) {
            HousePictureDTO pictureDTO = modelMapper.map(picture, HousePictureDTO.class);
            pictureDTOS.add(pictureDTO);
        }


        List<HouseTag> tags = houseTagRepository.findAllByHouseId(id);
        List<String> tagList = new ArrayList<>();
        for (HouseTag tag : tags) {
            tagList.add(tag.getName());
        }

        HouseDTO result = modelMapper.map(house, HouseDTO.class);
        result.setHouseDetail(detailDTO);
        result.setPictures(pictureDTOS);
        result.setTags(tagList);

        if (userId > 0) { // 已登录用户
            HouseSubscribe subscribe = subscribeRespository.findByHouseIdAndUserId(house.getId(), userId);
            if (subscribe != null) {
                result.setSubscribeStatus(subscribe.getStatus());
            }
        }

        return ServiceResult.of(result);
    }

    @Override
    public ServiceResult<Boolean> removePhoto(Long id) {
    	Optional<HousePicture> pictureOpt = housePictureRepository.findById(id);
        if (! pictureOpt.isPresent() ) {
            return ServiceResult.notFound();
        }
        HousePicture picture = pictureOpt.get();
        ApiResponse response = this.qiNiuService.delete(picture.getPath());
        if (ApiResponse.Status.SUCCESS.getCode() == response.getCode()) {
            housePictureRepository.deleteById(id);
            return ServiceResult.success();
        } else {
            return ServiceResult.fail(response.getMessage());
        }
    }

    @Override
    @Transactional
    public ServiceResult<Boolean> updateCover(Long coverId, Long targetId) {
    	Optional<HousePicture> coverOpt = housePictureRepository.findById(coverId);
        if (! coverOpt.isPresent()) {
            return ServiceResult.notFound();
        }
        HousePicture cover = coverOpt.get();

        houseRepository.updateCover(targetId, cover.getPath());
        return ServiceResult.success();
    }

    @Override
    @Transactional
    public ServiceResult<Boolean> addTag(Long houseId, String tag) {
    	Optional<House> houseOpt = houseRepository.findById(houseId);
        if (! houseOpt.isPresent()) {
            return ServiceResult.notFound();
        }

        HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
        if (houseTag != null) {
            return ServiceResult.fail("标签已存在");
        }

        houseTagRepository.save(new HouseTag(houseId, tag));
        return ServiceResult.success();
    }

    @Override
    @Transactional
    public ServiceResult<Boolean> removeTag(Long houseId, String tag) {
    	Optional<House> houseOpt = houseRepository.findById(houseId);
        if (! houseOpt.isPresent() ) {
            return ServiceResult.notFound();
        }

        HouseTag houseTag = houseTagRepository.findByNameAndHouseId(tag, houseId);
        if (houseTag == null) {
            return ServiceResult.fail("标签不存在");
        }

        houseTagRepository.deleteById(houseTag.getId());
        return ServiceResult.success();
    }

    @Override
    @Transactional
    public ServiceResult<Boolean> updateStatus(Long id, int status) {
    	Optional<House> houseOpt = houseRepository.findById(id);
        if (! houseOpt.isPresent()) {
            return ServiceResult.notFound();
        }
        
        House house = houseOpt.get();
        if (house.getStatus() == status) {
            return new ServiceResult<Boolean>(false, "状态没有发生变化");
        }

        if (house.getStatus() == HouseStatus.RENTED.getValue()) {
            return new ServiceResult<Boolean>(false, "已出租的房源不允许修改状态");
        }

        if (house.getStatus() == HouseStatus.DELETED.getValue()) {
            return new ServiceResult<Boolean>(false, "已删除的资源不允许操作");
        }

        houseRepository.updateStatus(id, status);

        // 上架更新索引 其他情况都要删除索引
        if (status == HouseStatus.PASSES.getValue()) {
            searchService.index(id);
        } else {
            searchService.remove(id);
        }
        
        return ServiceResult.success();
    }

    /**
     * 图片对象列表信息填充
     * @param form
     * @param houseId
     * @return
     */
    private List<HousePicture> generatePictures(HouseForm form, Long houseId) {
        List<HousePicture> pictures = new ArrayList<>();
        if (form.getPhotos() == null || form.getPhotos().isEmpty()) {
            return pictures;
        }

        for (PhotoForm photoForm : form.getPhotos()) {
            HousePicture picture = new HousePicture();
            picture.setHouseId(houseId);
            picture.setCdnPrefix(cdnPrefix);
            picture.setPath(photoForm.getPath());
            picture.setWidth(photoForm.getWidth());
            picture.setHeight(photoForm.getHeight());
            pictures.add(picture);
        }
        return pictures;
    }

    /**
     * 房源详细信息对象填充，以及地铁线路级联处理。
     * @param houseDetail
     * @param houseForm
     * @return
     */
    private ServiceResult<HouseDTO> wrapperDetailInfo(HouseDetail houseDetail, HouseForm houseForm) {
    	if(houseForm.getSubwayLineId() != null) {
    		// 地铁线路处理
    		Optional<Subway> subwayOpt = subwayRepository.findById(houseForm.getSubwayLineId());
    		if (! subwayOpt.isPresent()) {
    			return new ServiceResult<>(false, "Not valid subway line!");
    		}else {
    			// 地铁站信息处理
    			Subway subway = subwayOpt.get();
    			houseDetail.setSubwayLineId(subway.getId());
    			houseDetail.setSubwayLineName(subway.getName());
    			
    			Optional<SubwayStation> subwayStationOpt = subwayStationRepository.findById(houseForm.getSubwayStationId());
    			SubwayStation subwayStation = null;
    			if (! subwayStationOpt.isPresent() || subway.getId() != (subwayStation = subwayStationOpt.get()).getSubwayId()) {
    				return new ServiceResult<>(false, "Not valid subway station!");
    			}else {
    				houseDetail.setSubwayStationId(subwayStation.getId());
    				houseDetail.setSubwayStationName(subwayStation.getName());
    			}
    		}
    	}

        houseDetail.setDescription(houseForm.getDescription());
        houseDetail.setDetailAddress(houseForm.getDetailAddress());
        houseDetail.setLayoutDesc(houseForm.getLayoutDesc());
        houseDetail.setRentWay(houseForm.getRentWay());
        houseDetail.setRoundService(houseForm.getRoundService());
        houseDetail.setTraffic(houseForm.getTraffic());
        return null;

    }

	@Override
	public ServiceMultiResult<HouseDTO> query(RentSearch rentSearch) {
		if (rentSearch.getKeywords() != null && !rentSearch.getKeywords().isEmpty()) {
			// 关键字全文检索，走ElasticSearch
            ServiceMultiResult<Long> serviceResult = searchService.query(rentSearch);
            if (serviceResult.getTotal() == 0) {
                return new ServiceMultiResult<>(0, new ArrayList<>());
            }
            return new ServiceMultiResult<>(serviceResult.getTotal(), wrapperHouseResult(serviceResult.getResult()));
			
            //TODO 关键字检索，高亮显示
			//return wrapperHighlightResult(rentSearch);
        }
		
		// 高级检索走mysql查询
        return simpleQuery(rentSearch);
	}
	
	/**
	 * 关键词高亮显示包装
	 * @param rentSearch
	 * @return
	 */
	private ServiceMultiResult<HouseDTO> wrapperHighlightResult(RentSearch rentSearch){
		ServiceMultiResult<HouseHighlightDTO> highlightResult = searchService.highlightQuery(rentSearch);
        List<HouseHighlightDTO> highlightList = highlightResult.getResult();
        
        List<Long> houseIds = highlightList.stream()
        			.map(h -> h.getHouseId()).collect(Collectors.toList());
        List<HouseDTO> houseList = wrapperHouseResult(houseIds);
        
        // 高亮字段处理
        Map<Long, HouseHighlightDTO> highlightMap = highlightList.stream()
        		.collect(Collectors.toMap(h -> h.getHouseId() , h -> h));
        
        List<HouseDTO> results = houseList.stream().map(h -> {
        	HouseHighlightDTO dto = highlightMap.get(h.getId());
        	if(Strings.isNotBlank(dto.getTitle())) {
        		h.setTitle(dto.getTitle());
        	}
        	if(Strings.isNotBlank(dto.getDescription())) {
        		h.getHouseDetail().setDescription(dto.getDescription());
        	}
        	if(Strings.isNotBlank(dto.getLayoutDesc())) {
        		h.getHouseDetail().setLayoutDesc(dto.getLayoutDesc());
        	}
        	if(Strings.isNotBlank(dto.getRoundService())) {
        		h.getHouseDetail().setRoundService(dto.getRoundService());
        	}
        	if(Strings.isNotBlank(dto.getTraffic())) {
        		h.getHouseDetail().setTraffic(dto.getTraffic());
        	}
        	return h;
        }).collect(Collectors.toList());
        return new ServiceMultiResult<>(highlightResult.getTotal(), results);
	}
	
	/**
	 * 根据ElasticSearch中查询到的id，到数据库中查询构建响应内容。
	 * @param houseIds
	 * @return
	 */
	private List<HouseDTO> wrapperHouseResult(List<Long> houseIds) {
        List<HouseDTO> result = new ArrayList<>();

        Map<Long, HouseDTO> idToHouseMap = new HashMap<>();
        Iterable<House> houses = houseRepository.findAllById(houseIds);
        houses.forEach(house -> {
            HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(this.cdnPrefix + house.getCover());
            idToHouseMap.put(house.getId(), houseDTO);
        });

        wrapperHouseList(houseIds, idToHouseMap);

        // 矫正顺序
        for (Long houseId : houseIds) {
            result.add(idToHouseMap.get(houseId));
        }
        return result;
    }
	
	/**
     * 渲染详细信息 及 房屋标签
     * @param houseIds
     * @param idToHouseMap
     */
    private void wrapperHouseList(List<Long> houseIds, Map<Long, HouseDTO> idToHouseMap) {
        List<HouseDetail> details = houseDetailRepository.findAllByHouseIdIn(houseIds);
        details.forEach(houseDetail -> {
            HouseDTO houseDTO = idToHouseMap.get(houseDetail.getHouseId());
            HouseDetailDTO detailDTO = modelMapper.map(houseDetail, HouseDetailDTO.class);
            houseDTO.setHouseDetail(detailDTO);
        });

        List<HouseTag> houseTags = houseTagRepository.findAllByHouseIdIn(houseIds);
        houseTags.forEach(houseTag -> {
            HouseDTO house = idToHouseMap.get(houseTag.getHouseId());
            house.getTags().add(houseTag.getName());
        });
    }
    
    /**
     * 房源信息查询功能，非关键字全文检索，从mysql关系型数据库查询
     * @param rentSearch 查询请求参数体
     * @return
     */
    private ServiceMultiResult<HouseDTO> simpleQuery(RentSearch rentSearch) {
        Sort sort = HouseSort.generateSort(rentSearch.getOrderBy(), rentSearch.getOrderDirection());
        int page = rentSearch.getStart() / rentSearch.getSize();

        Pageable pageable = PageRequest.of(page, rentSearch.getSize(), sort);

        Specification<House> specification = (root, criteriaQuery, criteriaBuilder) -> {
            Predicate predicate = criteriaBuilder.equal(root.get("status"), HouseStatus.PASSES.getValue());

            predicate = criteriaBuilder.and(predicate, criteriaBuilder.equal(root.get("cityEnName"), rentSearch.getCityEnName()));

            if (HouseSort.DISTANCE_TO_SUBWAY_KEY.equals(rentSearch.getOrderBy())) {
                predicate = criteriaBuilder.and(predicate, criteriaBuilder.gt(root.get(HouseSort.DISTANCE_TO_SUBWAY_KEY), -1));
            }
            return predicate;
        };

        Page<House> houses = houseRepository.findAll(specification, pageable);
        List<HouseDTO> houseDTOS = new ArrayList<>();


        List<Long> houseIds = new ArrayList<>();
        Map<Long, HouseDTO> idToHouseMap = Maps.newHashMap();
        houses.forEach(house -> {
            HouseDTO houseDTO = modelMapper.map(house, HouseDTO.class);
            houseDTO.setCover(this.cdnPrefix + house.getCover());
            houseDTOS.add(houseDTO);

            houseIds.add(house.getId());
            idToHouseMap.put(house.getId(), houseDTO);
        });


        wrapperHouseList(houseIds, idToHouseMap);
        return new ServiceMultiResult<>(houses.getTotalElements(), houseDTOS);
    }
    
    /**====================================地图浏览操作=========================================*/
    @Override
    public ServiceMultiResult<HouseDTO> wholeMapQuery(MapSearch mapSearch) {
        ServiceMultiResult<Long> serviceResult = searchService.mapQuery(mapSearch.getCityEnName(), mapSearch.getOrderBy(), mapSearch.getOrderDirection(), mapSearch.getStart(), mapSearch.getSize());

        if (serviceResult.getTotal() == 0) {
            return new ServiceMultiResult<>(0, new ArrayList<>());
        }
        List<HouseDTO> houses = wrapperHouseResult(serviceResult.getResult());
        return new ServiceMultiResult<>(serviceResult.getTotal(), houses);
    }

    @Override
    public ServiceMultiResult<HouseDTO> boundMapQuery(MapSearch mapSearch) {
        ServiceMultiResult<Long> serviceResult = searchService.mapQuery(mapSearch);
        if (serviceResult.getTotal() == 0) {
            return new ServiceMultiResult<>(0, new ArrayList<>());
        }

        List<HouseDTO> houses = wrapperHouseResult(serviceResult.getResult());
        return new ServiceMultiResult<>(serviceResult.getTotal(), houses);
    }
    
    /**==========================================看房预约操作======================================*/
    @Override
    @Transactional
    public ServiceResult<Boolean> addSubscribeOrder(Long houseId, Long userId) {
        HouseSubscribe subscribe = subscribeRespository.findByHouseIdAndUserId(houseId, userId);
        if (subscribe != null) {
            return new ServiceResult<Boolean>(false, "已加入预约");
        }

        Optional<House> houseOpt = houseRepository.findById(houseId);
        if (! houseOpt.isPresent()) {
            return new ServiceResult<Boolean>(false, "查无此房");
        }
        House house = houseOpt.get();

        subscribe = new HouseSubscribe();
        Date now = new Date();
        subscribe.setCreateTime(now);
        subscribe.setLastUpdateTime(now);
        subscribe.setUserId(userId);
        subscribe.setHouseId(houseId);
        subscribe.setStatus(HouseSubscribeStatus.IN_ORDER_LIST.getValue());
        subscribe.setAdminId(house.getAdminId());
        subscribeRespository.save(subscribe);
        return ServiceResult.success();
    }

    @Override
    public ServiceMultiResult<HouseSubscribeDTO> querySubscribeList(
            HouseSubscribeStatus status,
            Long userId, 
            int start,
            int size) {
        Pageable pageable = PageRequest.of(start / size, size, Sort.by(Sort.Direction.DESC, "createTime"));

        Page<HouseSubscribe> page = subscribeRespository.findAllByUserIdAndStatus(userId, status.getValue(), pageable);

        return wrapperPage(page);
    }

    @Override
    @Transactional
    public ServiceResult<?> subscribe(Long houseId, Long userId, Date orderTime, String telephone, String desc) {
        HouseSubscribe subscribe = subscribeRespository.findByHouseIdAndUserId(houseId, userId);
        if (subscribe == null) {
            return new ServiceResult<>(false, "无预约记录");
        }

        if (subscribe.getStatus() != HouseSubscribeStatus.IN_ORDER_LIST.getValue()) {
            return new ServiceResult<>(false, "无法预约");
        }

        subscribe.setStatus(HouseSubscribeStatus.IN_ORDER_TIME.getValue());
        subscribe.setLastUpdateTime(new Date());
        subscribe.setTelephone(telephone);
        subscribe.setDesc(desc);
        subscribe.setOrderTime(orderTime);
        subscribeRespository.save(subscribe);
        return ServiceResult.success();
    }

    @Override
    @Transactional
    public ServiceResult<?> cancelSubscribe(Long houseId, Long userId) {
        HouseSubscribe subscribe = subscribeRespository.findByHouseIdAndUserId(houseId, userId);
        if (subscribe == null) {
            return new ServiceResult<>(false, "无预约记录");
        }

        subscribeRespository.deleteById(subscribe.getId());
        return ServiceResult.success();
    }

    @Override
    public ServiceMultiResult<HouseSubscribeDTO> findSubscribeList(Long userId, int start, int size) {
        Pageable pageable = PageRequest.of(start / size, size, Sort.by(Sort.Direction.DESC, "orderTime"));

        Page<HouseSubscribe> page = subscribeRespository.findAllByAdminIdAndStatus(userId, HouseSubscribeStatus.IN_ORDER_TIME.getValue(), pageable);

        return wrapperPage(page);
    }

    @Override
    @Transactional
    public ServiceResult<Boolean> finishSubscribe(Long houseId, Long subscribeId, Long userId) {
    	Optional<HouseSubscribe> subscribeOpt = subscribeRespository.findById(subscribeId);
        if (! subscribeOpt.isPresent()) {
            return ServiceResult.fail("无预约记录");
        }
        
        HouseSubscribe subscribe = subscribeOpt.get();
        if(subscribe.getHouseId() != houseId) {
        	return ServiceResult.fail("无预约记录");
        }

        subscribeRespository.updateStatus(subscribe.getId(), HouseSubscribeStatus.FINISH.getValue());
        houseRepository.updateWatchTimes(houseId);
        return ServiceResult.success();
    }
    
    private ServiceMultiResult<HouseSubscribeDTO> wrapperPage(Page<HouseSubscribe> page) {
        List<HouseSubscribeDTO> result = new ArrayList<>();

        if (page.getSize() < 1) {
            return new ServiceMultiResult<>(page.getTotalElements(), result);
        }

        List<HouseSubscribeDTO> subscribeDTOS = new ArrayList<>();
        List<Long> houseIds = new ArrayList<>();
        page.forEach(houseSubscribe -> {
            subscribeDTOS.add(modelMapper.map(houseSubscribe, HouseSubscribeDTO.class));
            houseIds.add(houseSubscribe.getHouseId());
        });

        Map<Long, HouseDTO> idToHouseMap = new HashMap<>();
        Iterable<House> houses = houseRepository.findAllById(houseIds);
        houses.forEach(house -> {
            idToHouseMap.put(house.getId(), modelMapper.map(house, HouseDTO.class));
        });

        for (HouseSubscribeDTO subscribeDTO : subscribeDTOS) {
        	subscribeDTO.setHouse(idToHouseMap.get(subscribeDTO.getHouseId()));
            result.add(subscribeDTO);
        }

        return new ServiceMultiResult<>(page.getTotalElements(), result);
    }
}
