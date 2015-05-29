import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.vz.data.analysis.sentiment.SentimentAnalyser;
import com.vz.data.analysis.sentiment.SentimentAnalyserWBImpl;
import com.vz.data.cache.CacheManager;
import com.vz.data.cache.DistributedCacheManagerImpl;

public class Main {
	private static FileWriter fileWriter = null;
	private static BufferedWriter bufferedWriter = null;

	private static List<String> loadInputFile() {

		String inFileName = "D:\\input\\HackathonInput.txt";
		List<String> inputList = new ArrayList<String>();

		// This will reference one line at a time
		String line = null;

		try {
			// FileReader reads text files in the default encoding.
			FileReader fileReader = new FileReader(inFileName);

			// Always wrap FileReader in BufferedReader.
			BufferedReader bufferedReader = new BufferedReader(fileReader);

			while ((line = bufferedReader.readLine()) != null) {
				inputList.add(line.trim());
				// System.out.println(line);
			}

			// Always close files.
			bufferedReader.close();
		} catch (FileNotFoundException ex) {
			System.out.println("Unable to open file '" + inFileName + "'");
		} catch (IOException ex) {
			System.out.println("Error reading file '" + inFileName + "'");
		}

		return inputList;
	}

	private static void write(String input, String result) {

		// OUT
		String outFileName = "D:\\output\\Hackathonoutput1.txt";

		try {

			if (fileWriter == null) {
				fileWriter = new FileWriter(outFileName);

				bufferedWriter = new BufferedWriter(fileWriter);

			}
			//bufferedWriter.write(input);
			//bufferedWriter.newLine();
			bufferedWriter.write(result);
			bufferedWriter.newLine();
			bufferedWriter.flush();

			//bufferedWriter.close();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Error writing file'" + outFileName + "'");
		}

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {

		CacheManager cacheManager = new DistributedCacheManagerImpl();
		cacheManager.init();

		SentimentAnalyser sentimentAnalyser = new SentimentAnalyserWBImpl();

		sentimentAnalyser.setCacheManager(cacheManager);

		List<String> loadInputFile = loadInputFile();

		for (String comment : loadInputFile) {

			System.out.println("Input : " + comment);

			String result = sentimentAnalyser.getSentiment(comment);

			write(comment, result);

			System.out.println(result);

			System.out.println("\n");
			System.out.println("\n");
		}

		if (fileWriter != null) {
			try {
				
				fileWriter.close();
				bufferedWriter.close();
			} catch (Throwable throwable) {
				System.out.println(throwable.getLocalizedMessage());
			}
		}

		System.out.println("");

	}

}
