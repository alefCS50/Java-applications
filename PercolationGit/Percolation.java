import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final int FIRST = 0;
    private int last;
    private int n;
    private WeightedQuickUnionUF qf;
    private boolean[][] grid;
    private int opensites;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n <= 0) {
            throw new IllegalArgumentException("Grid length must be positive");
        }
        this.n = n;

        // set new grid
        qf = new WeightedQuickUnionUF(n * n + 2);
        grid = new boolean[n][n];
        opensites = 0;
        last = n * n + 1;


    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }

        // register site as opened
        grid[row - 1][col - 1] = true;
        opensites++;

        // for first row cases
        if (row == 1) {
            qf.union(getloc(row, col), FIRST);
        }

        // for last row cases
        if (row == n) {
            qf.union(getloc(row, col), last);
        }

        // for all cases
        if ((row > 1) && isOpen(row - 1, col)) {
            qf.union(getloc(row, col), getloc(row - 1, col));
        }

        if ((row < n) && isOpen(row + 1, col)) {
            qf.union(getloc(row, col), getloc(row + 1, col));
        }

        if ((col > 1) && isOpen(row, col - 1)) {
            qf.union(getloc(row, col), getloc(row, col - 1));
        }

        if ((col < n) && isOpen(row, col + 1)) {
            qf.union(getloc(row, col), getloc(row, col + 1));
        }

        
    }

    private int getloc(int row, int col) {
        return n * (row - 1) + col;
    }


    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (row <= 0 || row > n || col <= 0 || col > n) {
            throw new IllegalArgumentException();
        }
        return grid[row - 1][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        if (row < 1 || row > n || col < 1 || col > n) {
            throw new IllegalArgumentException();
        }

        if (qf.find(getloc(row, col)) == qf.find(FIRST)) {
            return true;
        }
        else {
            return false;
        }
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return opensites;
    }

    // does the system percolate?
    public boolean percolates() {
        return qf.find(FIRST) == qf.find(last);
    }
}





