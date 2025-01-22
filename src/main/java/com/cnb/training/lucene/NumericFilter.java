package com.cnb.training.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
We have a 2 tier index for each of the products.  Numeric values are held in the mainindex_num.
So we need to get search results for a numeric query the mainindex_num  then  the resulting array
used to filter the main index to obtain the final result set
This test shows how to build a filter from the numeric index to the main index

 to see indexes names etc you must look at the ChemnetbaseResources database for example in this query below

 SELECT    CHCD_FIELDS.FIELD_ID, CHCD_FIELDS.LABEL, CHCD_FIELDS.INDEX_NAME, CHCD_FIELDS.MAXINDEXNAME, CHCD_FIELDS.DATA_TYPE, CHCD_FLAGS.SEARCH_FLAG,
 TBL_APPLICATION.ACRONYM, CHCD_FIELDS.CONDITIONAL_SQL
 FROM         CHCD_FIELDS INNER JOIN
 CHCD_FLAGS ON CHCD_FIELDS.FIELD_ID = CHCD_FLAGS.FIELD_ID INNER JOIN
 TBL_APPLICATION ON CHCD_FLAGS.APPLICATION_ID = TBL_APPLICATION.APPLICATION_ID
 WHERE     (TBL_APPLICATION.ACRONYM = 'DOD') AND (CHCD_FLAGS.SEARCH_FLAG = 1) AND (CHCD_FIELDS.DATA_TYPE = 'NUMERIC')

 */

public class NumericFilter {


    public static void main( String[] args ) {
        String pathIndex2 = "C:\\ChemNetBase_data\\DOD2022\\mainIndex_num";
        String pathIndex1 = "C:\\ChemNetBase_data\\DOD2022\\mainIndex";
        try {

            Analyzer analyzer = new WhitespaceAnalyzer();
            double lessthan = Double.NEGATIVE_INFINITY;
            double morethan = Double.POSITIVE_INFINITY;
          //  Query query2 =  DoublePoint.newRangeQuery("mass_spec_molecular_formula",150.0,morethan  );
       Query query2 =  DoublePoint.newRangeQuery("mass_spec_molecular_formula",lessthan, 300.0 );

       //
            Directory dir2 = FSDirectory.open( new File( pathIndex2 ).toPath() );
            IndexReader indexNum = DirectoryReader.open( dir2 );


            // you need to create a Lucene searcher
            IndexSearcher searchNum = new IndexSearcher( indexNum );

            //build up a term list to use as a queryfilter from the numeric index to create the array which will use for filtering

            TopDocs resultsNum= searchNum.search( query2, 100000 ); // retrieve the results stored in TopDocs, this shows the number of docs that can be returned
            ScoreDoc[] scoreDocsNum = resultsNum.scoreDocs;
            List<Term> termsList = new LinkedList<>();



            System.out.println("============== NUMBER OF DOCS from numeric index "+scoreDocsNum.length +"==================");
            BooleanQuery.setMaxClauseCount(1000000);  //the default value is 1024, this must be increased for bigger
            BooleanQuery.Builder builderNum = new BooleanQuery.Builder();
            QueryParser parser = new QueryParser( "exchange_number", analyzer );

            for (int i = 0; i < scoreDocsNum.length; ++i) {
                int docId = scoreDocsNum[i].doc;
                Document doc = searchNum.doc(docId);
                String  exno=doc.get("exchange_number");
                System.out.println("exno found in numeric index: " +exno);
                Query subQuery = parser.parse( exno); // this is Lucene's query object
                builderNum.add(new BooleanClause(subQuery , BooleanClause.Occur.SHOULD));  //these have to be should i.e. a list of exchange number 'OR's
            }

            BooleanQuery queryNumericFilter =  builderNum.build();
            System.out.println("============== FINISHED NUMERIC INDEX SEARCH  number found in the numeric index :"+scoreDocsNum.length+" ==================");
            //======now let us prepare for searching the main index  =======


            Directory dirMain = FSDirectory.open(new File(pathIndex1).toPath());
            IndexReader indexMain = DirectoryReader.open(dirMain);
            // you need to create a Lucene searcher
            IndexSearcher searcherMain = new IndexSearcher(indexMain);
            //we must be able to pull back any of the entries from the main index, so we need an 'OR' link for all entries so they are available
            BooleanQuery.Builder builderFinal = new BooleanQuery.Builder();
            QueryParser parserAllEntries = new QueryParser( "all_entries", analyzer );


            Query queryAllEntries = parserAllEntries.parse( "y" ); // this is Lucene's query object and we want to start with all entries then filter them
            builderFinal.add(queryAllEntries,BooleanClause.Occur.SHOULD);



            //now create queries for any searches fro properties required from the main index - this must be returned with a MUST.

            QueryParser parserMainIndex1 = new QueryParser( "type_of_compound", new StandardAnalyzer() ); // a query parser that transforms a text string into Lucene's query object

            String searchValue="D.B.15500"; //this is the value entered for type of compound on the search page
            //prepare the string from searching
            //fist convert to lowercase
            searchValue=searchValue.toLowerCase();
        //as lucene breaks up string into terms based on spaces and punctuation, we have replaced the full stops with zeros in the index
            //therefore we must do the same in the index;
            String mainIndexTextStr =  searchValue = searchValue.replace(".", "o");

            Query mainIndexQuery = parserMainIndex1 .parse( mainIndexTextStr );
            builderFinal.add(mainIndexQuery, BooleanClause.Occur.FILTER);//MUST is the same as FILTER without doc scoring, so using FILTER as more efficient


            builderFinal.add(queryNumericFilter, BooleanClause.Occur.FILTER);//these are the values from the numerical index, MUST is the same as FILTER without doc scoring

          Query finalQuery = builderFinal.build();
            int top1 = 100000; // this would be from the main index and is what will be returned to the results, page, so this would usually be the number of hits per page
            TopDocs resultsNUM= searcherMain.search( finalQuery, top1 ); // retrieved results are stored in TopDocs
            ScoreDoc[] scoreDocsMain = resultsNUM.scoreDocs;
            System.out.println("============== FINAL NUMBER OF DOCS "+scoreDocsMain.length +"==================");
            for (int i = 0; i < scoreDocsMain.length; ++i) {
                Document document = searcherMain.doc(scoreDocsMain[i].doc);
                List<IndexableField> fields=    document.getFields();
                System.out.println("==============hit compound fields==================");
                for (int j = 0; j < fields.size(); j++) {
                    IndexableField fieldout=fields.get(j);
                    System.out.println("name:"+ fieldout.name() +" value:"+ fieldout.stringValue());

                }

            }


           // MatchAllDocsQuery returns all documents
            System.out.println("==============NUMBER OF DOCS "+scoreDocsMain.length +"==================");
            indexMain.close();
            dirMain.close();


            indexNum.close();
            dir2.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
