package com.cnb.training.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.List;

/**
 below is and example of simple text search
 String qstr = "BBM92"; // this is the textual search query
 String field = "exchange_number"; // the field you hope to search for
 //QueryParser parser = new QueryParser( field, analyzer ); // a query parser that transforms a text string into Lucene's query object
 // Query query = parser.parse( qstr ); // this is Lucene's query object
 */

public class TextSearch {


    public static void main(String[] args) {

        String pathIndex = "C:\\ChemNetBase_data\\DOD2022\\mainIndex";
        try {
            Analyzer analyzer = new WhitespaceAnalyzer();

            // below is and example of  wildcard search

            Query query = new WildcardQuery(new Term("exchange_number", "N*"));

       // Okay, now let's open an index and search for documents
            Directory dir = FSDirectory.open(new File(pathIndex).toPath());
            IndexReader index = DirectoryReader.open(dir);

            // you need to create a Lucene searcher
            IndexSearcher searcher = new IndexSearcher(index);


            int top = 1000; // Let's just retrieve the  results
            TopDocs results = searcher.search(query, top); // retrieve the  results; retrieved results are stored in TopDocs
            ScoreDoc[] scoreDocs = results.scoreDocs;
            for (int i = 0; i < scoreDocs.length; ++i) {


                Document document = searcher.doc(scoreDocs[i].doc);

                List<IndexableField> fields = document.getFields();
                for (int j = 0; j < fields.size(); j++) {
                    IndexableField fieldout = fields.get(j);
                    System.out.println("name:" + fieldout.name() + " value:" + fieldout.stringValue());

                }
                System.out.println("==============NUMBER OF DOCS "+scoreDocs.length +"==================");
            }

            // remember to close the index and the directory
            index.close();
            dir.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
