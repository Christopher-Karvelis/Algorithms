/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private boolean[][] grid;
    private final int n;
    private int openSites;
    private final int virtualTop;
    private final int virtualBottom;
    private final WeightedQuickUnionUF weightedQuickUnionUF;
    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        // (ids from 0 to n^2-1) +2 for the virtual top and virtual bottom
        weightedQuickUnionUF = new WeightedQuickUnionUF(n*n+2);
        if (n <= 0) {
            throw new IllegalArgumentException("Grid must be of size bigger than 0!");
        }
        this.n = n;
        openSites = 0;
        grid = new boolean[n][n];
        virtualTop = n*n;
        virtualBottom = n*n + 1;
    }
    private boolean inRange(int row, int col) {
        return (1 <= row && row <= n && 1 <= col && col <= n);
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("row and col must be between 1 and n!");
        }
        if (!isOpen(row, col)) {
            row -= 1;
            col -= 1;
            grid[row][col] = true;
            openSites++;
            int siteID = row*n + col;
            if (row == 0) {
                // connect top row site with virtual top site
                weightedQuickUnionUF.union(virtualTop, siteID);
            }
            if (row == n - 1) {
                // connect bottom row site with virtual bottom site
                weightedQuickUnionUF.union(virtualBottom, siteID);
            }
            if (row - 1 >= 0) {
                if (isOpen(row, col+1)) {
                    int topSiteId = (row-1)*n + col;
                    weightedQuickUnionUF.union(siteID, topSiteId);
                }
            }
            if (row + 1 < n) {
                if (isOpen(row + 2, col+1)) {
                    int bottomSiteId =  (row+1)*n + col;
                    weightedQuickUnionUF.union(siteID, bottomSiteId);
                }
            }
            if (col - 1 >= 0) {
                if (isOpen(row+1, col)) {
                    int leftSiteID = (row*n) + col - 1;
                    weightedQuickUnionUF.union(siteID, leftSiteID);
                }
            }
            if (col + 1 < n) {
                if (isOpen(row+1, col + 2)) {
                    int rightSiteID = (row*n) + col + 1;
                    weightedQuickUnionUF.union(siteID, rightSiteID);
                }
            }
        }
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("row and col must be between 1 and n!");
        }
        return grid[row-1][col-1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (!inRange(row, col)) {
            throw new IllegalArgumentException("row and col must be between 1 and n!");
        }
        if (!isOpen(row, col)) {
            return false;
        }
        row--;
        col--;
        int site = row*n + col;
        return weightedQuickUnionUF.find(site) == weightedQuickUnionUF.find(virtualTop);
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openSites;
    }

    // does the system percolate?
    public boolean percolates() {
        return weightedQuickUnionUF.find(virtualBottom) == weightedQuickUnionUF.find(virtualTop);
    }

    // test client (optional)
    public static void main(String[] args) {
        int n = 10;
         int DELAY = 50;
        Percolation percolation = new Percolation(10);
        // turn on animation mode
        PercolationVisualizer visualizer = new PercolationVisualizer();
        StdDraw.enableDoubleBuffering();
        visualizer.draw(percolation, n);
        StdDraw.show();
        StdDraw.pause(DELAY);
        while (!percolation.percolates()) {
            int row = StdRandom.uniform(1, n+1);
            int col =  StdRandom.uniform(1, n+1);
            percolation.open(row, col);
            visualizer.draw(percolation, n);
            StdDraw.show();
            StdDraw.pause(DELAY);
            StdOut.println(percolation.numberOfOpenSites());
        }
    }
}
