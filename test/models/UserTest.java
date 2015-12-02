package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserTest {

	private User user1;
	private User user2;
	private User user3;
	private User user4;
	private Rating rating1;
	private Rating rating2;
	private Rating rating3;
	private Rating rating4;
	private Rating rating5;
	private Rating rating6;
	private Rating rating7;
	private Rating rating8;
	private Rating rating9;
	private Rating rating10;
	private Rating rating11;
	private Rating rating12;
	private Rating rating13;
	private Rating rating14;
	private Rating rating15;

	@Before
	public void setUp() throws Exception {
		user1 = new User(1l, "Adam", "Adams", 30, "male", "student", "example@gmail.com", "secret");
		user2 = new User(2l, "Bart", "Barts", 40, "male", "student", "anotherExample@gmail.com", "secret");
		user3 = new User(3l, "Charlie", "Charles", 50, "male", "student", "yetAnotherExample@gmail.com", "secret");
		user4 = new User(4l, "Adam", "Adams", 30, "male", "student", "example@gmail.com", "secret");
		// user1 ratings:
		rating1 = new Rating(1l, 1l, 5);
		rating2 = new Rating(1l, 2l, 5);
		rating3 = new Rating(1l, 3l, 5);
		rating4 = new Rating(1l, 4l, 5);
		rating5 = new Rating(1l, 5l, 5);
		// user2 ratings:
		rating6 = new Rating(2l, 1l, -5);
		rating7 = new Rating(2l, 2l, -5);
		rating8 = new Rating(2l, 3l, -5);
		rating9 = new Rating(2l, 4l, -5);
		rating10 = new Rating(2l, 5l, -5);
		// user3 ratings:
		rating11 = new Rating(3l, 1l, 1);
		rating12 = new Rating(3l, 2l, 1);
		rating13 = new Rating(3l, 3l, 1);
		rating14 = new Rating(3l, 4l, 1);
		rating15 = new Rating(3l, 5l, 1);
	}

	@After
	public void tearDown() {
		user1 = null;
		user2 = null;
		user3 = null;
		user4 = null;
		rating1 = null;
		rating2 = null;
		rating3 = null;
		rating4 = null;
		rating5 = null;
		rating6 = null;
		rating7 = null;
		rating8 = null;
		rating9 = null;
		rating10 = null;
		rating11 = null;
		rating12 = null;
		rating13 = null;
		rating14 = null;
		rating15 = null;
	}

	@Test // EXISTENCE
	public void testExistence() {
		assertNotNull(user1);
		assertNotNull(user2);
		assertNotNull(user3);
		assertNotNull(user4);
		assertNotNull(user1.getRatings());
		assertNotNull(user2.getRatings());
		assertNotNull(user3.getRatings());
		assertNotNull(user4.getRatings());
	}

	@Test // RIGHT
	public void testGetters() {
		assertEquals("Adam", user1.getFirstName());
		assertEquals("Adams", user1.getLastName());
		assertEquals("male", user1.getGender());
		assertEquals("student", user1.getOccupation());
		assertEquals("example@gmail.com", user1.getEmail());
		assertEquals("secret", user1.getPassword());
		assertEquals(30, user1.getAge());
	}

	

	@Test // RIGHT
	public void testEquals() {
		assertFalse(user4.equals(user1));
		assertFalse(user1.equals(user2));
		assertFalse(user1.equals(user3));
		assertFalse(user2.equals(user3));
		assertFalse(user1.hashCode() == (user2.hashCode()));
		assertFalse(user1.hashCode() == (user3.hashCode()));
		assertFalse(user1.hashCode() == (user4.hashCode()));
		assertFalse(user2.hashCode() == (user3.hashCode()));
	}

	/*
	 * if a user has not rated any movies - his/ her compareTo return value will
	 * be zero comparing to any other user, otherwise it will be dot product of
	 * comparison of his/her ratings and other user ratings, if their ids match,
	 * ie if the ratings are for the same movies
	 */
	
	@Test // RIGHT // RANGE
	public void testCompareToAndRatingsSize() {
		assertEquals(0, user1.compareTo(user2));
		assertEquals(0, user1.compareTo(user3));
		assertEquals(0, user2.compareTo(user3));
		assertTrue(user1.getRatings().size() == 0);
		assertTrue(user2.getRatings().size() == 0);
		assertTrue(user3.getRatings().size() == 0);
		assertTrue(user4.getRatings().size() == 0);

		user1.getRatings().put(1l, rating1);
		user1.getRatings().put(2l, rating2);
		user1.getRatings().put(3l, rating3);
		user1.getRatings().put(4l, rating4);
		user1.getRatings().put(5l, rating5);

		user2.getRatings().put(1l, rating6);
		user2.getRatings().put(2l, rating7);
		user2.getRatings().put(3l, rating8);
		user2.getRatings().put(4l, rating9);
		user2.getRatings().put(5l, rating10);

		user3.getRatings().put(1l, rating11);
		user3.getRatings().put(2l, rating12);
		user3.getRatings().put(3l, rating13);
		user3.getRatings().put(4l, rating14);
		user3.getRatings().put(5l, rating15);
		assertNotEquals(0, user1.compareTo(user2));
		assertEquals(-125, user1.compareTo(user2));
		assertEquals(25, user1.compareTo(user3));
		assertEquals(-25, user2.compareTo(user3));

		assertTrue(user1.getRatings().size() == 5);
		assertTrue(user2.getRatings().size() == 5);
		assertTrue(user3.getRatings().size() == 5);
		assertTrue(user4.getRatings().size() == 0);
		user1.removeRatings();
		assertTrue(user1.getRatings().size() == 0);
	}

	@Test // RIGHT
	public void testToString() {
		user1.getRatings().put(1l, rating1);
		user1.getRatings().put(2l, rating2);
		user1.getRatings().put(3l, rating3);
		assertTrue(user1.toString()
				.equals(user1.getFirstName() + " " + user1.getLastName() + "\nratings:\n" + "\tmovie id:" + rating1.getMovieID()
						+ "\tnote:" + rating1.getNote() + "\n" + "\tmovie id:" + rating2.getMovieID() + "\tnote:"
						+ rating2.getNote() + "\n" + "\tmovie id:" + rating3.getMovieID() + "\tnote:"
						+ rating3.getNote() + "\n"));
		assertTrue(
				user2.toString().equals(user2.getFirstName() + " " + user2.getLastName() + " (no movies rated yet)"));
	}
}
