package controllers;

import static org.junit.Assert.*;

import java.io.File;
import java.util.List;
import java.util.Random;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import edu.princeton.cs.introcs.Stopwatch;
import models.Movie;
import models.Rating;
import models.User;
import utils.JSONSerializer;
import utils.Serializer;
import utils.XMLSerializer;

public class MovieRecommenderAPITest {

	private Serializer serializer = new JSONSerializer(null);
	private MovieRecommenderAPI recommender;
	private User user1;
	private User user2;
	private Movie movie1;
	private Movie movie2;
	private Movie movie3;
	private Rating rating1;
	private Rating rating2;
	private Rating rating3;
	private Random random;

	@Before
	public void setUp() throws Exception {
		recommender = new MovieRecommenderAPI(serializer);
		user1 = new User(1l, "Adam", "Adams", 30, "male", "student", "example@gmail.com", "secret");
		user2 = new User(2l, "Bart", "Barts", 40, "male", "student", "anotherExample@gmail.com", "secret");
		movie1 = new Movie(1l, "Test Movie One", "2015", "test-movie-one.com");
		movie2 = new Movie(2l, "Test Movie Two", "2015", "test-movie-two.com");
		movie3 = new Movie(3l, "Test Movie Three", "2015", "test-movie-three.com");
		// user1 ratings:
		rating1 = new Rating(1l, 1l, 5);
		rating2 = new Rating(1l, 2l, 5);
		rating3 = new Rating(1l, 3l, 5);
		random = new Random();
	}

	@After
	public void tearDown() throws Exception {
		recommender = null;
		user1 = null;
		user2 = null;
		movie1 = null;
		movie2 = null;
		movie3 = null;
		rating1 = null;
		rating2 = null;
		rating3 = null;
		random = null;
	}
	
	@Test // EXISTENCE
	public void testExistence() {
		assertTrue(recommender != null);
		assertTrue(recommender.getMovies() != null);
		assertTrue(recommender.getUsersEmails() != null);
		assertTrue(recommender.getUsersIndices() != null);
		assertTrue(recommender.getUsersEmails().size() == recommender.getUsersIndices().size());
		assertTrue(user1 != null);
		assertTrue(user2 != null);
		assertTrue(movie1 != null);
		assertTrue(movie2 != null);
		assertTrue(movie3 != null);
		assertTrue(rating1 != null);
		assertTrue(rating2 != null);
		assertTrue(rating3 != null);
	}

	@Test // EXISTENCE
	public void testAddUser() {
		assertEquals(1, recommender.getUsersEmails().size()); // size is 1 because of the admin account
		recommender.addUser(user1.getFirstName(), user1.getLastName(), user1.getAge(), user1.getGender(),
				user1.getOccupation(), user1.getEmail(), user1.getPassword());
		recommender.addUser(user2.getFirstName(), user2.getLastName(), user2.getAge(), user2.getGender(),
				user2.getOccupation(), user2.getEmail(), user2.getPassword());
		assertEquals(3, recommender.getUsersEmails().size()); // CARDINALITY - 2 users + admin
		assertTrue(recommender.getUsersEmails().size() == recommender.getUsersIndices().size()); // CROSS-CHECK
	}

	@Test(expected = Exception.class)
	public void addUserWithInvalidAttributes() {
		assertEquals(1, recommender.getUsersEmails().size()); // admin account
		// invalid age, id and gender
		recommender.addUser(-1l, "Bob", "Bobs", 150, "unspecified", "student", "student@gmail.com", "secret");
		assertTrue(recommender.getUsersIndices().get(-1l) == null); // default id assigned, ie 1l

		// ERROR condition - NullPointerException expected
		assertFalse(-1l == recommender.getUsersIndices().get(-1l).getId());

		// default age, and gender set
		assertNotEquals(150, recommender.getUsersIndices().get(1l).getAge());
		assertNotEquals("unspecified", recommender.getUsersIndices().get(1l).getGender());
		assertEquals(30, recommender.getUsersIndices().get(1l).getAge()); // default age
		assertEquals("male", recommender.getUsersIndices().get(1l).getGender()); // default gender
		
		recommender = null;
	}
	
	@Test
	public void testRemoveUser() {
		recommender.addUser(user1.getFirstName(), user1.getLastName(), user1.getAge(), user1.getGender(),
				user1.getOccupation(), user1.getEmail(), user1.getPassword());
		assertEquals(2, recommender.getUsersEmails().size()); // CARDINALITY (one user + admin)
		recommender.login(user1.getEmail(), user1.getPassword());
		assertEquals(recommender.getLoggedInID(), user1.getId());
		recommender.removeUser(user1.getId());
		assertEquals(1, recommender.getUsersEmails().size()); // CARDINALITY
		assertFalse(recommender.isLoggedIn()); 
	}
	
