package controllers;

import java.io.File;
import java.util.List;
import java.util.TreeMap;

import asg.cliche.Command;
import asg.cliche.Param;
import asg.cliche.Shell;
import asg.cliche.ShellFactory;
import models.Movie;
import models.User;
import utils.Serializer;
import utils.Validator;
import utils.XMLSerializer;

/**
 * 
 * @author Pawel Paszki This class provides command-line interface.
 *
 */
public class Main {

	private MovieRecommenderAPI recommender;
	private Serializer serializer;

	/**
	 * constructor for Client class. if the file's path is correct - data is
	 * being loaded.
	 * 
	 * @throws Exception
	 *             and prints the appropriate info, if there is no consistency
	 *             between the Classes data structure and the file content is
	 *             not structured in a desired way
	 */
	public Main() {
		File dataStore = new File("datastore.xml");
		serializer = new XMLSerializer(dataStore);
		recommender = new MovieRecommenderAPI(serializer);
		if (dataStore.isFile()) {
			try {
				System.out.println("Please wait... Database is being loaded");
				recommender.load();
			} catch (Exception e) {
				System.out.println("The database could not be loaded");
			}
		}
		recommender.instantiateAdmin();

	}

	public static void main(String[] args) throws Exception {
		Main client = new Main();

		Shell shell = ShellFactory.createConsoleShell("pm", "Welcome to Movie-Recommender - ?help for instructions",
				client);
		shell.commandLoop();

		client.recommender.write();
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
	 *            are passed in order to register new user. email is being
	 *            validated in terms of correct format and the check is being
	 *            done in order to make sure, that there is no user with
	 *            specified email. if any of the conditions specified below are
	 *            not met - appropriate info is printed to the console
	 */
	@Command(description = "Register")
	public void Register(@Param(name = "first name") String firstName, @Param(name = "last name") String lastName,
			@Param(name = "age") int age, @Param(name = "gender") String gender,
			@Param(name = "occupation") String occupation, @Param(name = "email") String email,
			@Param(name = "password") String password) {
		if (!recommender.isLoggedIn()) {
			if (Validator.isValidEmailAddress(email)) {
				if (!recommender.getUsersEmails().containsKey(email)) {
					recommender.addUser(firstName, lastName, age, gender, occupation, email, password);
				} else {
					System.out.println("User with email: " + email + " already exists in the database");
				}
			} else {
				System.out.println("Registration failed. Invalid email address format or ");
			}
		} else {
			System.out.println("Log out in order to register another account");
		}
	}

	/**
	 * if there are users - their details are displayed, otherwise message
	 * "No users in the database" is displayed
	 */
	@Command(description = "get all users")
	public void getUsers() {
		if (recommender.getUsersIndices().size() > 0) {
			for (User user : recommender.getUsersIndices().values()) {
				System.out.println(user.getId() + ": " + user.getFirstName() + " " + user.getLastName());
			}
		} else {
			System.out.println("No users in the database");
		}
	}

	/**
	 * if there is a logged in user - its data is removed
	 */
	@Command(description = "Delete your account")
	public void deleteYourAccount() {
		if (recommender.isLoggedIn()) {
			if (!(recommender.getLoggedInID() == 0l)) {
				recommender.removeUser(recommender.getLoggedInID());
			} else {
				System.out.println("admin cannot remove his/her account");
			}
		} else {
			displayWarning();
		}
	}

	/**
	 * an admin can delete any other account
	 */
	@Command(description = "Delete other")
	public void deleteOtherAccount(@Param(name = "id") Long id) {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			if (id != 0l && recommender.getUsersIndices().containsKey(id)) {
				recommender.removeUser(id);
			} else {
				System.out.println("Deleting account failed");
			}
		} else {
			System.out.println("Only authorized users can remove accounts");
		}
	}
	
	/**
	 * 
	 * @param title
	 * @param year
	 * @param url
	 *            are passed. only logged in user can add movie to the database.
	 *            if year format is invalid - appropriate info is printed and no
	 *            movie is added
	 */
	@Command(description = "Add a movie")
	public void addMovie(@Param(name = "title") String title, @Param(name = "year") String year,
			@Param(name = "url") String url) {
		if (recommender.isLoggedIn()) {
			if (Validator.isValidDate(year)) {
				recommender.addMovie(title, year, url);
			} else {
				System.out.println("Invalid date format. dd-MMM-yyyy required. Adding movie failed");
			}
		} else {
			displayWarning();
		}
	}

