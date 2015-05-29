package com.vz.data.cache;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;

public class CacheLoaderFileSysImpl implements CacheLoader {

	private static final String CLASS_NAME = "com.vz.data.cache.CacheLoaderFileSysImpl";

	private Logger logger = Logger.getLogger(CLASS_NAME);

	private String dataStorePath = "D:\\sources";

	public void setdataStorePath(String dataStorePath) {
		this.dataStorePath = dataStorePath;
	}

	private Map<String, List<String>> loadDataSet() {

		File dataSourceFolder = new File(this.dataStorePath);

		File[] files = dataSourceFolder.listFiles();

		Map<String, List<String>> map = new HashMap<String, List<String>>();

		for (File file : files) {

			// This will reference one line at a time
			String line = null;

			BufferedReader bufferedReader = null;
			try {

				List<String> inputList = new ArrayList<String>();

				// FileReader reads text files in the default encoding.
				FileReader fileReader = new FileReader(file);

				// Always wrap FileReader in BufferedReader.
				bufferedReader = new BufferedReader(fileReader);

				while ((line = bufferedReader.readLine()) != null) {
					inputList.add(line.trim());
				}

				Collections.sort(inputList);

				if (file.getName().contains("pos")) {

					map.put("Positive", inputList);
				} else if (file.getName().contains("neg")) {
					map.put("Negative", inputList);

				}

			} catch (Throwable ex) {
				logger.error(ex.getMessage(), ex);
			} finally {

				if (bufferedReader != null) {

					try {
						bufferedReader.close();
					} catch (Exception exception) {
						logger.error(exception.getMessage(), exception);
					}

				}
			}

		}

		return map;

	}

	private void loadCache(String setType, List<String> dataSet,
			List<LexiconCache> caches) {

		int size = dataSet.size();

		int cacheVolume = size / 5;

		int start = 0;
		for (LexiconCache lexiconCache : caches) {

			if (setType.equalsIgnoreCase("Positive")) {

				lexiconCache.setPositiveWordList(dataSet.subList(start,
						cacheVolume));
				start = cacheVolume;
				cacheVolume += cacheVolume;

			} else if (setType.equalsIgnoreCase("Negative"))

			{

				lexiconCache.setNegativeWordList(dataSet.subList(start,
						cacheVolume));

				start = cacheVolume;
				cacheVolume += cacheVolume;

			}

			// avoid NPE
			if (cacheVolume > size) {
				break;
			}

		}

	}

	public List<LexiconCache> load() {

		Map<String, List<String>> dataSet = loadDataSet();

		Set<String> keySet = dataSet.keySet();

		List<LexiconCache> caches = new ArrayList<LexiconCache>();
		caches.add(new LexiconCache());
		caches.add(new LexiconCache());
		caches.add(new LexiconCache());
		caches.add(new LexiconCache());
		caches.add(new LexiconCache());

		for (String key : keySet) {

			List<String> list = dataSet.get(key);

			loadCache(key, list, caches);

		}
		return caches;
	}
}
