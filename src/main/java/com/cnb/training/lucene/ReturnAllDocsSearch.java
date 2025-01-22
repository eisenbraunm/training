package com.cnb.training.lucene;


import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.List;

/**

shows how to retrieve all the documents in an index
 */
public class ReturnAllDocsSearch {


    public static void main(String[] args) {

        String pathIndex = "C:\\ChemNetBase_data\\DOD2022\\browse\\molecular_formula";


        try {


            Directory dir = FSDirectory.open(new File(pathIndex).toPath());
            IndexReader index = DirectoryReader.open(dir);

            // you need to create a Lucene searcher
            IndexSearcher searcher = new IndexSearcher(index);
            Query query = new MatchAllDocsQuery();

            int top = 100000; // Let's just retrieve the  results
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
