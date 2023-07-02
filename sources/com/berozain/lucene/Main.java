package com.berozain.lucene;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.CharArraySet;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.core.LowerCaseFilter;
import org.apache.lucene.analysis.core.StopFilter;
import org.apache.lucene.analysis.en.EnglishMinimalStemFilter;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.analysis.standard.StandardTokenizer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.queryparser.classic.MultiFieldQueryParser;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.similarities.AfterEffectL;
import org.apache.lucene.search.similarities.AxiomaticF1LOG;
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.search.similarities.BasicModelG;
import org.apache.lucene.search.similarities.BooleanSimilarity;
import org.apache.lucene.search.similarities.ClassicSimilarity;
import org.apache.lucene.search.similarities.DFRSimilarity;
import org.apache.lucene.search.similarities.DistributionLL;
import org.apache.lucene.search.similarities.IBSimilarity;
import org.apache.lucene.search.similarities.LMDirichletSimilarity;
import org.apache.lucene.search.similarities.LambdaDF;
import org.apache.lucene.search.similarities.NormalizationH2;
import org.apache.lucene.search.similarities.Similarity;
import org.apache.lucene.search.similarities.TFIDFSimilarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

public class Main {
	public static enum Algorithm {
		IB("IB"),
		DFR("DFR"),
		BM_25("BM-25"),
		TF_IDF("TF-IDF"),
		Classic("Classic"),
		Boolean("Boolean"),		
		Axiomatic("Axiomatic"),
		LM_Dirichlet("LM-Dirichlet");
		
		public String name;
		
		Algorithm(String name) {
			this.name = name;
		}
	}
	
