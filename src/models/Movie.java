package models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Objects;

import utils.Validator;

/**
 * 
 * @author Pawel Paszki
 * This class represents Movie in the RecommmenderAPI application
 *
 */
public class Movie implements Comparable<Movie>, Cloneable {

	private Long id;
	private String title;
	private String year;
	private String url;
	
	// userID, Rating
	private Map<Long, Rating> ratings = new TreeMap<Long, Rating>(); 
	private ArrayList<String> categories = new ArrayList<String>();
	private double note;

	/**
	 * 
	 * @param id
	 * @param title
	 * @param year (validated in Client class)
	 * @param url
	 */
	public Movie(Long id, String title, String year, String url) {
		this.id = id;
		this.title = title;
		this.year = year;
		this.url = url;
		ratings = new HashMap<Long, Rating>();
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @return the year
	 */
	public String getYear() {
		return year;
	}

	/**
	 * @return the url
	 */
	public String getUrl() {
		return url;
	}

	/**
	 * @return the ratings
	 */
	public Map<Long, Rating> getRatings() {
		return ratings;
	}

	/**
	 * @return the categories
	 */
	public ArrayList<String> getCategories() {
		return categories;
	}

	/**
	 * @param categories
	 *            the categories to set
	 */
	public void setCategories(ArrayList<String> categories) {
		this.categories = categories;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the note
	 */
	public double getNote() {
		if (ratings.size() != 0) {
			double note = 0;
			for (Rating rating : ratings.values()) {
				note += rating.getNote();
			}
			return note / ratings.size();
		} else {
			return 0;
		}
	}

	/**
	 * @param note
	 *            the note to set
	 */
	public void setNote(double note) {
		this.note = note;
	}

	/**
	 * overridden equals method. checks
	 * @param Object obj is passed
	 * returns true, if all specified below fields of two compared Movies match, false otherwise
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Movie) {
			final Movie other = (Movie) obj;
			return Objects.equal(id, other.id) && Objects.equal(title, other.title) && Objects.equal(year, other.year)
					&& Objects.equal(url, other.url) && Objects.equal(ratings, other.ratings)
					&& Objects.equal(categories, other.categories) && Objects.equal(note, other.note);
		} else {
			return false;
		}
	}
	
	/**
	 * @return String representation of the class
	 */
	public String toString() {
		String movieRatings = "";
		if (ratings.size() > 0) {
			movieRatings += "(" + Validator.toTwoDecimalPlaces(getNote()) + ")";
		} else {
			movieRatings = " (not rated yet)";
		}
		return title + movieRatings;
	}

	/**
	 * compares movies
	 * @param is passed and other movie's note iscompared with 
	 * "this" movie's ratings. 
	 * @return -1, if other's movie note is higher than this movie. 1 is
	 * returned, if this movie has higher note. 0, if two notes are the same 
	 */
	@Override
	public int compareTo(Movie other) {
		if (this.getNote() < other.getNote())
			return -1;
		if (this.getNote() > other.getNote())
			return 1;
		return 0;
	}
}
