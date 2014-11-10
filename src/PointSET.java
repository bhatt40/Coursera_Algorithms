import java.util.Iterator;

/**
 * Created by rishee on 11/9/14.
 */
public class PointSET {

    private SET<Point2D> points;

    public PointSET() {                              // construct an empty set of points
        points = new SET<Point2D>();
    }

    public boolean isEmpty() {                     // is the set empty?

        return (points.size() == 0);
    }

    public int size() {                        // number of points in the set

        return points.size();
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)

        points.add(p);
    }

    public boolean contains(Point2D p) {           // does the set contain point p?

        return points.contains(p);
    }

    public void draw() {                       // draw all points to standard draw

        Iterator<Point2D> itr = this.points.iterator();
        while (itr.hasNext())
        {
            itr.next().draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle

        Queue<Point2D> pointsInRectangle = new Queue<Point2D>();

        for (Iterator<Point2D> pointsIter = points.iterator(); pointsIter.hasNext();) {
            Point2D point = pointsIter.next();
            if (rect.contains(point))
                pointsInRectangle.enqueue(point);
        }

        return pointsInRectangle;
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty

        Point2D nearestPoint = null;

        for (Iterator<Point2D> pointsIter = points.iterator(); pointsIter.hasNext();) {
            Point2D point = pointsIter.next();
            if(nearestPoint == null || point.distanceTo(p) < nearestPoint.distanceTo(p))
                nearestPoint = point;
        }

        return nearestPoint;
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional)
    }
}
