import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {
    private static final int R = 256; //ASCII

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        char[] arr = new char[R];
        //init arr
        for (int i = 0; i < R; i++) {
            arr[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {

            char c = (char) BinaryStdIn.readChar(8);
            int index = 0;
            for (int i = 0; i < R; i++) {
                if (arr[i] == c) index = i;
            }

            //print the index
            BinaryStdOut.write((int) index, 8);


            //move char to front
            for (int i = index; i > 0; i--) {
                char temp = arr[i];
                arr[i] = arr[i - 1];
                arr[i - 1] = temp;
            }
        }

        BinaryStdIn.close();
        BinaryStdOut.close();

    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        char[] arr = new char[R];
        //init arr
        for (int i = 0; i < R; i++) {
            arr[i] = (char) i;
        }

        while (!BinaryStdIn.isEmpty()) {
            int index = BinaryStdIn.readInt(8);

            BinaryStdOut.write(arr[index]);
            //move char to front
            for (int i = index; i > 0; i--) {
                char temp = arr[i];
                arr[i] = arr[i - 1];
                arr[i - 1] = temp;
            }

        }

        BinaryStdOut.close();
        BinaryStdIn.close();


    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-")) encode();
        if (args[0].equals("+")) decode();
    }

}
