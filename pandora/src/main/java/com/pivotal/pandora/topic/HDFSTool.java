package com.pivotal.pandora.topic;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;

import com.pivotal.pandora.common.PandoraConfiguration;

public class HDFSTool {
  private static final Log LOG = LogFactory.getLog(HDFSTool.class);
  private static Configuration conf = new PandoraConfiguration();
      
  public static void upload(String[] localDirs, String toDirInHDFS) throws IOException {
    conf.set("fs.defaultFS","hdfs://cncqcaoj7mbp1.corp.emc.com:9000");
    FileSystem fs = FileSystem.get(conf);
    
    Path[] localDirPaths = new Path[localDirs.length];
    fs.copyFromLocalFile(false, true, localDirPaths, new Path(toDirInHDFS));
    LOG.info("upload " + Arrays.toString(localDirs) + "to hdfs://" + toDirInHDFS);
  }

  public static void download(String fromDirInHDFS, String localDir) throws IOException {
    FileSystem fs = FileSystem.get(conf);
    fs.copyToLocalFile(false, new Path(fromDirInHDFS), new Path(localDir), true);
    LOG.info("download hdfs://" + fromDirInHDFS + " to " + localDir);
  }
}
