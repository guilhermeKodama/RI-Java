import java.util.ArrayList;
import java.util.Map;


public class TFIDF {
	Term t;
	Document d;
	double tfidf;
	
	public TFIDF(Term term,Document doc){
		this.t = term;
		this.d = doc;
		this.tfidf = 0.0;
	}
	
	public static double calculateTF(Term term,Document document){
		int frequency = 0;
		//calcula a frequencia do termo no documento
		for(String word : document.words){
			if(term.word.equals(word))
				frequency++;
		}
//		System.out.println("FREQUENCY : "+frequency);
		
		//se for 0 retorna 1 por que se não vai dar uma dizima
		if(frequency == 0)
			return 1;
		//calcula o TF
		return (double) (1 + Math.log(frequency));
	}
	
	public static double calculateIDF(Term term){
		double i = (double) VectorModel.documents.size()/term.referencies.size();
		return  (double) Math.log(i);
	}
	
	public static double[] calculateWeightVector(ArrayList<Term> terms,Document document){
		double[] weights = new double[terms.size()];
		int frequency = 0;
		double tf = 0.0,idf = 0.0;
		
		//calcular o peso para cada (termo,documento)
		for(int i = 0; i < weights.length; i++){
			Term term = terms.get(i);
//			System.out.println("======================");
//			System.out.println("TERM : "+term.word);
			
			//calcula o TF
			tf = calculateTF(term, document);
//			System.out.println("TF : "+tf);
			
			//calcula IDF
			idf = calculateIDF(term);
//			System.out.println("IDF : "+idf);
			
			//calcula o peso e insere no vetor
			if(document.id.equals("0")){
				weights[i] = idf;
			}else{
				weights[i] = tf * idf;
			}
		}
		return weights;
	}
	
	public void calculate(){
		double tf = TFIDF.calculateTF(this.t,this.d);
		double idf = TFIDF.calculateIDF(this.t);
		
		this.tfidf = (double) tf * idf;
	}
}
