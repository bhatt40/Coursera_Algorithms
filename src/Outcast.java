/**
 * Created by rishee on 11/10/14.
 */
public class Outcast {

    private WordNet wordnet;

    public Outcast(WordNet wordnet) {        // constructor takes a WordNet object
        this.wordnet = wordnet;
    }

    public String outcast(String[] nouns) {  // given an array of WordNet nouns, return an outcast

        int longestDistance = -1;
        int longestDistanceIndex = -1;
        int distance = 0;

        for (int a = 0; a < nouns.length; a++) {
            distance = 0;

            for (int b = 0; b < nouns.length; b++) {
                if (a != b) {
                    distance += wordnet.distance(nouns[a], nouns[b]);
                }
            }

            if(distance > longestDistance) {
                longestDistanceIndex = a;
                longestDistance = distance;
            }
        }

        return nouns[longestDistanceIndex];
    }

    public static void main(String[] args) { // see test client below
    }
}
