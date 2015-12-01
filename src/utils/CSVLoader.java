package utils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import edu.princeton.cs.introcs.In;
import models.Movie;
import models.Rating;
import models.User;

/**
 * @author Pawel Paszki
 * 
 *         This class is used to retrieve "pipe"-separated data from .dat files
 *         it uses stdlib Library to manage streaming the files in.
 */

public class CSVLoader {

	/**
	 * 
	 * @param filename
	 *            specifies the relative path of the file to be read in. For the
	 *            purposes of the application, which stores users in collection
	 *            mapped by email, unique "placeholder" email is generated for
	 *            each user added to the List
	 * @return List of Users, if the file exists, null otherwise
	 * @throws Exception
	 */
	public List<User> loadUsers(String filename) throws Exception {
		File usersFile = new File(filename);
		if (usersFile.exists()) {
			List<User> users = new ArrayList<User>();
			In inUsers = new In(usersFile);
			// each field is separated(delimited) by a '|'
			String delims = "[|]";
			int counter = 1;
			while (!inUsers.isEmpty()) {
				String password = "secret";
				// get user details
				String userDetails = inUsers.readLine();

				// parse user details string
				String[] userTokens = userDetails.split(delims);

				if (userTokens.length == 7) {
					users.add(new User(Long.valueOf(userTokens[0]), userTokens[1], userTokens[2],
							Integer.parseInt(userTokens[3]), userTokens[4], userTokens[5],
							"placeholder" + counter + "@email.com", password));
				}
				counter++; // used to be concatenated with placeholder email
							// such that each email is unique
			}
			return users;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param movieFilename
	 *            specifies the relative path of the file to be read in. if the
	 *            file with specified path exists - all movies from the file are
	 *            added to the List
	 * @param genreFilename
	 *            specifies the relative path of file with genres to be read in
	 * @return List of Movies, if the file exists, null otherwise
	 * @throws Exception
	 */
	public List<Movie> loadMovies(String movieFilename, String genreFilename) throws Exception {
		File moviesFile = new File(movieFilename);
		File genresFile = new File(movieFilename);
		if (moviesFile.exists() && genresFile.exists()) {
			String delims = "[|]";
			// genres:
			String[] genres = new String[19];
			int counter = 0;
			In inGenres = new In(genresFile);
			while (!inGenres.isEmpty()) {
				String genreDetails = inGenres.readLine();

				// parse user details string
				String[] genreTokens = genreDetails.split(delims);

				// output user data to console.
				if (genreTokens.length == 2) {
					genres[counter] = genreTokens[0];
				}
			}
			// movies
			List<Movie> movies = new ArrayList<Movie>();
			counter = 1;

			In inMovies = new In(moviesFile);
			while (!inMovies.isEmpty()) {
				// get movie details
				String movieDetails = inMovies.readLine();

				// parse user details string
				String[] movieTokens = movieDetails.split(delims);

				// output user data to console.
				if (movieTokens.length == 23) {
					ArrayList<String> categories = new ArrayList<String>();
					movies.add(new Movie((long) counter, movieTokens[1], movieTokens[2], movieTokens[3]));
					for (int i = 4; i < movieTokens.length; i++) {
						if (Integer.parseInt(movieTokens[i]) == 1) {
							categories.add(genres[i - 4]);
						}
					}
					movies.get(counter - 1).setCategories(categories);
				}
				counter++;
			}
			return movies;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @param filename
	 *            specifies relative path of the Ratings file to be read in
	 * @return the List with ratings, if the file exists, null otherwise
	 * @throws Exception
	 */
	public List<Rating> loadRatings(String filename) throws Exception {
		File ratingsFile = new File(filename);
		if (ratingsFile.exists()) {
			List<Rating> ratings = new ArrayList<Rating>();
			In inRatings = new In(ratingsFile);
			String delims = "[|]";
			while (!inRatings.isEmpty()) {
				// get movie details
				String ratingDetails = inRatings.readLine();

				// parse user details string
				String[] ratingTokens = ratingDetails.split(delims);

				// output user data to console.
				if (ratingTokens.length == 4) {
					ratings.add(new Rating(Long.valueOf(ratingTokens[0]), Long.valueOf(ratingTokens[1]),
							Integer.parseInt(ratingTokens[2])));
				}
			}
			return ratings;
		} else {
			return null;
		}
	}
}
