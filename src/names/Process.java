package names;

import java.util.*;
import java.io.File;

public class Process {

  private static final Input input = new Input();  //New input instance (used to readFile in readIn method)


  /**
   * This value represents the minimum number of babies born for a given name required to be
   * considered for method getHighestAverageRank
   * <p>
   * Basically: A higher value decreases runtime, but also increases chance of inaccurate results
   * for getHighestAverageRank (still pretty small chance) A lower value increases runtime in
   * exchange for always accurate results
   * <p>
   * (The only time where increasing this will give wrong results is if a name with a frequency
   * lower than MINIMUM_POPULARITY for a given year all of the sudden jumps to the top 5 for
   * example, which rarely happens with real data.)
   * <p>
   * side note - needs to be 249 or LOWER to pass all of the tests in ProcessTests, can raise for
   * use in Main
   */
  private static final int MINIMUM_POPULARITY = 249;


  /**
   * Change accordingly (at the moment, no alternative to "yob" as FILE_FORMAT is implemented)
   */
  private static final String EXTENSION = ".txt";
  private static final String FILE_FORMAT = "yob";


  /**
   * Gets year from file, assuming file format is yobXXXX.txt
   */
  private int getFileYear(File file) {
    return Integer.parseInt(file.getName().substring(3, 7));
  }


  /**
   * Determines if inputted gender is valid
   */
  private boolean isInvalidGender(String gender) {
    return !(gender.equals("F") || gender.equals("f") || gender.equals("M") || gender.equals("m"));
  }


  /**
   * Makes list of all files in given datapath
   */
  private File[] makeFileDirectory(String datapath) throws FileDirectoryEmptyException {
    File dir = new File(datapath);
    File[] allYears = dir.listFiles();
    if (allYears == null || allYears.length == 0) {
      throw new FileDirectoryEmptyException(
          "File directory located at: '" + datapath + "' is empty");
    }
    return allYears;
  }


  /**
   * USED IN getPairRanking
   *
   * @param file - given file
   * @return map of the year [key: gender ("F" or "M")] [value: map for gender (key: rank) (value:
   * "NAME,frequency")]
   */
  public Map<String, Map<Integer, String>> readIn(File file) {
    return input.readFile(file);
  }


  /**
   * @param map of the year
   * @return Female name with rank = 1
   */
  public String getTopRankedFemale(Map<String, Map<Integer, String>> map) {
    return map.get("F").get(1).split(",")[0];
  }


  /**
   * @param map of the year
   * @return Male name with rank = 1
   */
  public String getTopRankedMale(Map<String, Map<Integer, String>> map) {
    return map.get("M").get(1).split(",")[0];
  }


  /**
   * @param gender         - "F", "M", "f", or "m"
   * @param startingLetter - any SINGLE character A-z, lower or uppercase
   * @param map            - map of the year as described above
   * @return number of names starting with given startingLetter for a given year and gender
   */
  public int getNumberOfNamesForGivenLetter(String gender, char startingLetter,
      Map<String, Map<Integer, String>> map) {
    if (isInvalidGender(gender)) {
      return -1;
    }
    int nameTotal = 0;
    String temp = (startingLetter + "").toUpperCase();
    char letter = temp.charAt(0);
    for (String name : map.get(gender.toUpperCase()).values()) {
      if (name.split(",")[0].startsWith(letter + "")) {
        nameTotal++;
      }
    }
    return nameTotal;
  }


  /**
   * @param gender         - F, M, m, or f
   * @param startingLetter - any SINGLE character A-z, lower or uppercase
   * @param map            - map of the year as described above
   * @return total number of babies born with name starting with given startingLetter for a given
   * year and gender
   */
  public int getTotalCountForGivenLetter(String gender, char startingLetter,
      Map<String, Map<Integer, String>> map) {
    if (isInvalidGender(gender)) {
      return -1;
    }
    int babyTotal = 0;
    String temp = (startingLetter + "").toUpperCase();
    char letter = temp.charAt(0);
    for (String name : map.get(gender.toUpperCase()).values()) {
      if (name.split(",")[0].startsWith(letter + "")) {
        babyTotal += Integer.parseInt(name.split(",")[1]);
      }
    }
    return babyTotal;
  }


