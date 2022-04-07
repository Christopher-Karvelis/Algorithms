/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdRandom;

public class Board {
    private final int dimension;
    private int[][] board;
    private int emptyTilePostion;
    private Board twin;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {
        dimension = tiles.length;
        board = new int[dimension][dimension];
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                board[i][j] = tiles[i][j];
                if (tiles[i][j] == 0) {
                    emptyTilePostion = i * dimension + j + 1;
                }
            }
        }
    }

    // string representation of this board
    public String toString() {
        StringBuilder representation = new StringBuilder();

        representation.append(dimension);
        representation.append("\n");
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                representation.append(board[i][j]);
                representation.append(" ");
            }
            representation.append("\n");
        }
        return representation.toString();
    }

    // board dimension n
    public int dimension() {
        return dimension;
    }

    // number of tiles out of place
    public int hamming() {
        int numberOfTilesOutOfPlace = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] != 0 && board[i][j] - (i * dimension + j + 1) != 0) {
                    numberOfTilesOutOfPlace++;
                }
            }
        }
        return numberOfTilesOutOfPlace;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int manhattanDistance = 0;
        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                if (board[i][j] != 0) {
                    int tile = board[i][j] - 1;
                    int row = tile / dimension;
                    int col = tile % dimension;
                    row -= i;
                    col -= j;
                    manhattanDistance = manhattanDistance + Math.abs(row) + Math.abs(col);
                }
            }
        }
        return manhattanDistance;
    }

    // is this board the goal board?
    public boolean isGoal() {
        if (hamming() == 0) {
            return true;
        }
        return false;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y != null && y.getClass() == this.getClass()) {
            if (((Board) y).dimension != this.dimension) {
                return false;
            }
            for (int i = 0; i < dimension; i++) {
                for (int j = 0; j < dimension; j++) {
                    if (((Board) y).board[i][j] != this.board[i][j]) {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        Queue<Board> neighbors = new Queue<Board>();
        int pos = emptyTilePostion - 1;
        int row = pos / dimension;
        int col = pos % dimension;
        if (row < dimension - 1  && col < dimension - 1 && row > 0 && col > 0) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col - 1);
            neighbors.enqueue(neibor1);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row, col + 1);
            neighbors.enqueue(neibor2);
            Board neibor3 = new Board(board);
            neibor3.swap(row, col, row - 1, col);
            neighbors.enqueue(neibor3);
            Board neibor4 = new Board(board);
            neibor4.swap(row, col, row + 1, col);
            neighbors.enqueue(neibor4);
        } else if (row < dimension - 1 && row > 0 && col == 0) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col + 1);
            neighbors.enqueue(neibor1);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row - 1, col);
            neighbors.enqueue(neibor2);
            Board neibor3 = new Board(board);
            neibor3.swap(row, col, row + 1, col);
            neighbors.enqueue(neibor3);
        } else if (col < dimension -1 && row == 0 && col > 0) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col - 1);
            neighbors.enqueue(neibor1);
            Board neibor3 = new Board(board);
            neibor3.swap(row, col, row + 1, col);
            neighbors.enqueue(neibor3);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row, col + 1);
            neighbors.enqueue(neibor2);
        } else if (row == dimension - 1 && col < dimension -1 && col > 0) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col - 1);
            neighbors.enqueue(neibor1);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row, col + 1);
            neighbors.enqueue(neibor2);
            Board neibor3 = new Board(board);
            neibor3.swap(row, col, row - 1, col);
            neighbors.enqueue(neibor3);
        } else if (row < dimension - 1 && col == dimension -1 && row > 0) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col - 1);
            neighbors.enqueue(neibor1);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row - 1, col);
            neighbors.enqueue(neibor2);
            Board neibor3 = new Board(board);
            neibor3.swap(row, col, row + 1, col);
            neighbors.enqueue(neibor3);
        } else if (row == dimension - 1 && col == dimension -1) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col - 1);
            neighbors.enqueue(neibor1);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row - 1, col);
            neighbors.enqueue(neibor2);
        } else if (row == 0 && col == 0) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col + 1);
            neighbors.enqueue(neibor1);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row + 1, col);
            neighbors.enqueue(neibor2);
        } else if (row == 0 && col == dimension - 1) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col - 1);
            neighbors.enqueue(neibor1);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row + 1, col);
            neighbors.enqueue(neibor2);
        } else if (row == dimension - 1 && col == 0) {
            Board neibor1 = new Board(board);
            neibor1.swap(row, col, row, col + 1);
            neighbors.enqueue(neibor1);
            Board neibor2 = new Board(board);
            neibor2.swap(row, col, row - 1, col);
            neighbors.enqueue(neibor2);
        }
        return neighbors;
    }

    private void swap(int row1, int col1, int row2, int col2) {
        if (board[row1][col1] == 0) {
            emptyTilePostion = row2 * dimension + col2 + 1;
        }
        if (board[row2][col2] == 0) {
            emptyTilePostion = row1 * dimension + col1 + 1;
        }
        int temp = board[row1][col1];
        board[row1][col1] = board[row2][col2];
        board[row2][col2] = temp;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twin == null) {
            twin = new Board(board);
            int pos = emptyTilePostion - 1;
            int row = pos / dimension;
            int col = pos % dimension;
            int randomRow = StdRandom.uniform(0, dimension);
            int randomCol = StdRandom.uniform(0, dimension);
            int randomRow2 = StdRandom.uniform(0, dimension);
            int randomCol2 = StdRandom.uniform(0, dimension);
            while ((randomRow == randomRow2 && randomCol == randomCol2) || (randomRow == row && randomCol == col) || (randomRow2 == row && randomCol2 == col)) {
                randomRow = StdRandom.uniform(0, dimension);
                randomCol = StdRandom.uniform(0, dimension);
                randomRow2 = StdRandom.uniform(0, dimension);
                randomCol2 = StdRandom.uniform(0, dimension);
            }
            twin.swap(randomRow, randomCol, randomRow2, randomCol2);
        }
        return twin;
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        // use it for testing
    }


}