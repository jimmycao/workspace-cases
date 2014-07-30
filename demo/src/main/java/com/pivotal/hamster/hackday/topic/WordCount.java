package com.pivotal.hamster.hackday.topic;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.pivotal.hamster.topic.hackday.util.FileListFinder;

public class WordCount {
  private String rootDirPath = null;
  private BufferedWriter bw = null;
  private ArrayList<String> fileFullPathList = null;
  
  private HashSet<String> stopWordSet = null;

  public WordCount(String rootDirPath, String outputFilePath) {
    this.rootDirPath = rootDirPath;
    //get all file list
    FileListFinder fileListFinder = new FileListFinder(rootDirPath);
    this.fileFullPathList = fileListFinder.getFilesList();
    //prepare the BufferedWriter
    try {
      this.bw = new BufferedWriter(new FileWriter(outputFilePath));
    } catch (IOException e) {
      e.printStackTrace();
    }
    
    stopWordSet = new HashSet<String>();
    String[]  stopWordArray = {"a","about","above","after","again","against",
        "all","am","an","and","any","are","aren't","as","at","be","because",
        "been","before","being","below","between","both","but","by","can't",
        "cannot","could","couldn't","did","didn't","do","does","doesn't",
        "doing","don't","down","during","each","few","for","from","further",
        "had","hadn't","has","hasn't","have","haven't","having","he","he'd",
        "he'll","he's","her","here","here's","hers","herself","him","himself",
        "his","how","how's","i","i'd","i'll","i'm","i've","if","in","into","is",
        "isn't","it","it's","its","itself","let's","me","more","most","mustn't",
        "my","myself","no","nor","not","of","off","on","once","only","or","other",
        "ought","our","ours","","ourselves","out","over","own","same","shan't",
        "she","she'd","she'll","she's","should","shouldn't","so","some","such",
        "than","that","that's","the","their","theirs","them","themselves",
        "then","there","there's","these","they","they'd","they'll","they're",
        "they've","this","those","through","to","too","under","until","up",
        "very","was","wasn't","we","we'd","we'll","we're","we've","were","weren't",
        "what","what's","when","when's","where","where's","which","while","who","who's",
        "whom","why","why's","with","won't","would","wouldn't","you","you'd","you'll",
        "you're","you've","your","yours","yourself","yourselves"};
    
    for (String word : stopWordArray) {
      stopWordSet.add(word);
    }
  }

  private void filterString(String originStr, StringBuffer sb) {
    originStr = originStr.trim();
    for (int i = 0; i < originStr.length(); i++) {
      char ch = originStr.charAt(i);
      if (ch > 64 && ch < 91 || ch > 96 && ch < 123) {
        sb.append(ch);
      } else {
        sb.append(" ");
      }
    }
  }
  
  private String[] filterWord(String[] before) {
    List<String> afterList = new ArrayList<String>();
    for (String s : before) {
      //remove 
      String trimed = s.trim();
      if (trimed.isEmpty() || this.stopWordSet.contains(trimed)) {
        continue;
      } else {
        afterList.add(trimed);
      }
    }
    
    return afterList.toArray(new String[0]);
  }
  
  public void count() throws IOException {
    for (String fileFullName : this.fileFullPathList) {
      Map<String, Integer> wordMap = new HashMap<String, Integer>();
      BufferedReader br = new BufferedReader(new FileReader(fileFullName));
      String line = null;
      while ((line = br.readLine()) != null) {
        if (line.isEmpty() || line.length() == 0) {
          continue;
        }
        
        if (line.contains("@")) {
          continue;
        }
        //toLowerCase
        line = line.toLowerCase();
        //filter the character which is not in the range 'a-z' and 'A-Z'
        StringBuffer filterSB = new StringBuffer();
        filterString(line, filterSB);
        String filteredLine = filterSB.toString();
//        System.out.println(filteredLine);
        
        //filter " " and "to, of ...."
        String[] wordArray = filteredLine.split(" ");
        String[] filteredWordArray = filterWord(wordArray);
        for (String aWord : filteredWordArray) {
          aWord = aWord.toLowerCase();
          if (wordMap.containsKey(aWord)) {
            wordMap.put(aWord,Integer.valueOf(wordMap.get(aWord).intValue() + 1));
          } else {
            wordMap.put(aWord, Integer.valueOf(1));
          }
        }
      }
      br.close();
      // write the resut *wordMap* to output file
      StringBuffer sb = new StringBuffer();
      Set<Entry<String, Integer>> set = wordMap.entrySet();
      for (Entry<String, Integer> entry : set) {
        sb.append(entry.getKey()).append(" ").append(entry.getValue()).append(" ");
      }

      String toLine = sb.toString();
      this.bw.write(toLine);
      this.bw.newLine();
      this.bw.flush();
    }
    this.bw.close();
  }

  
 
  /**
   * @param args
   * @throws IOException
   */
  public static void main(String[] args) throws IOException {    
    String rootPath = "/Users/caoj7/data/training_doc_set";
    String outputFilePath = "/tmp/wordcount.txt";
    WordCount wc = new WordCount(rootPath, outputFilePath);
    wc.count();
  }
}
