package org.csi.yucca.dataservice.binaryapi;

import java.util.*;
import java.io.*;

public class ListOfFiles implements Enumeration<String> {

    private List<String> listOfFiles;
    private int current = 0;

    public ListOfFiles(List<String> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }
    
    public void addElement(String element){
    	listOfFiles.add(element);
    }

    public boolean hasMoreElements() {
        if (current < listOfFiles.size())
            return true;
        else
            return false;
    }

    public String nextElement() {
    	String nextElement = null;

        if (!hasMoreElements())
            throw new NoSuchElementException("No more files.");
        else {
        	
            nextElement = listOfFiles.get(current);
			current++;
        }
        return nextElement;
    }

    public String prevElement() {
    	String prevElement = null;

		current--;
		if (current < 0)
			current = 0;
        else {
    		prevElement = listOfFiles.get(current);
        }
        return prevElement;
    }
}
