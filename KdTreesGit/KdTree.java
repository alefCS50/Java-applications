import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

    private static final boolean vertical = true;
    private static final boolean horizontal = false;

    private Node root;    // the root of KdTree
    private int size;     // the number of points in the KdTree

    // KdTree helper node data type
    private static class Node {
        private Point2D p;            // the point
        private Node lb;              // the left/bottom subtree
        private Node rt;              // the right/top subtree
        private boolean divide;       // true->vertical, false->horizontal

        public Node(Point2D p) {
            this.p = p;
        }
    }

    // construct an empty set of points
    public KdTree() {
        size = 0;
        root = null;
    }

    // is the set empty?
    public boolean isEmpty() {
        return root == null;
    }

    // number of points in the set
    public int size() {
        return size;
    }

    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument to insert() is null\n");
        }

        root = insert(root, p, vertical);
    }

    private Node insert(Node h, Point2D p, boolean divide) {
        if (h == null) {
            Node tmp = new Node(p);
            tmp.divide = divide;
            size++;
            return tmp;
        }

        double x = p.x();
        double y = p.y();
        double hx = h.p.x();
        double hy = h.p.y();
        if (h.divide == vertical) {
            if (x > hx) h.rt = insert(h.rt, p, !h.divide);
            else if (x < hx) h.lb = insert(h.lb, p, !h.divide);
                // go right if x equals hx and do nothing if y also equals hy
            else if (y != hy) h.rt = insert(h.rt, p, !h.divide);
        }
        if (h.divide == horizontal) {
            if (y > hy) h.rt = insert(h.rt, p, !h.divide);
            else if (y < hy) h.lb = insert(h.lb, p, !h.divide);
                // go top if y equals hy and do nothing if x also equals hx
            else if (x != hx) h.rt = insert(h.rt, p, !h.divide);
        }

        return h;
    }

    // does the set contain point p?
    public boolean contains(Point2D p) {
        if (p == null) {
            throw new IllegalArgumentException("argument to contains() is null\n");
        }
        return contains(root, p);
    }

    private boolean contains(Node h, Point2D p) {
        while (h != null) {
            if (h.divide == vertical) {
                if (p.x() > h.p.x()) h = h.rt;
                else if (p.x() < h.p.x()) h = h.lb;
                else if (p.y() != h.p.y()) h = h.rt;
                else return true;
            } else if (h.divide == horizontal) {
                if (p.y() > h.p.y()) h = h.rt;
                else if (p.y() < h.p.y()) h = h.lb;
                else if (p.x() != h.p.x()) h = h.rt;
                else return true;
            }
        }
        return false;
    }

    public void draw() {

    }

    private void draw(Node x, double x1, double y1, double x2, double y2) {

    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new IllegalArgumentException();
        Queue<Point2D> q = new Queue<>();
        RectHV rootR = new RectHV(0.0, 0.0, 1.0, 1.0);
        range(root, rootR, rect, q);


        return q;

    }

    private void range(Node h, RectHV hRect, RectHV queryRect, Queue<Point2D> pointsInRect) {
        if (h == null) return;
        if (!hRect.intersects(queryRect)) return;

        if (queryRect.contains(h.p)) pointsInRect.enqueue(h.p);

        if (h.divide == vertical) {
            double ymin = hRect.ymin();
            double ymax = hRect.ymax();

            double xmin = h.p.x();
            double xmax = hRect.xmax();
            range(h.rt, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);

            xmin = hRect.xmin();
            xmax = h.p.x();
            range(h.lb, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);
        } else if (h.divide == horizontal) {
            double xmin = hRect.xmin();
            double xmax = hRect.xmax();

            double ymin = h.p.y();
            double ymax = hRect.ymax();
            range(h.rt, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);

            ymin = hRect.ymin();
            ymax = h.p.y();
            range(h.lb, new RectHV(xmin, ymin, xmax, ymax), queryRect, pointsInRect);
        }
    }


    public Point2D nearest(Point2D p) {
        if (p == null) throw new IllegalArgumentException();
        if (isEmpty()) return null;

        return null;

    }


    public static void main(String[] args) {

    }
}
