package org.csi.yucca.dataservice.binaryapi;

/*
 * Copyright 1994-2006 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Sun designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Sun in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.Enumeration;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.csi.yucca.dataservice.binaryapi.knoxapi.util.KnoxWebHDFSConnection;

import au.com.bytecode.opencsv.CSVReader;
import au.com.bytecode.opencsv.CSVWriter;

/**
 * A <code>SequenceInputStream</code> represents the logical concatenation of
 * other input streams. It starts out with an ordered collection of input
 * streams and reads from the first one until end of file is reached, whereupon
 * it reads from the second one, and so on, until end of file is reached on the
 * last of the contained input streams.
 * 
 * @author Author van Hoff
 * @since JDK1.0
 */
public class SequenceHDFSReader extends Reader {
	Enumeration<? extends HDFSFileProps> paths;
	Reader in;
	StringReader buf;
	CSVReader csvIn;
	int maxFields;
	int curMaxFields=0;
	
	int rows = 0;
	String[] headerLine;
	String[] extractpostValuesMetadata;
	/**
	 * Initializes a newly created <code>SequenceInputStream</code> by
	 * remembering the argument, which must be an <code>Enumeration</code> that
	 * produces objects whose run-time type is <code>InputStream</code>. The
	 * input streams that are produced by the enumeration will be read, in
	 * order, to provide the bytes to be read from this
	 * <code>SequenceInputStream</code>. After each input stream from the
	 * enumeration is exhausted, it is closed by calling its <code>close</code>
	 * method.
	 * @param maxFields 
	 * @param headerLine 
	 * @param extractpostValuesMetadata 
	 * 
	 * @param e
	 *            an enumeration of input streams.
	 * @see java.util.Enumeration
	 */
	public SequenceHDFSReader(
			Enumeration<? extends HDFSFileProps> paths, String[] headerLine, String[] extractpostValuesMetadata) {
		this.paths = paths;
//		this.maxFields = maxFields;
		this.headerLine = headerLine;
		this.extractpostValuesMetadata = extractpostValuesMetadata;
		try {
			firstPath();
		} catch (IOException ex) {
			ex.printStackTrace();
			throw new Error("panic");
		}
	}

	/**
	 * Continues reading in the next stream if an EOF is reached.
	 */
	final void firstPath() throws IOException {
        if (csvIn != null) {
        	csvIn.close();
        }

        if (paths.hasMoreElements()) {
        	//String p = (String) paths.nextElement();
        	HDFSFileProps curF=(HDFSFileProps) paths.nextElement();
        	String p = curF.getFullFilePath();
        	curMaxFields=curF.getMaxFileds();
            if (p == null)
                throw new NullPointerException();
            
            
            //System.out.println("))) firstPath  p --> "+p);
            //System.out.println("))) firstPath  curMaxFields --> "+curMaxFields);
            //System.out.println("))) firstPath  maxFields --> "+maxFields);
            csvIn = new CSVReader(new InputStreamReader(new KnoxWebHDFSConnection().open(p)),',','"',1);
            nextLine(true);
        }
        else csvIn = null;

    }


	/**
	 * Continues reading in the next stream if an EOF is reached.
	 */
	final void nextPath() throws IOException {
		if (csvIn != null) {
			csvIn.close();
		}

		if (paths.hasMoreElements()) {
			//String p = (String) paths.nextElement();
        	HDFSFileProps curF=(HDFSFileProps) paths.nextElement();
        	String p = curF.getFullFilePath();
        	curMaxFields=curF.getMaxFileds();
			
			if (p == null)
				throw new NullPointerException();

            //System.out.println("))) nextPath  p --> "+p);
            //System.out.println("))) nextPath  curMaxFields --> "+curMaxFields);
            //System.out.println("))) nextPath  maxFields --> "+maxFields);
			
			
			csvIn = new CSVReader(new InputStreamReader(new KnoxWebHDFSConnection().open(p)), ',', '"', 1);
			nextLine(false);

			
			
		} else
			csvIn = null;

	}
	
	/**
	 * Continues reading in the next line if an EOF is reached.
	 */
	final void nextLine(boolean writeHeader) throws IOException {
		if (csvIn!=null)
		{
			String[] fields = csvIn.readNext();
			if (fields==null) {
				nextPath();
			}
			else {
//				rows++;
//	            System.out.println(" ---> riga numero "+ rows);
	            //System.out.println("))) nextLine  curMaxFields --> "+curMaxFields);
	            //System.out.println("))) nextLine  maxFields --> "+maxFields);
	            //System.out.println("))) nextLine  xtractpostValuesMetadata.length --> "+(extractpostValuesMetadata!=null ? extractpostValuesMetadata.length :"null"));
	            //System.out.println("))) nextLine  headerLine.length --> "+(headerLine!=null ? headerLine.length :"null"));
				

	            
	            //System.out.println("))) nextLine  FIELDS   0 ("+fields.length+")--> " +Arrays.toString(fields));
	            
				if (curMaxFields>0 && curMaxFields<fields.length) {
					fields = Arrays.copyOf(fields, curMaxFields);
		            //System.out.println("))) nextLine  FIELDS   1 ("+fields.length+")--> " +Arrays.toString(fields));
				}
				
				if (maxFields>fields.length) {
					fields = Arrays.copyOf(fields, maxFields);
		            //System.out.println("))) nextLine  FIELDS   2 ("+fields.length+")--> " +Arrays.toString(fields));
				}
				
				if (extractpostValuesMetadata!=null && extractpostValuesMetadata.length>0) {
					fields=ArrayUtils.addAll(fields, extractpostValuesMetadata);
//					int size=fields.length;
//					fields = Arrays.copyOf(fields, size+extractpostValuesMetadata.length);
//					for (int k=0;k<extractpostValuesMetadata.length;k++) fields[size+k]=extractpostValuesMetadata[k];
		            //System.out.println("))) nextLine  FIELDS   3 ("+fields.length+")--> " +Arrays.toString(fields));
						
				}
	            //System.out.println("))) nextLine  FIELDS   4 ("+fields.length+")--> " +Arrays.toString(fields));
				
				StringWriter sw = new StringWriter();
				CSVWriter csvw =new CSVWriter(sw,';',CSVWriter.DEFAULT_QUOTE_CHARACTER,"\n" );
				if (writeHeader) csvw.writeNext(headerLine);
				csvw.writeNext(fields);
				
				csvw.flush();
				csvw.close();
				
				buf = new StringReader(sw.toString());
				
				
//				if (writeHeader) 
//					buf = new StringReader(Arrays.toString(headerLine)+"\n"+sw.toString());
//				else
//					buf = new StringReader(sw.toString());
			}
		}
		else
		{
			buf = null;
		}

	}

	public void close() throws IOException {
		do {
			nextPath();
		} while (in != null);
	}

	@Override
	public int read(char[] c, int off, int len) throws IOException {
		if (buf == null) {
			return -1;
		} else if (c == null) {
			throw new NullPointerException();
		} else if (off < 0 || len < 0 || len > c.length - off) {
			throw new IndexOutOfBoundsException();
		} else if (len == 0) {
			return 0;
		}

		int n = buf.read(c, off, len);
		if (n <= 0) {
			nextLine(false);
			return read(c, off, len);
		}
		return n;
	}
}
