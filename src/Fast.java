import java.util.Arrays;

/**
 * Created by rishee on 11/9/14.
 */
public class Fast {
    public static void main(String[] args)
    {
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] points = new Point[N];

        for (int i = 0; i < N; i++)
        {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            p.draw();
            points[i] = p;
        }

        Point[] q = new Point[N-1];
        int counter = 0;
        int consecutive = 1;
        double lastSlope = Double.NaN;
        String output = "";

        for (int a = 0; a < N; a++) {
            counter = 0;

            // Fill q with all other points
            for (int b = 0; b < N; b++) {
                if (a != b) {
                    q[counter] = points[b];
                    counter++;
                }
            }

            // Sort by size, then by slope order (Java array sort is stable)
            Arrays.sort(q);
            Arrays.sort(q, points[a].SLOPE_ORDER);

            consecutive = 1;
            lastSlope = Double.NaN;
            output = "";

            for (int c = 0; c <= q.length; c++)
            {
                // Check if slope between this point and origin is same as last point's
                if (c != q.length && q[c].slopeTo(points[a]) == lastSlope)
                {
                    consecutive++;
                }

                // Broke the chain of consecutive points with the same slopeTo point p, or done with all points
                else
                {
                    // If chain was long enough for there to be 4 collinear points
                    if (consecutive >= 3)
                    {
                        // Check that first point in chain is larger than point p
                        if(q[c-consecutive].compareTo(points[a]) > 0)
                        {
                            output = points[a].toString();
                            for(int x = c-consecutive; x < c; x++)
                            {
                                output = output + " -> " + q[x].toString();
                            }
                            StdOut.println(output);
                            points[a].drawTo(q[c-1]);
                        }
                    }
                    // Reset consecutive
                    consecutive = 1;
                }

                // Store this point's slope to the origin
                if (c != q.length)
                    lastSlope = q[c].slopeTo(points[a]);
            }
        }
        }
}
