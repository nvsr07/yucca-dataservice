package org.csi.yucca.dataservice.ingest.binary.webhdfs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * CSV file reader
 * 
 * @author Takaji
 */
public final class CSVFileReader {

    private CSVFormat format;
    private final BufferedReader reader;
    private int col = 1;
    private int row = 1;
    private StringBuilder element = new StringBuilder(); // store current
    // element
    private CSVLine currentLine = null; // current CSV line
    private boolean isOpened = false; // a string is opened with text delimiter

    // <editor-fold defaultstate="collapsed" desc="Constructors">
    public CSVFileReader(Reader reader) {
        //validate parameter
        checkNotNull(reader, "Reader cannot be null");
        this.reader = new BufferedReader(reader);
        this.format = new CSVFormat();
    }

    public CSVFileReader(Reader reader, CSVFormat format) {
        //validate parameter
        checkNotNull(reader, "Reader cannot be null");
        this.reader = new BufferedReader(reader);
        this.format = format;
    }

    public CSVFileReader(BufferedReader reader) throws FileNotFoundException {
        //validate parameter
        checkNotNull(reader, "Reader cannot be null");
        this.reader = reader;
        this.format = new CSVFormat();
    }

    public CSVFileReader(BufferedReader reader, CSVFormat format) {
        //validate parameter
        checkNotNull(reader, "Reader cannot be null");
        this.reader = reader;
        this.format = format;
    }

