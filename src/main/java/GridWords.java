import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * Created by rui on 4/14/15.
 */
public class GridWords {
    private final String dictFile;
    private final String gridFile;
    private static final Coordinate[] MOVES = {
            new Coordinate(0,1), new Coordinate(0,-1),
            new Coordinate(1,0), new Coordinate(-1,0),
            new Coordinate(1,1), new Coordinate(1,-1),
            new Coordinate(-1,1), new Coordinate(-1,-1),

    };

    public GridWords(String dictFile, String gridFile) {
        this.dictFile=dictFile;
        this.gridFile = gridFile;
    }
    private static BufferedReader createFileReader(String fileName){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new BufferedReader(fileReader);
    }

    private ArrayList<Character> parseLine(String line ){
        StringTokenizer st = new StringTokenizer(line);
        ArrayList<Character> parsed = new ArrayList<Character>();
        int i = 0;
        while (st.hasMoreTokens()){
            String tmp = st.nextToken().toLowerCase();
            Character c = tmp.charAt(0);
            parsed.add(c);
        }
        return parsed;
    }
    protected ArrayList<ArrayList<Character>> buildGridFromFile() throws IOException {
        ArrayList<ArrayList<Character>> grid = new ArrayList<ArrayList<Character>>();
        BufferedReader infile = createFileReader(this.gridFile);
        String dm = infile.readLine();
        String line = infile.readLine();
        while (line != null){
            ArrayList<Character> parsed = parseLine(line);
            grid.add(parsed);
            line = infile.readLine();
        }

        return grid;
    }


    protected Trie buildTrieFromDct() {
        Trie head = new Trie();
        BufferedReader infile = createFileReader(dictFile);
        try {
            String word = infile.readLine();

            while(word != null){
                head.buildTrie(word.toLowerCase().trim());
                word = infile.readLine();
            }
            if (infile!=null){
                infile.close();}
        } catch (IOException e) {
            e.printStackTrace();
        }

        return head;
    }
    protected TreeSet<String> gridWordGenerator(Trie head, ArrayList<ArrayList<Character>> grid){
        Trie lookup = buildTrieFromDct();
        TreeSet<String> ans= new TreeSet<String>();
        int rn = grid.size();
        if(rn==0){return ans;}
        int cn = grid.get(0).size();
        for(int i =0; i<rn; i++){
            for(int j =0; j<cn;j++){
                Coordinate coord = new Coordinate(i,j);
                SearchNode startCell = new SearchNode(coord,1);
                ArrayList<String> ansWords = search(grid,startCell,lookup);
                for(String word: ansWords){
                    ans.add(word);
                }
            }
        }
        return ans;
    }
    protected static ArrayList<String> search(ArrayList<ArrayList<Character>> grid, SearchNode start, Trie lookup){
        ArrayList<String> ans = new ArrayList<String>();
        int r  = grid.size();
        int c = grid.get(0).size();
        Stack<SearchNode> stack = new Stack<SearchNode>();
        stack.push(start);
        Path pathnodes = new Path();
        while(!stack.isEmpty()){
            SearchNode curCell = stack.pop();
            Coordinate curCoord = curCell.coord;
            char curChar = grid.get(curCoord.x).get(curCoord.y);
            while(!pathnodes.isEmpty() && pathnodes.peek().depth>=curCell.depth){
                pathnodes.pop();
            }
            String curWord = pathnodes.getPrefix()+curChar;
            boolean[] checks = lookup.contains(curWord);
            if(checks[0]){
                if(checks[1]) ans.add(curWord);
                for(Coordinate p : MOVES){
                    SearchNode nextCell = curCell.update(p);
                    if(nextCell.isOutBound(r)){
                        if(!pathnodes.contain(nextCell)) {
                            stack.push(nextCell);
                        }
                    }
                }
            }
            if(pathnodes.isEmpty() || pathnodes.peek().depth==curCell.depth-1){
                pathnodes.push(curCell,curChar);
            }

        }
        return ans;
    }


    private static class Path{
        private final HashSet<Coordinate> existnodes = new HashSet<Coordinate>();
        private final Stack<SearchNode>pathnodes = new Stack<SearchNode>();
        private final StringBuilder prefix= new StringBuilder();
        public boolean isEmpty(){
            return pathnodes.isEmpty();
        }
        public void push(SearchNode node, char c){
            pathnodes.add(node);
            prefix.append(c);
            existnodes.add(node.coord);
        }
        public SearchNode pop(){
            SearchNode popedNode= pathnodes.pop();
            existnodes.remove(popedNode.coord);
            prefix.deleteCharAt(prefix.length()-1);
            return popedNode;
        }
        public SearchNode peek(){
            return pathnodes.peek();
        }
        public boolean contain(SearchNode target){
            return existnodes.contains(target.coord);
        }
        public String getPrefix(){
            return prefix.toString();
        }
    }

    private static class SearchNode{
        private final Coordinate coord;
        private final int depth;
        public SearchNode(Coordinate coord, int depth){
            this.coord = coord;
            this.depth = depth;
        }
        public SearchNode update(Coordinate deltaCoordinate){
            Coordinate movedCoord = this.coord.move(deltaCoordinate);
            return new SearchNode(movedCoord,this.depth+1);
        }
        public boolean isOutBound(int n){
            return coord.isOutBound(n);
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof SearchNode)) return false;

            SearchNode that = (SearchNode) o;

            if (depth != that.depth) return false;
            if (!coord.equals(that.coord)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = coord.hashCode();
            result = 31 * result + depth;
            return result;
        }
    }
    public static class Coordinate {
        private final int x,y;
        public Coordinate(int r, int c){
            this.x =r;
            this.y =c;
        }
        public Coordinate move(Coordinate deltaCoordinate){
            return new Coordinate(this.x+ deltaCoordinate.x,this.y+ deltaCoordinate.y);
        }
        public boolean isOutBound(int n){
            return this.x<n && this.x>=0 && this.y<n && this.y>=0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Coordinate)) return false;

            Coordinate coordinate = (Coordinate) o;

            if (x != coordinate.x) return false;
            if (y != coordinate.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }

    public void run() throws IOException {
        ArrayList<ArrayList<Character>> grid = buildGridFromFile();
        Trie lookup = buildTrieFromDct();
        TreeSet<String> ans = gridWordGenerator(lookup,grid);
        System.out.println(ans.size());
        for(String s : ans){
            System.out.println(s);
        }
    }


}
