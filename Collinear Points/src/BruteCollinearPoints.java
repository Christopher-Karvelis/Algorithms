/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;
import java.util.Comparator;

public class BruteCollinearPoints {
    private LineSegment[] lineSegments;
    private int numberOfSegments;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new  IllegalArgumentException();
        }
        lineSegments = new LineSegment[1];
        numberOfSegments = 0;
        for (int i = 0; i < points.length; i++) {
            for (int j = i+1; j < points.length; j++) {
                for (int k = j+1; k < points.length; k++) {
                    for (int m = k+1; m < points.length; m++) {
                        if (points[i] == null || points[j] == null || points[k] == null || points[m] == null) {
                            throw new  IllegalArgumentException();
                        }
                        if (points[i] == points[j] || points[i] == points[k] || points[i] == points[m]) {
                            throw new IllegalArgumentException();
                        }
                        Comparator<Point> slopeComparator = points[i].slopeOrder();
                        if (slopeComparator.compare(points[j], points[k]) == 0 && slopeComparator.compare(points[k], points[m]) == 0) {
                            Point[] segmentPoints = new Point[4];
                            segmentPoints[0] = points[i];
                            segmentPoints[1] = points[j];
                            segmentPoints[2] = points[k];
                            segmentPoints[3] = points[m];
                            Point min = points[i];
                            Point max = points[j];
                            for (int n = 0; n < segmentPoints.length; n++) {
                                if (segmentPoints[n].compareTo(min) < 0) {
                                    min = segmentPoints[n];
                                }
                                if (segmentPoints[n].compareTo(max) > 0) {
                                    max = segmentPoints[n];
                                }
                            }
                            if (numberOfSegments == lineSegments.length) {
                                resize(2*lineSegments.length);
                            }
                            lineSegments[numberOfSegments++] = new LineSegment(min, max);
                        }
                    }
                }
            }
        }
    }   // finds all line segments containing 4 points

    private void resize(int capacity) {
        LineSegment[] copy = new LineSegment[capacity];
        for (int i = 0; i < numberOfSegments; i++) {
            copy[i] = lineSegments[i];
        }
        lineSegments = copy;
    }

    public int numberOfSegments() {
       return numberOfSegments;
    }     // the number of line segments

    public LineSegment[] segments() {
        return Arrays.copyOfRange(lineSegments, 0, numberOfSegments);
    }                // the line segments
}
