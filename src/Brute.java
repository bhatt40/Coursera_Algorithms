/**
 * Created by rishee on 11/9/14.
 */
public class Brute {
    public static void main(String[] args) {
        // rescale coordinates and turn on animation mode
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);

        String filename = args[0];
        In in = new In(filename);
        int N = in.readInt();
        Point[] points = new Point[N];

        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            Point p = new Point(x, y);
            p.draw();
            points[i] = p;
        }

        double slope = 0;
        // Iterate through points
        for (int a = 0; a < N; a++)
        {
            // Iterate through all other points
            for (int b = 0; b < N; b++)
            {
                // If point b is not the same point as a and is further than a
                if (b != a && points[b].compareTo(points[a]) > 0)
                {
                    slope = points[a].slopeTo(points[b]);
                    // Iterate through all other points
                    for (int c = 0; c < N; c++)
                    {
                        // If point c is not the same point as a or b, has the same slope to a as b has, and is further than b
                        if (c != a && c != b && points[a].slopeTo(points[c]) == slope && points[c].compareTo(points[b]) > 0)
                        {
                            // Iterate through all other points
                            for(int d = 0; d < N; d++)
                            {
                                // If point d is not the same point as a, b, or c, has the same slope to a as b has, and is further than c
                                if (d != a && d != b && d != c &&  points[a].slopeTo(points[d]) == slope && points[d].compareTo(points[c]) > 0)
                                {
                                    // We have a line segment!
                                    StdOut.println(points[a].toString() + " -> " + points[b].toString() + " -> " + points[c].toString() + " -> " + points[d].toString());
                                    points[a].drawTo(points[d]);
                                }
                            }
                        }
                    }
                }
            }
        }

        // display to screen all at once
        StdDraw.show(0);
    }
}
