package lucene;


import com.alibaba.fastjson.JSONObject;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Set;

public class HelloWorld {

    public static void main(String args[]){
        String indexDir = "D:\\tmp\\lucene_data\\product";

        //createIndex(indexDir);



                String jsonDoc = "{\n" +
                "\"songName\" : \"could this be love\",\n" +
                "\"singer\" : \"James\",\n" +
                "\"lyrics\" : \"Could This Be love,Woke Up This Morning Just Sat In My Bed,8 a.m. First Thing In My Head,Is A Certain Someone Who's Always On My Mind,He Treats Me\"\n" +
                "}";
        //indexDoc(indexDir, jsonDoc);

        query(indexDir, "love");
    }

    //创建索引
    public static void createIndex(String indexDir){
        IndexWriter writer = null;
        try {
            //准备目录
            Directory directory = FSDirectory.open(Paths.get((indexDir)));
            //准备分词器
            Analyzer analyzer = new StandardAnalyzer();
            //准备config
            IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
            //创建索引的实例
            writer = new IndexWriter(directory, iwConfig);

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }


    }


    //索引文档
    public static void indexDoc(String indexDir, String jsonDoc){
        IndexWriter writer = null;
        try {
            //准备目录
            Directory directory = FSDirectory.open(Paths.get((indexDir)));
            //准备分词器
            Analyzer analyzer = new StandardAnalyzer();
            //准备config
            IndexWriterConfig iwConfig = new IndexWriterConfig(analyzer);
            //创建索引的实例
            writer = new IndexWriter(directory, iwConfig);

            Document doc = json2Doc(jsonDoc);
            writer.addDocument(doc);

            System.out.println("indexed doc suc...");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static Document json2Doc(String strDoc){
        Document doc = new Document();

        JSONObject jsonDoc = JSONObject.parseObject(strDoc);
        Set<String> keys = jsonDoc.keySet();

        for (String key: keys){
            doc.add(new TextField(key, jsonDoc.getString(key), Field.Store.YES));
        }
        return doc;
    }

    //查询文档
    public static String query(String indexDir, String queryStr){
        String result = "";
        IndexReader reader = null;
        try {
            //准备目录
            Directory directory = FSDirectory.open(Paths.get((indexDir)));
            //拿到reader
            reader = DirectoryReader.open(directory);
            //创建indexSearcher实例
            IndexSearcher searcher = new IndexSearcher(reader);
            //准备分词器
            Analyzer analyzer = new StandardAnalyzer();

            //创建解析器
            QueryParser parser = new QueryParser("songName", analyzer);

            Query query = parser.parse(queryStr);
            TopDocs hits = searcher.search(query, 10);
            System.out.println("hits.size:" + hits.totalHits);

            for (ScoreDoc scoreDoc :hits.scoreDocs){
                //拿到文档实例
                Document doc = searcher.doc(scoreDoc.doc);
                //拿到所有文档字段
                List<IndexableField> fields = doc.getFields();

                //处理文档字段
                for (IndexableField f : fields) {
                    result += f.name() + ":" + f.stringValue() + ",\r\n";
                }
                System.out.println(result);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }







}
