package mzk.com.NLPawesome;

import java.util.*;
import edu.stanford.nlp.coref.data.CorefChain;
import edu.stanford.nlp.ling.*;
import edu.stanford.nlp.ling.CoreAnnotations.NamedEntityTagAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.PartOfSpeechAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.SentencesAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TextAnnotation;
import edu.stanford.nlp.ling.CoreAnnotations.TokensAnnotation;
import edu.stanford.nlp.pipeline.*;
import edu.stanford.nlp.semgraph.*;
import edu.stanford.nlp.trees.*;
import edu.stanford.nlp.util.CoreMap;

import java.util.*;

import org.apache.log4j.BasicConfigurator;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.io.*;
//This code is to store named entity words
public class NamedentityAnalysis implements Serializable{

    public static void main(String[] args){
        FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter("H:\\2018\\project_final\\output\\SundayMorningOutput.json");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
        BasicConfigurator.configure();
             // Always wrap FileWriter in BufferedWriter.
           BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
        // creates a StanfordCoreNLP object, with POS tagging, lemmatization, NER, parsing, and coreference resolution
        Properties props = new Properties();
        props.setProperty("annotators", "tokenize, ssplit, pos, lemma, ner, parse, dcoref");
        StanfordCoreNLP pipeline = new StanfordCoreNLP(props);
        JSONParser parser=new JSONParser();
        
        Object obj = null;
        try {
            obj = parser.parse(new FileReader("H:\\2018\\project_final\\input\\SundayMorning.json"));
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JSONObject js = (JSONObject) obj;
        JSONObject json=new JSONObject();
        
       boolean flag = false;
       Set<String> keys = js.keySet();
      
       for(String key : keys)
       {
           String text=(String)js.get(key);
          flag = false;
           System.out.println(text);
         
       

        // create an empty Annotation just with the given text
        Annotation document = new Annotation(text);

        // run all Annotators on this text
        pipeline.annotate(document);
        List<CoreMap> sentences = document.get(SentencesAnnotation.class);
            
        for(CoreMap sentence: sentences) {
          // traversing the words in the current sentenc
          // a CoreLabel is a CoreMap with additional token-specific methods
            String s="";
          for (CoreLabel token: sentence.get(CoreAnnotations.TokensAnnotation.class)) {
            // this is the text of the token
            String word = token.get(TextAnnotation.class);
            // this is the POS tag of the token
            //String pos = token.get(PartOfSpeechAnnotation.class);
            // this is the NER label of the token
            String ne = token.get(NamedEntityTagAnnotation.class);
            //System.out.println("words "+word+" pos "+pos+" ne "+ne);
            if(!ne.equals("O")) 
                {
                if(flag == false) {
                    s+=word;
                    flag = true;
                }
                else
                    s += " " + word;
         //  System.out.println("words "+word);
            }
            
          // out.writeObject(s);

    }
          
          
          if((!s.equals("")) || (!s.isEmpty())){
             System.out.println(s);
              String index=String.valueOf(key);
             json.put(index,s);
             
        
       }
          
       
}
        
        
      
       
      
    }
     String message=json.toString();
     System.out.println(message);
    try {
		bufferedWriter.write(json.toJSONString());
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
      try {
		bufferedWriter.close();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
       
       
    /*   FileReader fileReader = 
               new FileReader("/home/rafeeq/out2.txt");

           // Always wrap FileReader in BufferedReader.
           BufferedReader bufferedReader = 
               new BufferedReader(fileReader);
           String s2=null;
           while((s2=bufferedReader.readLine())!=null) {
               System.out.println(s2);
           }   

           // Always close files.
           bufferedReader.close(); */

    }
}