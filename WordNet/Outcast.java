/* *****************************************************************************
 *  Name: Koh Jun Jie
 *  Date: 7th September 2021
 *  Description: Immutable data type Outcast
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast {

    private WordNet wordNet;        // instance variable for WordNet

    /**
     * Constructor
     *
     * @param wordnet the WordNet variable
     */
    public Outcast(WordNet wordnet) {
        this.wordNet = wordnet;
    }

    /**
     * Method to find the outcast among a group of nouns, outcast defined as the noun whose sum of
     * distances from other nouns is the highest
     *
     * @param nouns the group of nouns
     * @return the noun which is the outcast
     */
    public String outcast(String[] nouns) {
        int maxSum = Integer.MIN_VALUE;
        String maxNoun = "";

        for (String s : nouns) {
            int tempSum = 0;
            for (String s2 : nouns) {
                if (!s.equals(s2)) {
                    int dist = wordNet.distance(s, s2);
                    tempSum += dist;
                }
            }
            if (tempSum > maxSum) {
                maxSum = tempSum;
                maxNoun = s;
            }
        }
        return maxNoun;
    }

    public static void main(String[] args) {
        WordNet wordnet = new WordNet(args[0], args[1]);
        Outcast outcast = new Outcast(wordnet);
        for (int t = 2; t < args.length; t++) {
            In in = new In(args[t]);
            String[] nouns = in.readAllStrings();
            StdOut.println(args[t] + ": " + outcast.outcast(nouns));
        }
    }
}
