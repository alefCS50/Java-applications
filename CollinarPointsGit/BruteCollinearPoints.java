import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class BruteCollinearPoints {
    private LineSegment lineArr[];

    public BruteCollinearPoints(Point[] points) {


        //checks if null
        if (points == null) {
            throw new IllegalArgumentException();
        }
        //check if something in it is null
        for (int a = 0; a < points.length; a++) {
            if (points[a] == null) {
                throw new IllegalArgumentException();
            }
        }
        //checks duplicates
        Point clone[] = points.clone();
        Arrays.sort(clone);

        if (clone.length > 1) {
            for (int b = 1; b < clone.length; b++) {
                if (clone[b].compareTo(clone[b - 1]) == 0) {
                    throw new IllegalArgumentException();
                }
            }
        }
        ArrayList<LineSegment> ListSeg = new ArrayList<LineSegment>();
        if (clone.length > 3) {
            for (int i = 0; i < clone.length - 3; i++) {
                for (int j = i + 1; j < clone.length - 2; j++) {
                    for (int k = j + 1; k < clone.length - 1; k++) {
                        for (int l = k + 1; l < clone.length; l++) {

                            double s1 = clone[i].slopeTo(clone[j]);
                            double s2 = clone[i].slopeTo(clone[k]);
                            double s3 = clone[i].slopeTo(clone[l]);
                            if ((Double.compare(s1, s2) == 0) && (Double.compare(s1, s3) == 0)) {
                                LineSegment temp = new LineSegment(clone[i], clone[l]);
                                ListSeg.add(temp);

                            }
                        }

                    }

                }
            }
            lineArr = ListSeg.toArray(new LineSegment[ListSeg.size()]);
        }

    }


    public int numberOfSegments() {
        return lineArr.length;
    }

    public LineSegment[] segments() {
        return lineArr.clone();
    }

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

        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }

        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }

}
