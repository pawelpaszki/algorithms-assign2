package utils;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import models.Rating;

public class ValidatorTest {
	private String email1;
	private String email2;
	private String email3;
	private String email4;
	private String email5;
	private String email6;
	private String email7;
	private String email8;
	private Rating rating1;
	private Rating rating2;
	private Rating rating3;
	private Rating rating4;
	private Rating rating5;

	@Before
	public void setUp() throws Exception {
		email1 = "incorrectEmail1@.gmail.com";
		email2 = "incorrectEmail2.@.gmail.com";
		email3 = "incorrectEmail3.gmail.com";
		email4 = "incorrectEmail3@..gmail.com";
		email5 = "correctEmail@gmail.com";
		email6 = "veryLongButStillCorrectEmail@gmail.com";
		email7 = "a@b.com";
		email8 = "correct-Email@gmail.com";
		rating1 = new Rating(1l, 1l, 1);
		rating2 = new Rating(1l, 2l, 2);
		rating3 = new Rating(1l, 3l, 3);
		rating4 = new Rating(1l, 4l, 4);
		rating5 = new Rating(1l, 5l, 5);
	}

	@After
	public void tearDown() throws Exception {
		email1 = null;
		email2 = null;
		email3 = null;
		email4 = null;
		email5 = null;
		email6 = null;
		email7 = null;
		email8 = null;
		rating1 = null;
		rating2 = null;
		rating3 = null;
		rating4 = null;
		rating5 = null;
	}
	
	@Test // CONFORMANCE
	public void testEmailValidation() {
		assertFalse(Validator.isValidEmailAddress(email1));
		assertFalse(Validator.isValidEmailAddress(email2));
		assertFalse(Validator.isValidEmailAddress(email3));
		assertFalse(Validator.isValidEmailAddress(email4));
		assertTrue(Validator.isValidEmailAddress(email5));
		assertTrue(Validator.isValidEmailAddress(email6));
		assertTrue(Validator.isValidEmailAddress(email7));
		assertTrue(Validator.isValidEmailAddress(email8));
	}
	
	@Test // CONFORMANCE
	public void testRatingValues() {
		assertTrue(Validator.isValidRating(rating1.getNote()));
		assertFalse(Validator.isValidRating(rating2.getNote()));
		assertTrue(Validator.isValidRating(rating3.getNote()));
		assertFalse(Validator.isValidRating(rating4.getNote()));
		assertTrue(Validator.isValidRating(rating5.getNote()));
	}

}

