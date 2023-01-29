public class CircularSuffixArray {
    private final int R = 256;
    private String s;
    private int[] sortedArr;
    private int[] index;


    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) throw new IllegalArgumentException("");
        this.s = s;

        int[] ogArr = new int[length()];

        for (int i = 0; i < length(); i++) {
            ogArr[i] = i;
        }

        sort(ogArr);


        index = new int[length()];

        for (int i = 0; i < length(); i++) {
            index[i] = sortedArr[i];
        }


    }

    private void sort(int[] OGarr) {
        int[] tempArr = new int[length()];
        for (int i = 0; i < length(); i++) {
            tempArr[i] = OGarr[i];
        }


        int[] aux = new int[length()];
        sortedArr = sort(tempArr, 0, length() - 1, 0, aux);
    }

    private int[] sort(int[] a, int lo, int hi, int d, int[] aux) {

        // compute frequency counts
        int[] count = new int[R + 2];
        for (int i = lo; i <= hi; i++) {
            //if the index goes out of range, change it to 0
            int index = a[i] + d;
            if (index >= length()) index = transform(index);
            int c = charAt(s, index);
            count[c + 2]++;
        }

        // transform counts to indices
        for (int r = 0; r < R + 1; r++)
            count[r + 1] += count[r];

        // distribute
        for (int i = lo; i <= hi; i++) {
            int index = a[i] + d;
            if (index >= length()) index = transform(index);
            int c = charAt(s, index);
            aux[count[c + 1]++] = a[i];
        }

        // copy back
        for (int i = lo; i <= hi; i++)
            a[i] = aux[i - lo];

        // recursively sort for each character (excludes sentinel -1)
        for (int r = 0; r < R; r++) {
            if (lo + count[r + 1] - 1 - (lo + count[r]) > 0)
                sort(a, lo + count[r], lo + count[r + 1] - 1, d + 1, aux);
        }
        return a;
    }

    private int transform(int index) {
        int over = index - length();
        return over;
    }

    // return dth character of s, -1 if d = length of string
    private int charAt(String s, int d) {
        assert d >= 0 && d <= s.length();
        if (d == s.length()) return -1;
        return s.charAt(d);
    }

    // length of s
    public int length() {
        return s.length();
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > s.length()) throw new IllegalArgumentException("");
        return index[i];
    }

    public static void main(String[] args) {
        CircularSuffixArray c = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(c.index(11));
    }


}
