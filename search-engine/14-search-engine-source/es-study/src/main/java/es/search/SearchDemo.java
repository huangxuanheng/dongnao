package es.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class SearchDemo {  //查询出年龄为25岁、或者28岁的所有人
    public static void main(String args[]) throws UnknownHostException {


        try(TransportClient client = InitDemo.getClient()) {
            //构建request
            SearchRequest request = new SearchRequest();
            request.indices("bank");
            request.types("account");

            //构建搜索
            SearchSourceBuilder search = new SearchSourceBuilder();
            BoolQueryBuilder boolBuilder = QueryBuilders.boolQuery();

            QueryBuilder builder1 = QueryBuilders.termQuery("age", 25);
            QueryBuilder builder2 = QueryBuilders.termQuery("age", 28);

            boolBuilder.should().add(builder1);
            boolBuilder.should().add(builder2);

            search.query(boolBuilder);

            search.timeout(new TimeValue(60, TimeUnit.SECONDS));

            //search放入到request中
            request.source(search);

            //执行搜索
            SearchResponse response = client.search(request).get();

            //获取命中的文档
            SearchHits hits = response.getHits();
            SearchHit[] hitArr = hits.getHits();
            System.out.println("搜索到" + hits.totalHits + "个文档");

            //处理命中的文档
            for (SearchHit hit : hitArr){
                //打印元信息
                System.out.println(hit.getType() + "," + hit.getScore());

                //打印原文档
                String sourceAsStrig = hit.getSourceAsString();
                System.out.println(sourceAsStrig);
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }
}
