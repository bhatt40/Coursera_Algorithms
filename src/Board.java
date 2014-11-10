import java.util.Arrays;

/**
 * Created by rishee on 11/9/14.
 */
public class Board {

    private int N;
    private int[][] blocks;

    public Board(int[][] blocks) {          // construct a board from an N-by-N array of blocks

        N = blocks[0].length;
        this.blocks = new int[N][];

        for(int a = 0; a < N; a++)
            this.blocks[a] = Arrays.copyOf(blocks[a], N);

    }

    public int dimension() {                // board dimension N
        return N;
    }

    public int hamming()                   // number of blocks out of place
    {
        int outOfPlace = N*N;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] == i * N + j + 1 || (i == N - 1 & j == N - 1)) {
                    outOfPlace--;
                }
            }
        }

        return outOfPlace;
    }

    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
        int totalDistances = 0;

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                if (blocks[i][j] != 0) {
                    totalDistances += Math.abs(i - ((blocks[i][j] - 1) / N));
                    totalDistances += Math.abs(j - ((blocks[i][j] - 1) % N));

                }
            }
        }

        return totalDistances;
    }

    public boolean isGoal() {               // is this board the goal board?

        return (hamming() == 0);
    }

    public Board twin() {                   // a board that is obtained by exchanging two adjacent blocks in the same row
        int newBlocks[][] = new int[N][N];

        for (int i = 0; i < this.N; i++)
        {
            for (int j = 0; j < this.N; j++)
            {
                newBlocks[i][j] = blocks[i][j];
            }
        }

        int swap;

        for (int i = 0; i < this.N; i++)
        {
            for (int j = 0; j < this.N - 1; j++)
            {
                if (newBlocks[i][j] != 0 & newBlocks[i][j+1] != 0)
                {
                    swap = newBlocks[i][j];
                    newBlocks[i][j] = newBlocks[i][j+1];
                    newBlocks[i][j+1] = swap;

                    return new Board(newBlocks);
                }
            }
        }

        return new Board(blocks);
    }

    public boolean equals(Object y) {       // does this board equal y?

        if (y == this)
            return true;

        if (y == null)
            return false;

        if (this.getClass() != y.getClass())
            return false;

        Board that = (Board) y;

        if (this.dimension() != ((Board) y).dimension())
            return false;

        return (Arrays.deepEquals(this.blocks, that.blocks));
    }

    public Iterable<Board> neighbors()     // all neighboring boards
    {
        int row = -1, column = -1;
        Queue<Board> queue = new Queue<Board>();
        int[][] newBlocks = new int[N][N];

        // Find empty block
        for (int i = 0; i < this.N; i++) {
            for (int j = 0; j < this.N; j++) {
                if (this.blocks[i][j] == 0) {
                    row = i;
                    column = j;
                }
            }
        }

        // Get top neighbor
        if (row != 0) {
            for (int a = 0; a < N; a++)
                newBlocks[a] = Arrays.copyOf(blocks[a], N);

            newBlocks[row][column] = newBlocks[row - 1][column];
            newBlocks[row - 1][column] = 0;

            queue.enqueue(new Board(newBlocks));
        }

        // Get bottom neighbor
        if (row != N - 1) {
            for (int a = 0; a < N; a++)
                newBlocks[a] = Arrays.copyOf(blocks[a], N);

            newBlocks[row][column] = newBlocks[row + 1][column];
            newBlocks[row + 1][column] = 0;

            queue.enqueue(new Board(newBlocks));
        }

        // Get left neighbor
        if (column != 0) {
            for (int a = 0; a < N; a++)
                newBlocks[a] = Arrays.copyOf(blocks[a], N);

            newBlocks[row][column] = newBlocks[row][column - 1];
            newBlocks[row][column - 1] = 0;

            queue.enqueue(new Board(newBlocks));
        }

        // Get right neighbor
        if (column != N - 1) {
            for (int a = 0; a < N; a++)
                newBlocks[a] = Arrays.copyOf(blocks[a], N);

            newBlocks[row][column] = newBlocks[row][column + 1];
            newBlocks[row][column + 1] = 0;

            queue.enqueue(new Board(newBlocks));
        }

        return queue;
    }

    public String toString() {              // string representation of this board (in the output format specified below)

        StringBuilder s = new StringBuilder();
        s.append(this.N + "\n");
        for (int i = 0; i < this.N; i++)
        {
            for (int j = 0; j < this.N; j++)
            {
                s.append(String.format("%2d ", this.blocks[i][j]));
            }
            s.append("\n");
        }
        return s.toString();
    }

    public static void main(String[] args) { // unit tests (not graded)

        int[][] bob = {{8, 1, 3},{4, 0, 2},{7, 6, 5}};

        Board board = new Board(bob);

        System.out.println(board.hamming());
        System.out.println("");
        System.out.println(board.manhattan());

    }
}

