package com.pivotal.pandora.common;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class FileListTool {
  private static final Log LOG = LogFactory.getLog(FileListTool.class);
  private String rootPath;
  private Map<String, String> filePathMap;
  
  public FileListTool(String rootPath) {
    this.rootPath = rootPath;
    this.filePathMap = new HashMap<String, String>();
  }
  
  private void tranverse(File rootFile) {
    if (rootFile.isFile()) {
      String absolutePath = rootFile.getAbsolutePath();
      this.filePathMap.put(rootFile.getName(), absolutePath);
    } else {
      File[] childArray = rootFile.listFiles();
      for (File child : childArray) {
        tranverse(child);
      }
    }
  }
  
  public Map<String, String> getFilesMap() {
    File rootFile = new File(this.rootPath);
    tranverse(rootFile);
    return this.filePathMap;
  }
  
   
//  public static void main(String[] args) throws IOException {
//    String rootPath = "/Users/caoj7/Music";
//
//    FileListTool fileListTool = new FileListTool(rootPath);
//    Map<String, String> filesMap = fileListTool.getFilesMap();
//    
//    BufferedWriter bw = new BufferedWriter(new FileWriter("map.txt"));
//    for (Entry<String, String> fileMap : filesMap.entrySet()) { 
//      bw.write(fileMap.getKey() + "=====" + fileMap.getValue());
//      bw.newLine();
//      bw.flush();
//      
//    }
//    bw.close();
//  }

}
