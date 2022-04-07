/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    private Node root;
    private int size;
    private double minDistance;

    private class Node
    {
        private double y;
        private double x;
        private Node left, right;
        private final boolean isVertical;
        private final RectHV nodeRect;
        private final Point2D point;

        public Node(double x, double y, Node left, Node right, boolean isVertical, RectHV nodeRect) {
            this.x = x;
            this.y = y;
            this.left = left;
            this.right = right;
            this.nodeRect = nodeRect;
            this.isVertical = isVertical;
            this.point = new Point2D(x, y);
        }

        public Iterable<Node> nodes()
        {
            Queue<Node> q = new Queue<Node>();
            inorder(root, q);
            return q;
        }

        private void inorder(Node node, Queue<Node> q)
        {
            if (node == null) return;
            inorder(node.left, q);
            q.enqueue(node);
            inorder(node.right, q);
        }
    }

    public KdTree() {
        size = 0;
    }

    public boolean isEmpty() {                              // is the set empty?
        return size == 0;
    }

    public int size() {                                     // number of points in the set
        return size;
    }

    public void insert(Point2D p) {                         // add the point to the set (if it is not already in the set)
        if (p == null) {
            throw new IllegalArgumentException();
        }
        root = put(root, null, p.x(), p.y(), true);
    }

    private Node put(Node node, Node father, double x, double y, boolean isVertical) {
        if (node == null) {
            RectHV nodeRect;
            if (size == 0) {
                nodeRect = new RectHV(0, 0, 1, 1);
            } else {
                if (father.isVertical) {
                    int cmp = Double.compare(x, father.x);
                    if (cmp < 0) {
                        nodeRect = new RectHV(father.nodeRect.xmin(), father.nodeRect.ymin(),
                                              father.x, father.nodeRect.ymax());
                    }
                    else if (cmp > 0) {
                        nodeRect = new RectHV(father.x, father.nodeRect.ymin(), father.nodeRect.xmax(),
                                              father.nodeRect.ymax());
                    }
                    else {
                        nodeRect = father.nodeRect;
                        isVertical = true;
                    }
                }
                else {
                    int cmp = Double.compare(y, father.y);
                    if (cmp < 0) {
                        nodeRect = new RectHV(father.nodeRect.xmin(), father.nodeRect.ymin(),
                                              father.nodeRect.xmax(), father.y);
                    }
                    else if (cmp > 0) {
                        nodeRect = new RectHV(father.nodeRect.xmin(), father.y,
                                              father.nodeRect.xmax(), father.nodeRect.ymax());
                    } else {
                        nodeRect = father.nodeRect;
                        isVertical = false;
                    }
                }
            }
            size++;
            return new Node(x, y, null, null, isVertical, nodeRect);
        }
        int cmp;
        if (isVertical) {
            cmp = Double.compare(x, node.x);
            isVertical = false;
        } else {
            cmp = Double.compare(y, node.y);
            isVertical = true;
        }

        if (cmp < 0) node.left = put(node.left, node, x, y, isVertical);
        else if (cmp > 0) node.right = put(node.right, node, x, y, isVertical);
        else {
            if (isVertical) {
                if (Double.compare(node.y, y) == 0) {
                    node.x = x;
                    node.y = y;
                } else {
                    node.right = put(node.right, node, x, y, isVertical);
                }
            } else {
                if (Double.compare(node.x, x) == 0) {
                    node.x = x;
                    node.y = y;
                }
                else {
                    node.right = put(node.right, node, x, y, isVertical);
                }
            }
        }
        return node;
    }

    public boolean contains(Point2D p) {                    // does the set contain point p?
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return false;
        }
        Node node = root;
        while (node != null)
        {
            int cmp;
            if (node.isVertical) {
                cmp = Double.compare(p.x(), node.x);
            } else {
                cmp = Double.compare(p.y(), node.y);
            }
            if (cmp < 0) node = node.left;
            else if (cmp > 0) node = node.right;
            else {
                if (node.isVertical) {
                    if (Double.compare(p.y(), node.y) == 0) {
                        return true;
                    }
                    node = node.right;
                }
                else {
                    if (Double.compare(p.x(), node.x) == 0) {
                        return true;
                    }
                    node = node.right;
                }
            }
        }
        return false;
    }

    public void draw() {                                     // draw all points to standard draw
        Iterable<Node> points = root.nodes();
        for (Node point: points) {
            StdDraw.point(point.x, point.y);
        }
    }

    public Iterable<Point2D> range(RectHV rect) {            // all points that are inside the rectangle (or on the boundary)
        if (rect == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Stack<Point2D> point2DStack = new Stack<>();
        searchRect(rect, point2DStack, root);
        return point2DStack;
    }

    private void searchRect(RectHV rect, Stack<Point2D> point2DStack, Node node) {
        if (node != null) {
            if (rect.intersects(node.nodeRect)) {
                if (rect.contains(node.point)) {
                    point2DStack.push(node.point);
                }
                searchRect(rect, point2DStack, node.left);
                searchRect(rect, point2DStack, node.right);
            }
        }
    }

    public Point2D nearest(Point2D p) {                      // a nearest neighbor in the set to point p; null if the set is empty
        if (p == null) {
            throw new IllegalArgumentException();
        }
        if (isEmpty()) {
            return null;
        }
        Point2D minPoint;
        minDistance = p.distanceSquaredTo(root.point);
        minPoint = searchNearest(p, root, root.point);
        return minPoint;
    }

    private Point2D searchNearest(Point2D p, Node node, Point2D minPoint) {
        if (node != null) {
            if (p.distanceSquaredTo(node.point) <= minDistance) {
                minDistance = p.distanceSquaredTo(node.point);
                minPoint = node.point;
            }
            int cmp;
            if (node.isVertical) {
                cmp = Double.compare(p.x(), node.x);
            } else {
                cmp = Double.compare(p.y(), node.y);
            }
            if (cmp < 0) {
                if (node.left != null && node.left.nodeRect.distanceSquaredTo(p) < minDistance) {
                    minPoint = searchNearest(p, node.left, minPoint);
                }
                if (node.right != null && node.right.nodeRect.distanceSquaredTo(p) < minDistance) {
                    minPoint = searchNearest(p, node.right, minPoint);
                }
            }
            else if (cmp > 0) {
                if (node.right != null && node.right.nodeRect.distanceSquaredTo(p) < minDistance) {
                    minPoint = searchNearest(p, node.right, minPoint);
                }
                if (node.left != null && node.left.nodeRect.distanceSquaredTo(p) < minDistance) {
                    minPoint = searchNearest(p, node.left, minPoint);
                }
            }
        }
        return minPoint;
    }

    public static void main(String[] args) {
        // fot testing
    }
}