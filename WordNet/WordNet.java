/* *****************************************************************************
 *  Name: Koh Jun Jie
 *  Date: 5th September 2021
 *  Description: Immutable data type WordNet
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.HashMap;

public class WordNet {
    private HashMap<Integer, String> stSynsets;
    private ST<String, ArrayList<Integer>> stNouns;
    private HashMap<Integer, int[]> stHypernyms;
    private int numSynsets;
    private SAP sap;

    /**
     * Constructor that takes the name of the two input files
     *
     * @param synsets   name of the synsets input file
     * @param hypernyms name of the hypernyms input file
     */
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new java.lang.IllegalArgumentException(
                    "Arguments to constructor cannot be null.");
        }

        constructSynsets(synsets);
        constructHypernyms(hypernyms);
        numSynsets = stSynsets.size();

        Digraph g = new Digraph(stSynsets.size());
        Iterable<Integer> k = stSynsets.keySet();
        for (int i : k) {
            if (!stHypernyms.containsKey(i)) {
                throw new IllegalArgumentException("Invalid ");
            }
            int[] a = stHypernyms.get(i);
            for (int j : a) {
                g.addEdge(i, j);
            }
        }

        DirectedCycle dc = new DirectedCycle(g);
        if (dc.hasCycle()) {
            throw new java.lang.IllegalArgumentException("Digraph has cycles!");
        }

        int rootCount = 0;
        for (int i = 0; i < g.V(); i++) {
            if (g.outdegree(i) == 0) {
                rootCount++;
            }
        }
        if (rootCount > 1) {
            throw new java.lang.IllegalArgumentException("Digraph is not rooted!");
        }

        sap = new SAP(g);
    }

    /**
     * Private method to construct symbol tables to represent synsets
     *
     * @param synsets the name of the text file containing the synsets
     */
    private void constructSynsets(String synsets) {
        stSynsets = new HashMap<Integer, String>();
        stNouns = new ST<String, ArrayList<Integer>>();
        In in = new In(synsets);

        while (!in.isEmpty()) {
            String text = in.readLine();
            String[] a = text.split(",");
            String[] nouns = a[1].split(" ");
            stSynsets.put(Integer.parseInt(a[0]), a[1]);

            for (String s : nouns) {
                // StdOut.println(s);
                if (stNouns.contains(s)) {
                    // StdOut.println("hello");
                    stNouns.get(s).add(Integer.parseInt(a[0]));
                }
                else {
                    stNouns.put(s, new ArrayList<Integer>());
                    stNouns.get(s).add(Integer.parseInt(a[0]));
                }
            }
        }
    }

    /**
     * Private method to construct symbol tables to represent hypernyms
     *
     * @param hypernyms the name of the text file containing the hypernyms
     */
    private void constructHypernyms(String hypernyms) {
        stHypernyms = new HashMap<>();
        In in2 = new In(hypernyms);

        while (!in2.isEmpty()) {
            String[] b = in2.readLine().split(",");
            int key = Integer.parseInt(b[0]);
            int[] valueArr = new int[b.length - 1];
            for (int i = 1; i < b.length; i++) {
                valueArr[i - 1] = Integer.parseInt(b[i]);
            }
            stHypernyms.put(key, valueArr);
        }
    }

    /**
     * Returns all WordNet nouns
     *
     * @return an Iterable containing all WordNet nouns
     */
    public Iterable<String> nouns() {
        return stNouns.keys();
    }

    /**
     * Check if the word is a WordNet noun
     *
     * @param word string to be checked
     * @return boolean
     */
    public boolean isNoun(String word) {
        if (word == null) {
            throw new java.lang.IllegalArgumentException("Argument cannot be null!");
        }
        return stNouns.contains(word);
    }

    /**
     * Method to calculate the shortest distance between two nouns
     *
     * @param nounA the first noun
     * @param nounB the second noun
     * @return the distance between the two nouns or -1 if no path exists between the two nouns
     */
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException("Argument is not a WordNet noun!");
        }

        ArrayList<Integer> intOfNounA = stNouns.get(nounA);
        ArrayList<Integer> intOfNounB = stNouns.get(nounB);

        return sap.length(intOfNounA, intOfNounB);
    }

    /**
     * Method to calculate the shortest common ancestor between the two nouns
     *
     * @param nounA the first noun
     * @param nounB the second noun
     * @return the shortest common ancestor between the two nouns
     */
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new java.lang.IllegalArgumentException("Argument is not a WordNet noun!");
        }

        ArrayList<Integer> intOfNounA = stNouns.get(nounA);
        ArrayList<Integer> intOfNounB = stNouns.get(nounB);
        int intOfShortestAncestor = sap.ancestor(intOfNounA, intOfNounB);

        if (intOfShortestAncestor == -1) {
            return "No path exists between the two nouns!";
        }
        else {
            return stSynsets.get(intOfShortestAncestor);
        }
    }

    /**
     * Getter method to get the number of Synsets
     *
     * @return the number of synsets
     */
    private int numOfSynsets() {
        return numSynsets;
    }

    public static void main(String[] args) {
        /* Code for testing purposes. */
        String synsets = args[0];
        String hypernyms = args[1];
        String a = args[2];
        String b = args[3];

        WordNet wn = new WordNet(synsets, hypernyms);
        StdOut.println("number of synsets: " + wn.numOfSynsets());
        StdOut.println("Distance between " + a + " and " + b + ": " + wn.distance(a, b));
        StdOut.println("Shortest common ancestor of " + a + " and " + b + ": " + wn.sap(a, b));
    }
}
