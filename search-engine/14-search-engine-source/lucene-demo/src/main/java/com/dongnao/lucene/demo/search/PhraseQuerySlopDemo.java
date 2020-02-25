package com.dongnao.lucene.demo.search;

import java.io.IOException;
import java.nio.file.Paths;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.PhraseQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import com.dongnao.lucene.demo.analizer.ik.IKAnalyzer4Lucene7;

public class PhraseQuerySlopDemo {

	/**
	 * lucene 搜索查询示例
	 */
	public static void main(String[] args) throws IOException, ParseException {
		// 使用的分词器
		Analyzer analyzer = new IKAnalyzer4Lucene7(true);
		// 索引存储目录
		Directory directory = FSDirectory.open(Paths.get("f:/test/indextest"));
		// 索引读取器
		IndexReader indexReader = DirectoryReader.open(directory);
		// 索引搜索器
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		// String name = "ThinkPad X1 Carbon 20KH0009CD/25CD 超极本轻薄笔记本电脑联想";

		// 3、PhraseQuery 短语查询
		PhraseQuery phraseQuery2 = new PhraseQuery(1, "name", "thinkpad",
				"carbon");
		System.out.println("************** phrase 短语查询  ******************");
		doSearch(phraseQuery2, indexSearcher);

		// slop示例
		PhraseQuery phraseQuery2Slop = new PhraseQuery(3, "name", "carbon",
				"thinkpad");
		System.out.println("********** phrase slop 短语查询  ***************");
		doSearch(phraseQuery2Slop, indexSearcher);

		PhraseQuery phraseQuery3 = new PhraseQuery("name", "笔记本电脑", "联想");
		System.out.println("************** phrase 短语查询  ******************");
		doSearch(phraseQuery3, indexSearcher);

		// slop示例
		PhraseQuery phraseQuery3Slop = new PhraseQuery(2, "name", "联想",
				"笔记本电脑");
		System.out.println("************** phrase s 短语查询  ******************");
		doSearch(phraseQuery3Slop, indexSearcher);

		// 使用完毕，关闭、释放资源
		indexReader.close();
		directory.close();
	}

	private static void doSearch(Query query, IndexSearcher indexSearcher)
			throws IOException {
		// 打印输出查询
		System.out.println("query:  " + query.toString());

		// 搜索，得到TopN的结果（结果中有命中总数，topN的scoreDocs（评分文档（文档id，评分）））
		TopDocs topDocs = indexSearcher.search(query, 10); // 前10条

		System.out.println("**** 查询结果 ");
		// 获得总命中数
		System.out.println("总命中数：" + topDocs.totalHits);
		// 遍历topN结果的scoreDocs,取出文档id对应的文档信息
		for (ScoreDoc sdoc : topDocs.scoreDocs) {
			// 根据文档id取存储的文档
			Document hitDoc = indexSearcher.doc(sdoc.doc);
			System.out.println("-------------- docId=" + sdoc.doc + ",score="
					+ sdoc.score);
			// 取文档的字段
			System.out.println("prodId:" + hitDoc.get("prodId"));
			System.out.println("name:" + hitDoc.get("name"));
			System.out.println("simpleIntro:" + hitDoc.get("simpleIntro"));
			System.out.println("price:" + hitDoc.get("price"));

			System.out.println();
		}

	}
}