	/**
	 * 
	 * @param movieId
	 * @param note
	 *            are passed in order to add rating. rating's note must be valid
	 *            and user must be logged in in order to add rating. appropriate
	 *            info is displayed, if valid note of no logged in user
	 */
	@Command(description = "Add rating (only -5,-3,-1,1,3 or 5 allowed)")
	public void addRating(@Param(name = "movieID") Long movieId, @Param(name = "note") int note) {
		if (recommender.isLoggedIn()) {
			if (Validator.isValidRating(note) && recommender.getMovies().containsKey(movieId)) {
				recommender.addRating(recommender.getLoggedInID(), movieId, note);
			} else {
				System.out.println("Adding a rating failed. Incorrect rating value or no movie with given id");
			}
		} else {
			displayWarning();
		}
	}

	/**
	 * 
	 * @param movieID
	 *            is passed and if movie with this id exists - its info - title
	 *            and note are being displayed.
	 */
	@Command(description = "Get a Movie details")
	public void getMovie(@Param(name = "movieID") Long movieID) {
		if (recommender.getMovie(movieID) != null) {
			System.out.println(recommender.getMovie(movieID).toString());
		} else {
			System.out.println("Movie with given id was not found in the database");
		}
	}

	/**
	 * displays logged in user's ratings, if any. if no ratings or no logged in
	 * user info is displayed
	 */
	@Command(description = "get logged in user's ratings")
	public void getYourRatings() {
		if (recommender.isLoggedIn()) {
			long id = recommender.getLoggedInID();
			if (recommender.getUsersIndices().get(recommender.getLoggedInID()).getRatings().size() > 0) {
				System.out.println(recommender.getUsersIndices().get(id).toString());
			} else {
				System.out.println("You have not rated any movies yet");
			}
		} else {
			displayWarning();
		}
	}

	/**
	 * if a user is logged in - he/she can check other user's ratings. if user
	 * id is valid and he has rated any movies - then the ratings are being
	 * displayed
	 * 
	 * @param userID
	 */
	@Command(description = "Get user's ratings")
	public void getUserRatings(@Param(name = "userID") Long userID) {
		if (recommender.isLoggedIn()) {
			if (recommender.getUsersIndices().get(userID) != null) {
				if (recommender.getUsersIndices().get(userID).getRatings().size() > 0) {
					System.out.println(recommender.getUsersIndices().get(userID).toString());
				} else {
					System.out.println("User with given id has not rated any movies yet");
				}
			} else {
				System.out.println("User with given id was not found in the database");
			}
		} else {
			displayWarning();
		}
	}

	/**
	 * when a user is logged in - he/she can see his/her recommendations. If the
	 * user has not rated any movies yet - top ten movies will be printed. if
	 * not logged in - appropriate info will be displayed
	 */
	@Command(description = "Get user's recommendations")
	public void getYourRecommendations() {
		if (recommender.isLoggedIn()) {
			long id = recommender.getLoggedInID();
			if (recommender.getUserRecommendations(id) != null && recommender.getUserRecommendations(id).size() > 0) {
				System.out.println("User's recommendations: ");
				for (Movie movie : recommender.getUserRecommendations(id)) {
					System.out.println(movie.getTitle());
				}
			} else {
				getTopTenMovies();
			}
		} else {
			displayWarning();
		}
	}

	/**
	 * returns top ten movies, or whatever number of movies, if there are less
	 * than 10 and more than 0. if no movies - appropriate info is displayed
	 */
	@Command(description = "Get top ten movies")
	public void getTopTenMovies() {
		List<Movie> topTen = recommender.getTopTenMovies();
		if (topTen != null && topTen.size() != 0) {
			System.out.println("You may find these movies interesting:");
			for (Movie movie : topTen) {
				System.out.println(movie.getTitle() + " (" + movie.getNote() + ")");
			}
		} else {
			System.out.println("No movies in the database yet");
		}
	}

