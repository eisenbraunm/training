package com.cnb.training.util;


import org.apache.commons.lang3.StringUtils;
import org.apache.commons.text.StringEscapeUtils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {


    public static String replace(String str, String pattern, String replace) {
        int s = 0;
        int e = 0;
        StringBuffer result = new StringBuffer();

        while ((e = str.indexOf(pattern, s)) >= 0) {
            result.append(str.substring(s, e));
            result.append(replace);
            s = e + pattern.length();
        }
        result.append(str.substring(s));
        return new String(result);
    }

    public static String toInitCap(String original) {
        if (original == null) {
            return null;
        } else if (original.length() < 1) {
            return original;
        }
        StringBuffer nameb = new StringBuffer(original.substring(0, 1).toUpperCase());
        nameb.append(original.substring(1));
        return nameb.toString();
    }

    /**
     * Pad to a line length to avoid line break within a word
     *
     * @param str the string to change
     * @param fieldLen lenght of line to fit
     * @return the updated string
     */
    public static String noWordWrap(String str, int fieldLen) {

        String result = new String();
        String temp = "";
        StringTokenizer st = new StringTokenizer(str, " ", true);
        int tempLen = 0;

        while (st.hasMoreTokens()) {
            temp = st.nextToken();
            tempLen = temp.length();
            if (result.length() + tempLen < fieldLen) {
                result += temp;
            } else {
                for (int i = result.length(); i <= fieldLen; i++) {
                    result += " ";
                }
            }
        }
        return result;
    }

    /**
     * Convert the object to a string
     *
     * @param o the Object to convert
     * @return the Object as string
     */
    public static String toString(Object o) {
        String s = null;
        if (o instanceof BigDecimal) {
            s = ((BigDecimal) o).toString();
        } else if (o instanceof Long) {
            s = ((Long) o).toString();
        } else if (o instanceof java.util.Date) {
            s = DateConverter.convertDateToString((java.util.Date) o);
        } else if (o instanceof Boolean) {
            s = ((Boolean) o).toString();
        } else {
            if (o != null) {
                s = o.toString();
            }
        }
        return s;
    }

    public static int safeLongToInt(long l) {
        if (l < Integer.MIN_VALUE || l > Integer.MAX_VALUE) {
            throw new IllegalArgumentException(l + " cannot be cast to int without changing its value.");
        }
        return (int) l;
    }

    //this adds 0 pad to the front of a number and returns a string
    public static String padNumberString(String Number, int reqLength) {


        while (Number.length() < reqLength) {
            Number = "0" + Number;
        }
        return Number;
    }

    public boolean isNumeric(String s) {

        return s.matches("[0-9]+");

    }

    public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        }
        // only got here if we didn't return false
        return true;
    }

    public static boolean isFloat(String s) {
        String FLOAT_REGEX = "^-{0,1}[0-9]+(\\.{1}[0-9]+){0,1}$";
        Pattern pattern;
        Matcher matcher;
        pattern = Pattern.compile(FLOAT_REGEX);
        matcher = pattern.matcher(s.toString());
        if (!matcher.matches()) {
            return false;
        } else {
            return true;
        }


    }

    public static String removeLeadingZeros(String str) {
        if (str == null) {
            return null;
        }
        char[] chars = str.toCharArray();
        int index = 0;
        for (; index < str.length(); index++) {
            if (chars[index] != '0') {
                break;
            }
        }
        return (index == 0) ? str : str.substring(index);
    }

    public static String accuracyNumberFormatted(Double Number, int accuracy) {
        String formattedNumber = null;


        BigDecimal bigd = BigDecimal.valueOf(Number);
        formattedNumber = bigd.toPlainString();

        formattedNumber = formattedNumber.replaceAll("(\\.[0-9]*?)0*$", "$1");
        int dotIndex = formattedNumber.lastIndexOf(".");
        if (dotIndex == -1) {
            formattedNumber = formattedNumber + ".";
            dotIndex = formattedNumber.lastIndexOf(".");
        }
        int currentAcc = formattedNumber.length() - (dotIndex + 1);

        while ((formattedNumber.length() - (dotIndex + 1)) < accuracy) {
            formattedNumber = formattedNumber + "0";
        }

        if (accuracy == 0) {
            formattedNumber = formattedNumber.substring(0, dotIndex);
        }

        return formattedNumber;
    }


    public static String accuracyNumberFormattedExp(Double Number, int accuracy) {
        String formattedNumber = null;

        if (Number > 999999 || Number < -999999) {



            NumberFormat formatter = new DecimalFormat();
            formatter = new DecimalFormat("0.#######E0");
            formattedNumber = formatter.format(Number);
            int eIndex = formattedNumber.indexOf("E");

            String start = formattedNumber.substring(0, eIndex);

            String end = formattedNumber.substring(eIndex + 1, formattedNumber.length());
            end = " 10<sup>" + end + "</sup>";



            start = start.replaceAll("(\\.[0-9]*?)0*$", "$1");
            //work out the accuracy by seeing the last point that is non zero
            int count=start.length();



            formattedNumber = start + end;

        } else if (Number < 0.0009 && Number > 0 || (Number > -0.0009 && Number < 0)) {
            formattedNumber = Number.toString();
            int eIndex = formattedNumber.indexOf("E");

            String start = formattedNumber.substring(0, eIndex);
            String end = formattedNumber.substring(eIndex + 1, formattedNumber.length());
            end = " 10<sup>" + end + "</sup>";

            start = start.replaceAll("(\\.[0-9]*?)0*$", "$1");
            int dotIndex = start.lastIndexOf(".");
            if (dotIndex == -1) {
                start = start + ".";
                dotIndex = start.lastIndexOf(".");
            }
            int currentAcc = start.length() - (dotIndex + 1);

            while ((start.length() - (dotIndex + 1)) < accuracy) {
                start = start + "0";
            }

            if (accuracy == 0) {
                start = start.substring(0, dotIndex);
            }
            formattedNumber = start + end;

        } else {
            BigDecimal bigd = BigDecimal.valueOf(Number);
            formattedNumber = bigd.toPlainString();

            formattedNumber = formattedNumber.replaceAll("(\\.[0-9]*?)0*$", "$1");
            int dotIndex = formattedNumber.lastIndexOf(".");
            if (dotIndex == -1) {
                formattedNumber = formattedNumber + ".";
                dotIndex = formattedNumber.lastIndexOf(".");
            }
            int currentAcc = formattedNumber.length() - (dotIndex + 1);

            while ((formattedNumber.length() - (dotIndex + 1)) < accuracy) {
                formattedNumber = formattedNumber + "0";
            }

            if (accuracy == 0) {
                formattedNumber = formattedNumber.substring(0, dotIndex);
            }
        }
        return formattedNumber;
    }


    public static Integer getAccuracy(String stringDouble) {

        stringDouble = stringDouble.toUpperCase();
        if (stringDouble.contains("E")) {

            int index = stringDouble.indexOf("E");
            stringDouble = stringDouble.substring(0, index);

        }

        Integer accuracy = 0;
        if (stringDouble.length() > 0) {
            int dotIndex = stringDouble.lastIndexOf(".");
            if (dotIndex >= 0) {
                stringDouble = stringDouble.substring(dotIndex + 1);
                accuracy = stringDouble.length();
            }
        }
        return accuracy;

    }

    public static String removeTags(String string) {
        Pattern REMOVE_TAGS = Pattern.compile("<.+?>");
        if (string == null || string.length() == 0) {
            return string;
        }
        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll("");
    }

    public static String removeEntities(String string) {
        Pattern REMOVE_TAGS = Pattern.compile("&.+?;");
        if (string == null || string.length() == 0) {
            return string;
        }
        Matcher m = REMOVE_TAGS.matcher(string);
        return m.replaceAll("");
    }

    public static boolean checkParentheses(String s) {
        int nesting = 0;
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            switch (c) {
                case '(':
                    nesting++;
                    break;
                case ')':
                    nesting--;
                    if (nesting < 0) {
                        return false;
                    }
                    break;
            }
        }
        return nesting == 0;
    }

    public static boolean checkSqBrackets(String s) {
        int nesting = 0;
        for (int i = 0; i < s.length(); ++i) {
            char c = s.charAt(i);
            switch (c) {
                case '[':
                    nesting++;
                    break;
                case ']':
                    nesting--;
                    if (nesting < 0) {
                        return false;
                    }
                    break;
            }
        }
        return nesting == 0;
    }

    //unescape entities except <,>, & ... replaces these with a holder then reinsert
    public static String unescapeEntites(String escapedString) {
        escapedString = escapedString.replace("&quot;", "foundQuote");
        escapedString = escapedString.replace("&#34;", "foundQuote");
        escapedString = escapedString.replace("&#160;", "nonBreakingSpace");
        escapedString = escapedString.replace("&#160;", "nonBreakingSpace");
        escapedString = escapedString.replace("&lt;", "foundLT");
        escapedString = escapedString.replace("&#60;", "foundLT");
        escapedString = escapedString.replace("&gt;", "foundGT");
        escapedString = escapedString.replace("&#62;", "foundGT");
        escapedString = escapedString.replace("&amp;", "foundAmperand");
        escapedString = escapedString.replace("&#38;", "foundAmperand");
        String unescapedString = StringEscapeUtils.unescapeHtml4(escapedString);
        unescapedString = unescapedString.replace("nonBreakingSpace", "&#160;");
        unescapedString = unescapedString.replace("foundAmperand", "&amp;");
        unescapedString = unescapedString.replace("foundLT", "&lt;");
        unescapedString = unescapedString.replace("foundGT", "&gt;");
        unescapedString = unescapedString.replace("foundQuote", "&quot;");
        return unescapedString;
    }

    public static String escapeXml(String data) {
        if (data == null) {
            data = "";
        }
        String escapes[][] = {
                {
                        "&", "&amp;"
                }, {
                ">", "&gt;"
        }, {
                "<", "&lt;"
        }, {
                "\"", "&quot;"
        }
        };
        for (int i = 0; i < escapes.length; i++) {
            data = StringUtils.replace(data, escapes[i][0], escapes[i][1]);
        }

        return data;
    }

    public static String escapeXmlCData(String data) {
        data = data.replace("&lt;", "<![CDATA[<]]>");
        data = data.replace("&gt;", "<![CDATA[>]]>");
        data = data.replace("&amp;", "<![CDATA[&]]>");
        return data;
    }

    public static String regexStringReplacer(String data, String pattern, String start, String end) {
        Matcher m = Pattern.compile(pattern).matcher(data);
        //&lt;h1 class=&quot;Heading&quot;&gt;CRC Handbook of Chemistry and Physics&lt;/h1&gt;  <Para>Editor-in-Chief</Para>
        //"(?:&lt;h1 class="Heading"[^>]*&gt;)([^<]*)(?:&lt;/h1&gt;)"
        StringBuffer s = new StringBuffer();
        while (m.find()) {
            for (int i = 1; i <= m.groupCount(); i++) {
                m.appendReplacement(s, start + m.group(i) + end);

            }
        }
        m.appendTail(s);

        return s.toString();
    }

    public static String escapeHTML(String data) {
        if (data == null) {
            data = "";
        }
        String escapes[][] = {
                {
                        "&", "&amp;"
                }, {
                ">", "&gt;"
        }, {
                "<", "&lt;"
        }
        };
        for (int i = 0; i < escapes.length; i++) {
            data = StringUtils.replace(data, escapes[i][0], escapes[i][1]);
        }

        return data;
    }
    //this converts unicode to entities

    public static String buildHtmlEntityCode(String input) {
        String result = null;

        if (input != null) {
            StringBuffer output = new StringBuffer(input.length() * 2);
            int len = input.length();
            int code, code1, code2, code3, code4;
            char ch;
//x >> y means to shift the bits of x by y places to the right (<< to the left).

            for (int i = 0; i < len;) {
                code1 = input.codePointAt(i);
                if (code1 >> 3 == 30) {
                    code2 = input.codePointAt(i + 1);
                    code3 = input.codePointAt(i + 2);
                    code4 = input.codePointAt(i + 3);
                    code = ((code1 & 7) << 18) | ((code2 & 63) << 12) | ((code3 & 63) << 6) | (code4 & 63);
                    i += 4;
                    output.append("&#" + code + ";");
                } else if (code1 >> 4 == 14) {
                    code2 = input.codePointAt(i + 1);
                    code3 = input.codePointAt(i + 2);
                    code = ((code1 & 15) << 12) | ((code2 & 63) << 6) | (code3 & 63);
                    i += 3;
                    output.append("&#" + code + ";");
                } else if (code1 >> 5 == 6) {
                    code2 = input.codePointAt(i + 1);
                    code = ((code1 & 31) << 6) | (code2 & 63);
                    i += 2;
                    output.append("&#" + code + ";");
                } else {

                    //I think this is the only bit used
                    code = code1;
                    i += 1;

                    ch = (char) code;
                    if (code <= 126) {
                        //  if (ch >= 'a' && ch <= 'z' || ch >= 'A' && ch <= 'Z' || ch >= '0' && ch <= '9') {
                        output.append(ch);
                    } else {
                        output.append("&#" + code + ";");
                    }
                }
            }


            result = output.toString();
            result = result.replace("&#56602;", "");  //invalid character pointer
        }
        return result;
    }

    //converts the mathml displayed as html back to true mathml so it can be saved to the database
    public static String convertMathHTMLtoMathML(String htmlString) {
        if (htmlString.contains("&lt;math")) {
            if (htmlString.contains("&lt;math&gt;")) {
                htmlString = htmlString.replace("&lt;math&gt;", "&lt;math xmlns=\"http://www.w3.org/1998/Math/MathML\"&gt;");
            }
            Pattern patt = Pattern.compile("&lt;math xmlns=\"http://www.w3.org/1998/Math/MathML\"&gt;(.+?)&lt;/math&gt;");
            Matcher m = patt.matcher(htmlString);
            StringBuffer sb = new StringBuffer(htmlString.length());
            while (m.find()) {
                String contents = m.group(1);
                contents = StringEscapeUtils.unescapeHtml4(m.group(1));
                contents = contents.replace("<br/>", "");
                contents = contents.replace("<br />", "");
                contents = contents.replace("&#160;", " ");
                //none breaking spaces should be nonbreaking spaces
                contents = contents.replace((char) 160, (char) 32);

                m.appendReplacement(sb, "\r\n<math xmlns=\"http://www.w3.org/1998/Math/MathML\">" + contents + "</math>\r\n");
            }
            m.appendTail(sb);

            return (sb.toString());
        } else {
            return htmlString;
        }
    }

    //converts the mathml back to html so can be seen in tiny
    public static String convertMathMLtoHTML(String htmlString) {
        if (htmlString.contains("<math")) {
            htmlString = htmlString.replace(" xmlns=\"http://www.w3.org/1998/Math/MathML\"", "");

            Pattern patt = Pattern.compile("<math>(.+?)</math>");
            Matcher m = patt.matcher(htmlString);
            StringBuffer sb = new StringBuffer(htmlString.length());
            while (m.find()) {
                String contents = m.group(1);

                contents = contents.replace("&", "&amp;");
                contents = contents.replace("\r\n", "<br/>");
                contents = contents.replace("<", "&lt;");
                contents = contents.replace(">", "&gt;");
                //none  nonbreaking spaces should be ordinary spaces.  Eh? below is the opposite, MJG
                contents = contents.replace((char) 32, (char) 160);
                m.appendReplacement(sb, "\r\n&lt;math xmlns=\"http://www.w3.org/1998/Math/MathML\"&gt;" + contents + "&lt;/math&gt;\r\n");
            }
            m.appendTail(sb);

            return (sb.toString());
        } else {
            return htmlString;
        }

    }


    public static String convertMathMLtoHTMLforWeb(String htmlString) {
        if (htmlString.contains("<math") && htmlString.contains("<table")) {
            htmlString = htmlString.replace(" xmlns=\"http://www.w3.org/1998/Math/MathML\"", "");

            Pattern patt = Pattern.compile("<math>(.+?)</math>");
            Matcher m = patt.matcher(htmlString);
            StringBuffer sb = new StringBuffer(htmlString.length());
            while (m.find()) {
                String contents = m.group(1);

                contents = contents.replace("&", "&amp;");
                contents = contents.replace("\r\n", "<br/>");
                contents = contents.replace("&#x003C;", "&#706;"); //replace literal less than with left arrowhead
                contents = contents.replace("&#x003E;", "&#707;"); //replace literal greater than with right arrowhead
                contents = contents.replace("<", "&lt;");
                contents = contents.replace(">", "&gt;");
                contents = contents.replace("\"", "&quot;");
                contents = contents.replace("'", "&quot;");

                m.appendReplacement(sb, "\r\n<h:outputText value=\"&lt;math xmlns=&quot;http://www.w3.org/1998/Math/MathML&quot;&gt;" + contents + "&lt;/math&gt;\" escape=\"false\"/> \r\n");
            }
            m.appendTail(sb);

            return (sb.toString());
        } else if (htmlString.contains("<math") && !htmlString.contains("<table")) { //don't need to escape mathml if not in a table but do still need to replace < and >
            htmlString = htmlString.replaceAll("&#x003C;", "&#706;"); // must replace < and > to prevent <tag><</tag> in mathml e.g. in doc 17_05
            htmlString = htmlString.replaceAll("&#x003E;", "&#707;");
            return htmlString;
        } else {
            return htmlString;
        }

    }


    public static String escapeSql(String str) {
      if (str == null) {
                  return null;
              }
               return StringUtils.replace(str, "'", "''");
       }





    public static void main(String[] args) {

String mystring ="hello &nbsp; &alpha;  &BBB34;  ";


        String    LINK_REGEX = "&[A-Za-z]{2,10};";
        Pattern pattern = Pattern.compile(LINK_REGEX);
        Matcher matcher = pattern.matcher("&nbsp; &alpha;  &BBB34;  ");
     String aString=   StringEscapeUtils.unescapeHtml4(mystring);
     System.out.println(aString);
        while (matcher.find()) {
            String match = matcher.group().trim();
            System.out.println("my match "+ match);
        }

    }


}

