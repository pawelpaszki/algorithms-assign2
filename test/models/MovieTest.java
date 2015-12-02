package models;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import utils.Validator;

public class MovieTest {

	private Movie movie1;
	private Movie movie2;
	private Movie movie3;
	private Movie movie4;
	private ArrayList<String> categories;
	private Rating rating1;
	private Rating rating2;
	private Rating rating3;
	private Rating rating4;
	private Rating rating5;

	@Before
	public void setUp() throws Exception {
		movie1 = new Movie(1l, "Test Movie One", "2015", "test-movie-one.com");
		movie2 = new Movie(2l, "Test Movie Two", "2015", "test-movie-two.com");
		movie3 = new Movie(3l, "Test Movie Three", "2015", "test-movie-three.com");
		movie4 = new Movie(4l, "Test Movie Four", "2015", "test-movie-four.com");
		categories = new ArrayList<String>();
		categories.add("drama");
		categories.add("comedy");
		categories.add("thriller");
		rating1 = new Rating(1l, 1l, 5); // userID movieID note
		rating2 = new Rating(2l, 1l, 5);
		rating3 = new Rating(3l, 1l, 5);
		rating4 = new Rating(1l, 2l, -3);
		rating5 = new Rating(2l, 2l, -3);
	}

	@After
	public void tearDown() throws Exception {
		movie1 = null;
		movie2 = null;
		movie3 = null;
		movie4 = null;
		categories = null;
		rating1 = null;
		rating2 = null;
		rating3 = null;
		rating4 = null;
		rating5 = null;
	}

	@Test // EXISTENCE
	public void testExistence() {
		assertNotNull(movie1);
		assertNotNull(movie2);
		assertNotNull(movie3);
		assertNotNull(movie4);
		assertNotNull(movie1.getCategories());
		assertNotNull(movie2.getCategories());
		assertNotNull(movie3.getCategories());
		assertNotNull(movie4.getCategories());
		assertNotNull(movie1.getRatings());
		assertNotNull(movie2.getRatings());
		assertNotNull(movie3.getRatings());
		assertNotNull(movie4.getRatings());
	}

	@Test // RIGHT
	public void testGetters() {
		assertSame(1l, movie1.getId());
		assertEquals("Test Movie One", movie1.getTitle());
		assertEquals("test-movie-one.com", movie1.getUrl());
		assertEquals("2015", movie1.getYear());
	}

	@Test // RIGHT
	public void testEquals() {
		assertTrue(movie1.equals(new Movie(1l, "Test Movie One", "2015", "test-movie-one.com")));
		assertFalse(movie1.equals(movie2));
		assertFalse(movie1.equals(movie3));
		assertFalse(movie1.equals(movie4));
	}

	@Test // RIGHT
	public void testCategoriesAndRatings() {
		assertTrue(movie1.getCategories().size() == 0);
		movie1.setCategories(categories);
		assertFalse(movie1.getCategories().size() == 0);
		assertTrue(movie1.getCategories().size() == 3);
		assertTrue(movie1.getCategories().contains("drama"));
		assertTrue(movie1.getCategories().contains("comedy"));
		assertTrue(movie1.getCategories().contains("thriller"));

		assertTrue(movie1.getRatings().size() == 0);
		movie1.getRatings().put(rating1.getUserID(), rating1);
		movie1.getRatings().put(rating2.getUserID(), rating2);
		movie1.getRatings().put(rating3.getUserID(), rating3);
		assertTrue(movie1.getRatings().size() == 3);

		movie2.getRatings().put(rating4.getUserID(), rating4);
		movie2.getRatings().put(rating5.getUserID(), rating5);
		assertTrue(movie2.getRatings().size() == 2);
	}

	@Test // RIGHT // RANGE
	public void testNotes() {
		movie1.getRatings().put(rating1.getUserID(), rating1);
		movie1.getRatings().put(rating2.getUserID(), rating2);
		movie1.getRatings().put(rating3.getUserID(), rating3);
		assertEquals(5, movie1.getNote(), 0);
	}

	@Test // RIGHT
	public void testCompareTo() {
		assertEquals(0, movie1.compareTo(movie2));
		movie1.getRatings().put(rating1.getUserID(), rating1);
		movie1.getRatings().put(rating2.getUserID(), rating2);
		movie1.getRatings().put(rating3.getUserID(), rating3);

		movie2.getRatings().put(rating4.getUserID(), rating4);
		movie2.getRatings().put(rating5.getUserID(), rating5);
		assertEquals(1, movie1.compareTo(movie2));
	}

	@Test // RIGHT
	public void testToString() {
		assertTrue(movie1.toString().equals(movie1.getTitle() + " (not rated yet)"));
		movie1.getRatings().put(rating1.getUserID(), rating1);
		assertTrue(movie1.toString()
				.equals(movie1.getTitle() + "(" + Validator.toTwoDecimalPlaces(movie1.getNote()) + ")"));
	}

}