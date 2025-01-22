package com.cnb.training.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.List;

/**
test for boolean searches, point the pathIndex to one existing on your machine
 MUST : A Document makes it to the list of the results, if and only if it contains that clause. (like AND)
 MUST NOT : It’s the exact opposite case. It is obligatory, for a Document to make it to the result list, not to contain that clause.(like NOT)
 SHOULD : This is for clauses that can occur in a Document, but it is not necessary for them to include it in order to make it to the results.(like OR)


 */
public class BooleanSearch {
    public static void main( String[] args ) {
        String pathIndex = "C:\\ChemNetBase_data\\DOD2022\\mainIndex";
        try {
            Analyzer analyzer = new WhitespaceAnalyzer();
            BooleanQuery.Builder builder = new BooleanQuery.Builder();
            builder.add(new BooleanClause(new WildcardQuery(new Term("exchange_number", "BBS*")), BooleanClause.Occur.MUST));

            QueryParser parser = new QueryParser( "dictionary_subset", analyzer ); // a query parser that transforms a text string into Lucene's query object
            String qstr = "organics"; // this is the textual search query
            Query subQuery = parser.parse( qstr ); // this is Lucene's query object

            builder.add(new BooleanClause(subQuery , BooleanClause.Occur.SHOULD));
            BooleanQuery query  = builder.build();

            // Okay, now let's open an index and search for documents
            Directory dir = FSDirectory.open( new File( pathIndex ).toPath() );
            IndexReader index = DirectoryReader.open( dir );

            // you need to create a Lucene searcher
            IndexSearcher searcher = new IndexSearcher( index );


            int top = 10; // Let's just retrieve the talk 10 results
            TopDocs results= searcher.search( query, top ); // retrieve the top 10 results; retrieved results are stored in TopDocs
            ScoreDoc[] scoreDocs = results.scoreDocs;
            for (int i = 0; i < scoreDocs.length; ++i) {
                Document document = searcher.doc(scoreDocs[i].doc);
                List<IndexableField> fields=    document.getFields();
                for (int j = 0; j < fields.size(); j++) {
                    IndexableField fieldout=fields.get(j);
                    System.out.println("name:"+ fieldout.name() +" value:"+ fieldout.stringValue());

                }
                System.out.println("================================");
            }

            // remember to close the index and the directory
            index.close();
            dir.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
