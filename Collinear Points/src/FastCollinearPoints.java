/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Arrays;
import java.util.Comparator;

public class FastCollinearPoints {
    private LineSegment[] lineSegments;
    private int numberOfSegments;
    private Point[] pointsOfSegments;
    private int numOfPointsOfSegments;

    public FastCollinearPoints(Point[] points) {
        if (points == null) {
            throw new  IllegalArgumentException();
        }
        int numOfPoints = points.length;
        lineSegments = new LineSegment[1];
        numberOfSegments = 0;
        numOfPointsOfSegments = 0;
        Point[] pointsWithSlopeToi = new Point[numOfPoints];
        for (int i = 0; i < numOfPoints; i++) {
            if (points[i] == null) {
                throw new  IllegalArgumentException();
            }
            pointsWithSlopeToi[i] = points[i];
        }
        pointsOfSegments = new Point[2];
        for (int i = 0; i < numOfPoints; i++) {
            Comparator<Point> slopeComparator = points[i].slopeOrder();
            Arrays.sort(pointsWithSlopeToi, slopeComparator);
            Point min = points[i];
            Point max = points[i];
            int numOfCollinearPoints = 1;
            for (int j = 1; j < numOfPoints-1; j++) {
                if (points[i].slopeTo(pointsWithSlopeToi[j]) == points[i].slopeTo(pointsWithSlopeToi[j+1])) {
                    if (pointsWithSlopeToi[j+1].compareTo(min) > 0) {
                        min = pointsWithSlopeToi[j+1];
                    }
                    if (pointsWithSlopeToi[j+1].compareTo(max) < 0) {
                        max = pointsWithSlopeToi[j+1];
                    }
                    if (pointsWithSlopeToi[j].compareTo(min) > 0) {
                        min = pointsWithSlopeToi[j];
                    }
                    if (pointsWithSlopeToi[j].compareTo(max) < 0) {
                        max = pointsWithSlopeToi[j];
                    }
                    numOfCollinearPoints += 2;
                } else if (points[i].slopeTo(pointsWithSlopeToi[j]) != points[i].slopeTo(pointsWithSlopeToi[j+1])) {
                    if (numOfCollinearPoints >= 4) {
                        if (pointsOfSegments.length == numOfPointsOfSegments) {
                            resizePoints(2*numOfPointsOfSegments);
                        }
                        pointsOfSegments[numOfPointsOfSegments++] = min;
                        pointsOfSegments[numOfPointsOfSegments++] = max;
                    }
                    min = points[i];
                    max = points[i];
                    numOfCollinearPoints = 1;
                }
                if (j == numOfPoints - 2 && numOfCollinearPoints >= 4) {
                    if (pointsOfSegments.length == numOfPointsOfSegments) {
                        resizePoints(2*numOfPointsOfSegments);
                    }
                    pointsOfSegments[numOfPointsOfSegments++] = min;
                    pointsOfSegments[numOfPointsOfSegments++] = max;
                }
            }
        }
        for (int i = 0; i < numOfPointsOfSegments; i += 2) {
            Point start = pointsOfSegments[i];
            Point end = pointsOfSegments[i+1];
            boolean isDouble = false;
            for (int j = i+2; j < numOfPointsOfSegments; j += 2) {
                if (start.compareTo(pointsOfSegments[j]) == 0 && end.compareTo(pointsOfSegments[j+1]) == 0) {
                    isDouble = true;
                }

                if (start.compareTo(pointsOfSegments[j+1]) == 0 && end.compareTo(pointsOfSegments[j]) == 0) {
                    isDouble = true;
                }
            }
            if (!isDouble) {
                if (numberOfSegments == lineSegments.length) {
                    resize(2 * lineSegments.length);
                }
                lineSegments[numberOfSegments++] = new LineSegment(start, end);
            }
        }
    }   // finds all line segments containing 4 or more points

    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < numberOfSegments; i++) {
            copy[i] = lineSegments[i];
        }
        lineSegments = copy;
    }

    private void resizePoints(int capacity) {
        Point[] copy = new Point[capacity];
        for (int i = 0; i < numOfPointsOfSegments; i++) {
            copy[i] = pointsOfSegments[i];
        }
        pointsOfSegments = copy;
    }

    public int numberOfSegments() {
        return numberOfSegments;
    }        // the number of line segments

    public LineSegment[] segments() {
        return Arrays.copyOfRange(lineSegments, 0, numberOfSegments);
    }               // the line segments

    public static void main(String[] args) {
        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        StdDraw.setPenRadius(0.02);
        StdDraw.setPenColor(255, 0, 0);
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(-1500, 40000);
        StdDraw.setYscale(-1500, 40000);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        int DELAY = 200;
        // print and draw the line segments
        StdDraw.setPenRadius(0.0020);
        StdDraw.setPenColor(0, 0, 255);
        // BruteCollinearPoints bruteCollinearPoints = new BruteCollinearPoints(points);
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            int randRed = StdRandom.uniform(0, 255);
            int randBlue = StdRandom.uniform(0, 255);
            int randGreen = StdRandom.uniform(0, 255);
            StdDraw.setPenColor(randRed, randBlue, randGreen);
            StdOut.println(segment);
            segment.draw();
            StdDraw.show();
            StdDraw.pause(DELAY);
        }
        // StdOut.println(collinear.numberOfSegments);
    }
}
