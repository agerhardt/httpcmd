package de.age.httpcmd.util;

import java.util.ArrayList;
import java.util.List;

public class EnhancedStringTokenizer {

    private String string;
    private String delim;
    private String quote;
    private int pos;

    public EnhancedStringTokenizer(String aString, String aDelim, String aQuote) {
        this.delim = aDelim;
        this.quote = aQuote;
        this.string = aString;
        pos = 0;
    }

    public String nextToken() {
        if (pos >= string.length()) {
            return null;
        }
        boolean isQuoting = false;
        StringBuilder buffer = new StringBuilder();
        StringBuilder result = new StringBuilder();
        for (int index = pos; index < string.length(); index++) {
            buffer.append(string.charAt(index));
            final String bufferAsString = buffer.toString();
            if (quote.startsWith(bufferAsString)) {
                if (bufferAsString.equals(quote)) {
                    isQuoting = !isQuoting;
                    buffer.delete(0, buffer.length());
                }
            } else if (!isQuoting && delim.startsWith(bufferAsString)) {
                if (delim.equals(bufferAsString)) {
                    pos = index + 1;
                    return result.toString();
                }
            } else {
                result.append(buffer);
                buffer.delete(0, buffer.length());
            }
        }
        pos = string.length();
        if (isQuoting) {
            throw new ArrayIndexOutOfBoundsException();
        }
        result.append(buffer);
        return result.toString();
    }

    public boolean hasMoreTokens() {
        return pos < string.length();
    }

    public List<String> getAllTokens() {
        List<String> allTokens = new ArrayList<String>();
        while (hasMoreTokens()) {
            allTokens.add(nextToken());
        }
        return allTokens;
    }
    
}
