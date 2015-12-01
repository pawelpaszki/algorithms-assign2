package models;

import com.google.common.base.Objects;

/**
 * 
 * @author Pawel Paszki
 * This class stores info about a Rating, ie userID, movieID and note
 *
 */
public class Rating {

	private Long userID;
	private Long movieID;
	private int note;

	/**
	 * 
	 * @param userID
	 * @param movieID
	 * @param note are passed (note is validated in Client class)
	 * and assigned to newly created Rating object
	 */
	public Rating(Long userID, Long movieID, int note) {
		this.userID = userID;
		this.movieID = movieID;
		this.note = note;
	}

	/**
	 * @return the userID
	 */
	public Long getUserID() {
		return userID;
	}

	/**
	 * @return the movieID
	 */
	public Long getMovieID() {
		return movieID;
	}

	/**
	 * @return the note
	 */
	public int getNote() {
		return note;
	}

	/**
	 * overridden equals method. checks
	 * @param Object obj is passed
	 * returns true, if all specified below fields of two compared Ratings match, false otherwise
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof Rating) {
			final Rating other = (Rating) obj;
			return Objects.equal(userID, other.userID) && Objects.equal(movieID, other.movieID)
					&& Objects.equal(note, other.note);
		} else {
			return false;
		}
	}
	
	/**
	 * @return String representation of the class
	 */
	public String toString() {
		return "\tuser id: " + userID + "\tmovie id: " + movieID + "\tnote: " + note;
	}
}
