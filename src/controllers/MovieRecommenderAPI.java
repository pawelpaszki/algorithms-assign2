package controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import models.Movie;
import models.Rating;
import models.User;
import utils.CSVLoader;
import utils.MergeX;
import utils.Serializer;

/**
 * 
 * @author Pawel Paszki This class contains all methods used to deal with User,
 *         Movie and Rating classes
 */

public class MovieRecommenderAPI {

	// userID, User
	private Map<Long, User> usersIndices = new TreeMap<Long, User>();
	// userEmail, User
	private Map<String, User> usersEmails = new HashMap<String, User>();
	// movieID, Movie
	private Map<Long, Movie> movies = new TreeMap<Long, Movie>();
	private Serializer serializer;
	private boolean loggedIn;
	private Long loggedInID;
	private User admin;

	/**
	 * constructor of RecommenderAPI class
	 * 
	 * @param serializer
	 *            passed as a parameter
	 */
	public MovieRecommenderAPI(Serializer serializer) {
		this.serializer = serializer;
		instantiateAdmin();
	}

	public MovieRecommenderAPI() {
		instantiateAdmin();
	}

	/**
	 * this method instantiates admin, who is the only user, which can clear the
	 * database
	 */
	private void instantiateAdmin() {
		admin = new User(0l, "Pawel", "Paszki", 30, "male", "student", "pawelpaszki@gmail.com", "secret");
		usersIndices.put(admin.getId(), admin);
		usersEmails.put(admin.getEmail(), admin);
	}

	/**
	 * 
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 * @param occupation
	 * @param email
	 * @param password
	 *            passed to create new user (email parameter is validated in
	 *            Client class in terms of its correct format and to make sure,
	 *            that there is no user with this email in the database). if
	 *            there are no users id is set to 1l. Otherwise to avoid
	 *            overriding existing users in the map id is set to the last's
	 *            user id incremented by 1. Users are stored in two maps for
	 *            lookup convenience.
	 */
	public void addUser(String firstName, String lastName, int age, String gender, String occupation, String email,
			String password) {
		User user;
		if (usersIndices.size() > 1) {
			user = new User((((TreeMap<Long, User>) usersIndices).lastKey() + 1), firstName, lastName, age, gender,
					occupation, email, password);
		} else {
			user = new User(1l, firstName, lastName, age, gender, occupation, email, password);
		}
		usersIndices.put(user.getId(), user);
		usersEmails.put(email, user);
	}

	/**
	 * 
	 * @param id
	 * @param firstName
	 * @param lastName
	 * @param age
	 * @param gender
	 * @param occupation
	 * @param email
	 * @param password
	 *            passed to create new user (email parameter is validated in
	 *            Client class in terms of its correct format and to make sure,
	 *            that there is no user with this email in the database). if
	 *            there are no users id is set to 1l. Otherwise to avoid
	 *            overriding existing users in the map id is set to the last's
	 *            user id incremented by 1. Users are stored in two maps for
	 *            lookup convenience.
	 */
	public void addUser(Long id, String firstName, String lastName, int age, String gender, String occupation,
			String email, String password) {
		if (usersIndices.size() > 1) {
			if (id != (((TreeMap<Long, User>) usersIndices).lastKey() + 1)) {
				id = (((TreeMap<Long, User>) usersIndices).lastKey() + 1);
			}
		} else {
			id = 1l;
		}
		User user = new User(id, firstName, lastName, age, gender, occupation, email, password);
		usersIndices.put(user.getId(), user);
		usersEmails.put(email, user);
	}

