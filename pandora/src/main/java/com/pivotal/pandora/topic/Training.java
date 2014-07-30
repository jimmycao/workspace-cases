package com.pivotal.pandora.topic;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.pivotal.pandora.common.CMDExecutor;
import com.pivotal.pandora.common.PandoraConstants;

public class Training {
  private static final Log LOG = LogFactory.getLog(Training.class);
  
  public static void train(String trainingDocPathInHDFS, String mpi_lda_path, int num_topics) throws IOException, InterruptedException {
    //1. download training-doc-set form HDFS
    File dir = new File(PandoraConstants.Training_Doc_Local_Dir);
    if (dir.exists()) {
      FileUtils.deleteDirectory(dir);
    }
    
    HDFSTool.download(trainingDocPathInHDFS, PandoraConstants.Training_Doc_Local_Dir);
    
    //2. transform training-doc-set to <word, count> format as training's input
    WordCount wc = new WordCount(PandoraConstants.Training_Doc_Local_Dir, PandoraConstants.Training_Input_FileName);
    wc.count();
    LOG.info("wordcount finished");
    
    //3. traning with PLDA
    String training_cmd = "hamster -np 4 " + mpi_lda_path + 
                          " --num_topics " + num_topics + 
                          " --alpha 0.1 --beta 0.01" + 
                          " --training_data_file " + PandoraConstants.Training_Input_FileName +
                          " --model_file " + PandoraConstants.Dictionary_Model +
                          " --total_iterations 150";
    LOG.info("training_cmd:" + training_cmd);
    
    CMDExecutor executor = new CMDExecutor();
    executor.run(training_cmd);
    
    LOG.info("training finished");
  }

 
  public static void main(String[] args) {
    int topicNum;
    if (args.length == 0) {
      topicNum = 2;
    } else {
      topicNum = Integer.parseInt(args[0]);
    }
    
    String trainingDocPathInHDFS = PandoraConstants.Training_Doc_Dir_In_HDFS;
    try {
      Training.train(trainingDocPathInHDFS, PandoraConstants.PLDA_Dir, topicNum);
    } catch (Exception e) {
      LOG.error("failed to training", e);
      System.exit(-1);
    }
  }
}