    public CSVFileReader(String filename) throws CSVException {
        //validate parameter
        checkNotNull(filename, "File name cannot be empty");
        try {
            this.reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVFileReader.class.getName()).log(Level.SEVERE, null, ex);
            throw new CSVException("Cannot create reading buffer", ex);
        }
        this.format = new CSVFormat();
    }

    public CSVFileReader(File f) {
        //validate parameter
        checkNotNull(f, "File object cannot be null");
        try {
            this.reader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVFileReader.class.getName()).log(Level.SEVERE, null, ex);
            throw new CSVException("Cannot create reading buffer", ex);
        }
        this.format = new CSVFormat();
    }

    public CSVFileReader(InputStream input) {
        //validate parameter
        checkNotNull(input, "InputStream cannot be null");
        reader = new BufferedReader(new InputStreamReader(input));
        this.format = new CSVFormat();
    }

    public CSVFileReader(String filename, CSVFormat format) {
        //validate parameter
        checkNotNull(filename, "File name cannot be empty");
        try {
            this.reader = new BufferedReader(new FileReader(filename));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVFileReader.class.getName()).log(Level.SEVERE, null, ex);
            throw new CSVException("Cannot create reading buffer", ex);
        }
        this.format = format;
    }

    public CSVFileReader(File f, CSVFormat format) {
        //validate parameter
        checkNotNull(f, "File object cannot be null");
        try {
            this.reader = new BufferedReader(new FileReader(f));
        } catch (FileNotFoundException ex) {
            Logger.getLogger(CSVFileReader.class.getName()).log(Level.SEVERE, null, ex);
            throw new CSVException("Cannot create reading buffer", ex);
        }
        this.format = format;
    }

    public CSVFileReader(InputStream input, CSVFormat format) {
        //validate parameter
        checkNotNull(input, "InputStream cannot be null");
        reader = new BufferedReader(new InputStreamReader(input));
        this.format = format;
    }

    // </editor-fold>
    /**
     * read next char
     */
    private int read() throws IOException {
        int next = -1;
        next = reader.read();
        if (next != -1) {
            col++;
        }
        return next;
    }

    /**
     * Do carriage return
     */
    private void carriageReturn() {
        synchronized (this) {
            row++;
            col = 1;
        }
    }

    /**
     * Finished read a cell, flush to current line
     */
    private void flushElement() {
        currentLine.add(element.toString());
        element.setLength(0); // reset current element
        isOpened = false;
    }

    /**
     * Parse a CSV string to data
     * 
     * @param rawData
     * @return a simple CSV object with data. null if the raw data cannot be
     *         parse
     * @throw CSVException
     */
    public CSVLine readLine() throws CSVException {
        if (reader == null) {
            throw new CSVException("No reader found.");
        }
        synchronized (this) {
            element = new StringBuilder(); // store current
            // element
            currentLine = null; // current CSV line
            isOpened = false; // a string is opened with text delimiter

            try {
                int currentInt = -1;
                // parse data
                while ((currentInt = read()) != -1) {
                    if (currentLine == null) {
                        currentLine = new CSVLine();
                    }

                    // reach field delimiter
                    if (currentInt == format.getTextDelimiter()) {
                        if (isOpened) {
                            // check next char is escape?
                            if ((currentInt = read()) != -1) {
                                if (currentInt == format.getTextDelimiter()) {
                                    // is special char
                                    element.append(format.getTextDelimiter());
                                    continue;
                                } else {
                                    // is close string then flush
                                    flushElement();
                                    // next char must be field delimiter
                                    //or a line terminator
                                    if (currentInt == format.getFieldDelimiter()) {
                                        continue;
                                    } else if (currentInt == format.getLineTerminator()) {
                                        carriageReturn();
                                        return currentLine;
                                    } else {
                                        raiseExpected(format.getFieldDelimiter(),
                                                (char) currentInt);
                                    }
                                    continue;
                                }
                            } else {
                                flushElement();
                                continue;
                            }
                        } else {
                            // is first open of the string
                            isOpened = true;
                            continue;
                        }
                    }

                    if (!isOpened) {
                        // ignore character
                        if (format.isIgnored((char) currentInt)) {
                            continue; // just ignore
                        } else if (currentInt == format.getLineTerminator()) {
                            flushElement();

                            if (currentLine != null) {
                                carriageReturn();
                            }
                            return currentLine;
                        }
                    }

                    // meet field delimiter
                    if (currentInt == format.getFieldDelimiter()) {
                        if (isOpened) {
                            element.append((char) currentInt);
                            continue;
                        } else {
                            flushElement();
                            continue;
                        }
                    }

                    // otherwise, it's a normal character, just add
                    element.append((char) currentInt);
                }// continue while

                // flush last element if needed
                if (element != null && currentLine != null) {
                    flushElement();
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                raiseException(ex);
            }
            // if currentLine exists, return carriage
            if (currentLine != null) {
                carriageReturn();
            }
        }// end of synchronize
        return currentLine;
    }

    // <editor-fold defaultstate="collapsed" desc="Exception related tasks">
    /**
     * Expected char [expected] but found another char [actual] so we
     * throw an exception here.
     * @param expected
     * @param actual
     */
    private void raiseExpected(char expected, char actual) {
        raiseException(new Exception(String.format(
                "Invalid data. Expected [%s] but [%s] was found.", String.valueOf(expected), String.valueOf(actual))));
    }

    private void raiseException(Exception ex) {
        throw new CSVException("Error happened while processing at row: " + row
                + " - col: " + (col - 1) + ")", ex);
    }

    /**
     * Check if argument null then throw a new CSV Exception
     * @param argument
     */
    public static void checkNotNull(Object argument, String message) throws CSVException {
        if (argument == null) {
            throw new CSVException(message);
        }
    }

    /**
     * Close current reader and underline stream
     */
    public void close() {
        try {
            synchronized (reader) {
                reader.close();
            }
        } catch (Exception ex) {
            Logger.getLogger(CSVFileReader.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
    }
    // </editor-fold>

    // <editor-fold defaultstate="collapsed" desc="Quick CSV reading">
    /**
     * Parse a CSV stream to data
     * 
     * @param stream
     *            InputStream
     * @return a simple CSV object with data. null if the raw data cannot be
     *         parse
     */
    public static CSVFile parse(InputStream stream) throws CSVException {
        try {
            CSVFileReader reader = new CSVFileReader(stream);
            CSVFile file = new CSVFile();
            CSVLine line = null;
            do {
                line = reader.readLine();
                file.append(line);
            } while (line != null);
            return file;
        } catch (Exception ex) {
            throw new CSVException(ex);
        }
    }

    /**
     * Parse file content to CSV
     * 
     * @param path
     * @return
     */
    public static CSVFile parseFile(String path) {
        if (path == null || !(new File(path)).exists()) {
            throw new CSVException("Invalid path");
        }
        try {
            return parse(new FileInputStream(path));
        } catch (Exception ex) {
            Logger.getLogger(CSVFile.class.getName()).log(Level.SEVERE, null,
                    ex);
            throw new CSVException(ex);
        }
    }
    // </editor-fold>
}
class CSVFile {

    private ArrayList<CSVLine> lines; //lines inside a CSV file

    /**
     * Default constructor
     */
    public CSVFile() {
        this.lines = new ArrayList<CSVLine>();
    }

    public CSVLine[] getLines() {
        return this.lines.toArray(new CSVLine[0]);
    }

    /**
     * Get line at a specified index
     * @param idx
     * @return
     * @throws IndexOutOfBoundsException
     */
    public CSVLine getLine(int idx) throws IndexOutOfBoundsException{
        return this.lines.get(idx);
    }

    /**
     * Add a new line to current csv
     * @return new line object
     */
    public CSVLine newLine() {
        CSVLine line = new CSVLine();
        this.lines.add(line);
        return line;
    }

    /**
     * get number of line
     * @return number of line
     */
    public int size() {
        return this.lines.size();
    }

    /**
     * discard a line at the specified index
     * @param idx
     */
    public void discard(int idx) {
        if (idx >= 0 && idx < size()) {
            this.lines.remove(idx);
        }
    }

    /**
     * discard empty record
     */
    public void discardEmpty() {
        for (int i = this.lines.size() - 1; i > -1; i--) {
            if (this.lines.get(i) == null || this.lines.get(i).isEmpty()) {
                discard(i);
            }
        }
    }

    /**
     * append a CSV line to file (line is ignored if null)
     * @param line
     */
    void append(CSVLine line) {
        if (line == null) {
            return;
        }
        this.lines.add(line);
    }
}
class CSVException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public CSVException() {
    }

    public CSVException(Throwable throwable) {
        super(throwable);
    }

    public CSVException(String message) {
        super(message);
    }

    public CSVException(String message, Throwable throwable) {
        super(message, throwable);
    }

}
class CSVLine {

    private ArrayList<Object> elements;

    /**
     * Default constructor
     */
    public CSVLine() {
        this.elements = new ArrayList<Object>();
    }

    /**
     * Get all elements (cells) inside a line
     * @return empty String array if there's no element found inside
     */
    public Object[] getElements() {
        return this.elements.toArray();
    }

    /**
     * Get element at index
     * @param idx
     * @return
     * @throws IndexOutOfBoundsException
     */
    public Object getElementAt(int idx) throws IndexOutOfBoundsException {
        return elements.get(idx);
    }

    /**
     * Set element at
     * @param idx
     * @param value
     * @throws IndexOutOfBoundsException
     */
    public void setElementAt(int idx, Object value) throws IndexOutOfBoundsException {
        elements.set(idx, value);
    }

    /**
     * count elements inside this line
     * @return
     */
    public int size() {
        return elements.size();
    }

    /**
     * Add a new element
     */
    public CSVLine add(Object obj) {
        this.elements.add(obj);
        return this;
    }

    /**
     * if CSV line is empty
     * @return
     */
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    /**
     * Remove all elements
     */
    public void clear() {
        this.elements.clear();
    }

    /**
     * Remove element at a specified index
     * @param idx
     * @throws IndexOutOfBoundsException
     */
    public void remove(int idx) throws IndexOutOfBoundsException {
        elements.remove(idx);
    }

    /**
     * Trim down or expand with null cell to a new length
     * @param newLength
     */
    public void setLength(int newLength) {
        if (elements.size() > newLength) {
            //trim down
            while (elements.size() > newLength) {
                elements.remove(elements.size() - 1);
            }
        } else {
            //add more
            while (elements.size() < newLength) {
                add(null);
            }
        }
    }
}
class CSVFormat {

    private String charset;
    private char fieldDelimiter;
    private char textDelimiter;
    private char lineTerminator;
    private char[] ignoreCharacters;

    /**
     * Construct a standard CSV format
     */
    public CSVFormat() {
        this.setCharset("UTF-8");
        this.setFieldDelimiter(',');
        this.setTextDelimiter('"');

        //auto detect line separator
        String s = System.getProperty("line.separator");
        if (s.length() > 0) {
            this.setLineTerminator(s.charAt(0));
            char[] ignoreChars = new char[s.length() - 1];
            for (int i = 1; i < s.length(); i++) {
                ignoreChars[i-1] = s.charAt(i);
            }
            this.setIgnoreCharacters(ignoreChars);
        } else {
            this.setLineTerminator('\n');
            this.setIgnoreCharacters(new char[]{'\r'});
        }
    }

    /**
     * @param lineTerminator
     *            the lineTerminator to set
     */
    public void setLineTerminator(char lineTerminator) {
        this.lineTerminator = lineTerminator;
    }

    /**
     * @return the lineTerminator
     */
    public char getLineTerminator() {
        return lineTerminator;
    }

    /**
     * @param ignoreCharacters
     *            the ignoreCharacters to set
     */
    public void setIgnoreCharacters(char[] ignoreCharacters) {
        this.ignoreCharacters = ignoreCharacters;
    }

    /**
     * @return the ignoreCharacters
     */
    public char[] getIgnoreCharacters() {
        return ignoreCharacters;
    }

    /**
     * @param charset
     *            the charset to set
     */
    public void setCharset(String charset) {
        this.charset = charset;
    }

    /**
     * @return the charset
     */
    public String getCharset() {
        return charset;
    }

    /**
     * @param fieldDelimiter
     *            the fieldDelimiter to set
     */
    public void setFieldDelimiter(char fieldDelimiter) {
        this.fieldDelimiter = fieldDelimiter;
    }

    /**
     * @return the fieldDelimiter
     */
    public char getFieldDelimiter() {
        return fieldDelimiter;
    }

    /**
     * @param textDelimiter
     *            the textDelimiter to set
     */
    public void setTextDelimiter(char textDelimiter) {
        this.textDelimiter = textDelimiter;
    }

    /**
     * @return the textDelimiter
     */
    public char getTextDelimiter() {
        return textDelimiter;
    }

    /**
     * is ignored characters
     * @param c
     * @return
     */
    public boolean isIgnored(char c) {
        return Arrays.binarySearch(ignoreCharacters, c) >= 0;
    }
}