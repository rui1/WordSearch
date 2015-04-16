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
    private static final Pair[] MOVES = {
            new Pair(0,1), new Pair(0,-1),
            new Pair(1,0), new Pair(-1,0),
            new Pair(1,1), new Pair(1,-1),
            new Pair(-1,1), new Pair(-1,-1),

    };

    public GridWords(String dictFile, String gridFile) {
        this.dictFile=dictFile;
        this.gridFile = gridFile;
    }
    private static BufferedReader createFileReader(String fileName){
        FileReader fileReader = null;
        try {
            fileReader = new FileReader(fileName);
            System.out.println(fileName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return new BufferedReader(fileReader);
    }

    public ArrayList<Character> parseLine(String line ){
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
    public ArrayList<ArrayList<Character>> buildGridFromFile() throws IOException {
        ArrayList<ArrayList<Character>> grid = new ArrayList<ArrayList<Character>>();
        BufferedReader infile = createFileReader(this.gridFile);
        String dm = infile.readLine();
        String line = infile.readLine();
        while (line != null){
            ArrayList<Character> parsed = parseLine(line);
            System.out.println(line.toString());
            grid.add(parsed);
            line = infile.readLine();
        }

        return grid;
    }


    public Trie buildTrieFromDct() {
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
    public TreeSet<String> gridWordGenerator(Trie head, ArrayList<ArrayList<Character>> grid){
        Trie lookup = buildTrieFromDct();
        TreeSet<String> ans= new TreeSet<String>();
        int rn = grid.size();
        int cn = grid.get(0).size();
        for(int i =0; i<rn; i++){
            for(int j =0; j<cn;j++){
                String prefix = "";
                Pair coord = new Pair(i,j);
                HashSet<Pair> pathnode = new HashSet<Pair>();
                SearchState startCell = new SearchState(coord,prefix,pathnode);
                System.out.println("start cell"+ startCell.prefix+" "+startCell.coord.x+" "+startCell.coord.y);
                ArrayList<String> ansWords = dfsWords(grid,startCell,lookup);
                System.out.println("current ans"+ ansWords.toString());
                for(String word: ansWords){
                    ans.add(word);
                }

            }
        }
        return ans;
    }

    public static ArrayList<String> dfsWords(ArrayList<ArrayList<Character>> grid, SearchState start,Trie lookup){
        ArrayList<String> ans = new ArrayList<String>();
        int r = grid.size();
        int c = grid.get(0).size();
        System.out.println("row, column"+r+c);
        boolean[][] visited = new boolean[r][c];
        visited[start.coord.x][start.coord.y]=true;
        Stack<SearchState> stack = new Stack<SearchState>();
        stack.push(start);
        while(!stack.isEmpty()){
            SearchState curCell = stack.pop();
            System.out.println("current cell"+ curCell.prefix+" "+curCell.coord.x+" "+curCell.coord.y+" "+grid.get(curCell.coord.x).get(curCell.coord.y));
            Pair curCoord = curCell.coord;
            String  curWord = curCell.prefix+grid.get(curCoord.x).get(curCoord.y);

            System.out.println("if statement "+lookup.contains(curWord)+" "+ curWord);
            boolean[]checks = lookup.contains(curWord);
            if(checks[0]){
                if (checks[1]) ans.add(curWord);
                for(Pair p : MOVES){
                    Pair nextCoord = curCoord.move(p);
                    HashSet<Pair> pathnode = new HashSet<Pair>(curCell.pathnode);
                    pathnode.add(curCoord);
                    if(nextCoord.isOutBound(r)){
                        System.out.println("next " + nextCoord.x + ' ' + nextCoord.y);
                        if(!pathnode.contains(nextCoord)) {
                            System.out.println("not in, next coord "+nextCoord.x +' '+nextCoord.y);
                            SearchState nextCell = new SearchState(nextCoord, curWord, pathnode);
                            stack.push(nextCell);
                        }
                    }
                }
            }

        }
        return  ans;

    }
    public static class Pair {
        private final int x,y;
        public Pair(int r, int c){
            this.x =r;
            this.y =c;
        }
        public Pair move(Pair deltaPair){
            return new Pair(this.x+deltaPair.x,this.y+deltaPair.y);
        }
        public boolean isOutBound(int n){
            return this.x<n && this.x>=0 && this.y<n && this.y>=0;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof Pair)) return false;

            Pair pair = (Pair) o;

            if (x != pair.x) return false;
            if (y != pair.y) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = x;
            result = 31 * result + y;
            return result;
        }
    }
    public static class SearchState {
        public final Pair coord;
        public final String prefix;
        public final HashSet<Pair> pathnode;

        public SearchState(Pair coord, String prefix,HashSet<Pair> pathnode) {
            this.coord = coord;
            this.prefix = prefix;
            this.pathnode = pathnode;
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

    public static void main(String[] args) throws IOException {
        long tic = System.currentTimeMillis();
        String dctfile = "englishwords.txt";
        String gridfile = "grid.txt";
        GridWords problem = new GridWords(dctfile,gridfile);
        problem.run();
        long tac = System.currentTimeMillis();
        System.out.println((tac-tic)/1000);
    }



}
