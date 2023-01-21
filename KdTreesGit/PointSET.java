import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.SET;

public class PointSET {
    private final SET<Point2D> points;

    public PointSET() {
        points = new SET<>();
    }


    public boolean isEmpty() {
        return points.size() == 0;
    }

    public int size() {
        return points.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        points.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        return points.contains(p);
    }

    public void draw() {
        for (Point2D p : points) p.draw();
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> q = new Queue<>();

        for (Point2D p : points) {
            if (rect.contains(p)) q.enqueue(p);
        }
        return q;
    }


    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException("");
        Point2D minP = null;
        double minValue = 1000.0;

        for (Point2D t : points) {
            if (t == p) continue;

            if (p.distanceTo(t) < minValue) {
                minP = t;
                minValue = p.distanceTo(t);

            }
        }
        return minP;
    }

}
