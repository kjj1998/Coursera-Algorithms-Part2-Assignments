/* *****************************************************************************
 *  Name: Koh Jun Jie
 *  Date: 6th September 2021
 *  Description: An immutable data type SAP
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class SAP {

    private Digraph G;      // instance variable for Digraph

    /**
     * Constructor for SAP
     *
     * @param G the Digraph to search in
     */
    public SAP(Digraph G) {
        if (G == null)
            throw new java.lang.IllegalArgumentException("Digraph argument cannot be null!");
        this.G = new Digraph(G);
    }

    /**
     * Compute length of shortest ancestral path between v and w.
     * 1. Apply BFS on both v and w
     * 2. Loop through all vertices in G and pick out common vertices that both v and w have paths
     * to
     * 3. Sum the distances from v and w to each of the common vertices respectively
     * 4. Return the shortest summation
     *
     * @param v the first vertex to be checked
     * @param w the second vertex to be checked
     * @return length of shortest ancestral path between v and w or -1 if no such path exists
     */
    public int length(int v, int w) {
        if (((v < 0) || (v > G.V())) || ((w < 0) || (w > G.V()))) {
            throw new java.lang.IllegalArgumentException("vertex out of range!");
        }
        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        ArrayList<Integer> arrList = new ArrayList<>();

        for (int i = 0; i < G.V(); i++) {
            if ((bfsV.hasPathTo(i)) && (bfsW.hasPathTo(i))) {
                arrList.add(i);
            }
        }

        if (arrList.size() == 0)
            return -1;

        int shortestLength = Integer.MAX_VALUE;

        for (Integer i : arrList) {
            int tempLength = bfsV.distTo(i) + bfsW.distTo(i);
            if (tempLength < shortestLength) {
                shortestLength = tempLength;
            }
        }

        return shortestLength;
    }

    /**
     * Same code as length(int v, int w) except now we return the shortest common ancestor instead
     * of the length of the shortest ancestral path;
     *
     * @param v the first vertex to be checked
     * @param w the second vertex to be checked
     * @return the shortest common ancestor of v and w or -1 if no such ancestor exists
     */
    public int ancestor(int v, int w) {
        if (((v < 0) || (v > G.V())) || ((w < 0) || (w > G.V()))) {
            throw new java.lang.IllegalArgumentException("vertex out of range!");
        }

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        ArrayList<Integer> arrList = new ArrayList<>();

        for (int i = 0; i < G.V(); i++) {
            if ((bfsV.hasPathTo(i)) && (bfsW.hasPathTo(i))) {
                arrList.add(i);
            }
        }

        if (arrList.size() == 0)
            return -1;

        int shortestLength = Integer.MAX_VALUE;
        int shortestCommonAncestor = 0;
        for (Integer i : arrList) {
            int tempLength = bfsV.distTo(i) + bfsW.distTo(i);
            if (tempLength < shortestLength) {
                shortestLength = tempLength;
                shortestCommonAncestor = i;
            }
        }

        return shortestCommonAncestor;
    }

    /**
     * Private method to check that an iterable of vertices are not null and not out of range
     *
     * @param v the Iterable of vertices to be checked
     * @return the boolean value
     */
    private boolean checkIterableVertex(Iterable<Integer> v) {
        int count = 0;
        for (Integer i : v) {
            if (i == null) {
                throw new java.lang.IllegalArgumentException("null vertex!");
            }
            else if ((i < 0) || (i > G.V())) {
                throw new java.lang.IllegalArgumentException("vertex out of range!");
            }
            count++;
        }
        if (count == 0) {
            return false;
        }
        else
            return true;
    }

    /**
     * Method to compute the shortest ancestral paths among the vertices in two Iterables
     *
     * @param v first Iterable of vertices
     * @param w second Iterable of vertices
     * @return the length of the shortest ancestral path
     */
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new java.lang.IllegalArgumentException("Iterable argument cannot be null!");

        if (!checkIterableVertex(v))
            return -1;
        if (!checkIterableVertex(w))
            return -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        ArrayList<Integer> arrList = new ArrayList<>();

        for (int i = 0; i < G.V(); i++) {
            if ((bfsV.hasPathTo(i)) && (bfsW.hasPathTo(i))) {
                arrList.add(i);
            }
        }

        if (arrList.size() == 0)
            return -1;

        int shortestLength = Integer.MAX_VALUE;
        for (Integer i : arrList) {
            int tempLength = bfsV.distTo(i) + bfsW.distTo(i);
            if (tempLength < shortestLength) {
                shortestLength = tempLength;
            }
        }

        return shortestLength;
    }

    /**
     * To compute the shortest common ancestor among the vertices in two Iterables
     *
     * @param v the first Iterable of vertices
     * @param w the second Iterable of vertices
     * @return the shortest common ancestor or -1 if such ancestor don't exist or the Iterables
     * submitted have errors
     */
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null)
            throw new java.lang.IllegalArgumentException("Iterable argument cannot be null!");

        if (!checkIterableVertex(v))
            return -1;
        if (!checkIterableVertex(w))
            return -1;

        BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(G, v);
        BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(G, w);
        ArrayList<Integer> arrList = new ArrayList<>();

        for (int i = 0; i < G.V(); i++) {
            if ((bfsV.hasPathTo(i)) && (bfsW.hasPathTo(i))) {
                arrList.add(i);
            }
        }

        if (arrList.size() == 0)
            return -1;

        int shortestLength = Integer.MAX_VALUE;
        int shortestCommonAncestor = 0;
        for (Integer i : arrList) {
            int tempLength = bfsV.distTo(i) + bfsW.distTo(i);
            if (tempLength < shortestLength) {
                shortestLength = tempLength;
                shortestCommonAncestor = i;
            }
        }

        return shortestCommonAncestor;
    }

    public static void main(String[] args) {
        /* Code for testing purposes */
        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);

        while (!StdIn.isEmpty()) {

            String v = StdIn.readLine();
            String w = StdIn.readLine();
            String[] v2 = v.split(" ");
            String[] w2 = w.split(" ");


            ArrayList<Integer> v3 = new ArrayList<>();

            for (int i = 0; i < v2.length; i++) {
                v3.add(Integer.parseInt(v2[i]));
            }

            ArrayList<Integer> w3 = new ArrayList<>();

            for (int i = 0; i < w2.length; i++) {
                w3.add(Integer.parseInt(w2[i]));
            }
            int length = sap.length(v3, w3);
            int ancestor = sap.ancestor(v3, w3);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}