  /**
   * USED IN getPairRanking
   *
   * @param gender - inherited from getPairRanking
   * @param name   - inherited from getPairRanking
   * @param map    - inherited from getPairRanking
   * @return - rank of name for a given year, -1 if name isn't present in year
   */
  public int getPairRankingForSingleYear(String gender, String name,
      Map<String, Map<Integer, String>> map) {
    if (isInvalidGender(gender)) {
      return -1;
    }
    List<String> names = new ArrayList<>(map.get(gender.toUpperCase()).values());
    for (int i = 0; i < names.size(); i++) {
      String str = names.get(i);
      names.set(i, str.split(",")[0]);
    }
    int i = 0;
    int place = 0;
    for (String n : names) {
      if (!names.contains(name)) {
        return -1;
      }
      if (n.split(",")[0].equals(name)) {
        place += i;
      }
      i++;
    }
    return place + 1;
  }


  /**
   * @param datapath - String of pathname to directory or file
   * @return - HashMap with (key: year) (value: rank) for every year. If name did not appear in a
   * given year, rank = -1
   */
  public Map<Integer, Integer> getPairRanking(String datapath, int startYear, int endYear,
      String gender, String name) throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return null;
    }

    Map<Integer, Integer> rankMap = new HashMap<>();
    File[] allYears = makeFileDirectory(datapath);

    for (File file : allYears) {
      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);

      if (fileYear >= startYear && fileYear <= endYear) {
        rankMap.put(fileYear, getPairRankingForSingleYear(gender, name, readIn(file)));
      }
    }
    return rankMap;
  }


  /**
   * @return - String "NAME, YEAR" where NAME = name  in most recent year with equivalent ranking to
   * input YEAR = most recent year
   */
  public String getEquivalentPair(String datapath, String name, String gender, int year)
      throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return "Invalid gender.";
    }
    if (datapath.contains(EXTENSION)) {
      int obvRank = getPairRankingForSingleYear(gender, name, readIn(new File(datapath)));
      return "You entered the current year!\nRank is:" + obvRank;
    }
    File[] allYears = makeFileDirectory(datapath);

    File mostRecent = allYears[allYears.length - 1];
    int curRank = 0;

    if (getFileYear(mostRecent) == year) {
      int obvRank = getPairRankingForSingleYear(gender, name, readIn(mostRecent));
      return "You entered the current year!\nRank is:" + obvRank;
    }

    for (File file : allYears) {

      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);

      if (fileYear == year) {
        curRank = getPairRankingForSingleYear(gender, name, readIn(file));
        break;
      }
    }

    if (curRank == -1) {
      return "Name not present in given year";
    }

    String equivalentName = readIn(mostRecent).get(gender.toUpperCase()).get(curRank).split(",")[0];
    return equivalentName + "," + getFileYear(mostRecent);
  }


  /**
   * @return - "NAME(S), ## year(s)" where NAME(S) = Name(s) within given year range that help top
   * spot for longest ## = How many years name(s) held top spot on list
   */
  public String getMostFrequentPopularName(String datapath, String gender, int startYear,
      int endYear) throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return "Invalid gender.";
    }

    Map<String, Integer> reignMap = new HashMap<>();
    File[] allYears = makeFileDirectory(datapath);

    for (File file : allYears) {

      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);

      if (fileYear >= startYear && fileYear <= endYear) {
        String fileTopName = readIn(file).get(gender.toUpperCase()).get(1).split(",")[0];
        int reignLength = 0;
        reignMap.putIfAbsent(fileTopName, reignLength);
        if (reignMap.containsKey(fileTopName)) {
          reignMap.put(fileTopName, reignMap.get(fileTopName) + 1);
        }
      }
    }
    return getMax(reignMap);
  }


  /**
   * Returns name associated with max map value USED IN: getMostFrequentPopularName and
   * getMostFrequentAtGivenRank
   */
  private String getMax(Map<String, Integer> reignMap) {
    String ret = "";
    if (reignMap.isEmpty()) {
      return "Map is empty! Years are wrong!";
    }
    int maxReign = Collections.max(reignMap.values());
    for (Map.Entry<String, Integer> entry : reignMap.entrySet()) {
      if (entry.getValue() == maxReign) {
        ret += entry.getKey() + ",";
      }
    }
    return ret + maxReign + " year(s)";
  }


  /**
   * @return - List of Names, if 1 letter; List of letters if 2+ letters
   */
  public List<String> getMostPopularFemaleLetter(String datapath, int startYear, int endYear)
      throws FileDirectoryEmptyException {

    File[] allYears = makeFileDirectory(datapath);
    Map<Character, Map<String, Integer>> freqMap = new HashMap<>();

    for (File file : allYears) {

      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);

      if (fileYear >= startYear && fileYear <= endYear) {
        Map<Integer, String> yearMap = readIn(file).get("F");
        updateYearMap(yearMap, freqMap);
      }
    }

    Map<Character, Integer> letterMap = new HashMap<>();
    List<Character> letters = new ArrayList<>(freqMap.keySet());
    List<Map<String, Integer>> namesAndFreqs = new ArrayList<>(freqMap.values());

    makeLetterMap(freqMap, letterMap, letters, namesAndFreqs);

    List<Character> maxLetters = new ArrayList<>();
    int maxFreq = Collections.max(letterMap.values());
    int i = 0;
    int maxIndex = 0;

    for (Map.Entry<Character, Integer> entry : letterMap.entrySet()) {
      if (entry.getValue() == maxFreq) {
        maxLetters.add(entry.getKey());
        maxIndex = i + 1;
      }
      i++;
    }

    if (maxLetters.size() == 1) {
      List<String> names = new ArrayList<>(namesAndFreqs.get(maxIndex - 1).keySet());
      names.sort(String::compareToIgnoreCase);
      return names;
    } else {
      List<String> ret = new ArrayList<>();
      for (char c : maxLetters) {
        ret.add(c + "");
      }
      ret.sort(String::compareToIgnoreCase);
      return ret;
    }
  }


  /**
   * USED exclusively in getMostPopularFemaleLetter
   */
  public void makeLetterMap(Map<Character, Map<String, Integer>> freqMap,
      Map<Character, Integer> letterMap, List<Character> letters,
      List<Map<String, Integer>> namesAndFreqs) {
    for (int i = 0; i < freqMap.size(); i++) {
      char startingLetter = letters.get(i);
      List<String> names = new ArrayList<>(freqMap.get(startingLetter).keySet());

      for (String name : names) {
        letterMap.putIfAbsent(startingLetter, 0);
        letterMap
            .put(startingLetter, letterMap.get(startingLetter) + namesAndFreqs.get(i).get(name));
      }
    }
  }


  /**
   * USED exclusively in getMostPopularFemaleLetter updates freqMap for letter (read comments in
   * method for details)
   */
  public void updateYearMap(Map<Integer, String> yearMap,
      Map<Character, Map<String, Integer>> freqMap) {

    for (Map.Entry<Integer, String> entry : yearMap.entrySet()) {
      String name = entry.getValue().split(",")[0];
      int freq = Integer.parseInt(entry.getValue().split(",")[1]);

      freqMap.putIfAbsent(name.toUpperCase().charAt(0), new HashMap<>());
      Map<String, Integer> nameMap = freqMap.get(name.toUpperCase().charAt(0));

      if (freqMap.containsKey(name.toUpperCase().charAt(0))) {
        nameMap.putIfAbsent(name, 0);

        if (nameMap.containsKey(name)) {
          nameMap.put(name, nameMap.get(name) + freq);
        }
      }
    }
  }


  /**
   * @return - Difference between first year ranking and last year ranking
   */
  public String differenceBetweenFirstAndLastRankings(String datapath, String name, String gender,
      int startYear, int endYear) throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return "Invalid gender.";
    }

    File[] allYears = makeFileDirectory(datapath);

    int firstRank = 0;
    int lastRank = 0;

    for (File file : allYears) {

      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);

      if (fileYear <= endYear && fileYear == startYear) {
        firstRank = getPairRankingForSingleYear(gender, name, readIn(file));
      }
      if (fileYear >= startYear && fileYear == endYear) {
        lastRank = getPairRankingForSingleYear(gender, name, readIn(file));
      }
    }
    return compareRanks(startYear, endYear, firstRank, lastRank);
  }


  /**
   * USED exclusively in differenceBetweenFirstAndLastRankings
   */
  private String compareRanks(int startYear, int endYear, int firstRank, int lastRank) {
    if (startYear == endYear) {
      return "First and most recent years are the same year! Rank: " + firstRank;
    } else if (firstRank == -1 || lastRank == -1) {
      return "Name not found in year range";
    } else if (firstRank == lastRank) {
      return "Rank is the same for first and most recent year! Rank: " + firstRank;
    } else if (firstRank <= lastRank) {
      return "Rank decreased from first to most recent year! Rank decreased by: " + (lastRank
          - firstRank);
    } else if (firstRank >= lastRank) {
      return "Rank increased from first to most recent year! Rank increased by: " + (firstRank
          - lastRank);
    } else {
      return "";
    }
  }


  /**
   * @return - Name which rank had biggest change between first and last years
   */
  public String mostVolatileNameBetweenFirstAndLastYears(String datapath, String gender,
      int startYear, int endYear) throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return "Invalid gender.";
    }

    File[] allYears = makeFileDirectory(datapath);

    Map<String, Integer> firstYearNameMap = new HashMap<>();
    Map<String, Integer> rankChangeMap = new HashMap<>();

    for (File file : allYears) {

      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);
      if (fileYear != startYear && fileYear != endYear) {
        continue;
      }

      buildRankChangeMap(gender, startYear, endYear, firstYearNameMap, rankChangeMap, file,
          fileYear);
    }

    if (rankChangeMap.size() == 0) {
      return "None of the names present in the first year were present in last year!";
    }
    int maxRankChange = Collections.max(rankChangeMap.values());
    if (maxRankChange == 0) {
      return "No names changed ranks!";
    }
    List<String> nameWithMaxRankChange = new ArrayList<>();
    for (Map.Entry<String, Integer> entry : rankChangeMap.entrySet()) {
      if (entry.getValue() == maxRankChange) {
        nameWithMaxRankChange.add(entry.getKey());
      }
    }

    return Arrays.toString(nameWithMaxRankChange.toArray());
  }

  /**
   * USED exclusively in mostVolatileNameBetweenFirstAndLastYears
   */
  private void buildRankChangeMap(String gender, int startYear, int endYear,
      Map<String, Integer> firstYearNameMap, Map<String, Integer> rankChangeMap, File file,
      int fileYear) {
    if (fileYear <= endYear && fileYear == startYear) {
      Map<Integer, String> firstYearMap = readIn(file).get(gender.toUpperCase());

      for (Map.Entry<Integer, String> entry : firstYearMap.entrySet()) {

        String nameOfEntry = entry.getValue().split(",")[0];
        int rankOfEntry = findNameRank(firstYearMap, nameOfEntry);

        firstYearNameMap.put(nameOfEntry, rankOfEntry);
      }
    }

    if (fileYear >= startYear && fileYear == endYear) {
      Map<Integer, String> lastYearMap = readIn(file).get(gender.toUpperCase());

      for (Map.Entry<Integer, String> entry : lastYearMap.entrySet()) {

        String nameOfEntry = entry.getValue().split(",")[0];
        int rankOfEntry = findNameRank(lastYearMap, nameOfEntry);

        if (firstYearNameMap.get(nameOfEntry) == null) {
          continue;
        }

        rankChangeMap.put(nameOfEntry, Math.abs(rankOfEntry - firstYearNameMap.get(nameOfEntry)));
      }
    }
  }

  /**
   * USED exclusively in buildRankChangeMap
   */
  public int findNameRank(Map<Integer, String> firstYearMap, String nameOfEntry) {
    int rank = 0;
    for (Map.Entry<Integer, String> entry : firstYearMap.entrySet()) {
      if (entry.getValue().split(",")[0].equals(nameOfEntry)) {
        rank += entry.getKey();
        break;
      }
    }
    return rank;
  }


  /**
   * @return - Average rank of given name and gender in year range
   */
  public float getAverageRank(String datapath, String name, String gender, int startYear,
      int endYear) throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return -1;
    }

    Map<Integer, Integer> yearToRankMap = getPairRanking(datapath, startYear, endYear, gender,
        name);

    float sumOfRanks = 0;
    float numberOfValidYears = 0;
    for (Map.Entry<Integer, Integer> entry : yearToRankMap.entrySet()) {
      if (entry.getValue() == -1) {
        continue;
      }
      sumOfRanks += entry.getValue();
      numberOfValidYears++;
    }

    if (sumOfRanks == 0) {
      return -1;
    }
    return sumOfRanks / numberOfValidYears;
  }


  /**
   * @return - Name which has highest average rank in given year range
   */
  public String getHighestAverageRank(String datapath, String gender, int startYear, int endYear)
      throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return "Invalid gender.";
    }

    File[] allYears = makeFileDirectory(datapath);

    Map<String, Float> nameAndRankMap = new HashMap<>();

    for (File file : allYears) {
      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);

      buildNameAndRankMap(datapath, gender, startYear, endYear, nameAndRankMap, file, fileYear);
    }

    float highestAverage = Collections.min(nameAndRankMap.values());
    List<String> nameWithHighestAverageRank = new ArrayList<>();

    for (Map.Entry<String, Float> entry : nameAndRankMap.entrySet()) {
      if (entry.getValue() == highestAverage) {
        nameWithHighestAverageRank.add(entry.getKey());
      }
    }
    return Arrays.toString(nameWithHighestAverageRank.toArray());
  }


  /**
   * USED exclusively in getHighestAverageRank
   */
  private void buildNameAndRankMap(String datapath, String gender, int startYear, int endYear,
      Map<String, Float> nameAndRankMap, File file, int fileYear)
      throws FileDirectoryEmptyException {
    if (fileYear >= startYear && fileYear <= endYear) {
      Map<Integer, String> yearMap = readIn(file).get(gender.toUpperCase());
      for (String entry : yearMap.values()) {
        String name = entry.split(",")[0];

        if (nameAndRankMap.containsKey(name)) {
          nameAndRankMap.put(name, nameAndRankMap.get(name) / 10);
        } else if (!nameAndRankMap.containsKey(name)
            && Integer.parseInt(entry.split(",")[1]) > MINIMUM_POPULARITY) {
          float nameAverageRank = getAverageRank(datapath, name, gender, startYear, endYear);
          nameAndRankMap.putIfAbsent(name, nameAverageRank);
        }
      }
    }
  }


  /**
   * @return - Average rank for name and gender in the most recent entered number of years
   */
  public float getAverageRankForRecentYears(String datapath, String name, String gender,
      int numberOfYears) throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return -1;
    }

    File[] allYears = makeFileDirectory(datapath);
    if (numberOfYears <= 0) {
      return -1;
    }

    int endYear = 0;

    //iterate through directory backwards, find first correctly formatted file ("last" file in directory)
    for (int i = allYears.length - 1; i >= 0; i--) {
      if (!allYears[i].getName().contains(EXTENSION) && !allYears[i].getName()
          .contains(FILE_FORMAT)) {
        continue;
      }
      endYear = getFileYear(allYears[i]);
      break;
    }

    int startYear = endYear - numberOfYears + 1;

    return getAverageRank(datapath, name, gender, startYear, endYear);
  }


  /**
   * @return - Map of names and associated years for a given rank
   */
  public Map<Integer, String> getNamesForARank(String datapath, String gender, int rank,
      int startYear, int endYear) throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return null;
    }

    File[] allYears = makeFileDirectory(datapath);

    Map<Integer, String> mapOfYearsAndRanks = new HashMap<>();

    for (File file : allYears) {
      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);
      if (fileYear >= startYear && fileYear <= endYear) {
        String name = readIn(file).get(gender.toUpperCase()).get(rank).split(",")[0];
        mapOfYearsAndRanks.put(fileYear, name);
      }
    }

    return mapOfYearsAndRanks;
  }


  /**
   * @return - Name which occupies given rank the most in year range
   */
  public String getMostFrequentAtGivenRank(String datapath, String gender, int rank, int startYear,
      int endYear) throws FileDirectoryEmptyException {
    if (isInvalidGender(gender)) {
      return "Invalid gender.";
    }

    Map<String, Integer> namesAndReigs = new HashMap<>();
    File[] allYears = makeFileDirectory(datapath);

    for (File file : allYears) {

      if (!file.getName().contains(EXTENSION) && !file.getName().contains(FILE_FORMAT)) {
        continue;
      }
      int fileYear = getFileYear(file);

      if (fileYear >= startYear && fileYear <= endYear) {
        String nameAtRank = readIn(file).get(gender.toUpperCase()).get(rank).split(",")[0];

        int reignLength = 0;
        namesAndReigs.putIfAbsent(nameAtRank, reignLength);
        if (namesAndReigs.containsKey(nameAtRank)) {
          namesAndReigs.put(nameAtRank, namesAndReigs.get(nameAtRank) + 1);
        }
      }
    }
    return getMax(namesAndReigs);
  }
}
