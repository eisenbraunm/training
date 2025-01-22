package com.cnb.training.lucene;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.core.WhitespaceAnalyzer;
import org.apache.lucene.analysis.miscellaneous.PerFieldAnalyzerWrapper;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.search.join.QueryBitSetProducer;
import org.apache.lucene.search.join.ScoreMode;
import org.apache.lucene.search.join.ToParentBlockJoinQuery;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.RAMDirectory;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * this is an example of building an index and saving it to the hard disk. It is using the  CNBfieldTyopes class to
 * define how the fields are stored
 * I encourage you to try various combinations of these and see how the index changes using Luke and attempt to create you own class for searching using
 * the many examples I have given in this package.
 * We use different analyzers here, to read about them read:
 * https://www.baeldung.com/lucene-analyzers
 *
 *this uses a perfield analyzer, to understand more about analyzers please read
 * https://www.hascode.com/lucene-by-example-specifying-analyzers-on-a-per-field-basis-and-writing-a-custom-analyzer/tokenizer/
 */

public class BuildCNBindex {

    public static void main(String[] args) throws IOException {
        String indexDirPath="C:\\temp\\myIndexTest";  // you should create a folder anywhere on your hard drive to write the index to.  (remember to clear it between runs!
        buildIndex(indexDirPath);
        searchIndex(indexDirPath);
    }

    public static void buildIndex(String indexDirPath) throws IOException {

        Directory dir = FSDirectory.open( new File( indexDirPath).toPath() );
        PerFieldAnalyzerWrapper pfanalyzer= createAnalyzer();

        IndexWriterConfig iwc = new IndexWriterConfig(pfanalyzer);
        iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
        IndexWriter writer = new IndexWriter(dir, iwc);

        String indexField1="name";
        String indexField2="exno";
        String indexField3="view_melting_Pt";
        String indexField4="melting_pt";
        String indexField5 ="noStore";
        String indexField6 ="colour";
        String indexField7="sortby";


        // first block
        final Document doc1 = new Document();

        doc1.add(new Field(indexField1, "abate", CNBFieldTypes.fieldTextTokenizeSearch())); //note the lower case for the value
        doc1.add(new Field(indexField2, "DLF49", CNBFieldTypes.fieldTextTokenizeSearch()));
        doc1.add(new Field(indexField3, "30&#176;", CNBFieldTypes.fieldTextViewNoTok()));  //note HTML entity for the the deg symbol (do not use named entities such as &deg;)
        CNBFieldTypes.addNumericFieldSearch(  doc1, indexField4, Double.parseDouble("30.0"));//note this has to be passes as an object with the correct datatype
        doc1.add(new Field(indexField5, "cat, horse This is a long string with we want sort search on but not stored in the database, ", CNBFieldTypes.fieldTextSearchNotStored()));
        doc1.add(new Field(indexField6, "red, green, blue", CNBFieldTypes.fieldTextTokenizeSearch()));
        CNBFieldTypes.addNumericFieldSort0nly(doc1, "sortby", Integer.parseInt("2"));
        writer.addDocument(doc1);

        final Document doc2 = new Document();
        doc2.add(new Field(indexField1, "acetic&#160;acid", CNBFieldTypes.fieldTextTokenizeSearch())); //note the lower case for the value also the non-breaking space html entity, so this name is not split into terms
        doc2.add(new Field(indexField2, "DLF49", CNBFieldTypes.fieldTextTokenizeSearch()));
        doc2.add(new Field(indexField3, "16.7&#176;", CNBFieldTypes.fieldTextViewNoTok()));
        CNBFieldTypes.addNumericFieldSearch(  doc2, indexField4, Double.parseDouble("16.7"));//note this has to be passes as an object with the correct datatype
        doc2.add(new Field(indexField5, "cat, dog This is a long string with we want sort search on but not stored in the database, ", CNBFieldTypes.fieldTextSearchNotStored()));
        doc2.add(new Field(indexField6, "white, green, brown", CNBFieldTypes.fieldTextTokenizeSearch()));
        CNBFieldTypes.addNumericFieldSort0nly(doc2, "sortby", Integer.parseInt("1"));
        writer.addDocument(doc2);

        final Document doc3 = new Document();
        doc3.add(new Field(indexField1, "technetium", CNBFieldTypes.fieldTextTokenizeSearch())); //note the lower case for the value
        doc3.add(new Field(indexField2, "KPN17", CNBFieldTypes.fieldTextTokenizeSearch()));
        doc3.add(new Field(indexField3, "2250&#176;", CNBFieldTypes.fieldTextViewNoTok()));
        CNBFieldTypes.addNumericFieldSearch(  doc3, indexField4, Double.parseDouble("2250"));//note this has to be passes as an object with the correct datatype
        doc3.add(new Field(indexField5, "badger, dog, horse This is a long string with we want sort search on but not stored in the database, ", CNBFieldTypes.fieldTextSearchNotStored()));
        doc3.add(new Field(indexField6, "white, green, blue", CNBFieldTypes.fieldTextTokenizeSearch()));
        CNBFieldTypes.addNumericFieldSort0nly(doc3, "sortby", Integer.parseInt("4"));
        writer.addDocument(doc3);

        final Document doc4 = new Document();
        doc4.add(new Field(indexField1, "neocinchophen", CNBFieldTypes.fieldTextTokenizeSearch())); //note the lower case for the value
        doc4.add(new Field(indexField2, "FZD66", CNBFieldTypes.fieldTextTokenizeSearch()));
        doc4.add(new Field(indexField3, "75&#176;", CNBFieldTypes.fieldTextViewNoTok()));
        CNBFieldTypes.addNumericFieldSearch(  doc4, indexField4, Double.parseDouble("75.0"));//note this has to be passes as an object with the correct datatype
        doc4.add(new Field(indexField5, "cat, chicken, dog This is a long string with we want sort search on but not stored in the database, ", CNBFieldTypes.fieldTextSearchNotStored()));
        doc4.add(new Field(indexField6, "red, green, brown", CNBFieldTypes.fieldTextTokenizeSearch()));
        CNBFieldTypes.addNumericFieldSort0nly(doc4, "sortby", Integer.parseInt("3"));
        writer.addDocument(doc4);


        writer.close();
        System.out.println("=================Index built- clear files in "+indexDirPath +" before running again ==============");

    }

