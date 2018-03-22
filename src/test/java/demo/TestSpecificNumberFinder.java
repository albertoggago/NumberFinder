package demo;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import demo.CustomNumberEntity;
import demo.SpecificNumberFinder;

public class TestSpecificNumberFinder {

	SpecificNumberFinder specificNumberFinder = new SpecificNumberFinder();

	
	@Before
	public void init() {
		System.setProperty("java.util.concurrent.ForkJoinPool.common.parallelism","12");
	}
	
	
	@Test
	public void testReadFromFile() {

		List<CustomNumberEntity> listCustumFiles = specificNumberFinder
				.readFromFile(System.getProperty("user.dir") + "/FileTestOk.txt");
		assertEquals("["+"CustomNumberEntity [number=67]"+", "
				        +"CustomNumberEntity [number=45]"+", "
				        +"CustomNumberEntity [number=45]"+", "
				        +"CustomNumberEntity [number=s]"+", "
				        +"CustomNumberEntity [number=-3]"+", "
				        +"CustomNumberEntity [number=12]"+", "
				        +"CustomNumberEntity [number=100]"+", "
				        +"CustomNumberEntity [number=3]"+"]",
				        listCustumFiles.toString());
	}

	@Test
	public void testReadFromFileNoExist() {
		List<CustomNumberEntity> listCustumFiles = specificNumberFinder
				.readFromFile(System.getProperty("user.dir") + "/FileTestNoExist.txt");
		assertEquals(0, listCustumFiles.size());
	}

	@Test
	public void testReadFromFileNoJsonCorrect() {
		List<CustomNumberEntity> listCustumFiles = specificNumberFinder
				.readFromFile(System.getProperty("user.dir") + "/FileTestNoCorrectJson.txt");
		assertEquals(0, listCustumFiles.size());
	}

	@Test
	public void testContainsTrue() {
		List<CustomNumberEntity> listCustumFiles = specificNumberFinder
				.readFromFile(System.getProperty("user.dir") + "/FileTestOk.txt");
		assertEquals(true, specificNumberFinder.contains(100, listCustumFiles));
	}

	@Test
	public void testContainsFalse() {
		List<CustomNumberEntity> listCustumFiles = specificNumberFinder
				.readFromFile(System.getProperty("user.dir") + "/FileTestOk.txt");
		assertEquals(false, specificNumberFinder.contains(200, listCustumFiles));
	}

}
