import edu.princeton.cs.algs4.Picture;

import java.awt.Color;

public class SeamCarver {
    private Picture picCopy;


    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException("");
        picCopy = new Picture(picture);

    }

    // current picture
    public Picture picture() {
        return picCopy;
    }

    // width of current picture
    public int width() {
        return picCopy.width();
    }

    // height of current picture
    public int height() {
        return picCopy.height();
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || x > picCopy.width() - 1 || y < 0 || y > picCopy.height() - 1)
            throw new IllegalArgumentException("");

        //if pixel is on corner, return 1000
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) return 1000.0;

        Color x1, x2, y1, y2;

        x1 = picCopy.get(x - 1, y);
        x2 = picCopy.get(x + 1, y);
        y1 = picCopy.get(x, y - 1);
        y2 = picCopy.get(x, y + 1);

        double deltaSquaredX = Math.pow(x1.getRed() - x2.getRed(), 2) + Math.pow(x1.getBlue() - x2.getBlue(), 2) + Math.pow(x1.getGreen() - x2.getGreen(), 2);
        double deltaSquaredY = Math.pow(y1.getRed() - y2.getRed(), 2) + Math.pow(y1.getBlue() - y2.getBlue(), 2) + Math.pow(y1.getGreen() - y2.getGreen(), 2);

        return Math.sqrt(deltaSquaredX + deltaSquaredY);
    }


    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        //transposing
        Picture temp = new Picture(height(), width());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                int rgb = picCopy.getRGB(col, row);
                temp.setRGB(row, col, rgb);
            }
        }
        picCopy = temp;

        //the answer
        int[] answer = findVerticalSeam();

        //transposing back
        Picture tempBack = new Picture(height(), width());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                int rgb = picCopy.getRGB(col, row);
                tempBack.setRGB(row, col, rgb);
            }
        }
        picCopy = tempBack;

        return answer;


    }


    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {
        char mode = 'v';
        String end = null;

        if (height() == 1) {
            return new int[]{0};
        }

        String[][] edgeTo = new String[width()][height()];
        double[][] energyTo = new double[width()][height()];
        double max = Double.MAX_VALUE;


        for (int row = 0; row < height() - 1; row++) {
            for (int col = 0; col < width(); col++) {

                String current = id2str(col, row);

                //for first row
                if (row == 0) {
                    edgeTo[col][row] = null;
                    energyTo[col][row] = energy(col, row);

                }
                //get the smallest of the above energies
                for (int k = col - 1; k <= col + 1; k++) {
                    if (k >= 0 && k < width()) {

                        String next = id2str(k, row + 1);
                        double newEn = energyTo[col][row] + energy(k, row + 1);

                        if (edgeTo[k][row + 1] == null || newEn < energyTo[k][row + 1]) {

                            energyTo[k][row + 1] = newEn;
                            edgeTo[k][row + 1] = current;


                            if (row + 1 == height() - 1 && newEn < max) {
                                max = newEn;
                                end = next;
                            }
                        }
                    }
                }
            }
        }


        return getSeam(mode, edgeTo, end);


    }


    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("");
        if (seam.length != width()) throw new IllegalArgumentException("");

        //transposing
        Picture temp = new Picture(height(), width());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                int rgb = picCopy.getRGB(col, row);
                temp.setRGB(row, col, rgb);
            }
        }
        picCopy = temp;

        removeVerticalSeam(seam);

        //transposing back
        Picture tempBack = new Picture(height(), width());
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                int rgb = picCopy.getRGB(col, row);
                tempBack.setRGB(row, col, rgb);
            }
        }
        picCopy = tempBack;

    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) throw new IllegalArgumentException("");
        if (seam.length != height()) throw new IllegalArgumentException("");

        Picture temp = new Picture(width() - 1, height());


        for (int row = 0; row < height(); row++) {
            int colCount = 0;
            for (int col = 0; col < width(); col++) {
                if (seam[row] != col) {
                    int rgb = picCopy.getRGB(col, row);
                    temp.setRGB(colCount, row, rgb);
                    colCount++;
                }
            }
        }

        picCopy = temp;


    }

    private String id2str(int col, int row) {
        return col + " " + row;
    }

    private int[] toInt(String s) {
        int[] answer = new int[2];
        answer[0] = Integer.parseInt(s.split(" ")[0]);
        answer[1] = Integer.parseInt(s.split(" ")[1]);
        return answer;
    }

    private int[] getSeam(char mode, String[][] edgeTo, String end) {
        int[] answer = new int[height()];
        int counter = height();

        String current = end;

        while (counter > 0) {
            answer[--counter] = toInt(current)[0];
            current = (String) edgeTo[toInt(current)[0]][toInt(current)[1]];

        }

        return answer;
    }


    //  unit testing (optional)
    public static void main(String[] args) {
        Picture test = new Picture("Red.jpg");
        SeamCarver seam = new SeamCarver(test);
        System.out.println(seam.energy(290, 150));
    }

}
