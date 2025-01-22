package com.cnb.training.lucene;


import org.apache.lucene.document.Document;
import org.apache.lucene.document.DoublePoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.File;
import java.util.List;

/**
Range searches in the main index
 A search can be submitted as a range with a minimum and maximum value - however some properties are stored in the
 index with minimum and maximum values.

 e.g. melting pt and boiling point

 //see the query below from ChemNetBaseResources sql database

 SELECT   TOP (200) CHCD_FIELDS.FIELD_ID, CHCD_FIELDS.LABEL, CHCD_FIELDS.INDEX_NAME, CHCD_FIELDS.MAXINDEXNAME, CHCD_FIELDS.DATA_TYPE, CHCD_FLAGS.SEARCH_FLAG,
 TBL_APPLICATION.ACRONYM, CHCD_FIELDS.CONDITIONAL_SQL
 FROM         CHCD_FIELDS INNER JOIN
 CHCD_FLAGS ON CHCD_FIELDS.FIELD_ID = CHCD_FLAGS.FIELD_ID INNER JOIN
 TBL_APPLICATION ON CHCD_FLAGS.APPLICATION_ID = TBL_APPLICATION.APPLICATION_ID
 WHERE     (TBL_APPLICATION.ACRONYM = 'DOD') AND (CHCD_FLAGS.SEARCH_FLAG = 1) AND (CHCD_FIELDS.DATA_TYPE = 'NUMERIC')

 We need to be able to perform range searches of range results in the product,
 as say,there is a compound with melting pt range of 31-35deg in the index, the user should get a hit for the foLLowing
 range searches
 search1= 32-33
 search2= 31-34
 search3 =33-36

 */

public class NumericRangeSearch {



    public static void main( String[] args ) {
        String fieldName="min_melting_point";

        String pathIndex= "C:\\ChemNetBase_data\\DOD2022\\mainIndex_num";

String minSearchVal="5.0";
       String maxSearchVal="10.0";
        double lessthan = Double.NEGATIVE_INFINITY;
        double morethan = Double.POSITIVE_INFINITY;
        try {
            BooleanQuery.Builder combinedQueryBuilder = new BooleanQuery.Builder();
            BooleanQuery.Builder combinedQueryRangeBuilder = new BooleanQuery.Builder();
            BooleanQuery.Builder combinedQueryRangeBuilderOuter = new BooleanQuery.Builder();

            Double minval = null;
            Double maxval = null;
            minval = Double.parseDouble(minSearchVal);
            maxval = Double.parseDouble(maxSearchVal);

            Query query1 = DoublePoint.newRangeQuery(fieldName, minval, maxval);
            combinedQueryBuilder.add(query1, BooleanClause.Occur.SHOULD);

            Query query2 = DoublePoint.newRangeQuery(fieldName.replace("min_", "max_"), minval, maxval);
            combinedQueryBuilder.add(query2, BooleanClause.Occur.SHOULD);
            //if the a range is submitted falls within the range in the index it is not found  i.e. index values min20.0 max 30 - search values 25 to 28
            //easiest way is to add these missing values using the equal boolean query
            //just looking for the minvalue will find these
            //must be greater than the min
            minval = Double.parseDouble(minSearchVal);
            maxval = null;
            Query query3 = DoublePoint.newRangeQuery(fieldName, minval, morethan);

            combinedQueryRangeBuilder.add(query3, BooleanClause.Occur.MUST);
            //and less then the maximum
            minval = null;
            maxval = Double.parseDouble(minSearchVal);  //note this IS correct the min value must be set the max
            Query query4 = DoublePoint.newRangeQuery(fieldName, lessthan, maxval);

            combinedQueryRangeBuilder.add(query4, BooleanClause.Occur.MUST);
            // if the range in the entry is wider than that in the query
            minval = Double.parseDouble(minSearchVal);
            maxval = null;
            Query query5 = DoublePoint.newRangeQuery(fieldName, lessthan, minval);
            combinedQueryRangeBuilderOuter.add(query5, BooleanClause.Occur.MUST);
            minval = null;
            maxval = Double.parseDouble(maxSearchVal);
            Query query6 = DoublePoint.newRangeQuery(fieldName.replace("min_", "max_"), maxval, morethan);
            combinedQueryRangeBuilderOuter.add(query6, BooleanClause.Occur.MUST);
            BooleanQuery combinedQuery = combinedQueryBuilder.build();
            System.out.println("Min max numeric query: " + combinedQuery.toString());


            //  now let's open an index and search for documents
            Directory dir = FSDirectory.open( new File( pathIndex ).toPath() );
            IndexReader index = DirectoryReader.open( dir );

            // you need to create a Lucene searcher
            IndexSearcher searcher = new IndexSearcher( index );
            Sort sort = new Sort(new SortField("sr", SortField.Type.INT,false));

            int top = 10000; // Let's just retrieve the results
            TopDocs results= searcher.search( combinedQuery, top, sort); // retrieve the top =results; retrieved results are stored in TopDocs
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
            System.out.println("=============number of results "+scoreDocs.length+"===================");
            // remember to close the index and the directory
            index.close();
            dir.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}


