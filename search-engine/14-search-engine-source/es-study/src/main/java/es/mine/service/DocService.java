package es.mine.service;

import com.alibaba.fastjson.JSONObject;
import es.mine.Settings;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.Set;

public class DocService {

    //索引文档
    public void indexDoc(String indexName, String jsonDoc){
        IndexWriter writer = null;
        try {
            String indexDir = Settings.DATA_DIR + "\\" + indexName;
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

    public Document json2Doc(String strDoc){
        Document doc = new Document();

        JSONObject jsonDoc = JSONObject.parseObject(strDoc);
        Set<String> keys = jsonDoc.keySet();

        for (String key: keys){
            doc.add(new TextField(key, jsonDoc.getString(key), Field.Store.YES));
        }
        return doc;
    }


}
