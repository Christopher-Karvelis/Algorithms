/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

import java.util.TreeSet;

public class PointSET {
    private final TreeSet<Point2D> pointSet;

    public PointSET() {                                     // construct an empty set of points
        pointSet = new TreeSet<>();
    }

    public boolean isEmpty()  {                             // is the set empty?
        return pointSet.isEmpty();
    }

    public int size() {                                     // number of points in the set
        return  pointSet.size();
    }

    public void insert(Point2D p) {                         // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (!contains(p)) {
            pointSet.add(p);
        }
    }

    public boolean contains(Point2D p) {                    // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        return pointSet.contains(p);
    }

    public void draw() {                                    // draw all points to standard draw
        for (Point2D p: pointSet) {
            StdDraw.point(p.x(), p.y());
        }
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Stack<Point2D> stack = new Stack<>();
        for (Point2D p: pointSet) {
            if (rect.contains(p))
                stack.push(p);
        }
        return stack;
    }

    public Point2D nearest(Point2D p) {                     // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Point2D minPoint;
        minPoint = pointSet.first();
        double minDistance = p.distanceSquaredTo(pointSet.first());
        for (Point2D point: pointSet) {
            if (p.distanceSquaredTo(point) < minDistance) {
                minPoint = point;
                minDistance = p.distanceSquaredTo(point);
            }
        }
        return minPoint;
    }

    public static void main(String[] args)  {
        // unit testing of the methods (optional)
    }
}
