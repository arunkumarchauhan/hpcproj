/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package topwords;

/**
 *
 * @author arunkumar
 */


import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.LongAdder;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.swing.*; 
import java.awt.*;
import java.awt.event.*;


import java.io.IOException;

import static java.lang.Integer.*;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileSystemView;

public class TopWords {

   public static void main(String[] args) throws Exception {
       // System.out.println("\n\nParallel word count example using Old Testement King James bible");
     
       Logic logic=new Logic(); 
       //logic.textWordCount("/home/arunkumar/NetBeansProjects/TopWords/src/topwords/1.txt");
    }
    /**
     * Return the top 5 most frequently used words from the sample text.
     * @throws Exception
     */
   
}

class Logic implements ActionListener
{   static private JFrame f;
    static   private  JButton b;
    static String str;
    private static JTextArea textArea; 
    private static JScrollPane scroll;
    public Logic()
    {   f=new JFrame();
       b=new JButton("Choose File");
       b.setBounds(130,50,150, 50);
       b.addActionListener(this);
       f.add(b);//adding button in JFrame  
       f.setSize(600,800);//400 width and 500 height  
       f.setLayout(null);//using no layout managers  
       f.setVisible(true);//making the frame visible  
    }
       public void actionPerformed(ActionEvent e) {
         if(e.getSource()==b)
         {JFileChooser jfc = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());

             int returnValue = jfc.showOpenDialog(null);
		// int returnValue = jfc.showSaveDialog(null);
                str="";
		if (returnValue == JFileChooser.APPROVE_OPTION) {
			File selectedFile = jfc.getSelectedFile();
             try {
                 textWordCount(selectedFile.getAbsolutePath());
                 
             } catch (Exception ex) {
                 Logger.getLogger(Logic.class.getName()).log(Level.SEVERE, null, ex);
             }
         }
      }
       }
    
  public static void textWordCount(String fileName) throws Exception {
        long start = Instant.now().toEpochMilli();
        String s="";
        ConcurrentHashMap<String, LongAdder> wordCounts = new ConcurrentHashMap<>();
        System.out.println("\tReading file: "+fileName);
        Path filePath = Paths.get(fileName);
        Files.readAllLines(filePath)
            .parallelStream()                               // Start streaming the lines
            .map(line -> line.split("\\s+"))                // Split line into individual words
            .flatMap(Arrays::stream)                        // Convert stream of String[] to stream of String
            .parallel()                                     // Convert to parallel stream
            .filter(w->w.length()>2) 
            .filter(w -> w.matches("\\w+"))                 // Filter out non-word items
            .map(String::toLowerCase)                       // Convert to lower case
            .forEach(word -> {                              // Use an AtomicAdder to tally word counts
                if (!wordCounts.containsKey(word))          // If a hashmap entry for the word doesn't exist yet
                    wordCounts.put(word, new LongAdder());  // Create a new LongAdder
                wordCounts.get(word).increment();           // Increment the LongAdder for each instance of a word
            });
        wordCounts
                .keySet()
                .stream()
                .map(key -> String.format("%-10d %s", wordCounts.get(key).intValue(), key))
                .sorted((prev, next) -> compare(parseInt(next.split("\\s+")[0]), parseInt(prev.split("\\s+")[0])))
                .limit(10)
		.skip(0)
                .forEach(t -> Logic.addStr(t));
        long end = Instant.now().toEpochMilli();
     
        System.out.print(str);
        System.out.println(String.format("\tCompleted in %d milliseconds", (end-start)));
    }
  
  private static void addStr(String s)
  {
      str+="\t"+s+"\n";
  }
}
	


