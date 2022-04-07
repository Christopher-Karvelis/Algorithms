/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private int minMoves;
    private Stack<Board> boardStack;
    // find a solution to the initial board (using the A* algorithm)

    private class SearchNode {
        private final SearchNode previousSearchNode;
        private final Board board;
        private final int moves;
        // private final int hammingDistance;
        private final int manhattanDistance;

        private SearchNode(Board board, int moves, SearchNode previousSearchNode) {
            this.board = board;
            this.moves = moves;
            this.previousSearchNode = previousSearchNode;
            // hammingDistance = board.hamming();
            manhattanDistance = board.manhattan();
        }

        // public Comparator<SearchNode> hammingOrder() {
        //     return new ByHamming();
        // }

        public Comparator<SearchNode> manhattanOrder() {
            return new ByManhattan();
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public SearchNode getPreviousSearchNode() {
            return previousSearchNode;
        }

        // public int getHammingDistance() {
        //      return hammingDistance;
        // }

        public int getManhattanDistance() {
             return manhattanDistance;
        }

        private class ByManhattan implements Comparator<SearchNode> {
            public int compare(SearchNode o1, SearchNode o2) {
                if (o1.getManhattanDistance() + o1.getMoves() < o2.getManhattanDistance() + o2.getMoves()) {
                    return -1;
                } else if (o1.getManhattanDistance() + o1.getMoves() > o2.getManhattanDistance() + o2.getMoves()) {
                    return 1;
                }
                return 0;
            }
        }

        // private class ByHamming implements Comparator<SearchNode> {
        //     public int compare(SearchNode o1, SearchNode o2) {
        //         if (o1.getHammingDistance() + o1.getMoves()  < o2.getHammingDistance() + o2.getMoves()) {
        //             return -1;
        //         }
        //         else if (o1.getHammingDistance() + o1.getMoves()  > o2.getHammingDistance() + o2.getMoves()) {
        //             return 1;
        //         }
        //         return 0;
        //     }
        // }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        SearchNode searchNode = new SearchNode(initial, 0, null);
        SearchNode twinSearchNode = new SearchNode(initial.twin(), 0, null);
        MinPQ<SearchNode> priorityQueue = new MinPQ<SearchNode>(searchNode.manhattanOrder());
        MinPQ<SearchNode> twinPriorityQueue = new MinPQ<SearchNode>(searchNode.manhattanOrder());
        while (!searchNode.getBoard().isGoal() && !twinSearchNode.getBoard().isGoal()) {
            Iterable<Board> neibors = searchNode.getBoard().neighbors();
            Iterable<Board> twinNeibors = twinSearchNode.getBoard().neighbors();
            for (Board neibor : neibors) {
                SearchNode newNode = new SearchNode(neibor, searchNode.getMoves() + 1, searchNode);
                if (searchNode.getPreviousSearchNode() != null) {
                    if (!newNode.getBoard().equals(searchNode.getPreviousSearchNode().getBoard())) {
                        priorityQueue.insert(newNode);
                    }
                } else {
                    priorityQueue.insert(newNode);
                }
            }
            for (Board neibor : twinNeibors) {
                SearchNode newNode = new SearchNode(neibor, twinSearchNode.getMoves() + 1, twinSearchNode);
                if (twinSearchNode.getPreviousSearchNode() != null) {
                    if (!newNode.getBoard().equals(twinSearchNode.getPreviousSearchNode().getBoard())) {
                        twinPriorityQueue.insert(newNode);
                    }
                } else {
                    twinPriorityQueue.insert(newNode);
                }
            }
            searchNode = priorityQueue.delMin();
            twinSearchNode = twinPriorityQueue.delMin();
        }

        if (searchNode.getBoard().isGoal()) {
            minMoves = searchNode.getMoves();
            boardStack = new Stack<>();
            while (searchNode != null) {
                boardStack.push(searchNode.getBoard());
                searchNode = searchNode.getPreviousSearchNode();
            }
        } else {
            minMoves = -1;
        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return minMoves >= 0;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        return minMoves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
         if (isSolvable()) {
            return boardStack;
         }
         return null;
    }

    // test client (see below)
    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
