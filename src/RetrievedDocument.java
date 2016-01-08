
public class RetrievedDocument implements Comparable<RetrievedDocument>{
	Document document;
	double similarity;
	
	public RetrievedDocument(Document doc, double similarity){
		this.document = doc;
		this.similarity = similarity;
	}

	@Override
	public int compareTo(RetrievedDocument o) {
		
		if (this.similarity > o.similarity) {
		    return 1;
		}
		if (this.similarity < o.similarity) {
		    return -1;
		}
		
		return 0;
	}
}
