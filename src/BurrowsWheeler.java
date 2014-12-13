import java.util.Arrays;

public class BurrowsWheeler {

    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode() {

        String s = BinaryStdIn.readString();

        CircularSuffixArray suffixArray = new CircularSuffixArray(s);

        int first = -1;
        char[] charArray = new char[suffixArray.length()];

        for (int i = 0; i < suffixArray.length(); i++) {

            int index = suffixArray.index(i);
            if (index == 0) {
                first = i;
                index = suffixArray.length();
            }

            charArray[i] = s.charAt(index - 1);
        }

        BinaryStdOut.write(Integer.toString(first));
        BinaryStdOut.write(String.valueOf(charArray));
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {

        String inputString = BinaryStdIn.readString();
        int first = Integer.decode(String.valueOf(inputString.charAt(0)));
        String lastChars = inputString.substring(1);

        char[] firstChars = lastChars.toCharArray();
        Arrays.sort(firstChars);

        int[] next = new int[firstChars.length];
        boolean[] marked = new boolean[firstChars.length];
        for (int i = 0; i < next.length; i++) {
            next[i] = -1;
            marked[i] = false;
        }

        // Fill next[] array
        for (int i = 0; i < firstChars.length; i++) {
            next[i] = findNext(firstChars[i], marked, lastChars);
            marked[next[i]] = true;
        }

        char[] decoded = new char[firstChars.length];

        // Rebuild string using next[] array
        int nextIndex = first;
        int decodedIndex = 0;
        do {
            decoded[decodedIndex++] = firstChars[nextIndex];
            nextIndex = next[nextIndex];
        } while (nextIndex != first);

        BinaryStdOut.write(String.valueOf(decoded));
    }

    private static int findNext(char letter, boolean[] marked, String lastChars) {

        for (int i = 0; i < lastChars.length(); i++) {
            if (lastChars.charAt(i) == letter & marked[i] == false)
                return i;
        }

        throw new IllegalArgumentException();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args) {

        switch (args[0].charAt(0)) {
            case '-':
                encode();
                break;
            case '+':
                decode();
                break;
            default:
                throw new IllegalArgumentException();
        }

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }
}