    public static void searchIndex(String indexDirPath) throws IOException {
        Directory dir = FSDirectory.open( new File( indexDirPath).toPath() );
        PerFieldAnalyzerWrapper pfanalyzer= createAnalyzer();

       // String searchTerm="cat";
       // String fieldName="noStore";

       String searchTerm="red";
        String fieldName="colour";
        boolean reverse = false;


//first open the searcher
        IndexReader reader = DirectoryReader.open(dir);
        IndexSearcher indexSearcher = new IndexSearcher(reader);
        int totalDocs=0;
        try {
            Query query;
            String term = prepareTerm(fieldName,searchTerm);
            QueryParser queryParser = new QueryParser(fieldName, pfanalyzer);
            query = queryParser.parse(term );
            // query = queryParser.parse(term + "*");
            System.out.println("the query is: " + query.toString());

            SortField.Type sortType = SortField.Type.INT;
            SortField sortField = new SortField("sortby", sortType, reverse);
            Sort sort = new Sort(sortField);
            TopDocs topDocs = indexSearcher.search(query, 1000000, sort);
            ScoreDoc[] scoreDocs = topDocs.scoreDocs;
            totalDocs = scoreDocs.length;
            for (int i = 0; i < scoreDocs.length; ++i) {



                Document document = indexSearcher.doc(scoreDocs[i].doc);

                List<IndexableField> fields=    document.getFields();
                for (int j = 0; j < fields.size(); j++) {
                    IndexableField fieldout=fields.get(j);

                    System.out.println("name:"+ fieldout.name() +" value:"+ fieldout.stringValue());

                }
                System.out.println("===============================");
            }
            System.out.println("=============number of results "+scoreDocs.length+"===================");
        } catch (IOException ex) {
            System.out.println("IO exception searchForTerm :" + fieldName + ":" + searchTerm + " " + ex.getMessage());
        } catch (ParseException e) {
            System.out.println("Parse exception searchForTerm :" + fieldName + ":" + searchTerm + " ");
        } catch (Exception ee) {
            System.out.println("exception searchForTerm :" + fieldName + ":" + searchTerm + " " + ee.getMessage());
        }

    }





    public static PerFieldAnalyzerWrapper createAnalyzer() {
     String[] stopWords = {
             "in",  "at", "the",  "an", "not","not", "this", "that", "these","a","1+",
        };  // note sometimes we have to use it as At is a chemical element Astatine!, or got get rid of certain terms
        CharArraySet stopSet = StopFilter.makeStopSet(stopWords);

        Map<String, Analyzer> analyzerPerField = new HashMap<String, Analyzer>();
        // whitespace analyzers are case sensitive and stores then with upper and lowercase
        //they are only broken up by whitespaces, in some of our indexes we put in non-breaking spaces so things like chemical name will not be tokenized
        analyzerPerField.put("name", new WhitespaceAnalyzer());
        analyzerPerField.put("exno", new WhitespaceAnalyzer());

        analyzerPerField.put("noStore", new StandardAnalyzer(stopSet));



        PerFieldAnalyzerWrapper pw =
                new PerFieldAnalyzerWrapper(new StandardAnalyzer(), analyzerPerField);//this shows if you have not specified the analyzer it will use the StandardAnalyzer with the defulat stop set
        return pw;


    }

    //sometimes terms must be prepared in a specific way in order to search the index correctly
    private static String prepareTerm(String fieldName, String term) {
        if (fieldName.equalsIgnoreCase("name")) {
            term = term.toLowerCase();
          term = term.replaceAll(" ", "&#160;");

        }
        return term;
    }


}

