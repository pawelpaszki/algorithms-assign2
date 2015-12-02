package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.junit.Test;

import controllers.MovieRecommenderAPI;
import models.User;

public class PersistenceTest {
	private MovieRecommenderAPI recommender;
	
	public void populate() throws Exception {
		recommender.prime("testData/users.dat", "testData/items.dat", "testData/genres.dat", "testData/ratings.dat");
	}

	@Test // EXISTANCE RIGHT CROSS-CHECK
	public void testPopulate() throws Exception {
		recommender = new MovieRecommenderAPI(null);
		assertEquals(1, recommender.getUsersIndices().size()); // admin account
		populate();

		assertNotEquals(0, recommender.getUsersIndices().size());
		assertNotEquals(0, recommender.getUsersEmails().size());
		assertNotEquals(0, recommender.getMovies().size());
		
		assertEquals(5, recommender.getUsersIndices().size());
		assertEquals(5, recommender.getUsersEmails().size());
		assertEquals(8, recommender.getMovies().size());
	}
	
	private void deleteFile(String fileName) {
		File datastore = new File(fileName);
		if (datastore.exists()) {
			datastore.delete();
		}
	}

	@Test
	public void testJSONSerializer() throws Exception {
		String datastoreFile = "testdatastore.json";
		deleteFile(datastoreFile);

		Serializer serializer = new JSONSerializer(new File(datastoreFile));

		recommender = new MovieRecommenderAPI(serializer);
		populate();
		recommender.write();

		MovieRecommenderAPI recommender2 = new MovieRecommenderAPI(serializer);
		recommender2.load();

		assertEquals(recommender.getUsersIndices().size(), recommender2.getUsersIndices().size());
		for (User user : recommender.getUsersIndices().values()) {
			assertTrue(recommender2.getUsersIndices().values().contains(user));
		}
		deleteFile("testdatastore.json");
	}
	
	@Test
	public void testXMLSerializer() throws Exception {
		String datastoreFile = "testdatastore.xml";
		deleteFile(datastoreFile);

		Serializer serializer = new JSONSerializer(new File(datastoreFile));

		recommender = new MovieRecommenderAPI(serializer);
		populate();
		recommender.write();

		MovieRecommenderAPI recommender2 = new MovieRecommenderAPI(serializer);
		recommender2.load();

		assertEquals(recommender.getUsersIndices().size(), recommender2.getUsersIndices().size());
		for (User user : recommender.getUsersIndices().values()) {
			assertTrue(recommender2.getUsersIndices().values().contains(user));
		}
		deleteFile("testdatastore.xml");
	}

}
