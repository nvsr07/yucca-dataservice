
package org.csi.yucca.dataservice.insertdataapi.hdfs.model;

import com.google.gson.annotations.Expose;

public class FileStatuses {

    @Expose
    private FileStatus[] FileStatus;

    public FileStatus[] getFileStatus (){
        return FileStatus;
    }

    public void setFileStatus (FileStatus[] FileStatus){
        this.FileStatus = FileStatus;
    }

    @Override
    public String toString(){
        return "ClassPojo [FileStatus = "+FileStatus+"]";
    }
}