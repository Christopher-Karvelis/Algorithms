/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.Topological;

import java.util.HashMap;
import java.util.HashSet;


public class WordNet {

    private final Digraph net;
    private final SAP sap;
    private final HashMap<Integer, String> synsetsMap;
    private final HashMap<String, HashSet<Integer>> mapSynset;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        In in = new In(synsets);
        synsetsMap = new HashMap<>();
        mapSynset = new HashMap<>();
        int v = 0;
        while (in.hasNextLine()) {
            String[] data = in.readLine().split(",");
            if (!synsetsMap.containsKey(Integer.parseInt(data[0]))) {
                v++;
            }
            synsetsMap.put(Integer.parseInt(data[0]), data[1]);
            String[] nouns = data[1].split(" ");
            for (int i = 0; i < nouns.length; i++) {
                if (!mapSynset.containsKey(nouns[i])) {
                    mapSynset.put(nouns[i], new HashSet<>());
                }
                mapSynset.get(nouns[i]).add(Integer.parseInt(data[0]));
            }
        }
        net = new Digraph(v);
        In in2 = new In(hypernyms);
        while (in2.hasNextLine()) {
            String[] data = in2.readLine().split(",");
            for (int i = 1; i < data.length; i++) {
                if (!inRange(Integer.parseInt(data[0])) || !inRange(Integer.parseInt(data[i]))) {
                    throw new IllegalArgumentException();
                }
                net.addEdge(Integer.parseInt(data[0]), Integer.parseInt(data[i]));
            }
        }
        if (!isRootedDAG()) {
            throw new IllegalArgumentException();
        }
        sap = new SAP(net);
    }

    private boolean isRootedDAG() {
        int numOfRoots = 0;
        for (int i = 0; i < net.V(); i++) {
            if (net.outdegree(i) == 0) {
                numOfRoots++;
            }
        }
        Topological topologicalSort = new Topological(net);

        if (topologicalSort.hasOrder() && numOfRoots == 1) {
            return true;
        }
        return false;
    }

    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return mapSynset.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return mapSynset.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        HashSet<Integer> v = mapSynset.get(nounA);
        HashSet<Integer> w = mapSynset.get(nounB);
        return sap.length(v, w);
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
        HashSet<Integer> v = mapSynset.get(nounA);
        HashSet<Integer> w = mapSynset.get(nounB);
        // StdOut.println(v + ", " + w);
        return synsetsMap.get(sap.ancestor(v, w));
    }

    private boolean inRange(int v) {
        return v >= 0 && v < net.V();
    }
    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        StdOut.println(wordNet.distance("mean_distance", "cautery"));
        StdOut.println(wordNet.sap("mean_distance", "cautery"));
    }
    // 47117, 76260
}
