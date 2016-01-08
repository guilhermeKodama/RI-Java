import java.util.ArrayList;


public class Term {
	ArrayList<TFIDF> referencies = new ArrayList<TFIDF>();
	String word;
	
	public Term(String word){
		this.word = word;
	}
	
	public void addReferency(Document doc){
		this.referencies.add(new TFIDF(this,doc));
	}
	
	public void calculateTFIDF(){
		for(TFIDF weight : this.referencies){
			weight.calculate();
		}
	}
	
	public String toString(){
		StringBuilder output = new StringBuilder();
		output.append("TERM : "+this.word+"\n");
		for(TFIDF referency : referencies){
			output.append("DOC : "+referency.d.id+" TFIDF : "+referency.tfidf+"\n");
		}
		return output.toString();
	}
	
	public double getWeightFromDocument(String id){
		double tfidf = 0;
		for(TFIDF referency : referencies){
			if(referency.d.id.equals(id))
				tfidf = referency.tfidf;
		}
		return tfidf;
	}
	
}
