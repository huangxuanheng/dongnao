package es.search;

import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutionException;

public class GetDocument {
    public static void main(String args[]){
        try (TransportClient client = InitDemo.getClient()){
            //显示的获取一个文档   GET /music_v1/songs/11

            GetRequest request = new GetRequest("music_v1", "songs", "11");

            request.fetchSourceContext(new FetchSourceContext(true));

            GetResponse reponse = client.get(request).get();
            String json = reponse.getSourceAsString();
            System.out.println(json);



        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }


}
