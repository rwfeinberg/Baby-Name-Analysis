data
====

This project uses data about [baby names from the US Social Security Administration](https://www.ssa.gov/oact/babynames/limits.html) to answer specific questions. 


Name: Ryan W. Feinberg

### Timeline

Start Date: 08/25/20

Finish Date: 09/06/20

Hours Spent: 20

### Resources Used

Stackoverflow

### Running the Program

Main class: 

In Main.java, all of the methods (bar a couple, due to runtime situations explained in inline comments) are automatically calculated with the inputs defined by the contstants and put into variables with respective names. 

To view the output of a given method, place desired variable/calculation in the System.out.println at the bottom of the class. To set inputs (name, gender, years, etc.), change respective constant at the top of Main.java (there are also comments describing each one). 

To select a certain directory or file to be ran, enter its path, starting at the project root, into the appropriate constant at the top of Main.java (either FILE for single file methods, or DATA_PATH for multi-file methods).

    - If the .txt file lists male names before female names, set the STARTING_GENDER_FEMALE constant in Input.java to false.
    - If the .txt file lists each line seperate by something other than a comma, set the DELIMITER constant in Input.java to the appropriate symbol.
    - Read in-line comments starting on line 11 of Process.java for an explanation of the MINIMUM_POPULARITY constant.

For testing purposes, the ProcessTest.java class in the test folder should be used. The tests are currently set up to all pass.


Data files needed: 

Mark a data folder as the resources root, have it contain:

For testing purposes, the yob1900, yob2100, yob2101, yob2102, and yob3000.txt files are required to pass
all of the tests, as well as an empty directory in the data folder called "empty". The contents of each of these files is visible in /data/ here on Gitlab.

For running the program on real data, a directory within the data folder containing all of the .txt files you would like needs to be included.
The files should be of the form "yobXXXX", but can have any file extension, as long as it's constant is changed at the top of the Process.java class.


Key/Mouse inputs: 

N/A

Cheat keys:

N/A

Known Bugs:

Can't handle data sources from URLs, only downloaded, line-separated text files. 
If the start year is set to be greater than the end year for a method, an error will occur.
A custom exception was made if the input data directory is empty, and an example is at the very bottom of ProcessTest.java

Extra credit:

N/A

### Notes/Assumptions
 - The data sets always start with Female names listed first. 
 
    **IF NEED TO CHANGE: Change boolean on line 18 of Input.java to false.**

- Processed data files should be of the form "yobXXXX" with any extension (explained above).

- When testing a range of years, START_YEAR < or = END_YEAR in Main.java or ProcessTest.java
