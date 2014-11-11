import java.util.ArrayList;

/**
 * Created by rishee on 11/10/14.
 */
public class WordNet{

    private SAP sap;
    private ArrayList<String> synsetArrayList;
    private BinarySearchST<String, Bag<Integer>> synST;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {

        synsetArrayList = new ArrayList<String>();
        synST = new BinarySearchST<String, Bag<Integer>>();
        int maxId = 0;
        String[] fields;

        // Read synsets file and put in array of Strings
        In synsetsIn = new In(synsets);
        while(!synsetsIn.isEmpty()) {
            fields = synsetsIn.readLine().split(",");
            synsetArrayList.add(fields[1]);
            if (Integer.decode(fields[0]) > maxId)
                maxId = Integer.decode(fields[0]);

            // Add each syn to the symbol table for lookup
            Bag<String> syns = new Bag<String>();
            for (String s : fields[1].split(" ")) {
                // If symbol table contains key with this noun, add it to existing bag
                if (synST.contains(s)) {
                    synST.get(s).add(Integer.decode(fields[0]));
                } else {
                    Bag<Integer> bag = new Bag<Integer>();
                    bag.add(Integer.decode(fields[0]));
                    synST.put(s, bag);
                }
            }
        }

        Digraph digraph = new Digraph(maxId + 1);

        boolean[] hasAdj = new boolean[maxId + 1];
        for (boolean a : hasAdj)
            a = false;

        In hypernymsIn = new In(hypernyms);
        while(!hypernymsIn.isEmpty()) {
            fields = hypernymsIn.readLine().split(",");
            if (fields.length > 1)
                hasAdj[Integer.decode(fields[0])] = true;
            for (int a = 1; a < fields.length; a++)
                digraph.addEdge(Integer.decode(fields[0]), Integer.decode(fields[a]));
        }

        int rootCount = 0;
        for (boolean a : hasAdj) {
            if (!a)
                rootCount++;
        }

        // Check that digraph is rooted
        if (rootCount != 1)
            throw new IllegalArgumentException();

        DirectedCycle dc = new DirectedCycle(digraph);
        if (dc.hasCycle())
            throw new IllegalArgumentException();

        this.sap = new SAP(digraph);
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        Bag<String> nouns = new Bag<String>();

        for (String s : synST.keys())
            nouns.add(s);

        return nouns;
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {

        return (synST.contains(word));
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {

        Iterable<Integer> v = this.findWord(nounA);
        Iterable<Integer> w = this.findWord(nounB);

        return this.sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {

        Iterable<Integer> v = this.findWord(nounA);
        Iterable<Integer> w = this.findWord(nounB);

        int ancestor = this.sap.ancestor(v, w);

        return synsetArrayList.get(ancestor);
    }

    private Iterable<Integer> findWord(String word) {

        if (!synST.contains(word))
            throw new IllegalArgumentException();

        return synST.get(word);
    }

    // do unit testing of this class
    public static void main(String[] args) {

        WordNet wordnet = new WordNet(args[0], args[1]);
        String v = StdIn.readString();
        String w = StdIn.readString();
        int distance = wordnet.distance(v, w);
        String sap = wordnet.sap(v, w);
        StdOut.printf("distance = %s, sap = %s\n", distance, sap);

    }
}