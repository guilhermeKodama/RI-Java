import java.util.ArrayList;


public class Document {
	String id;
	ArrayList<String> words = new ArrayList<String>();
	
	public Document(String id){
		this.id = id;
	}
	
	public Document(String id,ArrayList<String> words){
		this.id = id;
		this.words = words;
	}
	
	public void addWord(String word){
		words.add(word);
	}
}
