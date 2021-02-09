# Data Plan
## Ryan W. Feinberg - rwf8

This is the link to the [assignment](http://www.cs.duke.edu/courses/compsci307/current/assign/01_data/):


### What is the answer to the two questions below for the data file yob1900.txt (pick a letter that makes it easy too answer)? 
1. The top ranked female name for 1900 was Mary. The top ranked male name for 1900 was John.
2. For the year 1900, female, and letter "Z", there are 35 names total, and 910 total babies with names that start with "Z". 


### Describe the algorithm you would use to answer each one.
To answer question 1, top ranked female name is determined by simply returning the very first word in the data file, which terminates right before the first comma. The top ranked male name would be determined by scanning through each line in the file and recording the name and occurences (in a List of strings, each item as follows: "NAME,#" or something similar)as the value in a HashMap, and the starting letter of the name as the key. Then, when "M" is first read as the character following the first comma (possibly done by having a "seenMale" boolean), a new HashMap will be added to for the males, and the name that occurred with this first occurence of "M" is the top ranked male (and after this, "seenMale" would be set to false, and the scanning continued).

To answer question 2, we will first pick a key/value pair from a HashMap (that matches the desired gender and starting letter) to evaluate. Then, the total number of names can be determined by counting the number of items in the List of the value List, and the total number of babies can be determined by summing up the "#" appearing after the delimiter (probably a comma or a space) in each value list item.

### Likely you may not even need a data structure to answer the questions below, but what about some of the questions in Part 2?
Although I did choose to use a data structure for the questions below, it wasn't needed. And as the question states, the questions in Part 2 would require a data structure be used, in order to associate the amount of variables asked for with each entry. 

### What are some ways you could test your code to make sure it is giving the correct answer (think beyond just choosing "lucky" parameter values)?
Some ways I could test the code are to hand-calculate the answer to a question for certain parameters (ideally parameters that make the hand-calculation relatively easy), and then compare my answer to the result my program returns. 

I could also break up the questions asked into smaller parts, and test my program to see if it correctly answers each small part. For example, for the question asking for all of the rankings of a name/gender pair over time, I could instead test my program to see if it correctly returns a single ranking for a given year. Or in the question asking for, in a range of years, the most popular letter for girls' names, I could first test my program for return the correct list given a single year, then test it for correctly determining how many babies were born with a chosen letter, and so on.

### What kinds of things make the second question harder to test?
The second question is harder to test because it requires one to iterate through the entire list (or at least most of the list) to come up with an answer. The first question only requires 1 entry to be found, and additionally, we the human already know where the entry is. In the second question, we don't already know where to find the answer (because it isn't in 1 place), and it also requires us to access the 'values' of each name and keep track of them, instead of just returning a name.

### What kind of errors might you expect to encounter based on the questions or in the dataset?
I would expect to encounter errors scanning and recording the entries from the data file and correctly formatting the HashMap in the way I described. I would also expect errors in correctly iterating through the keys and value lists in general, as there is a level of complication there. 

### How would you detect those errors and what would a reasonable "answer" be in such cases?
Detecting these errors would be done by setting up tests that return perhaps a random entry in the data set, so that I can make sure that it is formatted correctly. A correct answer would look like a key that is a letter, and a value that is a list of strings, each string having a name, frequency, and rank (gender rank, not just rank for that letter).

To test iteration, I suppose I would make a test attempting to return the rank of the key-value pair whose value starts with "Abby". A correct answer, for the 1990 dataset anyways, would be 259.

### How would your algorithm and testing need to change (if at all) to handle multiple files (i.e., a range of years)?
In order to accomodate multiple files, I would most likely need to create a loop to read each new file, as opposed to just reading the first file, and recording the data from each new file into a new HashMap (of HashMaps, now according to year, then gender...).

