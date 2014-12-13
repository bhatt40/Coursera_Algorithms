public class MoveToFront {

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {


        char[] moveToFront = buildMoveToFront();

        char nextLetter = '\u0000';
        int output;

        String s = BinaryStdIn.readString();

        for (int i = 0; i < s.length(); i++) {

            BinaryStdOut.write((byte)(updateMoveToFront(s.charAt(i), moveToFront)));
        }
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

        char[] moveToFront = buildMoveToFront();

        String s = BinaryStdIn.readString();

        for (int i = 0; i < s.length(); i++) {

            BinaryStdOut.write(moveToFront[s.charAt(i)]);
            updateMoveToFront(moveToFront[s.charAt(i)], moveToFront);
        }

    }

    private static char[] buildMoveToFront() {

        char[] moveToFront = new char[256];

        for (int i = 0; i < moveToFront.length; i++) {
            moveToFront[i] = (char) i;
        }

        return moveToFront;
    }

    private static int updateMoveToFront(char nextLetter, char[] moveToFront) {

        int index = 0;
        char lastLetter = '\u0000';

        while (moveToFront[index] != nextLetter) {
            if (index != 0) {
                lastLetter = swapWithLast(moveToFront, index, lastLetter);
            } else {
                lastLetter = moveToFront[index];
            }
            index++;
        }

        if (lastLetter != '\u0000')
            moveToFront[index] = lastLetter;

        moveToFront[0] = nextLetter;
        return index;
    }

    private static char swapWithLast(char[] moveToFront, int index, char lastLetter) {

        char swap = moveToFront[index];
        moveToFront[index] = lastLetter;

        return swap;
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
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