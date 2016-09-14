package com.google.engedu.ghost;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Random;

public class SimpleDictionary implements GhostDictionary {
    private ArrayList<String> words;
    Random random;

    public SimpleDictionary(InputStream wordListStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(wordListStream));
        random = new Random();
        words = new ArrayList<>();
        String line = null;
        while((line = in.readLine()) != null) {
            String word = line.trim();
            if (word.length() >= MIN_WORD_LENGTH)
              words.add(line.trim());
        }
    }

    @Override
    public boolean isWord(String word) {
        return words.contains(word);
    }

    @Override
    public String getAnyWordStartingWith(String prefix) {
        if(prefix.length()==0)
            return  words.get(random.nextInt(words.size()));
        int flag = BinarySearch(words, prefix, 0, words.size()-1);
         if(flag==-1)
             return null;
         return words.get(flag);
    }

    @Override
    public String getGoodWordStartingWith(String prefix) {
        ArrayList<String> allWords,evenWords,oddWords;
        evenWords = new ArrayList<>();
        oddWords = new ArrayList<>();

        allWords = BinarySearchList(words,prefix,0,words.size()-1);

        if(allWords.size()==0){
            return null;
        }
        else
        {

            for(int i=0;i<allWords.size();i++)
            {
                if( (allWords.get(i)).length()%2==0 )
                    evenWords.add(allWords.get(i));
                else
                    oddWords.add(allWords.get(i));
            }

            int size = prefix.length();

            if(size%2==0)
            {
                if(evenWords.size()!=0)
                    return evenWords.get(random.nextInt(evenWords.size()));
                else
                    return oddWords.get(random.nextInt(oddWords.size()));
            }

            else
            {
                if(oddWords.size()!=0)
                    return oddWords.get(random.nextInt(oddWords.size()));
                else
                    return evenWords.get(random.nextInt(evenWords.size()));
            }
        }

    }

    public int BinarySearch(ArrayList<String> words, String word, int start, int end) {
        if(start==end) {
            if(words.get(start).startsWith(word))
                return start;
            else
                return -1;
        }

        int mid = (start+end)/2;
        if(words.get(mid).startsWith(word))
            return mid;
        if(word.compareTo(words.get(mid))>0)
            return BinarySearch(words,word,mid+1,end);
        else
            return BinarySearch(words,word,start,mid-1);
    }

    public ArrayList<String> BinarySearchList(ArrayList<String> words, String word, int start, int end) {
        ArrayList<String> A;
        A=new ArrayList<>();
        if(start>end)
            return  A;
        if(start==end) {

            if(words.get(start).startsWith(word))
                A.add(words.get(start));
            return A;
            }

        int mid = (start + end) / 2;
        if(words.get(mid).startsWith(word)) {
            ArrayList<String> B;
            B=BinarySearchList(words,word,start,mid-1);
            ArrayList<String> C;
            C=BinarySearchList(words,word,mid+1,end);
            B.add(words.get(mid));
            B.addAll(C);
            return B;
        }
        if(word.compareTo(words.get(mid))>0)
            return BinarySearchList(words,word,mid+1,end);
        else
            return BinarySearchList(words,word,start,mid-1);

    }
}
