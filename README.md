# algorithms-assign2
This project is the second assignment in Algorithms - programming module in second year Applied Computing course. 

The main aims of this assignment were to write a Java program that can:

- Load existing movie, user and rating data into a Java program.
- Develop a suitable data serializer.
- Create a suitable API to view and manipulate movie, user, and rating data.
- Make movie recommendations for user.
- Provide unit testing which provides adequate code coverage and uses C.O.R.R.E.C.T. and Right B.I.C.E.P guidelines

There are several external libraries and classes written by other people used in the assignment:

http://introcs.cs.princeton.edu/java/stdlib

https://code.google.com/p/cliche/ (command line interface)

http://mvnrepository.com/artifact/org.codehaus.jettison/jettison/1.3.7 (serialization)

http://mvnrepository.com/artifact/stax/stax/1.2.0 (serialization)

http://x-stream.github.io/ (xml serialization)

The whole project consists of 3 packages:
- controllers
- models
- utils
which are mirrored in the test folder as well. All classes except the class with command line interface are fully tested.

Required methods states in the assignment specification were following:
- addUser(firstName,lastName,age,gender,occupation)
- removeUser(userID)
- addMovie(title, year, url)
- addRating(userID, movieID, rating)
- getMovie(movieID)
- getUserRatings(userID)
- getUserRecommendations(userID)
- getTopTenMovies()
- load()
- write()

They all have been implemented. Some with modifications, ie:

- addUser(...) is called register(...)
- deleteOtherAccount(userID) - admin can remove any user but himself
- deleteYourAccount() (makes sense to delete your account only, unless you are an admin)
- getUserRatings()
- getUserRecommendations(userID) -> getYourRecommendations()
- load() - load in the MovieRecommender; loadSavedDatabase() in the Main class
- write()  - read in the MovieRecommender; saveDatabase() in the Main class

There was some extra methods added as well:
- getUsers()
- getYourRatings()
- findMovie()
- login() // user has to be registered in order to login
- logout()
- clearDatabase() - only allowed to be performed by an admin, which is created at the start of the app and 
- each time xml file is loaded

Extra features included:
- email format validation (during user creation) 
- rating validation (if valid value) 
- date format validation (add movie method)
- admin account - admin can clear/load/save the database, ie erase all users, movies and ratings from the database
- mergeXsort - available here: http://algs4.cs.princeton.edu/22mergesort/MergeX.java.html - this class was reworked to 
work with ArrayLists. This sorting algorithm is significantly faster than insertion or selection sorts.

When the application is started the XML file with all the data is loaded, if present (currently it is present, but can be
removed, which will cause the app to start without loading any data). 
There is a method, which reads in 4 .dat files, whose path is hardcoded into that method (prime()). 
This method is also overloaded with parameters, which allows to load any files 
(assuming that the path of those files passed as 4 parameters is correct and that the files exist 
and their content is formatted appropriately)

When it comes to serialization. Currently XML serialization is used, but JSON Serializer is tested and in working condition. 
it can be easily used by changing those two lines of code:

File dataStore = new File("datastore.xml");
serializer = new XMLSerializer(dataStore);

into those:

File dataStore = new File("datastore.json");
serializer = new JSONSerializer(dataStore);

More info in javadocs in the code
