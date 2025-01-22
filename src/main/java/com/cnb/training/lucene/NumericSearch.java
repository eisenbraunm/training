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

test for  numeric searches in the BROWSE
 showing greater and lesser searching on a browse index and ranges
 change the path to a browse index which you have on you system
 There are a few query options here to show their operation, just change which are commented out to test
not that mass_spec_molecular_formula and  does not have a range, and is called accurate mass in the product.
 Melting point has a min and max
these then appear in the browse index on the main search page
see the query below from ChemNetBaseResources sql database

 Wr are interested in the first item of the results as this will be used to determine which page the look ahead function in the browser to open.

 SELECT   TOP (200) CHCD_FIELDS.FIELD_ID, CHCD_FIELDS.LABEL, CHCD_FIELDS.INDEX_NAME, CHCD_FIELDS.MAXINDEXNAME, CHCD_FIELDS.DATA_TYPE, CHCD_FLAGS.SEARCH_FLAG,
 TBL_APPLICATION.ACRONYM, CHCD_FIELDS.CONDITIONAL_SQL
 FROM         CHCD_FIELDS INNER JOIN
 CHCD_FLAGS ON CHCD_FIELDS.FIELD_ID = CHCD_FLAGS.FIELD_ID INNER JOIN
 TBL_APPLICATION ON CHCD_FLAGS.APPLICATION_ID = TBL_APPLICATION.APPLICATION_ID
 WHERE     (TBL_APPLICATION.ACRONYM = 'DOD') AND (CHCD_FLAGS.SEARCH_FLAG = 1) AND (CHCD_FIELDS.DATA_TYPE = 'NUMERIC')

 */

public class NumericSearch {


    public static void main( String[] args ) {
 String pathIndex ="C:\\ChemNetBase_data\\DOD2022\\browse\\mass_spec_molecular_formula";
 //String pathIndex ="C:\\ChemNetBase_data\\DOD2022\\browse\\min_melting_point";
 Double searchvalue=900.0;
        try {
String fieldName="search";  // the name of searchable field in the index
           // double lessthan = Double.NEGATIVE_INFINITY;
            double morethan = Double.POSITIVE_INFINITY;
           //  Query query =  DoublePoint.newExactQuery( "search",		searchvalue);

            // less than or more than must use the range query option
           // Query query =  DoublePoint.newRangeQuery(fieldName,lessthan, searchvalue );


             //below shows range query for browse, we are only interested in those more that the value
            //we would take the first value to determine the point to open the browse
            boolean minInclusive = true;
            boolean maxInclusive = true;
            Double minval = searchvalue;

            Query query = DoublePoint.newRangeQuery(fieldName, minval, morethan);



            //++++++++++++++++++++++++++ Okay, now let's open an index and search for documents +++++++++++++++++++++++++++++++
            Directory dir = FSDirectory.open( new File( pathIndex ).toPath() );
            IndexReader index = DirectoryReader.open( dir );

            // you need to create a Lucene searcher
            IndexSearcher searcher = new IndexSearcher( index );
            Sort sort = new Sort(new SortField("sr", SortField.Type.INT,false));

            int top = 10000; // Let's just retrieve the results
            TopDocs results= searcher.search( query, top, sort); // retrieve the top =results; retrieved results are stored in TopDocs
            ScoreDoc[] scoreDocs = results.scoreDocs;
            String firstTermValue=null;
            String FirstermSR=null;
            for (int i = 0; i < scoreDocs.length; ++i) {



                Document document = searcher.doc(scoreDocs[i].doc);

                List<IndexableField> fields=    document.getFields();
                for (int j = 0; j < fields.size(); j++) {
                    IndexableField fieldout=fields.get(j);


                    System.out.println("name:"+ fieldout.name() +" value:"+ fieldout.stringValue());
                    if(i==1 && fieldout.name().equalsIgnoreCase("term")){
                        firstTermValue=fieldout.stringValue();

                    }
                    if(i==1 && fieldout.name().equalsIgnoreCase("sr")){
                        FirstermSR=fieldout.stringValue();

                    }
                }
                System.out.println("===============================");
            }
            System.out.println("=============number of results "+scoreDocs.length+"===================");
            System.out.println("=============the first term we look for in the browse is: "+firstTermValue+" sort rank: "+ FirstermSR+" ===================");
            // remember to close the index and the directory
            index.close();
            dir.close();

        } catch ( Exception e ) {
            e.printStackTrace();
        }
    }

}
