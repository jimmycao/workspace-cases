package com.pivotal.hamster.hackday.demo;

public class Constants {
  public static final String TIME = String.valueOf(System.currentTimeMillis());
  public static final String LOCAL_TMP_PATH = "/tmp/wordcount_" + TIME;
  
  public static final String WordCount_ROOT_HDFS_PATH = "/user/caoj7/wordcount";
  public static final String INPUT_HDFS_PATH = WordCount_ROOT_HDFS_PATH + "/input";
  public static final String OUTPUT_HDFS_PATH = WordCount_ROOT_HDFS_PATH + "/output";
      
  
  public static final String PATTERN_FILE = "patterns.txt";
  public static final String LOCAL_PATTERN_FILE = LOCAL_TMP_PATH  + "/" + PATTERN_FILE;
  
  public static final String JAR_FILE = "/Users/caoj7/workspace-j/hadoop-2.0.4-alpha/hadoop-mapreduce-project/hadoop-mapreduce-examples/target/hadoop-mapreduce-examples-2.0.4-alpha.jar";
  
//  public static final String HDFS_PREFIX = "hdfs://127.0.0.1:9000";
//  public static final String PATTERN_FILE_IN_HDFS = HDFS_PREFIX + WordCount_ROOT_HDFS_PATH + "/" + PATTERN_FILE;
  public static final String PATTERN_FILE_IN_HDFS = WordCount_ROOT_HDFS_PATH + "/" + PATTERN_FILE;
  
  
  public static final String DOWNLOAD_RESULT_DIR = LOCAL_TMP_PATH + "/download";
  public static final String PLDA_INPUT_FILE = LOCAL_TMP_PATH + "/plda.input";
  
  
  
  
}
