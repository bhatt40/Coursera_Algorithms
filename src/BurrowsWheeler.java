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

        BinaryStdOut.write(first);
        BinaryStdOut.write(String.valueOf(charArray));
        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode() {

        int first = BinaryStdIn.readInt();
        char[] lastChars = BinaryStdIn.readString().toCharArray();
        char[] firstChars = Arrays.copyOf(lastChars, lastChars.length);
        charSort(firstChars);

        int[] next = new int[firstChars.length];
        boolean[] marked = new boolean[firstChars.length];
        for (int i = 0; i < marked.length; i++) {
            marked[i] = false;
        }

        // Fill next[] array
        for (int i = 0; i < firstChars.length; i++) {
            next[i] = findNext(firstChars[i], marked, lastChars);
            marked[next[i]] = true;
        }

//        char[] decoded = new char[firstChars.length];

        // Rebuild string using next[] array
        int nextIndex = first;
        int counter = 0;
        do {
//            decoded[decodedIndex++] = firstChars[nextIndex];
            BinaryStdOut.write(firstChars[nextIndex]);
            nextIndex = next[nextIndex];
            counter++;
        } while (counter < firstChars.length);

//        BinaryStdOut.write(String.valueOf(decoded));

        BinaryStdOut.flush();
        BinaryStdOut.close();
    }

    private static int findNext(char letter, boolean[] marked, char[] lastChars) {

        for (int i = 0; i < lastChars.length; i++) {
            if (lastChars[i] == letter & marked[i] == false)
                return i;
        }

        throw new IllegalArgumentException();
    }

    private static void charSort(char[] charArray) {

        int[] numChars = new int[256];

        for (int i = 0; i < charArray.length; i++) {
            numChars[charArray[i]]++;
        }

        int charArrayIndex = 0;
        for (int i = 0; i < numChars.length; i++) {
            while (numChars[i] > 0) {
                charArray[charArrayIndex++] = (char) i;
                numChars[i]--;
            }
        }
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
    }
}