	@Test
	public void testAddAndGetMovieAndAddRating() {
		assertTrue(recommender.getMovies() != null); // EXISTENCE
		assertEquals(0, recommender.getMovies().size()); // CARDINALITY
		recommender.addMovie(movie1.getTitle(), movie1.getYear(), movie1.getUrl());
		assertEquals(movie1, recommender.getMovie(movie1.getId())); 
		assertEquals(1, recommender.getMovies().size()); // CARDINALITY
		assertEquals(0, recommender.getMovies().get(movie1.getId()).getRatings().size()); // CARDINALITY
		recommender.addUser(user1.getFirstName(), user1.getLastName(), user1.getAge(), user1.getGender(),
				user1.getOccupation(), user1.getEmail(), user1.getPassword());
		recommender.addRating(rating1.getUserID(), rating1.getMovieID(), rating1.getNote());
		assertEquals(1, recommender.getMovies().get(1l).getRatings().size()); // CARDINALITY
		assertEquals(1, recommender.getUsersIndices().get(user1.getId()).getRatings().size()); // CARDINALITY
		assertEquals(5, recommender.getUsersIndices().get(user1.getId()).getRatings().get(movie1.getId()).getNote()); // RIGHT
	}
	
	@Test
	public void testGetUserRatings() {
		recommender.addUser(user1.getFirstName(), user1.getLastName(), user1.getAge(), user1.getGender(),
				user1.getOccupation(), user1.getEmail(), user1.getPassword());
		assertEquals(0, recommender.getUserRatings(user1.getId()).size()); // CARDINALITY
		recommender.addMovie(movie1.getTitle(), movie1.getYear(), movie1.getUrl());
		recommender.addMovie(movie2.getTitle(), movie2.getYear(), movie2.getUrl());
		recommender.addMovie(movie3.getTitle(), movie3.getYear(), movie3.getUrl());
		recommender.addRating(rating1.getUserID(), rating1.getMovieID(), rating1.getNote());
		recommender.addRating(rating2.getUserID(), rating2.getMovieID(), rating2.getNote());
		recommender.addRating(rating3.getUserID(), rating3.getMovieID(), rating3.getNote());
		assertEquals(3, recommender.getUserRatings(user1.getId()).size()); // CARDINALITY
		assertEquals(5, recommender.getUserRatings(user1.getId()).get(movie1.getId()).getNote()); // RIGHT
		assertEquals(5, recommender.getUserRatings(user1.getId()).get(movie2.getId()).getNote()); // RIGHT
		assertEquals(5, recommender.getUserRatings(user1.getId()).get(movie3.getId()).getNote()); // RIGHT
	}

