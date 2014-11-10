import java.awt.Color;

/**
 * Created by rishee on 11/9/14.
 */
public class KdTree {

    private int N;
    private RedNode root;

    private abstract class Node {

        protected Point2D point;
        protected RectHV rect;
        protected Node left;
        protected Node right;

        public abstract boolean add(Point2D point);

        public abstract void draw();

        public abstract int checkLeftRight(Point2D point);

        public boolean contains(Point2D point) {
            if (this.point.equals(point))
                return true;
            else if (this.checkLeftRight(point) < 0) {
                if (left != null)
                    return left.contains(point);
                else
                    return false;
            } else {
                if (right != null)
                    return right.contains(point);
                else
                    return false;
            }
        }

        public void range(Queue<Point2D> points, RectHV rect) {
            if (rect.intersects(this.rect)) {
                if (rect.contains(this.point))
                    points.enqueue(this.point);
                if (this.left != null)
                    this.left.range(points, rect);
                if (this.right != null)
                    this.right.range(points, rect);
            }
        }

        public Point2D nearest(Point2D point, Point2D closestPoint) {
            if (this.point.distanceTo(point) < closestPoint.distanceTo(point))
                closestPoint = this.point;

            boolean checkLeft = false, checkRight = false;

            if (left != null) {
                if (left.rect.distanceTo(point) < closestPoint.distanceTo(point))
                    checkLeft = true;
            }

            if (right != null) {
                if (right.rect.distanceTo(point) < closestPoint.distanceTo(point))
                    checkRight = true;
            }

            if (checkLeft & !checkRight)
                closestPoint = left.nearest(point, closestPoint);
            else if (checkRight & !checkLeft)
                closestPoint = right.nearest(point, closestPoint);
            else if (checkLeft & checkRight) {
                if (this.checkLeftRight(point) < 0) {
                    closestPoint = left.nearest(point, closestPoint);
                    if (right != null & right.rect.distanceTo(point) < closestPoint.distanceTo(point))
                        closestPoint = right.nearest(point, closestPoint);
                } else {
                    closestPoint = right.nearest(point, closestPoint);
                    if (left != null & left.rect.distanceTo(point) < closestPoint.distanceTo(point))
                        closestPoint = left.nearest(point, closestPoint);
                }
            }

            return closestPoint;
        }

        public Point2D getPoint() {
            return point;
        }
    }

    private class BlueNode extends Node {

        public BlueNode(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
            this.left = null;
            this.right = null;
        }

        public boolean add(Point2D point) {
            if (point.y() < this.point.y()) {
                if (this.left == null) {
                    left = new RedNode(point, new RectHV(rect.xmin(), rect.ymin(), rect.xmax(), this.point.y()));
                    return true;
                } else {
                    return left.add(point);
                }
            } else if (this.point.compareTo(point) != 0) {
                if (this.right == null) {
                    right = new RedNode(point, new RectHV(rect.xmin(), this.point.y(), rect.xmax(), rect.ymax()));
                    return true;
                } else {
                    return right.add(point);
                }
            } else
                return false;
        }

        public void draw() {

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            this.point.draw();

            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(rect.xmin(), this.point.y(), rect.xmax(), this.point.y());

            if (this.left != null)
                this.left.draw();

            if (this.right != null)
                this.right.draw();
        }

        public int checkLeftRight(Point2D point) {
            if (point.y() < this.point.y())
                return -1;
            else
                return 1;
        }
    }

    private class RedNode extends Node {

        public RedNode(Point2D point, RectHV rect) {
            this.point = point;
            this.rect = rect;
            this.left = null;
            this.right = null;
        }

        public boolean add(Point2D point) {
            if (point.x() < this.point.x()) {
                if (this.left == null) {
                    left = new BlueNode(point, new RectHV(rect.xmin(), rect.ymin(), this.point.x(), rect.ymax()));
                    return true;
                } else {
                    return left.add(point);
                }
            } else if (this.point.compareTo(point) != 0) {
                if (this.right == null) {
                    right = new BlueNode(point, new RectHV(this.point.x(), rect.ymin(), rect.xmax(), rect.ymax()));
                    return true;
                } else {
                    return right.add(point);

                }
            } else
                return false;
        }

        public void draw() {

            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            this.point.draw();

            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.setPenRadius(0.005);
            StdDraw.line(this.point.x(), rect.ymin(), this.point.x(), rect.ymax());

            if (this.left != null)
                this.left.draw();

            if (this.right != null)
                this.right.draw();
        }

        public int checkLeftRight(Point2D point) {
            if (point.x() < this.point.x())
                return -1;
            else
                return 1;
        }
    }

    public KdTree() {                              // construct an empty set of points

        root = null;
        N = 0;
    }

    public boolean isEmpty() {                     // is the set empty?

        return (N == 0);
    }

    public int size() {                        // number of points in the set

        return N;
    }

    public void insert(Point2D p) {             // add the point to the set (if it is not already in the set)

        if (root == null) {
            root = new RedNode(p, new RectHV(0, 0, 1, 1));
            N++;
        } else {
            if (root.add(p))
                N++;
        }
    }

    public boolean contains(Point2D p) {           // does the set contain point p?
        if (p == null)
            return false;
        else if (root == null)
            return false;
        else
            return root.contains(p);
    }

    public void draw() {                        // draw all points to standard draw

        if (root != null)
            this.root.draw();

    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle

        Queue<Point2D> insidePoints = new Queue<Point2D>();

        if(root != null) {
            root.range(insidePoints, rect);
        }

        return insidePoints;
    }

    public Point2D nearest(Point2D p) {            // a nearest neighbor in the set to point p; null if the set is empty

        Point2D closestPoint = null;

        if (root != null) {
            closestPoint = root.getPoint();
            closestPoint = root.nearest(p, closestPoint);
        }

        return closestPoint;
    }

    public static void main(String[] args) {                 // unit testing of the methods (optional)

    }
}
