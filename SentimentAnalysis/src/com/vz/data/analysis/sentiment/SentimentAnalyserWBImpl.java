/*
 * Copyright (c) 2015, Verizon and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Verizon or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.vz.data.analysis.sentiment;

import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.log4j.Logger;

import com.vz.data.cache.CacheManager;
import com.vz.data.cache.LexiconCache;

/**
 * SentimentAnalyserWBImpl is a 'Bag of Words' algorithm' implementation of
 * {@link SentimentAanalyser}.
 * 
 * @author Verizon
 * 
 */
public class SentimentAnalyserWBImpl implements SentimentAnalyser {

	private static final String PLUS = " + ";

	private CacheManager cacheManager;

	private static final String CLASS_NAME = "com.vz.data.analysis.sentiment.SentimentAnalyserWBImpl";

	private Logger logger = Logger.getLogger(CLASS_NAME);

	private Map<String, String> formulaMap = new HashMap<String, String>();

	/**
	 * Default Constructor
	 */
	public SentimentAnalyserWBImpl() {

		logger.debug("SentimentAnalyserWBImpl class instantiated and loading formulaMap...");
		loadFormulaMap();
		logger.debug("SentimentAnalyserWBImpl class instantiated and formulaMap loaded...");

	}

	public void setCacheManager(CacheManager cacheManager) {

		this.cacheManager = cacheManager;
	}

	private void loadFormulaMap() {

		// pos + pos = pos
		formulaMap.put(SentimentAnalyser.POSITIVE + PLUS
				+ SentimentAnalyser.POSITIVE, SentimentAnalyser.POSITIVE);

		// Neg + Neg = Pos
		formulaMap.put(SentimentAnalyser.NEGATIVE + PLUS
				+ SentimentAnalyser.NEGATIVE, SentimentAnalyser.POSITIVE);

		// Neg + Pos = Neg
		formulaMap.put(SentimentAnalyser.NEGATIVE + PLUS
				+ SentimentAnalyser.POSITIVE, SentimentAnalyser.NEGATIVE);

		// Pos + Neg = Neg
		formulaMap.put(SentimentAnalyser.POSITIVE + PLUS
				+ SentimentAnalyser.NEGATIVE, SentimentAnalyser.NEGATIVE);

	}

	/**
	 * analysePositiveWords method is to analyze and determine words with
	 * positive meanings.</br>It returns number of positive words occurred in
	 * the given input string.It is thread safe routine.
	 * 
	 * 
	 * @param {@link String} inputText should be alphanumeric.
	 * @return {@link Integer} number of positive words.
	 */
	public int analysePositiveWords(final String inputText) {

		int count = 0;
		String previousResult = "";

		// Split the string by space
		StringTokenizer spaceTokenizer = new StringTokenizer(inputText);

		while (spaceTokenizer.hasMoreTokens()) {

			String token = spaceTokenizer.nextToken();
			// LexiconCache cache = cacheManager.getCache(token.substring(0,
			// 1).toLowerCase());
			LexiconCache cache = cacheManager.getCache(token.toLowerCase());
			if (cache == null) {
				continue;
			}
			String[] split = token.split(",");

			for (String word : split) {

				// remove '.' from the word to avoid match fail
				if (word.contains(".")) {
					word = word.replace(".", "").trim();
				}

				String currentResult = cache.getLexiconType(word);

				/*
				 * equals method used since all type comparisons are against
				 * constants.
				 */
				if ((!previousResult.equals(SentimentAnalyser.NEUTRAL))
						&& (!previousResult.endsWith(""))
						&& (!currentResult.equals(SentimentAnalyser.NEUTRAL))) {

					String result = findType(previousResult, currentResult);

					if (result.equals(SentimentAnalyser.POSITIVE)) {
						count++;
						previousResult = "";
					}

				} else if (currentResult.equals(SentimentAnalyser.POSITIVE)) {

					count++;
					previousResult = SentimentAnalyser.POSITIVE;
				} else if (currentResult.equals(SentimentAnalyser.NEGATIVE)) {

					previousResult = SentimentAnalyser.NEGATIVE;

				} else if (currentResult.equals(SentimentAnalyser.NEUTRAL)) {

					previousResult = SentimentAnalyser.NEUTRAL;

				}
			}

		}

		return count;
	}

	private String findType(final String previousResult,
			final String currentResult) {

		return formulaMap.get(previousResult.trim() + PLUS
				+ currentResult.trim());
	}

