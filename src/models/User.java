package models;

import java.util.Map;
import java.util.TreeMap;

import com.google.common.base.Objects;

/**
 * 
 * @author Pawel Paszki
 * This class contains info about the User
 * To increase performance of looking up the ratings, map is used to store them
 *
 */
public class User implements Comparable<User> {
	private Long id;
	private String firstName;
	private String lastName;
	private int age;
	private String gender;
	private String occupation;
	private String email;
	private String password;
	
	// movieID, Rating
	private Map<Long, Rating> ratings = new TreeMap<Long, Rating>();

	/**
	 * 
	 * @param 
	 * @param firstName 
	 * @param lastName
	 * @param age (validated - allowed in interval between 4 and 119 both inclusive)
	 * @param gender (if passed invalid value, then default male assigned)
	 * @param occupation
	 * @param email (validated in the Client class)
	 * @param password
	 */
	public User(Long id, String firstName, String lastName, int age, String gender, String occupation, String email,
			String password) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		if (gender.equalsIgnoreCase("f") || gender.equalsIgnoreCase("female")) {
			this.gender = "female";
		} else {
			this.gender = "male";
		}
		if (age > 3 && age < 120) {
			this.age = age;
		} else {
			this.age = 30; // default if outside boundary conditions
		}
		this.occupation = occupation;
		this.email = email;
		this.password = password;
	}

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @return the firstName
	 */
	public String getFirstName() {
		return firstName;
	}

	/**
	 * @return the lastName
	 */
	public String getLastName() {
		return lastName;
	}

	/**
	 * @return the age
	 */
	public int getAge() {
		return age;
	}

	/**
	 * @return the gender
	 */
	public String getGender() {
		return gender;
	}

	/**
	 * @return the occupation
	 */
	public String getOccupation() {
		return occupation;
	}

	/**
	 * @return the email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * @return the password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * returns User's object hashcode
	 */
	@Override
	public int hashCode() {
		return Objects.hashCode(this.id, this.firstName, this.lastName, this.age, this.gender, this.occupation,
				this.email, this.password);
	}

	/**
	 * overridden equals method. checks
	 * @param Object obj is passed
	 * returns true, if all specified below fields of two compared Users match, false otherwise
	 */
	@Override
	public boolean equals(final Object obj) {
		if (obj instanceof User) {
			final User other = (User) obj;
			return Objects.equal(id, other.id) && Objects.equal(firstName, other.firstName)
					&& Objects.equal(lastName, other.lastName) && Objects.equal(age, other.age)
					&& Objects.equal(gender, other.gender) && Objects.equal(occupation, other.occupation)
					&& Objects.equal(email, other.email) && Objects.equal(password, other.password)
					&& Objects.equal(ratings, other.ratings);
		} else {
			return false;
		}
	}

	/**
	 * @return the ratings
	 */
	public Map<Long, Rating> getRatings() {
		return ratings;
	}

	/**
	 * removes all user's ratings
	 */
	public void removeRatings() {
		ratings.clear();
	}

	/**
	 * @return String representation of the class
	 */
	public String toString() {
		String userRatings = "";
		if (ratings.size() > 0) {
			userRatings += "\nratings:\n";
			for (Rating rating : ratings.values()) {
				userRatings += "\tmovie id:" + rating.getMovieID() + "\tnote:" + rating.getNote() + "\n";
			}
		} else {
			userRatings += " (no movies rated yet)";
		}
		return firstName + " " + lastName + userRatings;

	}

	/**
	 * compares user's ratings
	 * @param is passed and other user's ratings are compared with 
	 * "this" user's ratings. if they both rated the same movie, then
	 * dotProduct will be increased by the multiplication of those ratings
	 * @return the dotProduct
	 */
	@Override
	public int compareTo(User other) {
		int dotProduct = 0;
		if (ratings.size() == 0) {
			return dotProduct;
		} else {
			for (Rating rating : getRatings().values()) {
				if (other.getRatings().containsKey(rating.getMovieID())) {
					dotProduct += other.getRatings().get(rating.getMovieID()).getNote() * rating.getNote();
				}
			}
			return dotProduct;
		}
	}
}
