/**
 * Created by rui on 4/14/15.
 */
public class Trie {
    public TrieNode head;
    public Trie(){

        this.head = new TrieNode();
    }
    public void buildTrie(String ValidWord){
        TrieNode node = this.head;
        for(char c : ValidWord.toCharArray()){
            node = node.addChild(c);
            if(node ==null)return;
        }
        node.setValid(true);
    }
    public boolean[] contains(String Word){
        TrieNode node = this.head;
        boolean[] ret = new boolean[2];
        for(char w : Word.toCharArray()){

            node = node.getChild(w);
            if(node ==null)return ret;
        }
        ret[0]=true;
        ret[1]=node.isValid();
        return ret;
    }


}
