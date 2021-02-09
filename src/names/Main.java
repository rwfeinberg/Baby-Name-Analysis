package names;

import java.util.*;
import java.io.File;

public class Main {

  /**
   * Used for single-file-based questions
   * <p>
   * Base directory is project directory, so to access a file in ssa_complete, change path to
   * "data/ssa_complete/yobXXXX.txt"
   */
  private static final File FILE = new File("data/ssa_complete/yob1900.txt");


  /**
   * Used for range-based questions (can do single file as well, see methods)
   * <p>
   * Base directory is project directory, so to choose the directory ssa_complete, change path to
   * "data/ssa_complete"
   * <p>
   * If using URL: Change FILE_TYPE_IS_URL in Process to true!
   */
  private static final String DATA_PATH = "data/ssa_complete";


  /**
   * START_YEAR is used as the inputted year when only 1 year is needed In this case, change
   * END_YEAR to a value > START_YEAR
   */
  private static final int START_YEAR = 1975;
  private static final int END_YEAR = 1977;

  /**
   * GENDER should be one of four strings: "F", "M", "f", or "m" NAME should be first letter
   * capitalized, and rest lowercase (ex. "Mark", "Mary Beth") STARTING_LETTER should be a single
   * character, upper or lowercase
   */
  private static final String GENDER = "M";
  private static final String NAME = "Ryan";
  private static final char STARTING_LETTER = 'A';
  private static final int NUM_OF_YEARS = 10;
  private static final int RANK = 3;


  /**
   * MAIN METHOD
   */
  public static void main(String[] args) throws FileDirectoryEmptyException {
    Process p = new Process();

    /**
     * Variables with answers to questions from Data: Test
     * (i.e. 1 file methods)
     */
    String topRankedFemale = p.getTopRankedFemale(p.readIn(FILE));
    String topRankedMale = p.getTopRankedMale(p.readIn(FILE));
    int numberOfNamesForGivenLetter = p
        .getNumberOfNamesForGivenLetter(GENDER, STARTING_LETTER, p.readIn(FILE));
    int totalCountForGivenLetter = p
        .getTotalCountForGivenLetter(GENDER, STARTING_LETTER, p.readIn(FILE));

    Map<Integer, Integer> pairRanking = p
        .getPairRanking(DATA_PATH, START_YEAR, END_YEAR, GENDER, NAME);
    int pairRankingForSingleYear = p.getPairRankingForSingleYear(GENDER, NAME, p.readIn(FILE));
    String equivalentPair = p.getEquivalentPair(DATA_PATH, NAME, GENDER, START_YEAR);
    String mostFrequentPopularName = p
        .getMostFrequentPopularName(DATA_PATH, GENDER, START_YEAR, END_YEAR);
    List<String> mostPopularFemaleLetter = p
        .getMostPopularFemaleLetter(DATA_PATH, START_YEAR, END_YEAR);

    //Call takes a little longer than others
    String mostVolatileName = p
        .mostVolatileNameBetweenFirstAndLastYears(DATA_PATH, GENDER, START_YEAR, END_YEAR);
    float averageRank = p.getAverageRank(DATA_PATH, NAME, GENDER, START_YEAR, END_YEAR);
    float averageRankForRecentYears = p
        .getAverageRankForRecentYears(DATA_PATH, NAME, GENDER, NUM_OF_YEARS);

    //This call will take a while unless MINIMUM_POPULARITY in Process is above ~2000
    //String highestAverageRank = p.getHighestAverageRank(DATA_PATH, GENDER, START_YEAR, END_YEAR);

    String mostFrequentAtGivenRank = p
        .getMostFrequentAtGivenRank(DATA_PATH, GENDER, RANK, START_YEAR, END_YEAR);

    /**
     * OUTPUT LINE:
     * Change print statement to desired variable
     */
    System.out.println();

  }
}
