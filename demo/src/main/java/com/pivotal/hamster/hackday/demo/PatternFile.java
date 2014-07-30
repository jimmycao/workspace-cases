package com.pivotal.hamster.hackday.demo;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class PatternFile {
  private static final Log LOG = LogFactory.getLog(PatternFile.class);
  
  public static final void createPatternFile(String fileFullPath) {
    BufferedWriter bw;
    try {
      bw = new BufferedWriter(new FileWriter(fileFullPath));
      bw.write("\\~\n");
      bw.write("\\!\n");
      bw.write("\\@\n");
      bw.write("\\#\n");
      bw.write("\\$\n");
      bw.write("\\%\n");
      bw.write("\\&\n");
      bw.write("\\*\n");
      bw.write("\\(\n");
      bw.write("\\)\n");
      bw.write("\\-\n");
      bw.write("\\_\n");
      bw.write("\\=\n");
      bw.write("\\+\n");
      bw.write("\\\\\n");
      bw.write("\\[\n");
      bw.write("\\]\n");
      bw.write("\\{\n");
      bw.write("\\}\n");
      bw.write("\\\'\n");
      bw.write("\\\"\n");
      bw.write("\\:\n");
      bw.write("\\.\n");
      bw.write("\\,\n");
      bw.write("\\?\n");
      bw.write("\\<\n");
      bw.write("\\>\n");
      bw.write("\\/\n");
      bw.write("0\n");
      bw.write("1\n");
      bw.write("2\n");
      bw.write("3\n");
      bw.write("4\n");
      bw.write("5\n");
      bw.write("6\n");
      bw.write("7\n");
      bw.write("8\n");
      bw.write("9\n");
      bw.write("come\ngo\n");
      bw.write("from\nto\nat\non\nin\nfor\nof\nwith\n");
      bw.write("up\ndown\n");
      bw.write("so\nas\n");
      bw.write("a\nA\nan\nAn\nthe\nThe\n");
      bw.write("this\nthat\n");
      bw.write("than\n");
      bw.write("they\nthem\ntheir\n");
      bw.write("here\nthere\n");
      bw.write("have\nhas\nhad\nhaving\n");
      bw.write("more\nmost\nless\n");
      bw.flush();
      bw.close();
    } catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    LOG.info("create " + fileFullPath + " finished.");
  }
}