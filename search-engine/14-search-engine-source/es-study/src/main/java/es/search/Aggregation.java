package es.search;

import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.rest.RestStatus;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.avg.Avg;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

/*

GET /bank/_search?size=0
{
  "aggs": {
    "by_state": {
      "terms": {
        "field": "state.keyword"
      },
      "aggs": {
        "avg_age": {
          "avg": {
            "field": "age"
          }
        }
      }
    }
  }
}

 */


public class Aggregation {
    public static void main(String args[]){
        try(TransportClient client = InitDemo.getClient()) {
            //构建request
            SearchRequest request = new SearchRequest("bank");

            SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();
            sourceBuilder.size(0);

            //桶聚合
            TermsAggregationBuilder by_state =
                    AggregationBuilders.terms("by_state").field("state.keyword");

            //指标聚合
            by_state.subAggregation(
                    AggregationBuilders.avg("avg_age").field("age"));

            request.source(sourceBuilder);
            sourceBuilder.aggregation(by_state);

            SearchResponse response = client.search(request).get();

            if (RestStatus.OK.equals(response.status())){
                Aggregations aggs = response.getAggregations();
                Terms by_state_agg = aggs.get("by_state");

                for (Terms.Bucket buck : by_state_agg.getBuckets()){
                     Avg  avg_age =buck.getAggregations().get("avg_age");
                     System.out.println(avg_age.getValue());
                     System.out.println("===========================");
                }

            }


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }


    }



}
