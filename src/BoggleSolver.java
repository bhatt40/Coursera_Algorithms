import java.util.Arrays;
import java.util.Set;
import java.util.TreeSet;

public class BoggleSolver {

    private TrieSET26 dictionary;

    private final int[] WORD_SCORES = {0, 0, 0, 1, 1, 2, 3, 5, 11};

    private SET<String> validWords;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {

        this.dictionary = new TrieSET26();

        // Create dictionary
        for (String s : dictionary) {
            this.dictionary.add(s);
        }
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {

        validWords = new SET<String>();

        for(int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {

                Boolean[][] visited = new Boolean[board.rows()][board.cols()];
                for (int a = 0; a < board.rows(); a++) {
                    for (int b = 0; b < board.cols(); b++) {
                        visited[a][b] = false;

                    }
                }
                StringBuilder word = new StringBuilder();
                addLetter(board, word, i, j);
                getValidWords(board, visited, i, j, word);
            }
        }

        return validWords;
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {

        if (dictionary.contains(word)) {
            return WORD_SCORES[Math.min(word.length(), 8)];
        }

        return 0;
    }

    private void getValidWords(BoggleBoard board, Boolean[][] visited, int row, int column, StringBuilder word) {

        if (validWord(word)) {
            validWords.add(word.toString());
        }

        visited[row][column] = true;

        // Top neighbor
        if (row != 0) {
            if (!visited[row - 1][column]) {
                addLetter(board, word, row - 1, column);
                if (dictionary.hasKeysWithPrefix(word.toString())) {
                    getValidWords(board, visited, row - 1, column, word);
                }
                removeLetter(word);
            }
        }

        // Bottom neighbor
        if (row != board.rows() - 1) {
            if (!visited[row + 1][column]) {
                addLetter(board, word, row + 1, column);
                if (dictionary.hasKeysWithPrefix(word.toString())) {
                    getValidWords(board, visited, row + 1, column, word);
                }
                removeLetter(word);
            }
        }

        // Left neighbor
        if (column != 0) {
            if (!visited[row][column - 1]) {
                addLetter(board, word, row, column - 1);
                if (dictionary.hasKeysWithPrefix(word.toString())) {
                    getValidWords(board, visited, row, column - 1, word);
                }
                removeLetter(word);
            }
        }

        // Right neighbor
        if (column != board.cols() - 1) {
            if (!visited[row][column + 1]) {
                addLetter(board, word, row, column + 1);
                if (dictionary.hasKeysWithPrefix(word.toString())) {
                    getValidWords(board, visited, row, column + 1, word);
                }
                removeLetter(word);
            }
        }

        // Top-left neighbor
        if (row != 0 & column != 0) {
            if (!visited[row - 1][column - 1]) {
                addLetter(board, word, row - 1, column - 1);
                if (dictionary.hasKeysWithPrefix(word.toString())) {
                    getValidWords(board, visited, row - 1, column - 1, word);
                }
                removeLetter(word);
            }
        }

        // Top-right neighbor
        if (row != 0 & column != board.cols() - 1) {
            if (!visited[row - 1][column + 1]) {
                addLetter(board, word, row - 1, column + 1);
                if (dictionary.hasKeysWithPrefix(word.toString())) {
                    getValidWords(board, visited, row - 1, column + 1, word);
                }
                removeLetter(word);
            }
        }

        // Bottom-left neighbor
        if (row != board.rows() - 1 & column != 0) {
            if (!visited[row + 1][column - 1]) {
                addLetter(board, word, row + 1, column - 1);
                if (dictionary.hasKeysWithPrefix(word.toString())) {
                    getValidWords(board, visited, row + 1, column - 1, word);
                }
                removeLetter(word);
            }
        }

        // Bottom-right neighbor
        if (row != board.rows() - 1 & column != board.cols() - 1) {
            if (!visited[row + 1][column + 1]) {
                addLetter(board, word, row + 1, column + 1);
                if (dictionary.hasKeysWithPrefix(word.toString())) {
                    getValidWords(board, visited, row + 1, column + 1, word);
                }
                removeLetter(word);
            }
        }

        visited[row][column] = false;
    }

    private void addLetter(BoggleBoard board, StringBuilder word, int row, int column) {

        char letter = board.getLetter(row, column);
        if (letter == 'Q') {
            word.append("QU");
        } else {
            word.append(letter);
        }
    }

    private void removeLetter(StringBuilder word) {

        char letter = word.charAt(word.length() - 1);
        word.deleteCharAt(word.length() - 1);
        if (letter == 'U') {
            letter = word.charAt(word.length() - 1);
            if (letter == 'Q') {
                word.deleteCharAt(word.length() - 1);
            }
        }
    }

    private Boolean validWord(StringBuilder word) {

        if (word.length() > 2) {
            return dictionary.contains(word.toString());
        }

        return false;
    }

    public static void main(String[] args)
    {
        In in = new In(args[0]);
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard(args[1]);
        int score = 0;
        int counter = 0;
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
            counter++;
        }
        StdOut.println("Words = " + counter);
        StdOut.println("Score = " + score);
    }

}