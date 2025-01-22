package com.cnb.training.util;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class HTMLparser {

    public static final String[] ABC = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    //public static final String[] ABC = {"B"};
    public static int BEFOREOFFSET = 20;
    public static int AFTEROFFSET = 20;

    public static String handleSAXParseEx(SAXParseException ex, String finalString) {
        String ErrorDesc = "";
        int lineNumber = ex.getLineNumber();
        String replace="<?xml version=\"1.0\"?><r>";
        int columnNumber = ex.getColumnNumber()-replace.length();

        String[] lines = finalString.split("\\n");
        String line = lines[lineNumber - 1];
        line= line.replace("<?xml version=\"1.0\"?><r>", "");
        line=line.replace("</r>", "");
        line=line.replace("<?xml version=\"1.0\"?>", "");
        int fromIndexToShow = columnNumber - BEFOREOFFSET > 0 ? columnNumber - BEFOREOFFSET : 0;
        int toIndexToShow = columnNumber + AFTEROFFSET < line.length() ? columnNumber + AFTEROFFSET : line.length();

        ErrorDesc = "HTML isn't well-formed!  Error in '" + line.substring(fromIndexToShow, toIndexToShow) + "'\n "+ex.getMessage();

        return ErrorDesc;

    }

    //this checks for brakets as well
    public static String validateHTMLString(String testString) {
        String ErrorDesc= "Valid";
        if (testString!=null){
            ErrorDesc=   parseHTMLString(testString.toString());

            if (ErrorDesc.equalsIgnoreCase("Valid")) {

                if (!StringUtil.checkParentheses(testString.toString())) {

                    ErrorDesc= "Parentheses not opened/closed correctly. " + testString.toString();

                } else if (!StringUtil.checkSqBrackets(testString.toString())) {

                    ErrorDesc="Square brackets not opened/closed correctly. " + testString.toString();

                }

            }
        }
        return ErrorDesc;

    }



    public static String parseHTMLString(String testString) {
        String ErrorDesc = "Valid";
        testString = testString.trim();
        String entityTest=testString;
        //need a regex to remove html entities
        testString = testString.replaceAll("&#{0,1}[A-Za-z0-9]+;", "");//otherwise reject entities

        testString = "<?xml version=\"1.0\"?><r>" + testString + "</r>";
        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            InputStream in = new ByteArrayInputStream(testString.getBytes("UTF-8"));
            DefaultHandler handler = new SaxHandler();

            saxParser.parse(in, handler);

        } catch (SAXParseException ex) {

            ErrorDesc = handleSAXParseEx(ex, testString);
        } catch (SAXException ex) {
            ErrorDesc = "HTML isn't well-formed!";

        } catch (ParserConfigurationException ex) {
            ErrorDesc = " Error in Parsing!";


        } catch (IOException ex) {
            ErrorDesc = "IO error " + ex.getMessage();
        }catch (Exception ex) {
            ErrorDesc = "Fatal " + ex.getMessage();
        }


        return ErrorDesc;

    }

    private static final class SaxHandler extends DefaultHandler {

        // we enter to element 'qName':
        public void startElement(String uri, String localName,
                                 String qName, Attributes attrs) throws SAXException {

            if (qName.equals("r")) {
                // ... here process element start:
            }
        }

        // this is called when document is not valid:
        public void error(SAXParseException ex) throws SAXException {
            //System.out.println("ERROR *********************: [at " + ex.getLineNumber() +
            //            "] " + ex);
        }

        // this is called when document is not well-formed:
        public void fatalError(SAXParseException ex) throws SAXException {
            // System.out.println("*************FATAL_ERROR: [at " +
            //     ex.getLineNumber() + "] " + ex);

        }
        public void warning(SAXParseException ex) throws SAXException {
            //   System.out.println("WARNING: [at " +
            //         ex.getLineNumber() + "] " + ex);
        }
    }



    public static void main(String[] args) {
        //   String html = "<p class=\"anotherClass\"> Here is some text the value is for H<sub>2</sub>) is > 1 and < 100 <p>";
        String html = "is > 1 and  100";
        // html =" >thwe";
        String html2=    parseHTMLString(html);
        System.out.println(html2);
        Document doc = Jsoup.parseBodyFragment(html);
        String parsedHTML = doc.body().unwrap().toString();
        System.out.println(parsedHTML);
    }


    public static void main1(String[] args) {
        String test = ">200<sup>1 &gt; &#638;  & hello </sup>";

        test = test.replaceAll("&#{0,1}[A-Za-z0-9]+;", "");
        test= StringUtil.removeTags(test);
        System.out.println(test);

        System.out.println(test);



        test = "<?xml version=\"1.0\"?><rootnode>" + test + "</rootnode>";


        try {

            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser saxParser = factory.newSAXParser();
            System.out.println("DEBUG: " + test);


            InputStream in = new ByteArrayInputStream(test.getBytes("UTF-8"));
            DefaultHandler handler = new DefaultHandler();

            saxParser.parse(in, handler);

        } catch (SAXParseException ex) {
            handleSAXParseEx(ex, test);
        } catch (SAXException ex) {
            System.out.println("HTML isn't well-formed!");
        } catch (IOException ex) {
            System.out.println("IO exception");

        } catch (ParserConfigurationException ex) {
            System.out.println(" Error in Parsing!");

        }

    }
}
