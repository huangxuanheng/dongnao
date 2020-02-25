package es.search;

import org.elasticsearch.action.admin.indices.create.CreateIndexRequest;
import org.elasticsearch.action.admin.indices.create.CreateIndexResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.xcontent.XContentType;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class CreateIndex {
    public static void main(String args[]){
        try(TransportClient client = InitDemo.getClient()) {
            CreateIndexRequest request = new CreateIndexRequest("music_v20");

            //指定分片、副本
            Settings.Builder builder = Settings.builder()
                    .put("index.number_of_shards", 3)
                    .put("index.number_of_replicas", 2);

            request.settings(builder);

            //指定mapping
            request.mapping("_doc","{\n" +
                    "  \"_doc\" : {\n" +
                    "        \"properties\" : {\n" +
                    "          \"lyrics\" : {\n" +
                    "            \"type\" : \"text\"\n" +
                    "          },\n" +
                    "          \"singer\" : {\n" +
                    "            \"type\" : \"text\"\n" +
                    "          },\n" +
                    "          \"songName\" : {\n" +
                    "            \"type\" : \"text\"\n" +
                    "          }\n" +
                    "        }\n" +
                    "      }\n" +
                    "}", XContentType.JSON);

            CreateIndexResponse response = client.admin().indices().create(request).get();
            System.out.println(response.isAcknowledged());;


        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
