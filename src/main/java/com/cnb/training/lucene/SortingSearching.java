package com.cnb.training.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.document.*;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.BytesRef;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
This class builds a small index and creates in the path set in pathIndex variable (change this to what you require).

There is a text example and a numeric example, you can change which is sorted by changing which sort expression is
uncommented on lines

You can also add a query to this if you require by uncommenting lines

I would highly recommend looking the CNBFieldTypes I have written.  I use this class in most of the index builders to make adding
different types of fields easier.  Each type and what is used for is described within the class.  Experiment with these for search
sorting and quering - both numeric and text to observe the differing behavior.

 */

public class SortingSearching {





    public static void main(String[] args) throws IOException, ParseException {
        //build the index
        String pathIndex = "C:\\CNBindexing\\ChemIndexes\\sortTest";

        Directory dir = FSDirectory.open( new File( pathIndex ).toPath() );
        File dirFile=new File(pathIndex);
        removeAllFilesFromDir( dirFile);
        Analyzer analyzer =new WhitespaceAnalyzer();
        IndexWriterConfig config = new IndexWriterConfig( analyzer );
        IndexWriter writer = new IndexWriter(dir,config);

        // This is the field setting for metadata field (no tokenization, searchable, and stored).
        // this is the old string field
        // This is the field setting for field (no tokenization searchable and stored).
        FieldType fieldTextViewOnly = new FieldType();
        fieldTextViewOnly.setOmitNorms( true );

        fieldTextViewOnly.setIndexOptions( IndexOptions.DOCS );
        fieldTextViewOnly.setStored( true );
        fieldTextViewOnly.setTokenized( false );
        fieldTextViewOnly.setStoreTermVectors( false );
        fieldTextViewOnly.setStoreTermVectorPositions( false );
        fieldTextViewOnly.freeze();

        // This is the field setting for normal text field (tokenized, searchable, store document vectors)
        FieldType fieldTypeText = new FieldType();
        fieldTypeText.setIndexOptions( IndexOptions.DOCS );
        fieldTypeText.setStoreTermVectors( true );
        fieldTypeText.setStoreTermVectorPositions( true );
        fieldTypeText.setTokenized( true );
        fieldTypeText.setStored( true );
        fieldTypeText.freeze();



// first document
        Document d = new Document();
        // Add each field to the document with the appropriate field type options
        d.add( new Field( "docno", "AA23", fieldTextViewOnly) );
        d.add( new Field( "text", "hello", fieldTypeText ) );
        //add the sorting values
        d.add( new Field( "title", "qqqq", fieldTypeText ) );
        d.add(new SortedDocValuesField("title", new BytesRef("qqqq")));



        double value = Double.parseDouble("15.0");
        d.add(new DoublePoint("some_no", (Double) value ));
        d.add(new StoredField("some_no", (Double) value ));
        d.add(new DoubleDocValuesField("some_no", (Double) value ));


        d.add( new Field( "text", "hello", fieldTypeText ) );
        // Add the document to the index
        System.out.println( "indexing document " + "AA23" );
        writer.addDocument( d );


// second  document
        Document d2 = new Document();
        // Add each field to the document with the appropriate field type options
        d2.add( new Field( "docno", "MM23", fieldTextViewOnly ) );
        d2.add( new Field( "text", "hello", fieldTypeText ) );
        //add the sorting values
        d2.add( new Field( "title", "mmmmmm", fieldTypeText ) );
        d2.add(new SortedDocValuesField("title", new BytesRef("mmmmmm")));

        double value2 = Double.parseDouble("3");
        d2.add(new DoublePoint("some_no", (Double) value2 ));
        d2.add(new StoredField("some_no", (Double) value2 ));
        d2.add(new DoubleDocValuesField("some_no", (Double) value2 ));

        // Add the document to the index
        System.out.println( "indexing document " + "MM23" );
        writer.addDocument( d2 );

        // third  document
        Document d3 = new Document();
        // Add each field to the document with the appropriate field type options
        d3.add( new Field( "docno", "ZZ23", fieldTextViewOnly ) );
        d3.add( new Field( "text", "hello there", fieldTypeText ) );
        //add the sorting values
        d3.add( new Field( "title", "zzzzz", fieldTypeText ) );
       d3.add(new SortedDocValuesField("title", new BytesRef("zzzzzz")));

        double value3 = Double.parseDouble("66.6");
        d3.add(new DoublePoint("some_no", (Double) value3 ));
        d3.add(new StoredField("some_no", (Double) value3 ));
        d3.add(new DoubleDocValuesField("some_no", (Double) value3));



        // Add the document to the index
        System.out.println( "indexing document " + "ZZ23" );
        writer.addDocument( d3 );
        writer.close();
        dir.close();


        ////////////////////////////////////////////////////////////////////////
        System.out.println("==============SEARCH and SORT INDEX==================");
      //  String qstr = "hello"; // this is the textual search query
      //  String field = "text"; // the field you hope to search for
      // QueryParser parser = new QueryParser( field, analyzer ); // a query parser that transforms a text string into Lucene's query object
     // Query query = parser.parse( qstr ); // this is Lucene's query object
        Query query = new MatchAllDocsQuery();
        //************sorting**********************
        // Play with the two sorts below by selecting which is uncommenteed
        Boolean reverse=true;
       // Sort sort = new Sort(new SortField("title", SortField.Type.STRING, reverse));

       Sort sort = new Sort(new SortField("some_no", SortField.Type.DOUBLE,reverse));
        //**********************************




        // Okay, now let's open an index and search for documents
        Directory dir2 = FSDirectory.open(new File(pathIndex).toPath());
        IndexReader index = DirectoryReader.open(dir2);

        // you need to create a Lucene searcher
        IndexSearcher searcher = new IndexSearcher(index);


        int top = 1000; // Let's just retrieve the  results
        TopDocs results = searcher.search(query, top,sort); // retrieve the  results; retrieved results are stored in TopDocs
        ScoreDoc[] scoreDocs = results.scoreDocs;
        for (int i = 0; i < scoreDocs.length; ++i) {

            System.out.println("================================");
            Document document = searcher.doc(scoreDocs[i].doc);

            List<IndexableField> fields = document.getFields();
            for (int j = 0; j < fields.size(); j++) {
                IndexableField fieldout = fields.get(j);
                System.out.println("name:" + fieldout.name() + " value:" + fieldout.stringValue());

            }

        }
        System.out.println("==============NUMBER OF DOCS "+scoreDocs.length +"==================");
        // remember to close the index and the directory
        index.close();
        dir.close();


    }










    public static void removeAllFilesFromDir(File dir) {
        File[] files = dir.listFiles();//Put the file subdirectory and submisss into the file array
        if (files != null) {//If it contains files for deletion operation
            for (int i = 0; i < files.length; i++) {
                if (files[i].isFile()) {//Delete subfacate
                    files[i].delete();
                } else if (files[i].isDirectory()) {//Delete the file of the sub -directory through the recursive method
                    removeAllFilesFromDir(files[i]);
                }
                files[i].delete();//Delete subdirectory
            }
        }
    }
}