	public static boolean enableStammer = true;
	public static boolean enableStopWords = true;
	public static Algorithm algorithm = Algorithm.BM_25;
	public static final String path_caches_directory = "/Users/behrouz/Documents/Files/Eclipse/lucene/data/CISI/";
	public static final String path_caches = path_caches_directory + "caches";
	public static final String path_documents = path_caches_directory + "CISI.ALL";
	public static final String path_queries = path_caches_directory + "CISI.QRY";
	public static final String path_relevance = path_caches_directory + "CISI.REL";
	//public static final List<String> stop_words = Arrays.asList("a", "an", "and", "are", "as", "at", "be", "but", "by", "for", "if", "in", "into", "is", "it", "no", "not", "of", "on", "or", "such", "that", "the", "their", "then", "there", "these", "they", "this", "to", "was", "will", "with");
	public static final List<String> stop_words = Arrays.asList("i", "me", "my", "myself", "we", "our", "ours", "ourselves", "you", "you're", "you've", "you'll", "you'd", "your", "yours", "yourself", "yourselves", "he", "him", "his", "himself", "she", "she's", "her", "hers", "herself", "it", "it's", "its", "itself", "they", "them", "their", "theirs", "themselves", "what", "which", "who", "whom", "this", "that", "that'll", "these", "those", "am", "is", "are", "was", "were", "be", "been", "being", "have", "has", "had", "having", "do", "does", "did", "doing", "a", "an", "the", "and", "but", "if", "or", "because", "as", "until", "while", "of", "at", "by", "for", "with", "about", "against", "between", "into", "through", "during", "before", "after", "above", "below", "to", "from", "up", "down", "in", "out", "on", "off", "over", "under", "again", "further", "then", "once", "here", "there", "when", "where", "why", "how", "all", "any", "both", "each", "few", "more", "most", "other", "some", "such", "no", "nor", "not", "only", "own", "same", "so", "than", "too", "very", "s", "t", "can", "will", "just", "don", "don't", "should", "should've", "now", "d", "ll", "m", "o", "re", "ve", "y", "ain", "aren", "aren't", "couldn", "couldn't", "didn", "didn't", "doesn", "doesn't", "hadn", "hadn't", "hasn", "hasn't", "haven", "haven't", "isn", "isn't", "ma", "mightn", "mightn't", "mustn", "mustn't", "needn", "needn't", "shan", "shan't", "shouldn", "shouldn't", "wasn", "wasn't", "weren", "weren't", "won", "won't", "wouldn", "wouldn't");
	public static void main(String[] args) throws Exception {
		Scanner scan = new Scanner(System.in);
		String input = "";
		for(int clear = 0; clear < 1000; clear++) {
		    System.out.println("\b") ;
		}
		showMenu();
		MAIN: while (true) {
			input = scan.nextLine();
			switch (input) {
				case "1":
					for(int clear = 0; clear < 30; clear++) {
					    System.out.println("\b") ;
					}
					System.out.println("");
					System.out.println("  Choose Similarity Algorithm:");
					System.out.println((algorithm == Algorithm.IB ? " *" : "  ") + "  1: " + Algorithm.IB.name);
					System.out.println((algorithm == Algorithm.DFR ? " *" : "  ") + "  2: " + Algorithm.DFR.name);
					System.out.println((algorithm == Algorithm.BM_25 ? " *" : "  ") + "  3: " + Algorithm.BM_25.name);
					System.out.println((algorithm == Algorithm.TF_IDF ? " *" : "  ") + "  4: " + Algorithm.TF_IDF.name);
					System.out.println((algorithm == Algorithm.Classic ? " *" : "  ") + "  5: " + Algorithm.Classic.name);
					System.out.println((algorithm == Algorithm.Boolean ? " *" : "  ") + "  6: " + Algorithm.Boolean.name);
					System.out.println((algorithm == Algorithm.Axiomatic ? " *" : "  ") + "  7: " + Algorithm.Axiomatic.name);
					System.out.println((algorithm == Algorithm.LM_Dirichlet ? " *" : "  ") + "  8: " + Algorithm.LM_Dirichlet.name);
					System.out.println();
					System.out.print("  Option: ");
					String option = scan.nextLine();
					System.out.println();
					switch(option) {
						case "1":
							algorithm = Algorithm.IB;
							System.out.print("  Similaity Algorithm Changed to " + Algorithm.IB.name);
							break;
						case "2":
							algorithm = Algorithm.DFR;
							System.out.print("  Similaity Algorithm Changed to " + Algorithm.DFR.name);
							break;
						case "3":
							algorithm = Algorithm.BM_25;
							System.out.print("  Similaity Algorithm Changed to " + Algorithm.BM_25.name);
							break;
						case "4":
							algorithm = Algorithm.TF_IDF;
							System.out.print("  Similaity Algorithm Changed to " + Algorithm.TF_IDF.name);
							break;
						case "5":
							algorithm = Algorithm.Classic;
							System.out.print("  Similaity Algorithm Changed to " + Algorithm.Classic.name);
							break;
						case "6":
							algorithm = Algorithm.Boolean;
							System.out.print("  Similaity Algorithm Changed to " + Algorithm.Boolean.name);
							break;
						case "7":
							algorithm = Algorithm.Axiomatic;
							System.out.print("  Similaity Algorithm Changed to " + Algorithm.Axiomatic.name);
							break;
						case "8":
							algorithm = Algorithm.LM_Dirichlet;
							System.out.print("  Similaity Algorithm Changed to " + Algorithm.LM_Dirichlet.name);
							break;
						default:
							System.out.print("  Error: value is out of range");
							break;
					}
					System.out.println();
					break;
				case "2":
					enableStammer = !enableStammer;
					System.out.println();
					System.out.print("  Stammer " + (enableStammer ? "Enabled" : "Disabled"));
					System.out.println();
					break;
				case "3":
					enableStopWords = !enableStopWords;
					System.out.println();
					System.out.print("  Stop Words " + (enableStopWords ? "Enabled" : "Disabled"));
					System.out.println();
					break;
				case "4":
					long timer = System.currentTimeMillis();
					indexDocuments();
					timer = System.currentTimeMillis() - timer;
					float size = FileUtils.sizeOfDirectory(new File(path_caches)) / 1024 / 1024f;
					System.out.println();
					System.out.printf("  Index created successfully in %dms and size of %.2fMB", timer, size);
					System.out.println();
					break;
				case "5":
					if (getIndexWriter() != null) {
						System.out.println();
						System.out.print("  Query: "); //System.out.println("docTitle:\"\" AND docAuthors:\"\" AND docContent:\"\"");
						String mQuery = scan.nextLine();
						if (mQuery.length() == 0) {
							System.out.println();
							System.out.println("  Error: query can not be empty");
							System.out.println();
							break;
						}
						timer = System.currentTimeMillis();
						ArrayList<Document> docs = searchQuery(mQuery);
						timer = System.currentTimeMillis() - timer;
						showSearchResults(docs, timer);
					} else {
						System.out.println();
						System.out.println("  Error: index not created");
						System.out.println();
					}
					break;
				case "6":
					if (getIndexWriter() == null) {
						System.out.println();
						System.out.println("  Error: index not created");
						System.out.println();
					} else {
						System.out.println("");
						System.out.println("  ---------------------------------------------------------------------------");
						System.out.println("  Results");
						System.out.println("  ---------------------------------------------------------------------------");
						System.out.println("  Recall    : Relevante Results / All Relevantes                   TP/(TP+FN)");
						System.out.println("  Precision : Relevante Results / All Results                      TP/(TP+FP)");
						System.out.println("  MAP       : Ranking Evaluation (Mean Average Precision)");
						System.out.println("  F-Measure : Precision Recall Evaluation");
						System.out.println("  rel : The number of documents that have been retrieved");
						System.out.println("  REL : The number of documents we expect to be retrieved");
						System.out.println("  ---------------------------------------------------------------------------");
						indexEvaluation();
						Evaluation evaluation = computeEvaluation();
						for (Integer i : evaluation.fmeasures.keySet()) {
							if (i != -1 && i != -2) {
								String counter = i + "";
								if (i < 10) {
									counter = "00" + counter;
								} else if (i < 100) {
									counter = "0" + counter;
								}
								System.out.printf("  Q-%s  Recall=%.3f  Precision=%.3f  F1=%.3f  MAP=%.3f  rel=%03.0f  REL=%03.0f", counter, evaluation.recalls.get(i), evaluation.precisions.get(i), evaluation.fmeasures.get(i), evaluation.maps.get(i), evaluation.rels.get(i), evaluation.RELs.get(i));
								System.out.println();
							}
						}
						System.out.println("  ---------------------------------------------------------------------------");
						System.out.printf("  Q-All  Recall=%.3f  Precision=%.3f  F1=%.3f  MAP=%.3f  rel=%03.0f  REL=%03.0f", evaluation.recalls.get(-1), evaluation.precisions.get(-1), evaluation.fmeasures.get(-1), evaluation.maps.get(-1), evaluation.rels.get(-1), evaluation.RELs.get(-1));
						System.out.println();
						System.out.printf("         Recall=%04.1f%%  Precision=%04.1f%%  F1=%04.1f%%  MAP=%04.1f%%", evaluation.recalls.get(-2), evaluation.precisions.get(-2), evaluation.fmeasures.get(-2), evaluation.maps.get(-2));
						System.out.println();
						System.out.println("  ---------------------------------------------------------------------------");
					}
					break;
				case "0":
					for(int clear = 0; clear < 1000; clear++) {
					    System.out.println("\b") ;
					}
					scan.close();
					break MAIN;
				default: 
					System.out.println();
					System.out.println("  Error: value is out of range");
				System.out.println();
			}
			scan.nextLine();
			for(int clear = 0; clear < 30; clear++) {
			    System.out.println("\b") ;
			}
			showMenu();
		}
	}
	
