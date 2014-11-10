/**
 * Created by rishee on 11/9/14.
 */
import java.util.Comparator;
import java.util.zip.Inflater;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();      // YOUR DEFINITION HERE

    private class SlopeOrder implements Comparator<Point>
    {
        public int compare(Point q, Point r)
        {
            if(Point.this.slopeTo(q) < Point.this.slopeTo(r))
                return -1;
            if(Point.this.slopeTo(q) > Point.this.slopeTo(r))
                return 1;
            return 0;
        }
    }

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        if (this.x == that.x) {
            if (this.y == that.y)
                return Double.NEGATIVE_INFINITY;
            else
                return Double.POSITIVE_INFINITY;
        }
        else if (this.y == that.y)
            return 0;
        else
            return (double)(that.y - this.y)/(that.x - this.x);
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if (this.y < that.y)
            return -1;
        else if (this.y > that.y)
            return 1;
        else
        {
            if (this.x < that.x)
                return -1;
            else if (this.x > that.x)
                return 1;
            else
                return 0;
        }
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}