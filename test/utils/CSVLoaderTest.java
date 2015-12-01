package utils;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Movie;
import models.Rating;
import models.User;

public class CSVLoaderTest {

	CSVLoader loader;
	@Before
	public void setUp() throws Exception {
		 loader = new CSVLoader();
	}

	@After
	public void tearDown() throws Exception {
		loader = null;
	}

	@Test // ORDERING CARDINALITY RIGHT
	public void testMovies() throws Exception {
		List<Movie> movies = loader.loadMovies("data_movieLens/items.dat", "data_movieLens/genres.dat");
		assertNotNull(movies);
		assertTrue(1l == movies.get(0).getId());
		assertTrue("Toy Story (1995)".equals(movies.get(0).getTitle()));
		assertEquals(3, movies.get(0).getCategories().size());
		assertEquals(1682, movies.size());
		for(int i = 0; i < movies.size()-1; i++) {
			assertTrue(movies.get(i).getId() < movies.get(i+1).getId());
		}
		// invalid path:
		movies = loader.loadMovies("invalid path", "another invalid path");
		assertNull(movies);
	}
	
	@Test // RIGHT ORDERING CARDINALITY
	public void testUsers() throws Exception {
		List<User> users = loader.loadUsers("data_movieLens/users.dat");
		assertNotNull(users);
		for(int i = 0; i < users.size()-1; i++) {
			assertTrue(users.get(i).getId() < users.get(i+1).getId());
		}
		assertEquals(943, users.size());
		assertEquals("Leonard", users.get(0).getFirstName());
		assertEquals("Hernandez", users.get(0).getLastName());
		assertEquals(24, users.get(0).getAge());
		assertEquals(24, users.get(0).getAge());
		assertEquals("male", users.get(0).getGender());
		assertEquals("technician", users.get(0).getOccupation());
		users = loader.loadUsers("invalid path");
		assertNull(users);
	}
	
	@Test // RIGHT 
	public void testRatings() throws Exception {
		List<Rating> ratings = loader.loadRatings("data_movieLens/ratings.dat");
		assertNotNull(ratings);
		assertTrue(ratings.size() == 100000);
		assertTrue(242l == ratings.get(0).getMovieID());
		assertTrue(196l == ratings.get(0).getUserID());
		assertTrue(1 == ratings.get(0).getNote());
		ratings = loader.loadRatings("invalid path");
		assertNull(ratings);
		
	}

}
