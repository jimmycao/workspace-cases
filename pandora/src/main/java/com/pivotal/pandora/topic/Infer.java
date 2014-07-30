package com.pivotal.pandora.topic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.hadoop.fs.FileUtil;

import com.pivotal.pandora.common.CMDExecutor;
import com.pivotal.pandora.common.FileListFinder;
import com.pivotal.pandora.common.PandoraConstants;

public class Infer {
  private static final Log LOG = LogFactory.getLog(Training.class);
  private String userName;
  private String topic0;
  private String topic1;
  
  private String inferRootDir;
  private String Infer_Combine_Dir;
  private String Infer_Combine_File;
  private String Infer_Input_File;
  private String Infer_Output_File;
  
  private String sportAd = "*********************************************\n"
      +"***     NBA Game is Live-broadcasting!    ***\n"
      +"***           ^_^, click here!            ***\n"
      +"*********************************************\n";
  
  private String computerAd = "*********************************************************\n"
      +"***  New Mac Product release is live-broadcasting !   ***\n"
      +"***                ^_^, click here!                   ***\n"
      +"*********************************************************\n";
  
  public Infer(String userName) {
    this.userName = userName;
    
    this.inferRootDir = PandoraConstants.Infer_Local_Dir + "/" + userName;
    this.Infer_Combine_Dir = this.inferRootDir + "/combine";
    this.Infer_Combine_File = this.inferRootDir + "/combine/infer_combined";
    this.Infer_Input_File = this.inferRootDir + "/infer_input";
    this.Infer_Output_File = this.inferRootDir + "/infer_output";
  }
  
  private void combine() throws IOException {
    File combineDir = new File(Infer_Combine_Dir);
    if (!combineDir.exists()) {
      combineDir.mkdirs();
    }
    
    FileListFinder ff = new FileListFinder(inferRootDir);
    List<String> files = ff.getFilesList();
    
    BufferedWriter bw = new BufferedWriter(new FileWriter(Infer_Combine_File));
    for (String file : files) {
      BufferedReader br = new BufferedReader(new FileReader(file));
      String line = null;
      while((line = br.readLine()) != null) {
        bw.write(line);
        bw.write('\n');
      }
      br.close();
    }
    bw.close();  
  }
  
  public void infer() throws IOException {
    //1. download file from HDFS
    File rootDir = new File(inferRootDir);
    if (rootDir.exists()) {
      FileUtils.deleteDirectory(rootDir);
    }
    rootDir.mkdirs();
    
    String dirInHDFS = PandoraConstants.Blog_Dir_In_HDFS + "/" + userName;
    HDFSTool.download(dirInHDFS, inferRootDir);
    
    //2. combile files 
    combine();
    LOG.info("combine finished.");
    
    //3. wordcount
    WordCount wc = new WordCount(Infer_Combine_Dir, Infer_Input_File);
    wc.count();
    LOG.info("wordcount finished.");
    
    
    //4. infer
    String infer_cmd =  PandoraConstants.Infer_Dir + " --alpha 0.1 --beta 0.01" +
        " --inference_data_file " + Infer_Input_File +
        " --inference_result_file " + Infer_Output_File + 
        " --model_file " + PandoraConstants.Dictionary_Model +
        " --total_iterations 15 --burn_in_iterations 10";

    CMDExecutor executor = new CMDExecutor();
    executor.run(infer_cmd);
    LOG.info("infer finished.");
  }

  private void confirmTopics() throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(PandoraConstants.Dictionary_Model));
    String line = null;
    while ((line = br.readLine()) != null) {
      if (line.contains("sport")) {
        String[] tmpArray = line.split("\t");
        String[] tmpStrArray = tmpArray[1].split(" ");
        double score0 = Double.parseDouble(tmpStrArray[0]);
        double score1 = Double.parseDouble(tmpStrArray[1]);
        if (score0 > score1) {
          this.topic0 = "sport";
          this.topic1 = "computer";
        } else {
          this.topic0 = "computer";
          this.topic1 = "sport";
        }
        break;
      }
    }
    
  }
  
  
  public void printInferResult() throws IOException {
    confirmTopics();
    LOG.info("topic0=" + this.topic0 + ", topic1=" + this.topic1);
    
    BufferedReader br = new BufferedReader(new FileReader(Infer_Output_File));
    String line = br.readLine();
    
    LOG.info(userName + "'s score: " + line);
    
    String[] scores = line.split(" ");
    double score0 = Double.parseDouble(scores[0]);
    double score1 = Double.parseDouble(scores[1]);
    String favoriteTopic = score0 > score1 ? this.topic0 : this.topic1;
    
    LOG.info("insert advertisement when " + userName + " log in:");
    if (favoriteTopic.equals("sport")) {
      System.out.println(this.sportAd);
    } else {
      System.out.println(this.computerAd);
    }
  }

  public static void main(String[] args) {
    String username = args[0];
    Infer in = new Infer(username);
    try {
      in.infer();
      in.printInferResult();
    } catch (IOException e) {
      LOG.error("failed to infer docs", e);
    }
  }
}
