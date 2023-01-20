import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

public class BoggleSolver {
    private Node root;
    private int col, row;
    private boolean[][] marked;
    private BoggleBoard board;
    private SET<String> result;
    private Stack<Tile>[][] adj;
    private static int R = 26;


    private static class Node {
        private boolean word;
        private Node[] next = new Node[R];
    }

    private void addWord(String word) {
        if (word == null) throw new IllegalArgumentException("");

        root = add(root, word, 0);
    }

    private Node add(Node x, String word, int d) {
        if (x == null) x = new Node();
        if (d == word.length()) x.word = true;
        else {
            char c = word.charAt(d);
            x.next[c - 'A'] = add(x.next[c - 'A'], word, d + 1);
        }
        return x;
    }

    private boolean contains(String word) {
        Node x = get(root, word, 0);
        if (x == null) return false;
        return x.word;
    }

    private Node get(Node x, String word, int d) {
        if (x == null) return null;
        if (d == word.length()) return x;
        char c = word.charAt(d);
        return get(x.next[c - 'A'], word, d + 1);
    }

    private boolean keysWithPrefix(String word) {
        Node x = get(root, word, 0);
        if (x == null) return false;
        if (x.next == null) return false;
        return true;
    }

    private class Tile {
        private int i, j;

        public Tile(int i, int j) {
            this.i = i;
            this.j = j;
        }
    }


    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        for (int i = 0; i < dictionary.length; i++) {
            addWord(dictionary[i]);
        }
        SET<String> s = new SET<>();

    }


    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        row = board.rows();
        col = board.cols();
        this.board = board;

        precompute();
        result = new SET<>();

        //do a depth first search on all the dices to find words
        //i is row, x is col
        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {


                marked = new boolean[row][col]; // new marked for each dice
                //dfs to all adjacent sites
                dfs(i, j, "" + board.getLetter(i, j));
            }
        }
        return result;
    }

    private void dfs(int i, int j, String prefix) {
        if (prefix.length() > 0) {
            if (prefix.charAt(prefix.length() - 1) == 'Q') prefix = prefix + 'U';
        }
        //if current string exists in dict, and it is longer than 3, append it
        if (prefix.length() > 0) {
            if (contains(prefix) && prefix.length() >= 3 && !result.contains(prefix)) result.add(prefix);
        }
        marked[i][j] = true;
        for (Tile t : (Stack<Tile>) adj[i][j]) {
            int y = t.i;
            int x = t.j;
            if (checkCo(y, x)) {
                if (!marked[y][x]) {
                    char c = board.getLetter(y, x);
                    if (prefix.length() == 0) dfs(y, x, prefix + c);
                    else if (keysWithPrefix(prefix))
                        dfs(y, x, prefix + c);
                }
            }
        }

        marked[i][j] = false;
    }


    private boolean checkCo(int i, int j) {
        if (i < 0 || i >= row) return false;
        if (j < 0 || j >= col) return false;
        return true;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!contains(word)) return 0;
        if (word.length() == 3 || word.length() == 4) return 1;
        if (word.length() == 5) return 2;
        if (word.length() == 6) return 3;
        if (word.length() == 7) return 5;
        if (word.length() >= 8) return 11;
        return 0;
    }

    private void precompute() {
        adj = (Stack<Tile>[][]) new Stack[row][col];

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {

                adj[i][j] = new Stack<>();
            }
        }

        for (int i = 0; i < row; i++) {
            for (int j = 0; j < col; j++) {
                for (int y = i - 1; y <= i + 1; y++) {
                    for (int x = j - 1; x <= j + 1; x++) {
                        adj[i][j].push(new Tile(y, x));
                    }
                }
            }
        }
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        for (String word : solver.getAllValidWords(board)) {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
}
