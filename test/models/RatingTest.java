package models;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class RatingTest {

	private Rating rating1;
	private Rating rating2;
	private Rating rating3;
	private Rating rating4;

	@Before
	public void setUp() throws Exception {
		rating1 = new Rating(1l, 1l, 5);
		rating2 = new Rating(1l, 2l, 5);
		rating3 = new Rating(1l, 3l, 5);
		rating4 = new Rating(1l, 1l, 5);
	}

	@After
	public void tearDown() throws Exception {
		rating1 = null;
		rating2 = null;
		rating3 = null;
	}

	@Test // EXISTENCE
	public void testExistence() {
		assertNotNull(rating1);
		assertNotNull(rating2);
		assertNotNull(rating3);
		assertNotNull(rating4);
	}

	@Test // RIGHT
	public void testGetters() {
		assertSame(rating1.getMovieID(), 1l);
		assertSame(rating1.getUserID(), 1l);
		assertSame(rating1.getNote(), 5);
		assertNotSame(rating2.getMovieID(), 1l);
		assertNotSame(rating3.getMovieID(), 1l);
	}

	@Test // RIGHT
	public void testEquals() {
		assertTrue(rating1.equals(rating1));
		assertTrue(rating1.equals(rating4));
		assertFalse(rating1.equals(rating2));
		assertFalse(rating1.equals(rating3));
		assertFalse(rating2.equals(rating3));
		assertFalse(rating1.hashCode() == rating2.hashCode());
	}

	@Test // RIGHT
	public void testToString() {
		assertTrue(rating1.toString().equals("\tuser id: " + rating1.getUserID() + "\tmovie id: " + rating1.getMovieID()
				+ "\tnote: " + rating1.getNote()));
	}

}
