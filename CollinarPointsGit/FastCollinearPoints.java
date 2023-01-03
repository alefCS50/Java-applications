import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    private double temp;
    private int counter;
    private LineSegment LArr[];


    public FastCollinearPoints(Point[] points) {
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


        //search copy
        Point search[] = clone.clone();

        ArrayList<LineSegment> sList = new ArrayList<LineSegment>();
        if (clone.length > 3) {
            for (int p = 0; p < clone.length; p++) {

                Arrays.sort(search, clone[p].slopeOrder());
                counter = 0;
                Point beginning = null;

                //check if there are lines
                for (int index = 0; index < search.length - 1; index++) {

                    if (search[index].slopeTo(clone[p]) == search[index + 1].slopeTo(clone[p])) {

                        counter++;
                        if (counter == 1) {
                            beginning = search[index];

                        } else if (counter >= 3 && index + 1 == search.length - 1) {

                            if (beginning.compareTo(clone[p]) > 0) {
                                LineSegment temp1 = new LineSegment(clone[p], search[index + 1]);
                                sList.add(temp1);

                            }
                            counter = 0;
                        }

                    } else if (counter >= 3) {

                        if (beginning.compareTo(clone[p]) > 0) {
                            LineSegment temp1 = new LineSegment(clone[p], search[index]);
                            sList.add(temp1);

                        }

                        counter = 0;
                    } else {
                        counter = 0;

                    }
                }
            }

        }
        LArr = sList.toArray(new LineSegment[sList.size()]);
    }


    public int numberOfSegments() {
        return LArr.length;
    }

    public LineSegment[] segments() {
        return LArr.clone();
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
