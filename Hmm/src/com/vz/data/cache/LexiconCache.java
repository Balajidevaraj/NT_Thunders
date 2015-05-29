package com.vz.data.cache;

import java.util.ArrayList;
import java.util.List;

public class LexiconCache {

	public List<String> getPositiveWordList() {
		return positiveWordList;
	}

	public List<String> getNegativeWordList() {
		return negativeWordList;
	}

	private String alphabetRange;

	private List<String> positiveWordList = new ArrayList<String>();

	private List<String> negativeWordList = new ArrayList<String>();

	public void setPositiveWordList(List<String> positiveWordList) {
		this.positiveWordList = positiveWordList;
	}

	public void setNegativeWordList(List<String> negativeWordList) {
		this.negativeWordList = negativeWordList;
	}

	public String getAlphabetRange() {

		return alphabetRange;
	}

	public void setAlphabetRange(String range) {

		this.alphabetRange = range;

	}

	public void loadCache() {

	}

	public String getLexiconType(String word) {

		String type = "Neutral";

		if (isNegativeWord(word)) {

			type = "Negative";
		} else if (isPositiveWord(word))

		{
			type = "Positive";
		}
		return type;
	}

	public boolean isNegativeWord(final String word) {

		boolean isNegative = false;

		if (negativeWordList.contains(word.toLowerCase())) {

			isNegative = true;
		}

		return isNegative;
	}

	public boolean isPositiveWord(final String word) {

		boolean isPositive = false;

		if (positiveWordList.contains(word.toLowerCase())) {
			isPositive = true;
		}

		return isPositive;
	}

	public void refresh() {
		loadCache();
	}

}
