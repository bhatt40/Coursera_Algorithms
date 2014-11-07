/**
 * Created by rishee on 11/6/14.
 */
public class Percolation {

    private WeightedQuickUnionUF uf;
    private WeightedQuickUnionUF ufWithVirtualSites;
    private boolean[] grid;
    private int N;

    public Percolation(int N) {               // create N-by-N grid, with all sites blocked
        if (N <= 0)
            throw new IllegalArgumentException("Grid size must be greater than 0.");
        else {
            this.uf = new WeightedQuickUnionUF(N*N + 2);
            this.ufWithVirtualSites = new WeightedQuickUnionUF(N*N + 2);
            this.grid = new boolean[N*N + 2];
            this.N = N;

            // Initialize all sites as closed
            for (int a = 0; a < grid.length; a++) {
                grid[a] = false;
            }

            // Open virtual sites at top and bottom
            grid[0] = true;
            grid[grid.length - 1] = true;
        }
    }

    public void open(int i, int j) {         // open site (row i, column j) if it is not open already

        // validate indices
        this.checkIndex(i);
        this.checkIndex(j);

        // mark site as open
        this.grid[this.xyTo1D(i, j)] = true;

        // link to open neighbors
        int[] left = this.getLeftNeighbor(i, j);
        if (left != null && this.isOpen(left[0], left[1])) {
            ufWithVirtualSites.union(xyTo1D(left[0], left[1]), xyTo1D(i, j));
            uf.union(xyTo1D(left[0], left[1]), xyTo1D(i, j));
        }

        int[] right = this.getRightNeighbor(i, j);
        if (right != null && this.isOpen(right[0], right[1])) {
            ufWithVirtualSites.union(xyTo1D(right[0], right[1]), xyTo1D(i, j));
            uf.union(xyTo1D(right[0], right[1]), xyTo1D(i, j));
        }

        int top[] = this.getTopNeighbor(i, j);
        if (top == null) {
            // Connect to virtual site
            ufWithVirtualSites.union(xyTo1D(1, 0), xyTo1D(i, j));
            uf.union(xyTo1D(1, 0), xyTo1D(i, j));
        } else if (this.isOpen(top[0], top[1])) {
            ufWithVirtualSites.union(xyTo1D(top[0], top[1]), xyTo1D(i, j));
            uf.union(xyTo1D(top[0], top[1]), xyTo1D(i, j));
        }

        int[] bottom = this.getBottomNeighbor(i, j);
        if (bottom == null) {
            // Connect to virtual site
            ufWithVirtualSites.union(xyTo1D(this.N, this.N+1), xyTo1D(i, j));
        } else if (this.isOpen(bottom[0], bottom[1])) {
            ufWithVirtualSites.union(xyTo1D(bottom[0], bottom[1]), xyTo1D(i, j));
            uf.union(xyTo1D(bottom[0], bottom[1]), xyTo1D(i, j));
        }

    }

    public boolean isOpen(int i, int j) {    // is site (row i, column j) open?

        this.checkIndex(i);
        this.checkIndex(j);

        return grid[this.xyTo1D(i, j)];
    }

    public boolean isFull(int i, int j) {     // is site (row i, column j) full?

        this.checkIndex(i);
        this.checkIndex(j);

        return uf.connected(this.xyTo1D(i, j), 0);
    }

    public boolean percolates() {           // does the system percolate?

        return ufWithVirtualSites.connected(0, N*N + 1);
    }

    private void checkIndex(int i) {

        if (i <= 0 || i > this.N)
            throw new IndexOutOfBoundsException("Index is out of bounds.");
    }

    private int xyTo1D(int i, int j) {

        return this.N*(i-1) + j;
    }


    private int[] getLeftNeighbor(int i, int j) {

        if (j <= 1)
            return null;
        else
            return new int[]{i, j - 1};
    }

    private int[] getRightNeighbor(int i, int j) {

        if (j >= this.N)
            return null;
        else
            return new int[]{i, j + 1};
    }

    private int[] getTopNeighbor(int i, int j) {

        if (i <= 1)
            return null;
        else
            return new int[]{i - 1, j};
    }

    private int[] getBottomNeighbor(int i, int j) {

        if (i >= this.N)
            return null;
        else
            return new int[]{i + 1, j};
    }

    public static void main(String[] args) {   // test client (optional)

        Percolation perc = new Percolation(3);

        perc.open(1, 1);
        StdOut.print(perc.percolates());
        StdOut.println();

        perc.open(1, 2);
        StdOut.print(perc.percolates());
        StdOut.println();

        perc.open(2, 2);
        StdOut.print(perc.percolates());
        StdOut.println();

        perc.open(3, 2);
        StdOut.print(perc.percolates());
        StdOut.println();

        perc.open(3, 3);
        StdOut.print(perc.percolates());
    }
}
