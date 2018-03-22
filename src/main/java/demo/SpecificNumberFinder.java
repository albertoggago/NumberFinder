package demo;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SpecificNumberFinder implements NumberFinder {
	Logger logger;
	FastestComparator fastestComparator = new FastestComparator();

	public SpecificNumberFinder() {
		logger = LoggerFactory.getLogger(SpecificNumberFinder.class);
	}


	@SuppressWarnings("unchecked")
	@Override
	public List<CustomNumberEntity> readFromFile(String filePath) {

		try {
			JSONParser parser = new JSONParser();
			File customNumbersFile = new File(filePath);

			Reader readerFile = new FileReader(customNumbersFile);
			JSONArray jsonArrayCustomNumbers = (JSONArray) parser.parse(readerFile);

		    return (List<CustomNumberEntity>) 
		    		 jsonArrayCustomNumbers.stream()
						.map(uniqueJson -> (String) ((JSONObject) uniqueJson).get("number"))
						.filter(propertieJson -> (propertieJson != null))
						.map(propertieJson -> {
							 return createCustomNumberEntity(propertieJson.toString());})
						.collect(Collectors.toList());
		} catch (IOException | ParseException | NullPointerException e) {
			logger.warn("Error Reading File: " + e.getMessage());
			return new ArrayList<>();
		}
	}

	private static CustomNumberEntity createCustomNumberEntity(String number) {
		try {
			Constructor<CustomNumberEntity> constructor = CustomNumberEntity.class.getDeclaredConstructor(String.class);
			constructor.setAccessible(true);
			CustomNumberEntity CustomNumberEntity = constructor.newInstance(number);
			return CustomNumberEntity;
		} catch (SecurityException | 
				 IllegalArgumentException | 
				 NoSuchMethodException | 
				 InstantiationException | 
				 IllegalAccessException | 
				 InvocationTargetException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		}
 	}



	@Override
	public boolean contains(int valueToFind, List<CustomNumberEntity> list) {
		Optional<Integer> exist = 
				list
				.stream()
				.parallel()
				.map(customNumber -> {
					try {
						return fastestComparator.compare(valueToFind, customNumber);
					} catch (NumberFormatException e) {
						return Integer.MAX_VALUE;
					}
					})
				.filter(a -> a.equals(0))
				.findAny();
			return exist.isPresent();
	}

}