	/**
	 * analyseNegativeWordss method is to analyze and determine words with
	 * negative meanings.</br>It returns number of negative words occurred in
	 * the given input string.It is thread safe routine.
	 * 
	 * 
	 * @param {@link String} inputText should be alphanumeric.
	 * @return {@link Integer} number of negative words.
	 */
	public int analyseNegativeWords(final String inputText) {

		int count = 0;
		String previousResult = "";

		// Split the string by space
		StringTokenizer spaceTokenizer = new StringTokenizer(inputText);

		while (spaceTokenizer.hasMoreTokens()) {

			String token = spaceTokenizer.nextToken();
			// LexiconCache cache = cacheManager.getCache(token.substring(0, 1)
			// .toLowerCase());

			LexiconCache cache = cacheManager.getCache(token.toLowerCase());

			if (cache == null) {
				continue;
			}
			// check for comma separated value
			String[] split = token.split(",");

			for (String word : split) {

				// remove '.' from the word to avoid match fail
				if (word.contains(".")) {
					word = word.replace(".", "").trim();
				}

				String currentResult = cache.getLexiconType(word);

				/*
				 * equals method used since all type comparisons are against
				 * constants.
				 */
				if ((!previousResult.equals(SentimentAnalyser.NEUTRAL))
						&& (!previousResult.endsWith(""))
						&& (!currentResult.equals(SentimentAnalyser.NEUTRAL))) {

					String result = findType(previousResult, currentResult);

					if (result.equals(SentimentAnalyser.NEGATIVE)) {
						count++;
						previousResult = "";
					}

				} else if (currentResult.equals(SentimentAnalyser.NEGATIVE)) {

					count++;
					previousResult = SentimentAnalyser.NEGATIVE;
				} else if (currentResult.equals(SentimentAnalyser.POSITIVE)) {

					previousResult = SentimentAnalyser.POSITIVE;

				} else if (currentResult.equals(SentimentAnalyser.NEUTRAL)) {

					previousResult = SentimentAnalyser.NEUTRAL;

				}
			}

		}

		return count;
	}

	/**
	 * getSentiment is responsible to analyze sentiment based on positive and
	 * negative word counts of a text.</br>This method is intended to be called
	 * after <b>analysePositiveWords,analyseNegativeWords</b> methods</br></br>
	 * 
	 * It is a thread safe method.returns either one of the following
	 * result.Positive or negative or neutral.
	 * 
	 * @param {@link Integer} positiveCounts
	 * @param {@link Integer} negativeCounts
	 * @return {@link String} sentiment analysis result
	 */
	public String getSentiment(final int positiveCounts,
			final int negativeCounts) {

		String sentiment;

		double posProbablity = (positiveCounts - negativeCounts)
				/ (positiveCounts + negativeCounts);

		if (posProbablity < 0) {
			sentiment = SentimentAnalyser.NEGATIVE;
		} else if (posProbablity > 0) {
			sentiment = SentimentAnalyser.POSITIVE;
		} else {
			sentiment = SentimentAnalyser.NEUTRAL;
		}

		return sentiment;
	}

	/**
	 * getSentiment is responsible to analyze sentiment of given input text. It
	 * is a thread safe method.returns either one of the following
	 * result.Positive or negative or neutral or error.
	 * 
	 * @param inputText
	 * @return {@link String} sentiment analysis result
	 */
	public String getSentiment(final String inputText) {

		String sentiment;

		logger.debug("getSentiment method invoked with input sring '"
				+ inputText + "'");

		try {
			// get positive word count
			int positiveWordCount = analysePositiveWords(inputText);
			// Get negative word count
			int negativeWordCount = analyseNegativeWords(inputText);

			double numerator = positiveWordCount - negativeWordCount;
			double denominator = positiveWordCount + negativeWordCount;

			double posProbablity = numerator / denominator;

			if (posProbablity < 0) {
				sentiment = SentimentAnalyser.NEGATIVE;
			} else if (posProbablity > 0) {
				sentiment = SentimentAnalyser.POSITIVE;
			} else {
				sentiment = SentimentAnalyser.NEUTRAL;
			}

		} catch (Throwable throwable) {

			sentiment = SentimentAnalyser.ERROR;

			logger.error(throwable.getMessage(), throwable);

		}

		logger.debug("getSentiment method ends with return value '" + sentiment
				+ "'");

		return sentiment;
	}
}