	private static void showMenu() {
		System.out.println("  Status:");
		System.out.println("    Algorithm  : " + algorithm.name);
		System.out.println("    Streemer   : " + (enableStammer ? "ON" : "OFF"));
		System.out.println("    Stop Words : " + (enableStopWords ? "ON" : "OFF"));
		System.out.println("");
		System.out.println("  Menu:");
		System.out.println("    1: Choose Algorithm");
		System.out.println(enableStammer ? "    2: Disable Streemer" : "    2: Enable Streemer");
		System.out.println(enableStopWords ? "    3: Disable Stop Words" : "    3: Enable Stop-Words");
		System.out.println("    4: Create Index");
		System.out.println("    5: Search in Documents");
		System.out.println("    6: Evaluation");
		System.out.println("    0: Exit");
		System.out.println();
		System.out.print("  Option: ");
	}
	
	private static void showSearchResults(ArrayList<Document> docs, long timer) throws Exception {
		Scanner scan = new Scanner(System.in);
		System.out.println();
		System.out.printf("    We found %d documents in %dms", docs.size(), timer);
		System.out.println();
		System.out.println();
		System.out.println("    #    Rank  ID    Title");
		int nonPrintIndex = -1;
		for (int i = 0; i < docs.size(); i++) {
			Document doc = docs.get(i);
			if (i > nonPrintIndex) {
				int index = i + 1;
				int docID = Integer.parseInt(doc.get("docID"));
				String docTitle = String.valueOf(doc.get("docTitle")).trim();
				float docScore = Float.parseFloat(doc.get("docScore"));
				System.out.printf("    %03d  %.2f  %04d  %s", index, docScore, docID, docTitle);
				System.out.println("");
			}
			if (nonPrintIndex < i) {
				nonPrintIndex = i;
			}
			if (i % 10 == 9) {
				String input = scan.nextLine();
				int isInteger = isInteger(input);
				if (input.equals(" ")) {
					continue;
				} else if (isInteger > 0 && isInteger <= docs.size()) {
					int m = Integer.parseInt(input) - 1;
					Document mDoc = docs.get(m);
					System.out.println("    " + mDoc.get("docID") + ". " + mDoc.get("docTitle").trim() + " :");
					System.out.println("    " + mDoc.get("docContent").trim());
					System.out.println("    Authors: " + mDoc.get("docAuthors").trim());
					i--;
				} else {
					break;
				}
			}
		}
	}
	
