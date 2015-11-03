package org.csi.yucca.dataservice.ingest.binary;

import java.util.*;
import java.io.*;

public class ListOfFiles implements Enumeration<org.apache.hadoop.fs.Path> {

    private List<org.apache.hadoop.fs.Path> listOfFiles;
    private int current = 0;

    public ListOfFiles(List<org.apache.hadoop.fs.Path> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }
    
    public void addElement(org.apache.hadoop.fs.Path element){
    	listOfFiles.add(element);
    }

    public boolean hasMoreElements() {
        if (current < listOfFiles.size())
            return true;
        else
            return false;
    }

    public org.apache.hadoop.fs.Path nextElement() {
    	org.apache.hadoop.fs.Path nextElement = null;

        if (!hasMoreElements())
            throw new NoSuchElementException("No more files.");
        else {
        	
            nextElement = listOfFiles.get(current);
			current++;
        }
        return nextElement;
    }

    public org.apache.hadoop.fs.Path prevElement() {
    	org.apache.hadoop.fs.Path prevElement = null;

		current--;
		if (current < 0)
			current = 0;
        else {
    		prevElement = listOfFiles.get(current);
        }
        return prevElement;
    }
}
