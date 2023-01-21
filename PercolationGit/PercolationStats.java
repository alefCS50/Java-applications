import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {


    private final double results[];
    private final int count;


    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        if (n < 1 || trials < 1) {
            throw new IllegalArgumentException();
        }
        count = trials;
        results = new double[count];
        for (int num = 0; num < count; num++) {
            Percolation pe = new Percolation(n);
            int opensites = 0;
            while (!pe.percolates()) {
                int random1 = StdRandom.uniformInt(1, n + 1);
                int random2 = StdRandom.uniformInt(1, n + 1);
                if (!pe.isOpen(random1, random2)) {
                    pe.open(random1, random2);
                    opensites++;

                }
            }
            double fraction = (double) opensites / (n * n);
            results[num] = fraction;
        }


    }

    // sample mean of percolation threshold
    public double mean() {
        return StdStats.mean(results);
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        return StdStats.stddev(results);
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        double s = (double) Math.sqrt(stddev());
        double sqt = (double) Math.sqrt(count);
        return mean() - (1.96 * s) / sqt;

    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        double s = (double) Math.sqrt(stddev());
        double sqt = (double) Math.sqrt(count);
        return mean() + (1.96 * s) / sqt;
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int t = Integer.parseInt(args[1]);
        PercolationStats per = new PercolationStats(n, t);


        System.out.println("mean = " + per.mean());
        System.out.println("stddev = " + per.stddev());
        System.out.println(
                "95% confidence interval = " + "[" + per.confidenceLo() + ", "
                        + per.confidenceHi()
                        + "]");

    }


}