	public static IndexWriter indexWriter;
	public static FileInputStream fisIndex;
	public static BufferedReader brIndex;
	
	public static void indexDocuments() throws Exception {
		fisIndex = new FileInputStream(new File(path_documents));
		brIndex = new BufferedReader(new InputStreamReader(fisIndex));
		Directory indexDir = FSDirectory.open((new File(path_caches)).toPath());
		//StandardAnalyzer analyzer = new StandardAnalyzer(CharArraySet.EMPTY_SET);
		Analyzer analyzer = createAnalyzer();
		IndexWriterConfig config = new IndexWriterConfig(analyzer);
		config.setOpenMode(OpenMode.CREATE);
		config.setSimilarity(createSimilarity());
		indexWriter = new IndexWriter(indexDir, config);
		String line = null;
		String moreThanOneLine = "";
		line = brIndex.readLine();
		while (true) {
			Document doc = new Document();
			moreThanOneLine = "";

			// Read id
			doc.add(new StringField("docID", line.split(" ")[1], Field.Store.YES));
			line = brIndex.readLine();

			// Read title
			while (!line.startsWith(".A")) {
				line = brIndex.readLine();
				if (!line.startsWith(".A")) {
					moreThanOneLine += line + "\n";
				}
			}
			doc.add(new TextField("docTitle", moreThanOneLine, Field.Store.YES));

			// Read authors
			moreThanOneLine = "";
			while (!line.startsWith(".W")) {
				line = brIndex.readLine();
				if (!line.startsWith(".W")) {
					moreThanOneLine += line + "\n";
				}
			}
			doc.add(new TextField("docAuthors", moreThanOneLine, Field.Store.YES));

			// Read content
			moreThanOneLine = "";
			if (line != null) {
				while (!line.startsWith(".X") && line != null) {
					line = brIndex.readLine();
					if (!line.startsWith(".X")) {
						moreThanOneLine += line + "\n";
					}
				}
			}
			doc.add(new TextField("docContent", moreThanOneLine, Field.Store.YES));

			// Skip .X
			if (line != null) {
				while (!line.startsWith(".I")) {
					line = brIndex.readLine();
					if (line == null) {
						break;
					}
				}
			}
			indexWriter.addDocument(doc);
			if (line == null) {
				break;
			}
		}
		indexWriter.close();
	}
	
	public static IndexWriter getIndexWriter() {
		return indexWriter;
	}
	
	public static Directory getDirectory() {
		return getIndexWriter().getDirectory();
	}
	
	public static FileInputStream fis;
	public static BufferedReader br;
	public static TreeMap<Integer, String> queries;
	public static TreeMap<Integer, ArrayList<Integer>> relevances;
	
	public static void indexEvaluation() throws IOException {
		queries = new TreeMap<Integer, String>();
		relevances = new TreeMap<Integer, ArrayList<Integer>>();
		indexEvaluationQueries();
		indexEvaluationRelevances();
	}
	
