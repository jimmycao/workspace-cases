package com.pivotal.pandora.topic;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.security.UserGroupInformation;

import com.pivotal.pandora.common.FileListTool;
import com.pivotal.pandora.common.FileUploader;
import com.pivotal.pandora.common.PandoraConfiguration;
import com.pivotal.pandora.common.PandoraConstants;
import com.pivotal.pandora.common.PandoraRTException;


public class Uploader {
  private static final Log LOG = LogFactory.getLog(Uploader.class);
  private Configuration conf; 
  
  public Uploader() {
    this.conf = new PandoraConfiguration();
  }
  
  private void uploadFile(String localFileFullName) throws IOException {
    FileSystem fs = FileSystem.get(conf);
    
    Path uploadPath = new Path(PandoraConstants.Training_Doc_Dir_In_HDFS);
    
    //normalize uploadPath if it's not absolute path
    if (!uploadPath.isAbsolute()) {
      uploadPath = new Path(fs.getHomeDirectory().toString() + "/" + uploadPath.toString());
    }
    
    if (!fs.exists(uploadPath)) {
      fs.mkdirs(uploadPath);
    }
    
    //check if need upload
    boolean needUpload = true;
    File file = new File(localFileFullName);
    Path uploadFilePath = new Path(uploadPath, file.getName());
    if (fs.exists(uploadFilePath)) {
      if (!fs.isFile(uploadFilePath)) {
        String errMSG = "error when trying to upload a file, but a dir with the same name already existed in target path:"
            + uploadFilePath.toUri().toString(); 
        LOG.error(errMSG);
        throw new IOException(errMSG);
      }
      FileStatus fsStatus = fs.getFileStatus(uploadFilePath);
      if (fsStatus.getLen() == file.length()) {
        needUpload = false;
      } else {
        fs.delete(uploadFilePath, false);
      }
    }
    
    //let's upload
    if (needUpload) {
      FileUploader.uploadFileToHDFS(localFileFullName, fs, uploadPath.toString());
    }
  }
  
  private void uploadDir(String localDir) throws IOException {
    FileSystem fs = FileSystem.get(conf);
    Path uploadPath = new Path(PandoraConstants.Training_Doc_Dir_In_HDFS);
    if (fs.exists(uploadPath)) {
      fs.delete(uploadPath, true);
    }
    
    FileListTool fileListTool = new FileListTool(localDir);
    Map<String, String> filesMap = fileListTool.getFilesMap(); 
    for (String localFileFullName : filesMap.values()) {
      uploadFile(localFileFullName);
    }
  }
  
  public static void main(String[] args) {
    if (args.length == 0) {
      String errMSG = "training doc set in HDFS is not specified.";
      LOG.error(errMSG);
      throw new PandoraRTException(errMSG);
    }
    
    String localDir = args[0];
    Uploader uploader = new Uploader();
    try {
      uploader.uploadDir(localDir);
      LOG.info("upload " + localDir + " to hdfs finished");
    } catch (IOException e) {
      LOG.error("faild to upload " + localDir + " to hdfs", e);
      System.exit(-1);
    }
  }
}