	/**
	 * 
	 * @param title
	 *            is specified to find a movie, whose title contains the
	 *            parameter. if there is no such a movie - info is displayed
	 */
	@Command(description = "Find a movie")
	public void findMovie(@Param(name = "title") String title) {
		TreeMap<Long, Movie> movies = recommender.findMovie(title);
		if (movies.size() > 0) {
			System.out.println("Results for \"" + title + "\":");
			for (Movie movie : movies.values()) {
				System.out.println(movie.getId() + ": " + movie.getTitle());
			}
		} else {
			System.out.println("There is no movie with title containing " + title);
		}
	}

	/**
	 * 
	 * @param email
	 * @param password
	 *            are passed in order to login. if either of them is invalid or
	 *            user is already logged in - info is displayed
	 */
	@Command(description = "Log in")
	public void login(@Param(name = "email") String email, @Param(name = "password") String password) {
		if (!recommender.isLoggedIn()) {
			if (recommender.login(email, password)) {
				System.out.println("Hello " + recommender.getUsersEmails().get(email).getFirstName());
			} else {
				System.out.println("Incorrect email/ password. Login failed");
			}
		} else {
			System.out.println("There is already a logged in user");
		}
	}

	/**
	 * if there a logged in user - he/she will logout. otherwise info stating
	 * that no user is logged in will be displayed
	 */
	@Command(description = "logout")
	public void logout() {
		if (recommender.isLoggedIn()) {
			recommender.logout();
			System.out.println("Logout successful");
		} else {
			System.out.println("No user is logged in at the moment");
		}
	}

	/**
	 * loads the data from .dat files with hardcoded paths, if the files exist
	 * and the data format match the parser. prints appropriate message
	 * otherwise
	 */
	@Command(description = "prime")
	public void loadPredefinedDatabase() {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			try {
				recommender.prime();
			} catch (Exception e) {
				System.out.println("The database was not loaded...");
			}
		} else {
			System.out.println("Only authorized users can load the database");
		}
	}

	/**
	 * loads the data from .dat files with specified paths, if the files exist
	 * and the data format match the parser. prints appropriate message
	 * otherwise
	 */
	@Command(description = "prime")
	public void loadPredefinedDatabase(@Param(name = "users file path") String usersPath,
			@Param(name = "movies file path") String moviesPath, @Param(name = "genres file path") String genresPath,
			@Param(name = "ratings file path") String ratingsPath) {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			try {
				recommender.prime(usersPath, moviesPath, genresPath, ratingsPath);
			} catch (Exception e) {
				System.out.println("The database was not loaded...");
			}
		} else {
			System.out.println("Only authorized users can load the database");
		}
	}

	/**
	 * loads data from xml file, if exists
	 */
	@Command(description = "load saved xml file")
	public void loadSavedDatabase() {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			try {
				recommender.load();
				recommender.instantiateAdmin();
			} catch (Exception e) {
				System.out.println("The database could not be loaded");
			}
		} else {
			System.out.println("Only authorized users can load the database");
		}

	}

	/**
	 * saves data using serializer (if any data is present)
	 * 
	 * @throws Exception
	 *             if writing is not successful
	 */
	@Command(description = "save the database")
	public void saveDatabase() throws Exception {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			if (recommender.getMovies().size() > 0 || recommender.getUsersIndices().size() > 1) {
				try {
					recommender.write();
					System.out.println("Info: The database is being saved...");
				} catch (Exception e) {
					System.out.println("The database could not be saved");
				}
			} else {
				System.out.println("There is nothing to save...");
			}
		} else {
			System.out.println("Only authorized users can save the database");
		}
	}

	/**
	 * 
	 */
	@Command(description = "clear the database")
	public void clearDatabase() {
		if (recommender.isLoggedIn() && recommender.getLoggedInID() == 0l) {
			File dataStore = new File("datastore.xml");
			serializer = new XMLSerializer(dataStore);
			recommender = new MovieRecommenderAPI(serializer);
			recommender.login(recommender.getUsersIndices().get(0l).getEmail(),
					recommender.getUsersIndices().get(0l).getPassword());
			System.out.println("The database has been cleared");
		} else {
			System.out.println("Only authorized users can clear the database");
		}
	}

	/**
	 * displays warning to user, who tries to access data, which can only be
	 * accessed by logged in user
	 */
	public void displayWarning() {
		System.out.println("you need to be logged in to access this menu option");
	}

}