	public static void indexEvaluationQueries() throws IOException {
		fis = new FileInputStream(new File(path_queries));
		br = new BufferedReader(new InputStreamReader(fis));
		String line = br.readLine();
		String moreThanOneLine = "";
		String consQuery = "";
		int docID = 0;
		while (line != null) {
			if (line.startsWith(".I")) {
				consQuery = "";
				docID = Integer.parseInt(line.split(" ")[1]);
				line = br.readLine();
				if (line.startsWith(".T")) {
					moreThanOneLine = "";
					while (!line.startsWith(".A") && !line.startsWith(".W")) {
						line = br.readLine();
						if (!line.startsWith(".A") && !line.startsWith(".W"))
							moreThanOneLine += line + " ";
					}
					consQuery += "docTitle:(" + moreThanOneLine + ") OR ";
					// consQuery += moreThanOneLine + " ";
				}
				if (line.startsWith(".A")) {
					moreThanOneLine = "";
					while (!line.startsWith(".W")) {
						line = br.readLine();
						if (!line.startsWith(".W"))
							moreThanOneLine += line + " ";
					}
					consQuery += "docAuthors:(" + moreThanOneLine + ") OR ";
					// consQuery += moreThanOneLine + " ";
				}
				if (line.startsWith(".W")) {
					moreThanOneLine = "";
					while (line != null && !line.startsWith(".I")) {
						line = br.readLine();
						if (line == null)
							break;
						if (!line.startsWith(".I"))
							moreThanOneLine += line + " ";
					}
					consQuery += "docContent:(" + moreThanOneLine + ") OR ";
					// consQuery += moreThanOneLine + " ";
				}
			} else {
				line = br.readLine();
			}
			if (consQuery.endsWith("OR "))
				consQuery = consQuery.substring(0, consQuery.length() - 4);

			queries.put(docID, consQuery);
		}
		fis.close();
		br.close();
	}
	
	public static void indexEvaluationRelevances() throws IOException {
		fis = new FileInputStream(new File(path_relevance));
		br = new BufferedReader(new InputStreamReader(fis));
		String line = br.readLine();
		while (line != null) {
			line = line.trim().replaceAll(" +", " ").replace("\t", " ");
			if (relevances.containsKey(Integer.parseInt(line.split(" ")[0]))) {
				relevances.get(Integer.parseInt(line.split(" ")[0])).add(Integer.parseInt(line.split(" ")[1]));
			} else {
				ArrayList<Integer> temp = new ArrayList<Integer>();
				temp.add(Integer.parseInt(line.split(" ")[1]));
				relevances.put(Integer.parseInt(line.split(" ")[0]), temp);
			}
			line = br.readLine();
		}
		fis.close();
		br.close();
	}
	
	static class Evaluation {
		public TreeMap<Integer, Double> recalls = new TreeMap<Integer, Double>();
		public TreeMap<Integer, Double> precisions = new TreeMap<Integer, Double>();
		public TreeMap<Integer, Double> fmeasures = new TreeMap<Integer, Double>();
		public TreeMap<Integer, Double> maps = new TreeMap<Integer, Double>();
		public TreeMap<Integer, Double> rels = new TreeMap<Integer, Double>();
		public TreeMap<Integer, Double> RELs = new TreeMap<Integer, Double>();
	}
	
	public static Evaluation computeEvaluation() throws IOException, ParseException {
		Evaluation evaluation = new Evaluation();
		for (Integer ID : relevances.keySet()) {
			double REL = relevances.get(ID).size(); // True Positive + False Negative
			double rel = 0; // True Positive
			double recall = 0;
			double precision = 0;
			double sumPosPrecision = 0;
			double fmeasure = 0;
			double map = 0;
			ArrayList<Document> docs = searchQuery(queries.get(ID));
			for (int i = 0; i < docs.size(); i++) {
				if (relevances.get(ID).contains(Integer.parseInt(docs.get(i).get("docID")))) {
					rel++;
					double posPrecision = (double) rel / (i + 1);
					sumPosPrecision += posPrecision;
				}
			}
			recall = rel / REL; // TP/(TP+FN)
			precision = rel / docs.size(); // TP/(TP+FP)
			fmeasure = computeF(precision, recall);
			map = sumPosPrecision / rel;
			if (rel == 0) {
				map = 0;
			}
			evaluation.recalls.put(ID, recall);
			evaluation.precisions.put(ID, precision);
			evaluation.fmeasures.put(ID, fmeasure);
			evaluation.maps.put(ID, map);
			evaluation.rels.put(ID, rel);
			evaluation.RELs.put(ID, REL);
		}
		double finres = 0;
		for (int i : evaluation.recalls.keySet()) {
			finres += evaluation.recalls.get(i);
		}
		finres = finres / (double) evaluation.recalls.size();
		evaluation.recalls.put(-1, finres);
		evaluation.recalls.put(-2, finres * 100);
		
		finres = 0;
		for (int i : evaluation.precisions.keySet()) {
			finres += evaluation.precisions.get(i);
		}
		finres = finres / (double) evaluation.precisions.size();
		evaluation.precisions.put(-1, finres);
		evaluation.precisions.put(-2, finres * 100);
		
		finres = 0;
		for (int i : evaluation.fmeasures.keySet()) {
			finres += evaluation.fmeasures.get(i);
		}
		finres = finres / (double) evaluation.fmeasures.size();
		evaluation.fmeasures.put(-1, finres);
		evaluation.fmeasures.put(-2, finres * 100);
		
		finres = 0;
		for (int i : evaluation.maps.keySet()) {
			finres += evaluation.maps.get(i);
		}
		finres = finres / (double) evaluation.maps.size();
		evaluation.maps.put(-1, finres);
		evaluation.maps.put(-2, finres * 100);
		
		finres = 0;
		for (int i : evaluation.rels.keySet()) {
			finres += evaluation.rels.get(i);
		}
		finres = finres / (double) evaluation.rels.size();
		evaluation.rels.put(-1, finres);
		evaluation.rels.put(-2, finres * 100);
		
		finres = 0;
		for (int i : evaluation.RELs.keySet()) {
			finres += evaluation.RELs.get(i);
		}
		finres = finres / (double) evaluation.RELs.size();
		evaluation.RELs.put(-1, finres);
		evaluation.RELs.put(-2, finres * 100);
		
		return evaluation;
	}

