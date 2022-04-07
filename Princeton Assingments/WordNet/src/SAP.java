/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

public class SAP {
    private final Digraph G;
    private int v1, w1;
    private BFS bfs;
    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        if (!inRange(v) || !inRange(w)) {
            throw new IllegalArgumentException();
        }
        if (v == v1 && w == w1 && bfs != null) {
            return bfs.length;
        }
        bfs = new BFS(v, w);
        v1 = v;
        w1 = w;
        return bfs.length;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        if (!inRange(v) || !inRange(w)) {
            throw new IllegalArgumentException();
        }
        if (v == v1  && w == w1 && bfs != null) {
            return bfs.ancestor;
        }
        bfs = new BFS(v, w);
        v1 = v;
        w1 = w;
        return bfs.ancestor;
    }

    private boolean inRange(int v) {
        return v >= 0 && v < G.V();
    }

    private class BFS {
        private int[] distFromV, distFromW;
        private int ancestor, length;
        private final int v, w;

        private BFS(int v, int w) {
            this.v = v;
            this.w = w;
            distFromV = new int[G.V()];
            distFromW = new int[G.V()];
            length = -1;
            ancestor = -1;
            if (v != w) {
                bfs(G);
            } else {
                ancestor = v;
                length = 0;
            }
        }

        private void bfs(Digraph digraph) {
            // StdOut.println(s);
            boolean[] markedV = new boolean[G.V()];
            boolean[] markedW = new boolean[G.V()];
            Queue<Integer> queueV = new Queue<>();
            Queue<Integer> queueW = new Queue<>();
            queueV.enqueue(v);
            queueW.enqueue(w);
            distFromV[v] = 0;
            distFromW[w] = 0;
             
            markedW[w] = true;
            while (!queueV.isEmpty() || !queueW.isEmpty()) {
                if (!queueV.isEmpty()) {
                    int vertex1 = queueV.dequeue();
                    for (int t: digraph.adj(vertex1)) {
                        if (!markedV[t]) {
                            distFromV[t] = distFromV[vertex1] + 1;
                            markedV[t] = true;
                            // StdOut.println("Checking from V -> " + t);
                            if (markedW[t] && ancestor != -1 && (distFromV[t] + distFromW[t]) < length) {
                                ancestor = t;
                                length = distFromV[t] + distFromW[t];
                            }
                            if (markedW[t] && ancestor == -1) {
                                ancestor = t;
                                length = distFromV[t] + distFromW[t];
                            }
                            queueV.enqueue(t);
                        }
                    }
                }
                if (!queueW.isEmpty()) {
                    int vertex2 = queueW.dequeue();
                    for (int t: digraph.adj(vertex2)) {
                        if (!markedW[t]) {
                            distFromW[t] = distFromW[vertex2] + 1;
                            markedW[t] = true;
                            // StdOut.println("Checking from W -> " + t);
                            if (markedV[t] && ancestor != -1 && (distFromV[t] + distFromW[t]) < length) {
                                ancestor = t;
                                length = distFromV[t] + distFromW[t];
                            }
                            if (markedV[t] && ancestor == -1) {
                                ancestor = t;
                                length = distFromV[t] + distFromW[t];
                            }
                            queueW.enqueue(t);
                        }
                    }
                }
            }
        }
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int minlength = G.E() + 1;
        for (Integer x: v) {
            for (Integer y: w) {
                if (x == null || y == null || !inRange(x) || !inRange(y)) {
                    throw new IllegalArgumentException();
                }
                if (x != v1 || y != w1 || bfs == null) {
                    bfs = new BFS(x, y);
                }
                if (bfs.length != -1 && bfs.length < minlength) {
                    minlength = bfs.length;
                }
            }
        }
        if (minlength != G.E() + 1) {
            return minlength;
        }
        return -1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int minAncestor = -1;
        int minlength = G.E() + 1;
        for (Integer x: v) {
            for (Integer y: w) {
                if (x == null || y == null || !inRange(x) || !inRange(y)) {
                    throw new IllegalArgumentException();
                }
                if (x != v1 || y != w1 || bfs == null) {
                    bfs = new BFS(x, y);
                }
                if (bfs.length != -1 && bfs.length < minlength) {
                    minlength = bfs.length;
                    minAncestor = bfs.ancestor;
                }
            }
        }
        return minAncestor;
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = 13;
        int w = 7;
        int length   = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}