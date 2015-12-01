package utils;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;


import org.junit.Test;

import controllers.MovieRecommenderAPI;
import edu.princeton.cs.introcs.In;
import models.User;

public class PersistenceTest {
	private MovieRecommenderAPI recommender;
	private int usersCount = 0;
	private int moviesCount = 0;
	
	public void populate(MovieRecommenderAPI recommender) {
		// users:
		File usersFile = new File("testData/users.dat");
		In inUsers = new In(usersFile);
		// each field is separated(delimited) by a '|'
		String delims = "[|]";
		int counter = 1;
		while (!inUsers.isEmpty()) {
			String password = "secret";
			// get user details
			String userDetails = inUsers.readLine();

			// parse user details string
			String[] userTokens = userDetails.split(delims);

			// output user data to console.
			if (userTokens.length == 7) {
				recommender.addUser(Long.valueOf(userTokens[0]), userTokens[1], userTokens[2],
						Integer.parseInt(userTokens[3]), userTokens[4], userTokens[5], "placeholder@email." + counter,
						password);
			}
			counter++; // used to be concatenated with placeholder email
						// such that each email is unique
			usersCount++;
		}

		// genres:
		String[] genres = new String[19];
		counter = 0;
		File genresFile = new File("testData/genre.dat");
		In inGenres = new In(genresFile);
		while (!inGenres.isEmpty()) {
			String genreDetails = inGenres.readLine();

			// parse user details string
			String[] genreTokens = genreDetails.split(delims);

			// output user data to console.
			if (genreTokens.length == 2) {
				genres[counter] = genreTokens[0];
			}
		}

		// movies:

		File moviesFile = new File("testData/items.dat");
		In inMovies = new In(moviesFile);
		counter = 1;
		while (!inMovies.isEmpty()) {
			// get movie details
			String movieDetails = inMovies.readLine();

			// parse user details string
			String[] movieTokens = movieDetails.split(delims);

			// output user data to console.
			if (movieTokens.length == 23) {
				ArrayList<String> categories = new ArrayList<String>();
				recommender.addMovie(movieTokens[1], movieTokens[2], movieTokens[3]);
				for (int i = 4; i < movieTokens.length; i++) {
					if (Integer.parseInt(movieTokens[i]) == 1) {
						categories.add(genres[i - 4]);
					}
				}
				recommender.getMovie(counter).setCategories(categories);
			}
			counter++;
			moviesCount++;
		}

		File ratingsFile = new File("testData/ratings.dat");
		In inRatings = new In(ratingsFile);
		while (!inRatings.isEmpty()) {
			// get movie details
			String ratingDetails = inRatings.readLine();

			// parse user details string
			String[] ratingTokens = ratingDetails.split(delims);

			// output user data to console.
			if (ratingTokens.length == 4) {
				recommender.addRating(Integer.parseInt(ratingTokens[0]), Integer.parseInt(ratingTokens[1]),
						Integer.parseInt(ratingTokens[2]));
			}
		}
	}

	@Test // EXISTANCE RIGHT CROSS-CHECK
	public void testPopulate() {
		recommender = new MovieRecommenderAPI(null);
		assertEquals(0, recommender.getUsersIndices().size());
		populate(recommender);

		assertNotEquals(0, recommender.getUsersIndices().size());
		assertNotEquals(0, recommender.getUsersEmails().size());
		assertNotEquals(0, recommender.getMovies().size());
		
		assertEquals(usersCount, recommender.getUsersIndices().size());
		assertEquals(usersCount, recommender.getUsersEmails().size());
		assertEquals(moviesCount, recommender.getMovies().size());
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
		populate(recommender);
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
		populate(recommender);
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
