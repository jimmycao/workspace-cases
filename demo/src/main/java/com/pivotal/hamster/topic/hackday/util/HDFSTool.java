package com.pivotal.hamster.topic.hackday.util;

import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class HDFSTool {
  private static final Log LOG = LogFactory.getLog(HDFSTool.class);
  
  public static final void download(String pathInHDFS, String localPath) {
    String download_cmd = "hadoop fs -get " + pathInHDFS + " " + localPath;
    LOG.info(download_cmd);
    try {
      Process p = Runtime.getRuntime().exec(download_cmd);
      p.waitFor();
    } catch (IOException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }
}
