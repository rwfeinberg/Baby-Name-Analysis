package names;

import java.util.*;
import java.io.File;
import java.io.FileNotFoundException;

public class Input {

  //Change this String to whatever separates Name, gender, and frequency in data file
  private static final String DELIMITER = ",";

  //Change this to false if data file lists Male names first
  private static final boolean STARTING_GENDER_FEMALE = true;


  /**
   * @return map of the year [key: gender ("F" or "M")] [value: map for gender (key: rank) (value:
   * "NAME,frequency")]
   */
  public Map<String, Map<Integer, String>> readFile(File file) {
    Map<String, Map<Integer, String>> yearMap = new HashMap<>();
    Map<Integer, String> maleMap = new HashMap<>();
    Map<Integer, String> femaleMap = new HashMap<>();

    try {
      Scanner scanner = new Scanner(file);
      String[] firstEntry = scanner.nextLine().split(DELIMITER); //initialize first line

      return makeMapFromFile(yearMap, maleMap, femaleMap, scanner, firstEntry);
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }


  /**
   * USED exclusively in readFile
   */
  private Map<String, Map<Integer, String>> makeMapFromFile(
      Map<String, Map<Integer, String>> yearMap, Map<Integer, String> maleMap,
      Map<Integer, String> femaleMap, Scanner scanner, String[] firstEntry) {

    /**
     * Add to female Map if female
     */
    if (STARTING_GENDER_FEMALE) {
      femaleMap.putIfAbsent(1, firstEntry[0] + "," + firstEntry[2]);
    }

    /**
     * Add to male map if not female
     */
    else if (!STARTING_GENDER_FEMALE) {
      maleMap.putIfAbsent(1, firstEntry[0] + "," + firstEntry[2]);
    }

    int i = 2; //to keep track of rank

    /**
     * Case 1: Female names come first
     */
    if (STARTING_GENDER_FEMALE) {
      while (scanner.hasNextLine()) {
        String[] entry = scanner.nextLine().split(DELIMITER);
        if (entry[1].equals("M")) {
          i = 1;
          maleMap.putIfAbsent(i, entry[0] + "," + entry[2]);
          i++;
          break;
        }
        femaleMap.putIfAbsent(i, entry[0] + "," + entry[2]);
        i++;
      }
      return buildOtherMap(yearMap, maleMap, femaleMap, scanner, i);
    }

    /**
     * Case 2: Male names come first
     */
    else if (!STARTING_GENDER_FEMALE) {
      while (scanner.hasNextLine()) {
        String[] entry = scanner.nextLine().split(DELIMITER);
        if (entry[1].equals("F")) {
          i = 1;
          femaleMap.putIfAbsent(i, entry[0] + "," + entry[2]);
          i++;
          break;
        }
        maleMap.putIfAbsent(i, entry[0] + "," + entry[2]);
        i++;
      }
      buildOtherMap(yearMap, femaleMap, maleMap, scanner, i);
    }
    return null;
  }


  /**
   * USED exclusively in readFile
   */
  private Map<String, Map<Integer, String>> buildOtherMap(Map<String, Map<Integer, String>> yearMap,
      Map<Integer, String> otherMap, Map<Integer, String> builtMap, Scanner scanner, int i) {
    while (scanner.hasNextLine()) {
      String[] entry = scanner.nextLine().split(DELIMITER);
      otherMap.putIfAbsent(i, entry[0] + "," + entry[2]);
      i++;
    }

    scanner.close();
    yearMap.putIfAbsent("F", builtMap);
    yearMap.putIfAbsent("M", otherMap);
    return yearMap;
  }
}
