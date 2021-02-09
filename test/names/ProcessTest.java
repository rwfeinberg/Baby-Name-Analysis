package names;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.*;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class ProcessTest {
    private static final File FILE = new File("data/yob3000.txt");
    private static final String DATA_PATH = "data";
    private static final String DATA_PATH_LARGE = "data/ssa_complete";
    Process process = new Process();

    /**
     * 3 Tests for Data: Test, Question 1
     */
    @Test
    void getTopRankedFemaleTest() {
        Map<String, Map<Integer, String>> map = process.readIn(FILE);
        assertEquals(process.getTopRankedFemale(map), "Abby");
        assertNotEquals(process.getTopRankedFemale(map), "Earl");
    }

    @Test
    void getTopRankedMaleTest() {
        Map<String, Map<Integer, String>> map = process.readIn(FILE);
        assertEquals(process.getTopRankedMale(map), "Earl");
        assertNotEquals(process.getTopRankedMale(map), "Evan");
    }

    @Test
    void topRankedInvalidTest() {
        Map<String, Map<Integer, String>> map = process.readIn(FILE);
        assertNotEquals(process.getTopRankedMale(map), 3);
        assertNotEquals(process.getTopRankedMale(map), null);
    }

    /**
     * 3 Tests for Data: Test, Question 2
     */
    @Test
    void simpleNumberOfNamesTest() {
        Map<String, Map<Integer, String>> map = new HashMap<>();
        Map<Integer, String> testMap = new HashMap<>();
        testMap.put(2, "A,5");
        testMap.put(0, "A,20");
        testMap.put(1, "Aa,10");
        testMap.put(3, "B,4");   //3 "A" names, 1 "B" name

        map.put("F", testMap);

        assertEquals(3, process.getNumberOfNamesForGivenLetter("F", 'A', map));
    }

    @Test
    void getNumberOfNamesForGivenLetterTest() {
        Map<String, Map<Integer, String>> map = process.readIn(FILE);
        assertSame(2, process.getNumberOfNamesForGivenLetter("f", 'b', map));
        assertSame(2, process.getNumberOfNamesForGivenLetter("M", 'b', map));
        assertNotSame(1, process.getNumberOfNamesForGivenLetter("F", 'd', map));
        assertNotSame(3, process.getNumberOfNamesForGivenLetter("m", 'd', map));
        assertSame(0, process.getNumberOfNamesForGivenLetter("f", 'P', map));
    }

    @Test
    void getTotalCountForGivenLetter() {
        Map<String, Map<Integer, String>> map = process.readIn(FILE);
        assertEquals(7500, process.getTotalCountForGivenLetter("f", 'b', map));
        assertEquals(5500, process.getTotalCountForGivenLetter("M", 'b', map));
        assertNotEquals(2, process.getTotalCountForGivenLetter("F", 'D', map));
        assertNotEquals(2000, process.getTotalCountForGivenLetter("m", 'd', map));
        assertEquals(0, process.getTotalCountForGivenLetter("f", 'P', map));
    }

    /**
     * 3 Tests for Data: Basic, Question 1
     * ALSO
     * Complete: q1 (get rankings given range of years)
     */
    @Test
    void getPairRankingTest() throws FileDirectoryEmptyException {
        assertEquals(4, process.getPairRanking(DATA_PATH, 2100, 2102, "f", "A").get(2101));
        assertEquals(6, process.getPairRanking(DATA_PATH, 2100, 2102, "f", "Dd").get(2102));
        assertEquals(3, process.getPairRanking(DATA_PATH, 2100, 2102, "m", "C").get(2100));
    }

    @Test
    void getPairRankingEdgeCaseTest() throws FileDirectoryEmptyException {
        assertEquals(11, process.getPairRanking(DATA_PATH, 2100, 2102, "M", "Ee").get(2102));
        assertEquals(-1, process.getPairRanking(DATA_PATH, 2100, 2102, "f", "Z").get(2102));
    }

    @Test
    void getPairRankSingleYear() {
        Map<String, Map<Integer, String>> map = process.readIn(FILE);
        assertEquals(1, process.getPairRankingForSingleYear("f", "Abby", map));
        assertEquals(10, process.getPairRankingForSingleYear("M", "Abe", map));
        assertEquals(-1, process.getPairRankingForSingleYear("F", "John", map));
    }

    /**
     * 3 Tests for Data: Basic, Question 2
     */
    @Test
    void getEquivalentPairTest() throws FileDirectoryEmptyException {
        assertEquals("Eliza,3000", process.getEquivalentPair(DATA_PATH, "Aa", "f", 2100));
        assertEquals("Earl,3000", process.getEquivalentPair(DATA_PATH, "C", "m", 2101));
        assertNotEquals("Ee,3000", process.getEquivalentPair(DATA_PATH, "Ee", "f", 2100));
    }

    @Test
    void getEquivalentPairTestEdgeCases() throws FileDirectoryEmptyException {
        assertNotEquals("Ee,2102", process.getEquivalentPair(DATA_PATH, "Emily", "f", 2100));
        assertNotEquals("A,2102", process.getEquivalentPair(DATA_PATH_LARGE, "C", "m", 1935));
    }

    @Test
    void getEquivalentPairTestSingleYear() throws FileDirectoryEmptyException {
        assertEquals("You entered the current year!\nRank is:" + 10, process.getEquivalentPair(DATA_PATH, "Emily", "f", 3000));
        assertEquals("You entered the current year!\nRank is:" + -1, process.getEquivalentPair(DATA_PATH, "C", "m", 3000));
        assertEquals("You entered the current year!\nRank is:" + 2, process.getEquivalentPair(DATA_PATH_LARGE, "Noah", "m", 2018));
    }

    /**
     * 3 Tests for Data: Basic, Question 3
     */
    @Test
    void getMostFrequentPopularName() throws FileDirectoryEmptyException {
        assertEquals("Ashley,2 year(s)", process.getMostFrequentPopularName(DATA_PATH_LARGE, "f", 1990, 1992));
        assertEquals("Jessica,4 year(s)", process.getMostFrequentPopularName(DATA_PATH_LARGE, "f", 1990, 1995));

    }

    @Test
    void testMostFrequencyPopularNameEqualReigns() throws FileDirectoryEmptyException {
        assertEquals("Ashley,Jessica,2 year(s)", process.getMostFrequentPopularName(DATA_PATH_LARGE, "f", 1991, 1994));
        assertEquals("A,C,E,1 year(s)", process.getMostFrequentPopularName(DATA_PATH, "m", 2100, 2102));
    }

    @Test
    void testMostFrequentPopularNameEdgeCases() throws FileDirectoryEmptyException {
        assertEquals("Jessica,1 year(s)", process.getMostFrequentPopularName(DATA_PATH_LARGE, "f", 1990, 1990));
        assertEquals("Cc,Mary,1 year(s)", process.getMostFrequentPopularName(DATA_PATH, "f", 1900, 2100));
    }

    /**
     * 3 Tests for Data: Basic, Question 4
     */
    @Test
    void testUpdateMapMethod() {
        Map<Integer, String> yearMap = process.readIn(FILE).get("F");
        Map<Character, Map<String, Integer>> freqMap = new HashMap<>();
        process.updateYearMap(yearMap, freqMap);

        assertEquals(4500, freqMap.get('A').get("Amelia"));
        assertEquals(5000, freqMap.get('A').get("Abby"));
        assertEquals(2500, freqMap.get('C').get("Claire"));
        assertNotEquals(4000, freqMap.get('C').get("Chris"));
    }

    @Test
    void testMakeLetterMap() {
        Map<Character, Map<String, Integer>> freqMap = new HashMap<>();

        //Add 5 diff years to freqMap
        //The 5 files are local test files I created myself, replace them with others with known values
        Map<Integer, String> yearMap2 = process.readIn(new File("data/yob2100.txt")).get("F");
        process.updateYearMap(yearMap2, freqMap);
        Map<Integer, String> yearMap3 = process.readIn(new File("data/yob2101.txt")).get("F");
        process.updateYearMap(yearMap3, freqMap);
        Map<Integer, String> yearMap4 = process.readIn(new File("data/yob2102.txt")).get("F");
        process.updateYearMap(yearMap4, freqMap);
        Map<Integer, String> yearMap5 = process.readIn(new File("data/yob3000.txt")).get("F");
        process.updateYearMap(yearMap5, freqMap);

        Map<Character, Integer> letterMap = new HashMap<>();

        List<Character> letters = new ArrayList<>(freqMap.keySet());
        List<Map<String, Integer>> namesAndFreqs = new ArrayList<>(freqMap.values());

        process.makeLetterMap(freqMap, letterMap, letters, namesAndFreqs);

        assertEquals(30000, letterMap.get('A'));
        assertEquals(14000, letterMap.get('D'));
        assertEquals(11000, letterMap.get('E'));
        assertNotEquals(20500, letterMap.get('F'));
    }

    @Test
    void testMostPopularFemaleLetter() throws FileDirectoryEmptyException {
        assertEquals(new ArrayList<>(Arrays.asList("C", "Cass", "Cc", "Claire")), process.getMostPopularFemaleLetter(DATA_PATH, 2100, 3000));
        assertEquals(new ArrayList<>(Arrays.asList("C", "Cc")), process.getMostPopularFemaleLetter(DATA_PATH, 2100, 2101));
        assertEquals(new ArrayList<>(Arrays.asList("A", "C")), process.getMostPopularFemaleLetter(DATA_PATH, 2101, 2102));

    }

    /**
     * Test Complete: q2 (Change between first and last year rank)
     */
    @Test
    void differenceBetweenFirstAndLastRankingsExceptionTest() throws FileDirectoryEmptyException {
        assertEquals("Name not found in year range", process.differenceBetweenFirstAndLastRankings(DATA_PATH, "Abby", "F", 2100, 2102));
        assertEquals("First and most recent years are the same year! Rank: " + 5, process.differenceBetweenFirstAndLastRankings(DATA_PATH, "B", "m", 2101, 2101));
    }

    @Test
    void differenceBetweenFirstAndLastRankingsIncreaseTest() throws FileDirectoryEmptyException {
        assertEquals("Rank increased from first to most recent year! Rank increased by: " + 5, process.differenceBetweenFirstAndLastRankings(DATA_PATH, "A", "F", 2100, 2102));
        assertEquals("Rank increased from first to most recent year! Rank increased by: " + 2, process.differenceBetweenFirstAndLastRankings(DATA_PATH, "B", "m", 2101, 2102));
    }

    @Test
    void differenceBetweenFirstAndLastRankingsDecreaseTest() throws FileDirectoryEmptyException {
        assertEquals("Rank decreased from first to most recent year! Rank decreased by: " + 3, process.differenceBetweenFirstAndLastRankings(DATA_PATH, "Cc", "F", 2100, 2102));
        assertEquals("Rank decreased from first to most recent year! Rank decreased by: " + 4, process.differenceBetweenFirstAndLastRankings(DATA_PATH, "C", "m", 2101, 2102));
    }

    /**
     * Test Complete: q3 (What name's rank changed the most?)
     */
    @Test
    void mostVolatileNameTestSameYearCase() throws FileDirectoryEmptyException {
        assertEquals("No names changed ranks!", process.mostVolatileNameBetweenFirstAndLastYears(DATA_PATH, "f", 2100, 2100));
        assertEquals("No names changed ranks!", process.mostVolatileNameBetweenFirstAndLastYears(DATA_PATH, "M", 2102, 2102));
    }

    @Test
    void mostVolatileNameTestExceptions() throws FileDirectoryEmptyException {
        Map<Integer, String> map = process.readIn(FILE).get("F");

        assertEquals("None of the names present in the first year were present in last year!", process.mostVolatileNameBetweenFirstAndLastYears(DATA_PATH, "f", 2100, 3000));
        assertEquals(8, process.findNameRank(map, "Dory"));
    }

    @Test
    void mostVolatileNameTest() throws FileDirectoryEmptyException {
        assertEquals("[Aa]", process.mostVolatileNameBetweenFirstAndLastYears(DATA_PATH, "f", 2100, 2102));
        assertEquals("[Ee]", process.mostVolatileNameBetweenFirstAndLastYears(DATA_PATH, "m", 2100, 2102));
    }

    /**
     * Test Complete: q4 (What name's average rank?)
     */
    @Test
    void getAverageRankTestSingleYear() throws FileDirectoryEmptyException {
        assertEquals(10, process.getAverageRank(DATA_PATH, "Aa", "m", 2100, 2100));
        assertEquals(3, process.getAverageRank(DATA_PATH, "B", "f", 2100, 2100));
    }

    @Test
    void getAverageRankTest() throws FileDirectoryEmptyException {
        assertEquals(9.5, process.getAverageRank(DATA_PATH, "Aa", "m", 2100, 2101));
        assertEquals(5.5, process.getAverageRank(DATA_PATH, "Aa", "m", 2101, 2102));
        assertEquals(1.5, process.getAverageRank(DATA_PATH_LARGE, "Ashley", "f", 1990, 1991));

    }

    @Test
    void getAverageRankTestEdges() throws FileDirectoryEmptyException {
        assertEquals(-1, process.getAverageRank(DATA_PATH, "Abby", "m", 2100, 2100));
        assertEquals(2.5, process.getAverageRank(DATA_PATH, "A", "f", 2101, 3000));
        assertEquals(10.5, process.getAverageRank(DATA_PATH, "Z", "m", 2100, 2102));
    }

    /**
     * Test Complete: q5 (What name has highest average rank?)
     */
    @Test
    void getHighestAverageRankTestSingleYear() throws FileDirectoryEmptyException {
        assertEquals("[E]", process.getHighestAverageRank(DATA_PATH, "m", 2100, 2100));
        assertEquals("[Cc]", process.getHighestAverageRank(DATA_PATH, "f", 2100, 2100));
    }

    @Test
    void getHighestAverageRankTest() throws FileDirectoryEmptyException {
        assertEquals("[Cc]", process.getHighestAverageRank(DATA_PATH, "f", 2100, 2102));
        assertEquals("[A]", process.getHighestAverageRank(DATA_PATH, "m", 2101, 2102));
    }

    @Test
    void getHighestAverageRankTestEdges() throws FileDirectoryEmptyException {
        assertEquals("[C, E]", process.getHighestAverageRank(DATA_PATH, "m", 2100, 2101));
        assertEquals("[Cc, Aa, A, C]", process.getHighestAverageRank(DATA_PATH, "f", 2101, 3000));

        //This test takes a while if MINIMUM_POPULARITY in Process.java is low
        //assertEquals("[Ashley]", process.getHighestAverageRank(DATA_PATH_LARGE, "f", 1990, 1992));
    }

    /**
     * Test Complete: q6 (Average rank of name for X recent years)
     */
    @Test
    void getAverageRankForRecentYearTestSingleYear() throws FileDirectoryEmptyException {
        assertEquals(2, process.getAverageRankForRecentYears(DATA_PATH_LARGE, "Olivia", "f", 1));
        assertEquals(3, process.getAverageRankForRecentYears(DATA_PATH_LARGE, "William", "m", 1));

    }

    @Test
    void getAverageRankForRecentYearsTest() throws FileDirectoryEmptyException {
        assertEquals(2, process.getAverageRankForRecentYears(DATA_PATH_LARGE, "Olivia", "f", 3));
        assertEquals(4.333333492279053, process.getAverageRankForRecentYears(DATA_PATH_LARGE, "Isabella", "f", 3));
    }

    @Test
    void getAverageRankForRecentYearsTestEdges() throws FileDirectoryEmptyException {
        assertEquals(-1, process.getAverageRankForRecentYears(DATA_PATH_LARGE, "Olivia", "f", 0));
        assertNotEquals(-1, process.getAverageRankForRecentYears("data/ssa_2000s", "Emily", "f", 20));
    }

    /**
     * Test Complete: q7 (What names held the rank for the given years?)
     */
    @Test
    void getNamesForARankTestSingleYear() throws FileDirectoryEmptyException {
        assertEquals("B", process.getNamesForARank(DATA_PATH, "f", 3, 2100, 2100).get(2100));
        assertEquals("William", process.getNamesForARank(DATA_PATH, "m", 2, 1900, 1900).get(1900));
    }

    @Test
    void getNamesForARankTest() throws FileDirectoryEmptyException {
        assertEquals("Samantha", process.getNamesForARank(DATA_PATH_LARGE, "f", 5, 1990, 1995).get(1990));
        assertEquals("Samantha", process.getNamesForARank(DATA_PATH_LARGE, "f", 5, 1990, 1995).get(1991));
        assertEquals("Sarah", process.getNamesForARank(DATA_PATH_LARGE, "f", 5, 1990, 1995).get(1992));
        assertEquals("Emily", process.getNamesForARank(DATA_PATH_LARGE, "f", 5, 1990, 1995).get(1993));
        assertEquals("Sarah", process.getNamesForARank(DATA_PATH_LARGE, "f", 5, 1990, 1995).get(1994));
        assertEquals("Sarah", process.getNamesForARank(DATA_PATH_LARGE, "f", 5, 1990, 1995).get(1995));
    }

    @Test
    void getNamesForARankTestEdges() throws FileDirectoryEmptyException {
        assertEquals("Cass", process.getNamesForARank(DATA_PATH, "f", 5, 1889, 3001).get(3000));
        assertTrue(process.getNamesForARank(DATA_PATH, "m", 2, 3001, 3001).isEmpty());
    }

    /**
     * Test Complete: q8 (What name held given rank longest and for how long?)
     */
    @Test
    void testmostFrequentNameAtRankBasic() throws FileDirectoryEmptyException {
        assertEquals("Amanda,2 year(s)", process.getMostFrequentAtGivenRank(DATA_PATH_LARGE, "f", 4, 1990, 1992));
        assertEquals("William,3 year(s)", process.getMostFrequentAtGivenRank(DATA_PATH_LARGE, "m", 2, 1899, 1901));
    }

    @Test
    void testmostFrequentNameAtRankEqual() throws FileDirectoryEmptyException {
        assertEquals("Megan,Emily,Lauren,1 year(s)", process.getMostFrequentAtGivenRank(DATA_PATH_LARGE, "f", 10, 1990, 1992));
        assertEquals("Amy,Mary,1 year(s)", process.getMostFrequentAtGivenRank(DATA_PATH_LARGE, "f", 6, 1968, 1969));
    }

    @Test
    void testmostFrequentNameAtRankEdges() throws FileDirectoryEmptyException {
        assertEquals("Amanda,1 year(s)", process.getMostFrequentAtGivenRank(DATA_PATH_LARGE, "f", 4, 1990, 1990));
        assertEquals("Noah,1 year(s)", process.getMostFrequentAtGivenRank(DATA_PATH_LARGE, "m", 2, 2018, 2020));
    }


    /**
     * Test that shows custom exception
     */
    @Test
    void testExceptions() throws FileDirectoryEmptyException {
        //Tests for invalid gender
        assertEquals("Ashley,2 year(s)", process.getMostFrequentAtGivenRank(DATA_PATH_LARGE, "f", 4, 2000, 2001));
        assertNull(process.getPairRanking(DATA_PATH, 2100, 2102, "a", "A"));

        //Tests invalid name entry
        assertEquals(-1, process.getAverageRankForRecentYears(DATA_PATH_LARGE, "rgobwefvi", "f", 3));

        //Tests weird year ranges
        assertEquals("Noah,1 year(s)", process.getMostFrequentAtGivenRank(DATA_PATH_LARGE, "m", 2, 2018, 2020));
        assertEquals("Cass", process.getNamesForARank(DATA_PATH, "f", 5, 1889, 3001).get(3000));
        assertTrue(process.getNamesForARank(DATA_PATH, "m", 2, 3001, 3001).isEmpty());

        //Throws FileDirectoryEmpty Exception if data set is empty (uncomment to view exception)
        System.out.println(process.getEquivalentPair("data/empty", "Aa", "f", 2100));
    }
}