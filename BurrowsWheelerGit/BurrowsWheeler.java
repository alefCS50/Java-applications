import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {
    private final static int R = 256;

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray cArr = new CircularSuffixArray(input);
        int[] pointers = new int[cArr.length()];
        //init pointers
        for (int i = 0; i < cArr.length(); i++) {
            pointers[i] = cArr.index(i);
        }
        int first = 0;
        for (int i = 0; i < cArr.length(); i++) {
            if (cArr.index(i) == 0) first = i;
        }

        BinaryStdOut.write(first);
        //System.out.println(first);

        for (int i = 0; i < cArr.length(); i++) {
            int lastindex = pointers[i] + cArr.length() - 1;
            if (lastindex > cArr.length() - 1) lastindex = transform(lastindex, cArr.length());
            char c = input.charAt(lastindex);
            BinaryStdOut.write(c);
            //System.out.print(c);
        }
        BinaryStdIn.close();
        BinaryStdOut.close();
    }

    private static int transform(int index, int length) {
        int over = index - length;
        return over;
    }


    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String input = BinaryStdIn.readString();
        //int first = StdIn.readInt();
        //String input = StdIn.readString();
        //Do radix sort
        int[] count = new int[R + 1];
        char[] firstCol = new char[input.length()];
        int[] next = new int[input.length()];

        for (int i = 0; i < input.length(); i++) {
            count[input.charAt(i) + 1]++;
        }
        for (int i = 0; i < R; i++) {
            count[i + 1] += count[i];
        }
        for (int i = 0; i < input.length(); i++) {
            int index = count[input.charAt(i)]++;
            firstCol[index] = input.charAt(i);
            next[index] = i;
        }

        int index = first;
        for (int i = 0; i < firstCol.length; i++) {
            BinaryStdOut.write(firstCol[index]);
            //System.out.print(firstCol[index]);
            index = next[index];
        }

        BinaryStdOut.close();
        BinaryStdIn.close();

    }


    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-")) transform();
        if (args[0].equals("+")) inverseTransform();
    }

}