	/**
	 * 
	 * @param userID
	 *            (which is currently logged in User) passed to remove a User
	 *            with given id from the database
	 * @return
	 */
	public User removeUser(long userID) {
		if (usersIndices.containsKey(userID)) {
			User user = usersIndices.remove(userID);
			usersEmails.remove(user.getEmail());
			for (Movie movie : movies.values()) {
				movie.getRatings().remove(user.getId()); // removing user's
															// ratings
			}
			if (getLoggedInID() != 0l) {
				logout();
			}
			return user;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param title
	 * @param year
	 *            (validated in Client class)
	 * @param url
	 *            are passed new Movie object is created and put into movies map
	 */
	public void addMovie(String title, String year, String url) {
		Long id;
		if (movies.size() == 0) {
			id = 1l;
		} else {
			id = (((TreeMap<Long, Movie>) movies).lastKey() + 1);
		}
		Movie movie = new Movie(id, title, year, url);
		movies.put(movie.getId(), movie);
	}

	/**
	 * 
	 * @param userID
	 *            (logged in user id)
	 * @param movieID
	 * @param note
	 *            (validated in Client class) are passed in order to add Rating,
	 *            which will be stored in user's Ratings and movie's ratings
	 *            maps
	 */
	public void addRating(long userID, long movieID, int note) {
		// slight change - spec says 'int rating'
		Rating rating = new Rating(userID, movieID, note);
		usersIndices.get(userID).getRatings().put(movieID, rating);
		movies.get(movieID).getRatings().put(userID, rating);
	}

	/**
	 * 
	 * @param movieID
	 *            passed in order to get info about movie
	 * @return Movie, if exists, null otherwise
	 */
	public Movie getMovie(long movieID) {
		if (movies.containsKey(movieID)) {
			movies.get(movieID).setNote(movies.get(movieID).getNote());
			return movies.get(movieID);
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param userID
	 *            passed
	 * @return user's ratings
	 */
	public Map<Long, Rating> getUserRatings(long userID) {
		return getUsersIndices().get(userID).getRatings();
	}

	/**
	 * 
	 * @param userID
	 *            is passed in order to get his / her recommendations
	 * @return null, if user has not rated any movies yet, otherwise compares
	 *         user's ratings with each other user's ratings and finds the best
	 *         match - ie the highest dot product of user's ratings. if
	 *         "the best match" user does not have any other movies rated other
	 *         than those compared with user with given id, then topTen movies
	 *         are returned, otherwise list of movies rated by the compared user
	 *         and not rated by the user with given id is returned
	 */
	public ArrayList<Movie> getUserRecommendations(long userID) {
		if (!(usersIndices.get(userID).getRatings().size() == 0)) {
			Map<Long, Integer> dotProducts = new HashMap<Long, Integer>();
			ArrayList<Movie> recommendations = new ArrayList<Movie>();
			for (User user : usersIndices.values()) {
				if (user.getId() != userID) {
					dotProducts.put(user.getId(), usersIndices.get(userID).compareTo(user));
				}
			}
			Collection<Integer> dotProductValues = dotProducts.values();
			Integer max = Collections.max(dotProductValues);
			Long bestMatchUserId = 0l;
			for (Entry<Long, Integer> entry : dotProducts.entrySet()) {
				if (Objects.equals(max, entry.getValue())) {
					bestMatchUserId = entry.getKey();
				}
			}
			Set<Long> movieIDsSet = usersIndices.get(bestMatchUserId).getRatings().keySet();
			Set<Long> userMovieIDsSet = usersIndices.get(userID).getRatings().keySet();
			for (Long id : movieIDsSet) {
				if (!(userMovieIDsSet.contains(id))) {
					recommendations.add(movies.get(id));
				}
			}
			return recommendations;
		} else {
			return null;
		}

	}

	/**
	 * 
	 * @return List of the movies with the highest notes. MergeX sort is used to
	 *         sort the list and then sublist of those movies is created. by
	 *         getting the 10 movies at the far end, which have the highest
	 *         notes. if there are no more than 10 movies stored in the movies
	 *         map -
	 */
	public List<Movie> getTopTenMovies() {
		List<Movie> movieList = new ArrayList<Movie>(movies.values());
		if (movieList.size() > 10) {
			MergeX.sort(movieList);
			List<Movie> topTen = movieList.subList(movieList.size() - 10, movieList.size());
			Collections.reverse(topTen);
			return topTen;
		} else {
			MergeX.sort(movieList);
			Collections.reverse(movieList);
			return movieList;
		}
	}

	/**
	 * reads the serialized data
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void load() throws Exception {
		serializer.read();
		movies = (Map<Long, Movie>) serializer.pop();
		usersEmails = (Map<String, User>) serializer.pop();
		usersIndices = (Map<Long, User>) serializer.pop();
		instantiateAdmin();
	}

	/**
	 * writes data to the external file
	 * 
	 * @throws Exception
	 */
	public void write() throws Exception {
		serializer.push(usersIndices);
		serializer.push(usersEmails);
		serializer.push(movies);
		serializer.write();
	}

	/**
	 * Loads data from hardcoded .dat files, if they exist and puts that data to
	 * collections of Users and Movies.
	 * 
	 * @throws Exception
	 *             if the files don't exist
	 */
	public void prime() throws Exception {
		CSVLoader loader = new CSVLoader();
		List<User> users = loader.loadUsers("data_movieLens/users.dat");
		for (User user : users) {
			usersIndices.put(user.getId(), user);
			usersEmails.put(user.getEmail(), user);
		}
		List<Movie> movieList = loader.loadMovies("data_movieLens/items.dat", "data_movieLens/genres.dat");
		for (Movie movie : movieList) {
			movies.put(movie.getId(), movie);
		}
		List<Rating> ratings = loader.loadRatings("data_movieLens/ratings.dat");
		for (Rating rating : ratings) {
			usersIndices.get(rating.getUserID()).getRatings().put(rating.getMovieID(), rating);
			movies.get(rating.getMovieID()).getRatings().put(rating.getUserID(), rating);
		}
		instantiateAdmin();
	}

	/**
	 * Loads data from hardcoded .dat files, if they exist and puts that data to
	 * collections of Users and Movies.
	 * 
	 * @throws Exception
	 *             if the files don't exist
	 */
	public void prime(String usersPath, String moviesPath, String genresPath, String ratingsPath) throws Exception {
		CSVLoader loader = new CSVLoader();
		List<User> users = loader.loadUsers(usersPath);
		for (User user : users) {
			usersIndices.put(user.getId(), user);
			usersEmails.put(user.getEmail(), user);
		}
		List<Movie> movieList = loader.loadMovies(moviesPath, genresPath);
		for (Movie movie : movieList) {
			movies.put(movie.getId(), movie);
		}
		List<Rating> ratings = loader.loadRatings(ratingsPath);
		for (Rating rating : ratings) {
			usersIndices.get(rating.getUserID()).getRatings().put(rating.getMovieID(), rating);
			movies.get(rating.getMovieID()).getRatings().put(rating.getUserID(), rating);
		}
		instantiateAdmin();
	}

	/**
	 * 
	 * @param title
	 *            is passed
	 * @return map of titles, whose title contains title specified as a
	 *         parameter
	 */
	public TreeMap<Long, Movie> findMovie(String title) {
		TreeMap<Long, Movie> titles = new TreeMap<Long, Movie>();
		for (Movie movie : movies.values()) {
			if (movie.getTitle().toLowerCase().contains(title.toLowerCase())) {
				titles.put(movie.getId(), movie);
			}
		}
		return titles;
	}

	/**
	 * @return the usersIndices
	 */
	public Map<Long, User> getUsersIndices() {
		return usersIndices;
	}

	/**
	 * @return the usersEmails
	 */
	public Map<String, User> getUsersEmails() {
		return usersEmails;
	}

	/**
	 * @return the movies
	 */
	public Map<Long, Movie> getMovies() {
		return movies;
	}

	/**
	 * 
	 * @param email
	 * @param password
	 *            are passed
	 * @return true, if login was successful, false otherwise when true -
	 *         loggedIn id is set to the id of user with specified params
	 */
	public boolean login(String email, String password) {
		if (usersEmails.containsKey(email)) {
			if (usersEmails.get(email).getPassword().equals(password)) {
				setLoggedIn(true);
				setLoggedInID(usersEmails.get(email).getId());
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * logs out a user
	 */
	public void logout() {
		setLoggedIn(false);
		setLoggedInID(-1l);
	}

	/**
	 * @return the loggedIn
	 */
	public boolean isLoggedIn() {
		return loggedIn;
	}

	/**
	 * @param loggedIn
	 *            the loggedIn to set
	 */
	public void setLoggedIn(boolean loggedIn) {
		this.loggedIn = loggedIn;
	}

	/**
	 * @return the loggedInID
	 */
	public Long getLoggedInID() {
		return loggedInID;
	}

	/**
	 * @param loggedInID
	 *            the loggedInID to set
	 */
	public void setLoggedInID(Long loggedInID) {
		this.loggedInID = loggedInID;
	}
}
