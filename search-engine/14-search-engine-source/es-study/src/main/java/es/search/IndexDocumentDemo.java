package es.search;

import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.transport.TransportClient;

import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

public class IndexDocumentDemo {
    public static void main(String args[]){
        try(TransportClient client = InitDemo.getClient()) {
            //创建indexRequest实例，指定 index type，id
            IndexRequest request = new IndexRequest("music_v1", "songs", "11");

            /*String jsonStr = "{\n" +
                    "  \"songName\" : \"right heere waitint\",\n" +
                    "  \"singer\" : \"Kody Blunt\",\n" +
                    "  \"lyrics\" : \"right here waiting for you\"\n" +
                    "}";

            request.source(jsonStr, XContentType.JSON);*/

            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("songName","right heere waitint");
            jsonMap.put("singer","Kody Blunt");
            jsonMap.put("lyrics", "right here waiting for you");
            request.source(jsonMap);

            client.index(request).get();



        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }
}
