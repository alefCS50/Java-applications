import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.Topological;

import java.util.LinkedList;

public class WordNet {

    private Digraph G;
    private ST<Integer, String> id_word;
    private ST<String, SET<Integer>> word_ids;
    private int wordCounter = 0;
    private String fields[];
    private String words[];
    private SAP sap;
    private int outCount = 0;
    private boolean[] outEdge;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null) throw new IllegalArgumentException("");
        if (hypernyms == null) throw new IllegalArgumentException("");


        //reading from synsets
        In synset = new In(synsets);
        id_word = new ST<>();
        word_ids = new ST<>();

        while (synset.hasNextLine()) {
            wordCounter++;
            String str = synset.readLine();
            fields = str.split(",");
            int id = Integer.parseInt(fields[0]);
            words = fields[1].split(" ");
            id_word.put(id, fields[1]);
            for (int i = 0; i < words.length; i++) {
                if (word_ids.contains(words[i])) {
                    word_ids.get(words[i]).add(id);
                } else {
                    SET<Integer> tempSet = new SET<>();
                    tempSet.add(id);
                    word_ids.put(words[i], tempSet);

                }
            }
        }


        //read from hypernyms and create graph

        G = new Digraph(wordCounter);
        In hypernym = new In(hypernyms);
        outEdge = new boolean[wordCounter];

        while (hypernym.hasNextLine()) {
            String str = hypernym.readLine();
            String[] parts = str.split(",");
            int id1 = Integer.parseInt(parts[0]);
            for (int i = 1; i < parts.length; i++) {
                int temp = Integer.parseInt(parts[i]);
                G.addEdge(id1, temp);
                outCount++;
            }
        }


        //init new SAP
        sap = new SAP(G);

    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        LinkedList<String> answer = new LinkedList<>();

        for (String word : word_ids) {
            answer.addFirst(word);
        }
        return answer;

    }


    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) throw new IllegalArgumentException("");
        return word_ids.contains(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

        if (nounA == null || nounB == null) throw new IllegalArgumentException("");
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("");


        //get ids
        SET<Integer> nouna = word_ids.get(nounA);
        SET<Integer> nounb = word_ids.get(nounB);


        if (nouna.size() == 1 && nounb.size() == 1) return sap.length(nouna.max(), nounb.max());
        else return sap.length(nouna, nounb);


    }


    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {

        if (nounA == null || nounB == null) throw new IllegalArgumentException("");
        if (!isNoun(nounA) || !isNoun(nounB)) throw new IllegalArgumentException("");

        //get ids
        SET<Integer> nouna = word_ids.get(nounA);
        SET<Integer> nounb = word_ids.get(nounB);

        int id;

        if (nouna.size() == 1 && nounb.size() == 1) {
            id = sap.ancestor(nouna.max(), nounb.max());
        } else {
            id = sap.ancestor(nouna, nounb);
        }

        return id_word.get(id);
    }


    private void isRooted(Digraph gr) {
        if (wordCounter - 1 != outCount) throw new IllegalArgumentException("");

        Topological top = new Topological(gr);
        if (!top.hasOrder()) throw new IllegalArgumentException("");

    }


    // do unit testing of this class

}
