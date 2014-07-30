package com.pivotal.pandora.common;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FSDataOutputStream;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

public class FileUploader {
  
  private static final Log LOG = LogFactory.getLog(FileUploader.class);
  
  public static String uploadFileToHDFS(String fullPath, FileSystem fs, String dirInHDFS) throws IOException {
    //parse fullPath to obtain localFilePath and fileName
    String localFilePath = null;
    String fileName = null;
    if (-1 != fullPath.indexOf('#')) {
      String[] splitPath = fullPath.split("#");
      localFilePath = splitPath[0];
      fileName = splitPath[1];
    } else {
      localFilePath = fullPath;
      File f = new File(localFilePath);
      fileName = f.getName();
    }
    
    //upload local file to HDFS
    Path filePathInHDFS = new Path(dirInHDFS, fileName);
    
    LOG.info("upload file:" + localFilePath + " to path:" + filePathInHDFS);
    
    FSDataOutputStream os = fs.create(filePathInHDFS);
    BufferedInputStream bis = new BufferedInputStream(new FileInputStream(localFilePath));
    byte[] buffer = new byte[1024];
    int len = 0;
    while (-1 != (len = bis.read(buffer))) {
      os.write(buffer, 0, len);
    }
    os.flush();
    os.close();
    bis.close();
    return fileName;
  }
}
