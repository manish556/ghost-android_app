package com.google.engedu.ghost;

import java.util.Collections;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.StringTokenizer;
import java.util.Random;



public class TrieNode {
    private Random random = new Random();
    private HashMap<String, TrieNode> children;
    private boolean isWord;

    public TrieNode() {
        children = new HashMap<>();
        isWord = false;
    }

    public void add(String s) {
        if (s.length()==0)
            isWord = true;
        else {
            if( children.containsKey( s.substring(0,1) ) )
                children.get( s.substring(0,1) ).add(s.substring(1));
            else{
                children.put( s.substring(0,1) , new TrieNode() );
                children.get( s.substring(0,1) ).add(s.substring(1));
            }
        }
    }

    public boolean isWord(String s) {
      if( s.length()==0 && isWord ) {
          return true;
      }

      else if( s.length()==0 ) {
          return false;
      }

      else {
          if(children.containsKey(s.substring(0,1)))
            return children.get(s.substring(0,1)).isWord( s.substring(1) );
          else
              return false;
      }
    }

    public String getAnyWordStartingWith(String s) {
        if(s.length()!=0) {
            if( !children.containsKey(s.substring(0,1)) )
                return null;
            else {
                String temp = children.get(s.substring(0,1)).getAnyWordStartingWith(s.substring(1));
                if(temp == null)
                    return null;
                else
                 return s.substring(0,1)+temp;
            }
        }else {
            if(isWord)
                return "";
            else {
                ArrayList<String> aList = new ArrayList<>(children.keySet());
                 int r = random.nextInt(aList.size());
                     return aList.get(r)+children.get(aList.get(r)).getAnyWordStartingWith(s);
            }
        }

    }

    public String getGoodWordStartingWith(String s) {
        if(s.length()!=0) {
            if( !children.containsKey(s.substring(0,1)) )
                return null;
            else {
                String temp = children.get(s.substring(0,1)).getGoodWordStartingWith(s.substring(1));
                if(temp == null)
                    return null;
                else
                    return s.substring(0,1)+temp;
            }
        }
        else {

            if(!isWord) {
                ArrayList<String> aList = new ArrayList<>(children.keySet());
                //shuffle
                Collections.shuffle(aList);
                for(int i=0;i<aList.size();i++)
                {
                    if(!children.get(aList.get(i)).isWord(""))
                        return aList.get(i);
                }
                 return aList.get(0);
            }

            else
                return "";
        }
    }

}
