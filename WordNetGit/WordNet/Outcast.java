import java.util.HashMap;

public class Outcast {
    private final WordNet wn;

    public Outcast(WordNet wordnet) {
        if (wordnet == null) throw new IllegalArgumentException("");
        this.wn = wordnet;

    }


    // given an array of WordNet nouns, return an outcast
    public String outcast(String[] nouns) {
        if (nouns == null) throw new IllegalArgumentException("");
        HashMap<Integer, String> count_noun = new HashMap<>();

        for (String str : nouns) {
            int counter = 0;
            for (String noun : nouns) {
                counter += wn.distance(str, noun);
            }
            count_noun.put(counter, str);
        }
        int greatestValue = 0;
        for (int i : count_noun.keySet()) {
            if (i > greatestValue) greatestValue = i;
        }

        return count_noun.get(greatestValue);
    }


}
