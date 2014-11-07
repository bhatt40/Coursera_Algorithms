/**
 * Created by rishee on 11/6/14.
 */


public class PercolationStats {

    private double[] thresholds;

    public PercolationStats(int N, int T) {    // perform T independent experiments on an N-by-N grid

        this.checkArgs(N, T);

        this.thresholds = new double[T];
        int row, column, counter;

        Percolation perc;

        for (int a = 0; a < T; a++) {

            perc = new Percolation(N);

            counter = 0;

            while(!perc.percolates()) {

                row = StdRandom.uniform(1, N+1);
                column = StdRandom.uniform(1, N+1);

                if(!perc.isOpen(row, column)) {
                    perc.open(row, column);
                    counter++;
                }
            }

            thresholds[a] = (double)counter/(double)(N*N);
        }
    }

    public double mean() {                    // sample mean of percolation threshold

        return StdStats.mean(this.thresholds);
    }

    public double stddev() {                   // sample standard deviation of percolation threshold

        return StdStats.stddev(this.thresholds);
    }

    public double confidenceLo() {              // low  endpoint of 95% confidence interval

        return StdStats.mean(this.thresholds) - 1.96*StdStats.stddev(this.thresholds)/Math.sqrt(thresholds.length);
    }

    public double confidenceHi() {             // high endpoint of 95% confidence interval

        return StdStats.mean(this.thresholds) + 1.96*StdStats.stddev(this.thresholds)/Math.sqrt(thresholds.length);
    }

    private void checkArgs(int N, int T) {
        if (N < 1 || T < 1) {
            throw new IllegalArgumentException("Bad arguments.");
        }
    }

    public static void main(String[] args) {   // test client (described below)

        PercolationStats percStats = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        StdOut.print("mean = ");
        StdOut.print(percStats.mean());
        StdOut.println();

        StdOut.print("stddev = ");
        StdOut.print(percStats.stddev());
        StdOut.println();

        StdOut.print("95% confidence interval = ");
        StdOut.print(percStats.confidenceLo());
        StdOut.print(", ");
        StdOut.print(percStats.confidenceHi());
        StdOut.println();
    }

}
