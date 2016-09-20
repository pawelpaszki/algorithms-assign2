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

When the application is started the XML file with all the data is loaded, if present (currently it is present, but can be
removed, which will cause the app to start without loading any data). 
There is a method, which reads in 4 .dat files, whose path is hardcoded into that method (prime()). 
This method is also overloaded with parameters, which allows to load any files 
(assuming that the path of those files passed as 4 parameters is correct and that the files exist 
and their content is formatted appropriately)


More info in javadocs in the code