	/*
	 * User1: Paddy Paddy, 
	 * recommendations: 
	 * movieID: 1 rating: 5 title: Toy Story (1995) 
	 * movieID: 2 rating: 3 
	 * movieID: 3 rating: -5 
	 * User2: Clodagh Clodagh 
	 * recommendations: 
	 * movieID: 1 rating: 1 title: Toy Story (1995)
	 * movieID: 2 rating: 5 title: GoldenEye (1995) 
	 * movieID: 3 rating: -3 title: Four Rooms (1995) 
	 * movieID: 4 rating: 1 title: Get Shorty (1995) 
	 * movieID: 5 rating: 1 title: Copycat (1995) 
	 * movieID: 6 rating: -5 title: Shanghai Triad (Yao a yao yao dao waipo qiao) (1995) 
	 * movieID: 7 rating: -3 title: Babe (1995) 
	 * movieID: 8 rating: 1 title: Twelve Monkeys (1995) 
	 * User3: Ronan Ronan 
	 * recommendations: 
	 * movieID: 1 rating: 5 
	 * movieID: 2 rating: -3
	 * movieID: 3 rating: 5 
	 * User4: Michael Michael 
	 * recommendations: movieID: 1 rating: 1 
	 * movieID: 2 rating: 3 
	 * movieID: 3 rating: 1 
	 * Similarities using dot product: 
	 * Paddy and Clodagh: 5*1 + 3*5 + -3 * -5 = 35 
	 * Paddy and Ronan: 5*5 + 3*-3 + -5 * 5 = -9 
	 * Paddy and Michael: 5*1 + 3*3 + -5*1 = 9 
	 * Clodagh and Paddy: 35 
	 * Clodagh and Ronan: 1*5 + 5*-3 + -3*5 = -25 
	 * Clodagh and Michael: 1*1 + 5*3 + -3*1 = 13 
	 * Ronan and Paddy: -9 
	 * Ronan and Clodagh: -25
	 * Ronan and Michael: 5*1 + -3*3 + 5*1 = 1 
	 * Michael and Paddy: 9 
	 * Michael and Clodagh: 13 Michael and Ronan: 1
	 */
	@Test
	public void testUserRecommendations() throws Exception {

		File dataStore = new File("testData/datastore.xml");
		serializer = new XMLSerializer(dataStore);
		recommender = new MovieRecommenderAPI(serializer);
		recommender.load();
		assertEquals(4, recommender.getUsersIndices().size()); 
		// recommendations for Paddy contain all movies from Clodagh list
		// which Paddy has not rated, because Clodagh's ratings are the
		// best match for Paddy in terms of similarity
		List<Movie> recommendations = recommender.getUserRecommendations(1l); 
		assertEquals(5, recommendations.size());
		
		// movies in the recommendations list are sorted by note - high to low:
		assertTrue(recommendations.get(0).getTitle().contains("Get Shorty"));
		assertTrue(recommendations.get(1).getTitle().contains("Copycat"));
		assertTrue(recommendations.get(2).getTitle().contains("Shanghai"));
		assertTrue(recommendations.get(3).getTitle().contains("Twelve Monkeys"));
		assertTrue(recommendations.get(4).getTitle().contains("Babe"));
		
		// Clodagh has rated all movies, which Paddy - her closest match has
		// rated
		// as well, therefore there are no ratings for Clodagh
		assertEquals(0, recommender.getUserRecommendations(recommender.getUsersIndices().get(2l).getId()).size());
		// the best match for Ronan is Michael, but they both rated the same
		// movies only, therefore
		// there are no recommendations for Ronan
		assertEquals(0, recommender.getUserRecommendations(recommender.getUsersIndices().get(3l).getId()).size());
		// recommendations for Michael contain all movies from Clodagh list
		// which Paddy has not rated, because Clodagh's ratings are the
		// best match for Paddy in terms of similarity
		assertEquals(5, recommender.getUserRecommendations(recommender.getUsersIndices().get(4l).getId()).size());
	}
	
	@Test
	public void testGetTopTenMoviesAndPerformance() {
		int[] ratings = {-5,-3,-1,1,3,5};
		for (int i = 0; i < 100; i++) {
			recommender.addUser("firstName" + i, "lastName" + i, 30 + i, "male", "student",
					i + "exampleEmail@gmail.com", "secret");
			for (int j = 0; j < 10; j++) {
				recommender.addMovie("randomtitle" + i*j + i, "1995-01-01", "randomMovie" + i*j + i + "com");
			}
		}
		assertTrue(recommender.getMovies().size() == 1000);
		assertTrue(recommender.getUsersIndices().size() == 100 + 1); // + admin account
		
		for(long i = 1; i <= 100; i++) {
			for(long j = 1; j <=1000; j++) {
				recommender.addRating(i,  j, ratings[random.nextInt(6)]);
			}
		}
		
		// 1000 movies with 100 ratings each are sorted and top 10 movies are
		// returned back in less than 0.2s, otherwise the performance test fails
		Stopwatch stopwatch = new Stopwatch();
		List<Movie> topTen = recommender.getTopTenMovies();
		double time = stopwatch.elapsedTime();
		assertTrue(time < 0.2); // PERFORMANCE
		// ORDERING
		for(int i = 0; i < topTen.size()-1;i++) {
			assertTrue(topTen.get(i).getNote() >= topTen.get(i+1).getNote());
		}
	}
	
	@Test
	public void testFindMovie() throws Exception{
		File dataStore = new File("testData/datastore.xml");
		serializer = new XMLSerializer(dataStore);
		recommender = new MovieRecommenderAPI(serializer);
		recommender.load();
		assertTrue(recommender.findMovie("toy").size() > 0); // RANGE
		assertTrue(recommender.findMovie("TOY").size() > 0); // RANGE
		assertTrue(recommender.findMovie("tOY").size() > 0); // RANGE
	}
	
	@Test
	public void testPrime() throws Exception {
		assertTrue(recommender.getMovies().size() == 0); 
		assertTrue(recommender.getUsersIndices().size() == 1); 
		assertTrue(recommender.getUsersEmails().size() == 1); // admin account
		recommender.prime();
		assertFalse(recommender.getMovies().size() == 0);
		assertFalse(recommender.getUsersIndices().size() == 1);
		assertFalse(recommender.getUsersEmails().size() == 1);
		assertEquals(943 + 1, recommender.getUsersIndices().size()); // admin account
		assertTrue(recommender.getUsersEmails().size() == recommender.getUsersIndices().size());
		assertEquals(1682, recommender.getMovies().size());
	}

}
