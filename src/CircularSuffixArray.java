import java.util.Arrays;

public class CircularSuffixArray {

    private Suffix[] suffixes;


    // circular suffix array of s
    public CircularSuffixArray(String s) {

        int N = s.length();
        this.suffixes = new Suffix[N];
        for (int i = 0; i < N; i++)
            suffixes[i] = new Suffix(s, i);
        Arrays.sort(suffixes);
    }

    private static class Suffix implements Comparable<Suffix> {
        private final String text;
        private final int index;

        private Suffix(String text, int index) {
            this.text = text;
            this.index = index;
        }

        private int length() {
            return text.length() - index;
        }

        private char charAt(int i) {
            return text.charAt((index + i) % text.length());
        }

        public int compareTo(Suffix that) {
            if (this == that) return 0;  // optimization
            for (int i = 0; i < text.length(); i++) {
                if (this.charAt(i) < that.charAt(i)) return -1;
                if (this.charAt(i) > that.charAt(i)) return +1;
            }
            return this.length() - that.length();
        }

        public String toString() {
            return (text.substring(index) + text.substring(0, index));
        }
    }

    // length of s
    public int length() {

        return suffixes.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {

        if (i < 0 || i >= suffixes.length)
            throw new IndexOutOfBoundsException();

        return suffixes[i].index;
    }

    // returns ith suffix
    public String ith(int i) {
        return suffixes[i].toString();
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
//        String s = StdIn.readAll().replaceAll("\\s+", " ").trim();
        String s = args[0].trim();
        CircularSuffixArray suffix = new CircularSuffixArray(s);

        StdOut.println("  i ind lcp rnk select");
        StdOut.println("---------------------------");

        for (int i = 0; i < s.length(); i++) {
            int index = suffix.index(i);
            String ith = "\"" + suffix.ith(i) + "\"";
            if (i == 0) {
                StdOut.printf("%3d %3d %s\n", i, index, ith);
            }
            else {
                StdOut.printf("%3d %3d %s\n", i, index, ith);
            }
        }
    }
}