	public static double computeF(double precision, double recall) {
		double beta = 1.0F;
		double res = ((beta * beta + 1.0) * precision * recall) / ((beta * beta * precision) + recall);
		if (new Double(res).isNaN()) {
			return 0;
		}
		return res;
	}
	
	public static ArrayList<Document> searchQuery(String userQuery) throws ParseException, IOException {
		Analyzer analyzer = createAnalyzer();
		HashMap<String, Float> boosts = new HashMap<String, Float>();
		boosts.put("docTitle", 0.3f);
		boosts.put("docAuthors", 0.3f);
		boosts.put("docContent", 0.4f);
		QueryParser parser = new MultiFieldQueryParser(new String[] {"docTitle", "docAuthors", "docContent" }, analyzer, boosts);
		Query query = parser.parse(QueryParser.escape(userQuery));
		DirectoryReader reader = DirectoryReader.open(getDirectory());
		IndexSearcher searcher = new IndexSearcher(reader);
		searcher.setSimilarity(createSimilarity());
		ArrayList<Document> result = new ArrayList<Document>();
		TopDocs hits = searcher.search(query, 46);
		for (ScoreDoc scoreDoc : hits.scoreDocs) {
			Document doc = searcher.doc(scoreDoc.doc);
			doc.add(new TextField("docScore", scoreDoc.score + "", Field.Store.YES));
			result.add(doc);
		}
		return result;
	}
	
	public static Analyzer createAnalyzer() {
		Analyzer analyzer = new Analyzer() {
		    @Override
		    protected TokenStreamComponents createComponents(String fieldName) {		    	
		    	List<String> stopWords = new ArrayList<>();
		    	if (enableStopWords) {
		    		stopWords.addAll(stop_words);
		    	}
		    	StandardTokenizer source = new StandardTokenizer();
		    	source.setMaxTokenLength(StandardAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
		    	TokenStream filter = new LowerCaseFilter(source);
		    	CharArraySet stopSet = new CharArraySet(stopWords, true);
		        filter = new StopFilter(filter, stopSet);
		        if (enableStammer) {
		        	filter = new EnglishMinimalStemFilter(filter);
		        }
		        return new TokenStreamComponents(reader -> {
                	source.setMaxTokenLength(StandardAnalyzer.DEFAULT_MAX_TOKEN_LENGTH);
                	source.setReader(reader);
                }, filter);
		    }
		};
		return analyzer;
	}
	
	public static Similarity createSimilarity() {
		if (algorithm == Algorithm.TF_IDF) {
			TFIDFSimilarity similarity = new ClassicSimilarity();
			return similarity;
		} else if (algorithm == Algorithm.BM_25) {
			return new BM25Similarity();
		} else if (algorithm == Algorithm.LM_Dirichlet) {
			return new LMDirichletSimilarity();
		} else if (algorithm == Algorithm.Boolean) {
			return new BooleanSimilarity(); 
		} else if (algorithm == Algorithm.Axiomatic) {
			return new AxiomaticF1LOG(); 
		} else if (algorithm == Algorithm.IB) {
			return new IBSimilarity(new DistributionLL(), new LambdaDF(), new NormalizationH2());
		} else if (algorithm == Algorithm.DFR) {
			return new DFRSimilarity(new BasicModelG(), new AfterEffectL(), new NormalizationH2());
		}
		return new ClassicSimilarity();
	}
	
	public static int isInteger(String input) {
        try {
            int number = Integer.parseInt(input);
            return number;
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
