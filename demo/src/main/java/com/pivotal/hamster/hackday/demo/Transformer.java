package com.pivotal.hamster.hackday.demo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class Transformer {
  private static final Log LOG = LogFactory.getLog(Transformer.class);
  
  private Map<String, String> filePathMap;
  private String localRootPath;

  private Executor exec;

  public Transformer(String localRootPath, int threadPool) {
    this.localRootPath = localRootPath;
    // 1) get map <fileName, fileRelativePathInHDFS>
    FileListTool tool = new FileListTool(localRootPath);
    this.filePathMap = tool.getFilesMap();
    // 2) create the local download dir
    File resultDir = new File(Constants.DOWNLOAD_RESULT_DIR);
    resultDir.mkdirs();
    // 3) create pattern file
    PatternFile.createPatternFile(Constants.LOCAL_PATTERN_FILE);
    // 4)
    this.exec = Executors.newFixedThreadPool(threadPool);
  }

  public void init() throws IOException, InterruptedException {
    Process p = Runtime.getRuntime().exec("hadoop fs -rm -r " + Constants.WordCount_ROOT_HDFS_PATH);
    p.waitFor();
    
    Process p1 = Runtime.getRuntime().exec("hadoop fs -mkdir -p " + Constants.INPUT_HDFS_PATH);
    p1.waitFor();

    Process p2 = Runtime.getRuntime().exec("hadoop fs -put " + Constants.LOCAL_PATTERN_FILE + " "
                + Constants.WordCount_ROOT_HDFS_PATH + "/" + Constants.PATTERN_FILE);
    p2.waitFor();
  }

  public void put() throws IOException, InterruptedException {
    Process p = Runtime.getRuntime().exec("hadoop fs -put " + this.localRootPath + " " + Constants.INPUT_HDFS_PATH);
    p.waitFor();
  }

  // ----------------------------------------
  private static class RealWordCountRunnable implements Runnable {
    private String jar_cmd = null;

    private RealWordCountRunnable(String jar_cmd) {
      this.jar_cmd = jar_cmd;
    }

    public void run() {
      try {
        Process p = Runtime.getRuntime().exec(jar_cmd);
        p.waitFor();
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
  }

  // ----------------------------------------
  
  
  void transform() throws IOException, InterruptedException {
    for (Entry<String, String> fileMap : this.filePathMap.entrySet()) {
      String SingleFileInHDFS = Constants.INPUT_HDFS_PATH + "/"
          + fileMap.getValue();
      String singleOutputDirInHDFS = Constants.OUTPUT_HDFS_PATH + "/"
          + fileMap.getKey();

      String jar_cmd = "hadoop jar " + Constants.JAR_FILE + " wordcount -Dwordcount.case.sensitive=false " 
          + SingleFileInHDFS + " " + singleOutputDirInHDFS 
          + " -skip " + Constants.PATTERN_FILE_IN_HDFS;
      LOG.info("jar_cmd: " + jar_cmd);
//      exec.execute(new RealWordCountRunnable(jar_cmd));
       Process p = Runtime.getRuntime().exec(jar_cmd);
//       p.waitFor();   //here, we don't need to wait the process to finish 
    }
  }

  public void download() throws IOException, InterruptedException {
    String download_cmd = "hadoop fs -get " + Constants.OUTPUT_HDFS_PATH + " " + Constants.DOWNLOAD_RESULT_DIR;
    Process p = Runtime.getRuntime().exec(download_cmd);
    p.waitFor();
  }

  public void combine() throws IOException {
    BufferedWriter bw = new BufferedWriter(new FileWriter(Constants.PLDA_INPUT_FILE));

    for (Entry<String, String> fileMap : this.filePathMap.entrySet()) {
      String localOutputFilePath = Constants.DOWNLOAD_RESULT_DIR + "/output/" + fileMap.getKey() + "/" + "part-00000";
      BufferedReader br;
      try {
        br = new BufferedReader(new FileReader(localOutputFilePath));
        String line = null;
        while (null != (line = br.readLine())) {
          bw.append(line).append(" ");
        }
        br.close();
        bw.newLine();
        bw.flush();
      } finally {
        continue;
      }
    }
    bw.close();
  }

  public static void main(String[] args) throws IOException,
      InterruptedException {
    String rootPath = "/Users/caoj7/data/training_doc_set";
    int threadPool = 4;
    Transformer transformer = new Transformer(rootPath, threadPool);
    transformer.init();
    transformer.put();
    transformer.transform();
    transformer.download();
    transformer.combine();
  }

}
