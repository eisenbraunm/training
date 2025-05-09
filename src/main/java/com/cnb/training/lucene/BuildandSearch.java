package com.cnb.training.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * this is a very simple index build and search illustrating how and index is built and searched
 * In this situation the index in built in memory, in the other examples it is output to directory which you will specify
 *
 *
 */

public class BuildandSearch {

    public static void main(String[] args) throws IOException {
        Directory dir = new RAMDirectory();
        Analyzer analyzer = new StandardAnalyzer();
        IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(dir, iwc);

        // first block
        final Document sku1_1 = new Document();
        sku1_1.add(new TextField("color", "red", Field.Store.YES));
        sku1_1.add(new TextField("size", "xl", Field.Store.YES));
        sku1_1.add(new TextField("type", "sku", Field.Store.YES));

        final Document sku1_2 = new Document();
        sku1_2.add(new TextField("color", "blue", Field.Store.YES));
        sku1_2.add(new TextField("size", "m", Field.Store.YES));
        sku1_2.add(new TextField("type", "sku", Field.Store.YES));

        final Document product1 = new Document();
        product1.add(new TextField("brand", "adidas", Field.Store.YES));
        product1.add(new TextField("title", "dress", Field.Store.YES));
        product1.add(new TextField("type", "product", Field.Store.YES));

        writer.addDocuments(Arrays.asList(new Document[] { sku1_1, sku1_2, product1 }));

        // second block
        final Document sku2_1 = new Document();
        sku2_1.add(new TextField("color", "red", Field.Store.YES));
        sku2_1.add(new TextField("size", "m", Field.Store.YES));
        sku2_1.add(new TextField("type", "sku", Field.Store.YES));

        final Document sk2_2 = new Document();
        sk2_2.add(new TextField("color", "blue", Field.Store.YES));
        sk2_2.add(new TextField("size", "xl", Field.Store.YES));
        sk2_2.add(new TextField("type", "sku", Field.Store.YES));

        final Document product2 = new Document();
        product2.add(new TextField("brand", "puma", Field.Store.YES));
        product2.add(new TextField("title", "dress", Field.Store.YES));
        product2.add(new TextField("type", "product", Field.Store.YES));

        writer.addDocuments(Arrays.asList(new Document[] { sku2_1, sk2_2, product2 }));

        writer.close();//    ww   w   . d   e  mo  2   s .  c   o   m

        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher searcher = new IndexSearcher(reader);

        // i want to find out all products of the query red m dress
        BooleanQuery.Builder builder = new BooleanQuery.Builder();
        builder.add(new BooleanClause(new TermQuery(new Term("color", "red")), BooleanClause.Occur.MUST));

        builder.add(new BooleanClause(new TermQuery(new Term("size", "m")), BooleanClause.Occur.MUST));
        BooleanQuery childQuery = builder.build();
        ToParentBlockJoinQuery parentQuery = new ToParentBlockJoinQuery(childQuery, new QueryBitSetProducer(new TermQuery(new Term("type", "product"))), ScoreMode.Avg);




        TopDocs results = searcher.search(parentQuery, 100);
        ScoreDoc[] scoreDocs = results.scoreDocs;
        for (int i = 0; i < scoreDocs.length; ++i) {

            System.out.println(searcher.explain(parentQuery, scoreDocs[i].doc));
            System.out.println("+++++++++++++++++++++++++++++++++");
            Document document = searcher.doc(scoreDocs[i].doc);

        List<IndexableField>  fields=    document.getFields();
            for (int j = 0; j < fields.size(); j++) {
                IndexableField field=fields.get(j);
                System.out.println("name:"+ field.name() +" value:"+ field.stringValue());

            }
            System.out.println("================================");
        }
    }
}