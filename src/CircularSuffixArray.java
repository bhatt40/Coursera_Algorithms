
public class CircularSuffixArray {

    private static final int CUTOFF =  5;   // cutoff to insertion sort (any value between 0 and 12)

    private final char[] text;
    private final int[] index;   // index[i] = j means text.substring(j) is ith largest suffix
    private final int N;         // number of characters in text


    // circular suffix array of s
    public CircularSuffixArray(String s) {

        N = s.length();
        s = s + '\0';
        this.text = s.toCharArray();
        this.index = new int[N];
        for (int i = 0; i < N; i++)
            index[i] = i;

        // shuffle

        sort(0, N-1, 0);
    }

    // length of s
    public int length() {

        return N;
    }

    // returns index of ith sorted suffix
    public int index(int i) {

        if (i < 0 || i >= N)
            throw new IndexOutOfBoundsException();

        return index[i];
    }

    // 3-way string quicksort lo..hi starting at dth character
    private void sort(int lo, int hi, int d) {

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        char v = text[index[lo] + d];
        int i = lo + 1;
        while (i <= gt) {
            int t = text[index[i] + d];
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else            i++;
        }

        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi].
        sort(lo, lt-1, d);
        if (v > 0) sort(lt, gt, d+1);
        sort(gt+1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(index[j], index[j-1], d); j--)
                exch(j, j-1);
    }

    // is text[i+d..N) < text[j+d..N) ?
    private boolean less(int i, int j, int d) {
        if (i == j) return false;
        i = i + d;
        j = j + d;
        while (i < N && j < N) {
            if (text[i] < text[j]) return true;
            if (text[i] > text[j]) return false;
            i++;
            j++;
        }
        return i > j;
    }

    // exchange index[i] and index[j]
    private void exch(int i, int j) {
        int swap = index[i];
        index[i] = index[j];
        index[j] = swap;
    }

    // unit testing of the methods (optional)
    public static void main(String[] args) {
        String s = args[0].trim();
        CircularSuffixArray suffix = new CircularSuffixArray(s);

        SuffixArray suffixReference = new SuffixArray(s);
        boolean check = true;
        for (int i = 0; check && i < s.length(); i++) {
            if (suffixReference.index(i) != suffix.index(i)) {
                StdOut.println("suffixReference(" + i + ") = " + suffixReference.index(i));
                StdOut.println("suffix(" + i + ") = " + suffix.index(i));
                String ith = "\"" + s.substring(suffix.index(i), Math.min(suffix.index(i) + 50, s.length())) + "\"";
                String jth = "\"" + s.substring(suffixReference.index(i), Math.min(suffixReference.index(i) + 50, s.length())) + "\"";
                StdOut.println(ith);
                StdOut.println(jth);
                check = false;
            }
        }

        // StdOut.println("rank(" + args[0] + ") = " + suffix.rank(args[0]));

        StdOut.println("  i ind lcp rnk  select");
        StdOut.println("---------------------------");

        for (int i = 0; i < s.length(); i++) {
            int index = suffix.index(i);
            String ith = "\"" + s.substring(index, Math.min(index + 50, s.length())) + "\"";
            if (i == 0) {
                StdOut.printf("%3d %3d  %s\n", i, index, ith);
            }
            else {
                // int lcp  = suffix.lcp(suffix.index(i), suffix.index(i-1));
                StdOut.printf("%3d %3d  %s\n", i, index, ith);
            }
        }
    }
}