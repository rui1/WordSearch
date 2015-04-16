import org.junit.Test;

import java.util.AbstractCollection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import static org.junit.Assert.*;

public class GridWordsTest {

    @Test
    public void testBuildGridFromFile() throws Exception {

    }

    @Test
    public void testBuildTrie() throws Exception {
        Trie test = new Trie();
        String word1 = "abc";
        test.buildTrie(word1);
        boolean[] ans1 = {true,true};
        boolean[] ans2 = {true,false};
        boolean[] ans3 = {true,false};
        boolean[] ans4 = {false,false};
        boolean[] ans5 = {false,false};
        System.out.println(test.contains("abc"));
        assert(test.contains("abc")==ans1);
        assert(test.contains("a")==ans2);
        assert(test.contains("ab")==ans3);
        assert(test.contains("ac")==ans4);
        assert(test.contains("")==ans5);
    }

    @Test
    public void testGridWordGenerator() throws Exception {

    }

    @Test
    public void testDfsWords() throws Exception {

        ArrayList<String> correct = new ArrayList<String>();
        correct.add("abc");

        Trie test = new Trie();
        String word1 = "abc";
        test.buildTrie(word1);
        ArrayList<ArrayList<Character>> grid= new ArrayList<ArrayList<Character>>();
        ArrayList<Character> line1 = new ArrayList(Arrays.asList('a','b'));
        grid.add(line1);
        ArrayList<Character> line2 = new ArrayList(Arrays.asList('a','c'));
        grid.add(line2);
        GridWords.Pair coord=new GridWords.Pair(0,0);
        HashSet<GridWords.Pair> pathnode = new HashSet<GridWords.Pair>();
        GridWords.SearchState start = new GridWords.SearchState(coord,"",pathnode);
        ArrayList<String> ans = GridWords.dfsWords(grid,start,test);
        assert(ans.equals(correct));

    }
}