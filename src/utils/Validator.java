package utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Pawel Paszki This helper class contains methods used to validate
 *         certain parameters and format double values to display at most 2
 *         digits after decimal point
 *
 */
public class Validator {

	// allowed rating values:
	public static final Integer[] SET_VALUES = new Integer[] { -5, -3, -1, 1, 3, 5 };
	public static final Set<Integer> validRatings = new HashSet<Integer>(Arrays.asList(SET_VALUES));

	/**
	 * 
	 * @param rating
	 *            value is passed
	 * @return true, if value is valid, ie if equal to one of validRatings
	 */
	public static boolean isValidRating(int rating) {
		return (validRatings.contains(rating));
	}

	/**
	 * 
	 * @param email
	 *            is passed
	 * @return true, if the parameter is a valid address, false otherwise
	 */
	public static boolean isValidEmailAddress(String email) {
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		java.util.regex.Pattern p = java.util.regex.Pattern.compile(ePattern);
		java.util.regex.Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * 
	 * @param num
	 *            is passed
	 * @return formatted value of the parameter, ie max 2 digits after the
	 *         decimal point
	 */
	public static double toTwoDecimalPlaces(double num) {
		return (int) (num * 100) / 100.0;
	}

	/**
	 * 
	 * @param date is passed in order to determine its format correctness
	 * @return true if correct, false otherwise
	 */
	@SuppressWarnings("unused")
	public static boolean isValidDate(String date) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
		try	{
			Date test = sdf.parse(date);
			return true;
		} catch (ParseException pe) {
			return false;
		}

	}
}
