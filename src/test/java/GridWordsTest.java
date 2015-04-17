import org.junit.Test;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.TreeSet;

public class GridWordsTest {
    private static final String TEST_DIR = "src/test/data/";
    private static final String DIC_DIR = "src/test/dictionaries/";
    private static final String ANS_DIR = "src/test/answers/";

    @Test
    public void testBuildTrie() throws Exception {
        Trie test = new Trie();
        String word1 = "abc";
        test.buildTrie(word1);
        boolean[] ans1 = {true,true};
        boolean[] ans2 = {true,false};
        boolean[] ans3 = {true,false};
        boolean[] ans4 = {false,false};
        boolean[] ans5 = {true,false};
        System.out.println(test.contains("abc"));
        assert Arrays.equals(test.contains("abc"),ans1);
        assert Arrays.equals(test.contains("a"),ans2);
        assert Arrays.equals(test.contains("ab"), ans3);
        assert Arrays.equals(test.contains("ac"), ans4);
        assert Arrays.equals(test.contains(""), ans5);
    }

    @Test
    public void testGridWordGenerator() throws Exception {

        String dctfile =DIC_DIR+ "englishwords.txt";
        int n = 2;
        for(int i = 0;i<n;i++) {
            String gridtest = TEST_DIR + "gridtest"+i+".txt";
            String gridans = ANS_DIR + "gridans"+i+".txt";
            System.out.println("testing "+gridtest+" "+gridans);
            GridWords test = new GridWords(dctfile, gridtest);
            Trie testLookup = test.buildTrieFromDct();
            ArrayList<ArrayList<Character>> gridt = test.buildGridFromFile();
            TreeSet<String> testAns = test.gridWordGenerator(testLookup, gridt);
            test.run();
            TreeSet<String> correctAns = testAnsReader(gridans);
            assert testAns.equals(correctAns);
        }


        String dctfile1 =DIC_DIR+ "englishword_test.txt";
        int i=2;
        String gridtest1 = TEST_DIR + "gridtest"+i+".txt";
        String gridans1 = ANS_DIR + "gridans"+i+".txt";
        System.out.println("testing "+gridtest1+" "+gridans1);
        GridWords test = new GridWords(dctfile1, gridtest1);
        Trie testLookup = test.buildTrieFromDct();
        ArrayList<ArrayList<Character>> gridt = test.buildGridFromFile();
        TreeSet<String> testAns = test.gridWordGenerator(testLookup, gridt);
        test.run();
        TreeSet<String> correctAns = testAnsReader(gridans1);
        assert testAns.equals(correctAns);





    }
    public TreeSet<String > testAnsReader(String filename){
        TreeSet<String> ans = new TreeSet<String>();
        try {
            BufferedReader infile =  new BufferedReader( new FileReader(filename));
            System.out.println(filename);
            String line = infile.readLine();
            while(line!=null){
                ans.add(line.toLowerCase().trim());
                line = infile.readLine();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return ans;

    }

}