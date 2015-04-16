/**
 * Created by rui on 4/14/15.
 */
public class TrieNode {
    private  TrieNode[] children;
    private boolean isValid;
    public TrieNode(){
        this.children = new TrieNode[26];
    }
    public TrieNode addChild(char c){
        int val = c-'a';
        if (c<'a' || c>'z') return null;
        if(this.children[val]==null){
            this.children[val]= new TrieNode();
        }
        //System.out.println("add children "+ c);
        return children[c-'a'];
    }
    public boolean isValid(){
        return isValid;
    }
    public void setValid(boolean isValid){
        this.isValid = isValid;
    }
    public TrieNode getChild(char c){
        return this.children[c-'a'];
    }
}
