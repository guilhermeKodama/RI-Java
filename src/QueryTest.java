import java.util.ArrayList;


public class QueryTest {
	String queryID;
	String query;
	ArrayList<String> relevantDocs;
	ArrayList<RetrievedDocument> resultDocs;
	
	public QueryTest(String queryID,String query,ArrayList<String> relevantDocs){
		this.queryID = queryID;
		this.query = query;
		this.relevantDocs = relevantDocs;
		
//		for(Integer id : this.relevantDocs){
//			System.out.println("ID : "+id);
//		}
	}
	
	public void setResulDocs(ArrayList<RetrievedDocument> result){
		this.resultDocs = result;
	}
	
	public float getPrecision(int numDocs){
		int numRelevantDocs = 0;
		
		for(int i = 0; i < numDocs ; i++){
			for (String docID : this.relevantDocs) {
				
				if(Integer.valueOf(docID).equals(Integer.valueOf(this.resultDocs.get(i).document.id))){
					numRelevantDocs++;
				}
					
			}
		}
		
		
		return  (float) numRelevantDocs/numDocs;
	}
	
	public float getMAP(){
		float sum_precisions = (float) 0.0;
		int count = 0;

		for(int i = 0; i < this.resultDocs.size() ; i++){
			for (String docID : this.relevantDocs) {
				
				if(Integer.valueOf(docID).equals(Integer.valueOf(this.resultDocs.get(i).document.id))){
					sum_precisions += getPrecision(i);
					count++;
				}
					
			}
		}
		
		
		return (float) sum_precisions/count;
	}
}
