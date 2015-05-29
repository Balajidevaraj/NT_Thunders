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

import com.vz.data.cache.CacheManager;

/**
 * SentimentAanalyser provides functionalities to analyze sentiment of
 * reviews,comments.</br>It is thread safe.Input text for analysis is should not
 * be more than 2000 characters.</br></br>
 * 
 * @author Verizon
 * 
 */
public interface SentimentAnalyser {

	/*
	 * Developer Note : This class definition is PMD complaint and adhered to
	 * SOLID principle .
	 */

	// By default fields are public , static final
	String POSITIVE = "Positive";
	String NEGATIVE = "Negative";
	String NEUTRAL = "Neutral";
	String ERROR = "Error";

	/**
	 * analysePositiveWords method is to analyze and determine words with
	 * positive meanings.</br>It returns number of positive words occurred in
	 * the given input string.It is thread safe routine.
	 * 
	 * 
	 * @param {@link String} inputText should be alphanumeric.
	 * @return {@link Integer} number of positive words.
	 */
	int analysePositiveWords(final String inputText);

	/**
	 * analyseNegativeWordss method is to analyze and determine words with
	 * negative meanings.</br>It returns number of negative words occurred in
	 * the given input string.It is thread safe routine.
	 * 
	 * 
	 * @param {@link String} inputText should be alphanumeric.
	 * @return {@link Integer} number of negative words.
	 */
	int analyseNegativeWords(final String inputText);

	/**
	 * getSentiment is responsible to analyze sentiment based on positive and
	 * negative word counts of a text.</br>This method is intended to be called
	 * after <b>analysePositiveWords,analyseNegativeWords</b> methods</br></br>
	 * 
	 * It is a thread safe method.returns either one of the following
	 * result.Positive or negative or neutral or error.
	 * 
	 * @param {@link Integer} positiveCounts
	 * @param {@link Integer} negativeCounts
	 * @return {@link String} sentiment analysis result
	 */
	String getSentiment(final int positiveCounts, final int negativeCounts);

	/**
	 * getSentiment is responsible to analyze sentiment of given input text. It
	 * is a thread safe method.returns either one of the following
	 * result.Positive or negative or neutral or error.
	 * 
	 * @param inputText
	 * @return {@link String} sentiment analysis result
	 */
	String getSentiment(final String inputText);
	
	
 void setCacheManager(CacheManager cacheManager);

}
