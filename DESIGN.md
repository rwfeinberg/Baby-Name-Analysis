data - Design
====

Name(s): Ryan W. Feinberg (Head/Solo developer)

### Design Goals
The goal of this project was to create a "back-end" implementation program to read in a text file of names, genders, and frequencies for a large number of years, and interpolate information about the relationships between names, their info, and certain periods of time.

* This program could be used to create infographics for a webpage, or be the basis of a website to take in user-inputted parameters and displays certain information about the data inputted.

* I wanted to make it easy to use this program as the data processing part of a complete interface, in general.

### High Level Design
The project is divided into three main classes, plus an extra class used to define a particular exception (FileNotFoundException.java).

The three main classes are:
* **Input.java -** This class consists of one overall method (readFile) with a helper method. The readFile method takes in a Java File, as formatted as described in the README, and creates a HashMap. The map contains names with frequencies, sorted by gender. A new map would be made for each file (year) desired.

* **Process.java -** This class does all of the processing of the information, in order to draw conclusions. It contains numerous methods, all named descriptively, that take in an input name, gender, year, rank, or etc. and calculate certain associations or relationships.

* **Main.java -** This class is the class that the user will run. It predefines numerous variables that represent the associtaed information calculated by the variables respective method in Process.java. Then, the user would simply place the name of the desired variable into the print line at the bottom in order for the program to display the desired information.


### Assumptions/Decisions
The assumptions made during this process are listed in the project's README. They consist of that:
* The text files given to the program are separated by line, and each line lists name, gender, then frequency from left to right.
* The files/directory given to the program is present on the machine in which the program is running, not on a separate server.

### Adding New Features
To add a feature to process files from a remote URL, ideally, a new method should be created in Input.java, preferably under readFile and above makeMapFromFile. This method should take in a string (URL) as a parameter, and should the return statement should be: `return makeMapFromFile(yearMap, maleMap, femaleMap, scanner, firstEntry);`.

The method should take in the url parameter and should first access the file that the URL is denotoing and create a Java File from it. Then, this file should be put into a Java Scanner, through: `Scanner scanner = new Scanner(FILE);`. This scanner, three blank HashMaps (yearMap, maleMap, femaleMap), and the first line of the scanner (`String firstEntry = scanner.nextLine().split(DELIMITER);`) should then be passed into makeMapFromFile in the return statement described.


To add a new processing feature in general, create a new, pubilc method in the Process.java class with a name that accurately describes the computation that the method will perform, and then add a corresponding variable in Main.java for that method (see Main.java for examples of variables).