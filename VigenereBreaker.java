import java.util.*;
import edu.duke.*;
import java.io.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        StringBuilder answer = new StringBuilder();
        for(int k=whichSlice; k < message.length(); k += totalSlices){
        answer.append(message.charAt(k));
        }
        return answer.toString();
    }

    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] keySet = new int[klength];
       for(int k=0; k < klength; k ++){
        String part = sliceString(encrypted,k,klength);
        CaesarCracker cc = new CaesarCracker(mostCommon);
        int key = cc.getKey(part);
        keySet[k] = key;
        }
        return keySet;
    }
    
    public HashSet readDictionary(FileResource fr){
    HashSet<String> words = new HashSet();
    for (String line : fr.lines() ){
    words.add(line.toLowerCase());
    }
    return words;
    }
    
    //count the words in message which are in dictionary
    public int countWords (String message, HashSet dictionary){
    int count=0;
    String[] words = message.split("\\W+");
    for (int k=0; k < words.length; k ++ ){
        if (dictionary.contains(words[k].toLowerCase())) count++;
    }
    return count;
    }
    
    public char mostCommonCharIn(HashSet<String> dictionary){
    HashMap <Character,Integer> map = new HashMap <Character,Integer>();
    for (String i : dictionary){
        StringBuilder word = new StringBuilder(i);
    for(int k=0; k<i.length(); k++){
        char currChar = word.charAt(k);
        if (! map.containsKey(currChar)) map.put(currChar, 1);
        else map.put(currChar, map.get(currChar)+1);
    }
    }
    int max = 0;
    //StringBuilder characterSB = new StringBuilder();
   char mostCommon = ' '; 
   for (Integer v : map.values()){
        if (v>max) max=v;
   }
    for (Character s : map.keySet()){
        if (map.get(s)== max){
            mostCommon = s;
            //characterSB.insert(0,s);
        }
   }   
    //return characterSB.toString() ;
    return mostCommon;
    }
    
    private int maxidx(int[] numArr){
    int max = 0;
    int maxIndex = 0;
    for (int k=0; k < numArr.length; k ++ ){
        if (numArr[k]> max){
            max = numArr[k];
            maxIndex = k;
        }
    }
    System.out.println("Max (real words): "+ max);
    return maxIndex;
    }
    
    //out the decrypted message for a language
    public String breakForLanguage(String encrypted, HashSet<String> dictionary){
   
    int[] realWordCount= new int[100];
    for(int k=0; k < 100; k ++){
    int[] keySet = tryKeyLength(encrypted,k+1,'e');
    VigenereCipher VigenereObject = new VigenereCipher(keySet);
    String decrypted = VigenereObject.decrypt(encrypted);
    realWordCount[k] = countWords(decrypted, dictionary);
    }
    
    int keyLength = maxidx(realWordCount) + 1;
    System.out.println("Key Length : " + keyLength);
    char mostCommon = mostCommonCharIn(dictionary);
    int[] keySetConfirmed = tryKeyLength(encrypted,keyLength,mostCommon);
    //System.out.println("Key length 38- valid words : " + realWordCount[37]);
    System.out.println("Confirmed key set: ");
    
    for(int k=0; k < keySetConfirmed.length; k ++){
    System.out.println(keySetConfirmed[k]);
    }
    
    
    //int[] keySetConfirmed = {19,13,10,11,13,14,5,6,25,20,18,20,12,15,9,19,0,13,15,4,15,20,15,19,0,25,17,15,17,8,16,24,19,11,9,13,6,20,8,8,21,14,23,16,6,23,21,4,3,12,16,16,22,6,10,21,20};
    VigenereCipher VigenereObjectConfirmed = new VigenereCipher(keySetConfirmed);
    //System.out.println("Total words in file:" + VigenereObjectConfirmed.decrypt(encrypted).split("\\W+").length );
    return VigenereObjectConfirmed.decrypt(encrypted);
    }
    
        
    public void breakForAllLangs(String encrypted,HashMap<String,HashSet<String>> languagesDict){
    HashMap <String,Integer> map = new HashMap <String,Integer>();
    HashMap <String,String> mapDecrypt = new HashMap <String,String>();
        for (String language : languagesDict.keySet()){
        HashSet<String> dict = languagesDict.get(language);
        System.out.println("Language: "+language);
        String decrypt = breakForLanguage(encrypted, dict);
        mapDecrypt.put(language,decrypt);
        int wordsValid = countWords(decrypt, dict);
        map.put(language,wordsValid);
    }
    for (String s : map.keySet()){
    System.out.println(s+" : "+map.get(s));
    }
    
    int max = 0;
    String languageCorrect="";
    for (Integer v : map.values()){
        if (v>max) {
            max=v;
        }
    }
    
    System.out.println("Maximum valid word count: "+max);
    
    for (String s : map.keySet()){
    if(map.get(s) == max) {
    languageCorrect = s;
    System.out.println("##Correct: "+ s);    
    }
    }
    
    String content= mapDecrypt.get(languageCorrect);
    //String content = breakForLanguage(encrypted,languagesDict.get(languageCorrect) );
    System.out.println("Language Correct: "+languageCorrect);
    System.out.println(content);
    }
    
    
    public void breakVigenere () {
        FileResource frEnc = new FileResource();
        String content = frEnc.asString();
        
        HashMap<String,HashSet<String>> dictSet = new HashMap<String,HashSet<String>>();
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles()){
            FileResource fr = new FileResource(f);
            dictSet.put(f.getName(),readDictionary(fr));
        }
        
        breakForAllLangs(content,dictSet);
    }
    
    public void tester(){
        //System.out.println(sliceString("abcdefghijklm",0,5));
        /*
        FileResource text = new FileResource("athens_keyflute.txt");
        int[] keySet = tryKeyLength(text.asString(),5,'e');
        for(int k=0; k < keySet.length; k ++){
        System.out.println(keySet[k]);
        }
        */
        breakVigenere ();
    }
    
}
