package com.pivotal.pandora.user;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.pivotal.pandora.common.FileUploader;
import com.pivotal.pandora.common.PandoraConfiguration;
import com.pivotal.pandora.common.PandoraConstants;

public class PostBlog {
  private static final Log LOG = LogFactory.getLog(PostBlog.class);
  private static Configuration conf = new PandoraConfiguration();
  
  private String name;
  private String blog;
  
  public PostBlog(String name, String blog) {
    this.name = name;
    this.blog = blog;
  }
  
  public void post() throws IOException {
    FileSystem fs = FileSystem.get(conf);
    
    String uploadRoot = PandoraConstants.Blog_Dir_In_HDFS;
    Path uploadPath = new Path(uploadRoot, name);
    
    //normalize uploadPath if it's not absolute path
    if (!uploadPath.isAbsolute()) {
      uploadPath = new Path(fs.getHomeDirectory().toString() + "/" + uploadPath.toString());
    }
    if (!fs.exists(uploadPath)) {
      fs.mkdirs(uploadPath);
    }
      
    FileUploader.uploadFileToHDFS(blog, fs, uploadPath.toString());
    LOG.info("upload " + blog + " to " + uploadPath.toString() + " finished.");

  }
  
  public static void main(String[] args) {
    String name = args[0];
    String blog = args[1];
    System.out.println(name + "  " + blog);
    PostBlog postBlog = new PostBlog(name, blog);
    try {
      postBlog.post();
    } catch (IOException e) {
      LOG.error("failed to post blog", e);
    } 
  }
}
