import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import com.sun.org.apache.regexp.internal.REUtil;


public class VectorModel {
	public static ArrayList<Document> documents = new ArrayList<Document>();
	public static Map<String,Term> terms = new HashMap<String,Term>();
	
	public static void addDocument(Document doc){
		documents.add(doc);
	}
	
	public static void generateBagOfTerms(){
		for(Document doc : documents){
			for(String word : doc.words){
				
				if(terms.containsKey(word)){
					terms.get(word).addReferency(doc);
				}else{
					terms.put(word,new Term(word));
					terms.get(word).addReferency(doc);
				}
				
			}
		}
	}
	
	public static void calculateWeightsTerms(){
		for(String key : terms.keySet()){
			Term term = terms.get(key);
			term.calculateTFIDF();
		}
	}
	
	private static Map<String,String> colideArray(Map<String,String> A, String[] B){
		
		for(String word : B){
			A.put(word,word);
		}
		
		return A;
	}
	
	//carrega todos os documentos a suas respectivas lista de termos
	public static void load(String path) throws Exception{
		Scanner sc = null;
		File folderDocs = new File(path);
	    
	 
	    for (File f : folderDocs.listFiles()) {
	    	String docNumber = "";
	    	Map<String,String> words = new HashMap<String,String>();
	    	String section = "   ";
	    	System.out.println(f);
	    	try {
		        sc = new Scanner(f);
		    } catch (FileNotFoundException e) {
		    	e.printStackTrace();
		    }
	    	
	    	while (sc.hasNextLine()) {
		    	String line = sc.nextLine().toUpperCase();
		    	
		    	if(line.length() > 3){
		    		
		    		if(!line.substring(0,3).equals("   "))
		    			section = line.substring(0,3);
		    		
		    		line = line.substring(3,line.length());
		    		String[] moreWords = line.replace('-',' ').replace(":","").replace("\"","").replace(".","").split(" ");
		    		
		    		
		    		//pega informação do numero do artigo
			    	if(section.equals("RN ")){
			    		//adiciona os termos e referencias referentes a esse artigo
			    		if(words.size() > 0){
			    			//add document
			    			Document newDocument = new Document(docNumber);
			    			for(String key : words.keySet()){
			    				newDocument.addWord(words.get(key));
			    			}
			    			documents.add(newDocument);
			    		}
			    			
			    		words = new HashMap<String,String>();
			    		docNumber = moreWords[0];
			    	}
			    	//pega informação do autor do artigo
			    	if(section.equals("AU ")){
			    		colideArray(words,moreWords);
			    	}
			    	//pega informação do titulo do artigo
			    	if(section.equals("TI ")){
			    		colideArray(words,moreWords);
			    	}
			    	//pega informação dos assuntos mais importantes abordados
			    	if(section.equals("MJ ")){
			    		colideArray(words,moreWords);
			    	}
			    	//pega informação do abstract
			    	if(section.equals("AB ")){
			    		colideArray(words,moreWords);
			    	}
			    	
		    	}
		    }
	    }
	}
	
	public static ArrayList<RetrievedDocument> query(String q){
		ArrayList<RetrievedDocument> result = new ArrayList<RetrievedDocument>();
		String[] words = q.replace('-',' ').replace(":","").replace("\"","").replace(".","").replace("?","").toUpperCase().split(" ");
		ArrayList<String> queryWords = new ArrayList<String>();
		ArrayList<Term> queryTerms = new ArrayList<Term>();
		Map <String,Document> documentsWithTerms = new HashMap<String,Document>();
		
		//verifica quais palavras da query são termos relevantes
		for(String word : words){
			if(terms.containsKey(word)){
				double idf = TFIDF.calculateIDF(terms.get(word));
				if(idf > 3){
					queryTerms.add(terms.get(word));
					queryWords.add(word);
				}
			}
				
		}
		
		//pega todos os documentos que contém um ou mais termos contidos na query
		for(Term term : queryTerms){
			ArrayList <TFIDF> referencies = term.referencies;
			
			for(TFIDF referency : referencies){
				documentsWithTerms.put(referency.d.id,referency.d);
			}
		}
		
		//calcula peso da query
		double[] queryWeights = new double[queryTerms.size()];
		queryWeights = TFIDF.calculateWeightVector(queryTerms,new Document("0",queryWords));
		
		//calcula a similaridade de cada documento
		for( String key : documentsWithTerms.keySet()){
			double[] docWeights = new double[queryTerms.size()];
			Document doc = documentsWithTerms.get(key);
			
			
			
			//calcula o vetor de pesos do documento
			for(int i = 0 ; i < queryWeights.length ; i++){
				docWeights[i] = terms.get(queryTerms.get(i).word).getWeightFromDocument(doc.id);
			}
			
			double sum_mult = 0,sum_squared_query = 0,sum_squared_doc = 0,sim = 0;
			
			for(int i = 0 ; i < queryWeights.length ; i++){
				sum_mult += docWeights[i] * queryWeights[i];
				sum_squared_query += queryWeights[i] * queryWeights[i];
				sum_squared_doc += docWeights[i] * docWeights[i];
			}
			
			sim = sum_mult / (sum_squared_doc * sum_squared_query);
			
			result.add(new RetrievedDocument(doc,sim));
		}
		//ordena os documentos por ordem de similaridade
		//Sorting
		System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
		Collections.sort(result);
		
//		for(int i =0;i<10;i++){
//			System.out.println("RESULTADO : "+result.get(i).document.id);
//		}
		return result;
	}
	
	private static void printArray(double[] W){
		for(double w : W){
			System.out.println("weight : "+w);
		}
	}
	
	public static String stringfy(){
		
		StringBuilder output = new StringBuilder();
		
		for(String key : terms.keySet()){
			output.append(terms.get(key).toString());
		}
		
		return output.toString();
	}
}
