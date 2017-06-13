package org.csi.yucca.dataservice.binaryapi;

import java.util.*;
import java.io.*;

public class ListOfFiles implements Enumeration<HDFSFileProps> {

    private List<HDFSFileProps> listOfFiles;
    private int current = 0;

    public ListOfFiles() {
    	this.listOfFiles=new ArrayList<HDFSFileProps>();
    }    
    
    public ListOfFiles(List<HDFSFileProps> listOfFiles) {
        this.listOfFiles = listOfFiles;
    }
    
    public void addElement(HDFSFileProps element){
    	listOfFiles.add(element);
    }

    public boolean hasMoreElements() {
        if (current < listOfFiles.size())
            return true;
        else
            return false;
    }

    public HDFSFileProps nextElement() {
    	HDFSFileProps nextElement = null;

        if (!hasMoreElements())
            throw new NoSuchElementException("No more files.");
        else {
        	
            nextElement = listOfFiles.get(current);
			current++;
        }
        return nextElement;
    }

    public HDFSFileProps prevElement() {
    	HDFSFileProps prevElement = null;

		current--;
		if (current < 0)
			current = 0;
        else {
    		prevElement = listOfFiles.get(current);
        }
        return prevElement;
    }
}
