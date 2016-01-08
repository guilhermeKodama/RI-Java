import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		ArrayList<QueryTest> queries = loadQueries(new File("/Users/guilherme/Desktop/cfc/cfquery"));
		
		try {
			VectorModel.load("/Users/guilherme/Desktop/docs");
		} catch (Exception e) {
			e.printStackTrace();
		}

		VectorModel.generateBagOfTerms();
		VectorModel.calculateWeightsTerms();
		// System.out.println(VectorModel.stringfy());
		
		//guarda os resultados de cada query
		for(QueryTest query : queries){
			query.setResulDocs(VectorModel.query(query.query));
		}
		
		//calcula o P@10
		for(QueryTest query : queries){
			System.out.println("QUERY : "+query.queryID+" PRECISION : "+query.getPrecision(10)+" MAP : "+query.getMAP());
		}
	}

	public static ArrayList<QueryTest> loadQueries(File f) {
		int count = 0;
		
		Scanner sc = null;

		String queryNumber = "";
		String query = "";
		ArrayList<String> releventDocuments = new ArrayList<String>();
		ArrayList<QueryTest> queries = new ArrayList<QueryTest>();
		
		String section = "   ";
		
		try {
			sc = new Scanner(f);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

		while (sc.hasNextLine()) {
			String line = sc.nextLine().toUpperCase();

			if (line.length() > 3) {

				if (!line.substring(0, 3).equals("   "))
					section = line.substring(0, 3);

				line = line.substring(3, line.length());
				String[] moreWords = line.split(" ");

				// pega informação do numero do artigo
				if (section.equals("QN ")) {
					if(queryNumber != ""){
						queries.add(new QueryTest(queryNumber,query.replace("\n",""),releventDocuments));
					}
					count = 0;
					queryNumber = line;
					releventDocuments = new ArrayList<String>();
				}
				
				if (section.equals("QU ")) {
					query += line;
				}
				
				// pega informação do autor do artigo
				if (section.equals("RD ")) {
					for(int i = 1; i < moreWords.length; i++){
						if(!moreWords[i].equals("")){
							if(count % 2 ==0){
								releventDocuments.add(moreWords[i]);
							}
							count++;
						}
							
					}
				}
			}
		}
		
		//adiciona o ultimo
		queries.add(new QueryTest(queryNumber,query,releventDocuments));

		return queries;
	}
}
