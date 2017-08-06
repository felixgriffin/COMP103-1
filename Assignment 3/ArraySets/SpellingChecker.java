// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2017T2, Assignment 3
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;
import java.io.*;

/** SpellingChecker checks all the words in a file against a dictionary.
 *  The dictionary is read from the dictionary file into a set.
 *  When reading through the document with spelling errors, the program checks
 *  each word against the dictionary.
 *  Any word that is not in the dictionary is considered to be an error and
 *  is printed out.
 *
 *  The program records and prints out the total time taken to read
 *  all the words into the dictionary.
 *  It also records and prints out the total time taken to check all the words.
 *
 *  Note that the dictionary and the file to check are assumed to be all
 *  lowercase, with all punctuation removed.
 */

public class SpellingChecker{

    public static void main(String[] arguments){
        UI.setDivider(1);
        UI.println("Reading Dictionary");
        try{

            Scanner dictFile = new Scanner(new File("dictionaryUnsorted.txt"));
            //Scanner dictFile = new Scanner(new File("dictionarySorted.txt"));

            //load the words from the file into an arraylist, to avoid measuring
            //the file reading time
            List<String> dictWords = new ArrayList<String>();

            while (dictFile.hasNext())
                dictWords.add(dictFile.next()); 

            //measure the set construction
            long start = System.currentTimeMillis();
            Set<String> dictionary = new ArraySet<String>();
            // Set<String> dictionary = new SortedArraySet<String>();
            // Set<String> dictionary = new HashSet<String>();

            for (String word : dictWords){
                dictionary.add(word);

                if (dictionary.size()%1000==0) 
                    UI.println(dictionary.size());
            }

            long loading = (System.currentTimeMillis()-start);
            dictFile.close();
            UI.println("Dictionary Loaded");

            //load the words from the file into an arraylist, to avoid measuring
            //the file reading time
            Scanner wordFile = new Scanner(new File("plato.txt")); //UIFileChooser.open()));
            List<String> words = new ArrayList<String>();
            while (wordFile.hasNext()){ words.add(wordFile.next().toLowerCase()); }

            //measure the set construction
            start = System.currentTimeMillis();

            for (String word : words){
                if (!dictionary.contains(word)) {
                    UI.println(word);
                }
            }

            long checking = (System.currentTimeMillis()-start);
            UI.println("------------------");
            UI.printf("Loading took: %.3f seconds\n", loading/1000.0);
            UI.printf("Checking took: %.3f seconds\n", checking/1000.0);

            wordFile.close();
        }
        catch(IOException e){UI.println("Fail: " + e);}
    }        
}
