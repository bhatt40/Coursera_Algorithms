import java.util.Comparator;

/**
 * Created by rishee on 11/9/14.
 */
public class Solver {

    private class Node {

        private Board board;
        private int moves;
        private Node previous;

        public Node(Board board, int moves, Node previous)
        {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
        }

        public Board getBoard() {
            return board;
        }

        public int getMoves() {
            return moves;
        }

        public Node getPrevious() {
            return previous;
        }
    }

    private class ManhattanOrder implements Comparator<Node> {

        @Override
        public int compare(Node a, Node b) {
            int aMan = a.getBoard().manhattan() + a.getMoves();
            int bMan = b.getBoard().manhattan() + b.getMoves();

            if (aMan < bMan)
                return -1;
            else if (aMan == bMan)
                return 0;
            else
                return 1;
        }
    }

    private final Comparator<Node> MANHATTAN_ORDER = new ManhattanOrder();

    private MinPQ<Node> priorityQueue;
    private MinPQ<Node> twinPriorityQueue;
    private boolean solvable;
    private int moves;
    private Node solution;

    public Solver(Board initial) {          // find a solution to the initial board (using the A* algorithm)

        Node node = new Node(initial, 0, null);
        Node twinNode = new Node(initial.twin(), 0, null);

        priorityQueue = new MinPQ<Node>(MANHATTAN_ORDER);
        priorityQueue.insert(node);

        twinPriorityQueue = new MinPQ<Node>(MANHATTAN_ORDER);
        twinPriorityQueue.insert(twinNode);

        Boolean solved = false;

        if (initial.isGoal()) {
            solvable = true;
            moves = 0;
            solution = new Node(initial, 0, null);
        }
        else {
            while (!solved) {
                node = priorityQueue.delMin();
                twinNode = twinPriorityQueue.delMin();

                Node neighborNode, twinNeighborNode;

                if (node.getBoard().isGoal() || twinNode.getBoard().isGoal()) {
                    solved = true;
                    if(node.getBoard().isGoal()) {
                        solvable = true;
                        moves = node.getMoves();
                        solution = node;
                    }
                    else {
                        solvable = false;
                        solution = null;
                        moves = -1;
                    }
                } else {

                    for (Board neighbor : node.getBoard().neighbors()) {
                        neighborNode = new Node(neighbor, node.getMoves() + 1, node);
                        if (node.getPrevious() == null || !neighborNode.getBoard().equals(node.getPrevious().getBoard())) {
                            priorityQueue.insert(neighborNode);
                        }
                    }

                    for (Board twinNeighbor : twinNode.getBoard().neighbors()) {
                        twinNeighborNode = new Node(twinNeighbor, node.getMoves() + 1, twinNode);
                        if (twinNode.getPrevious() == null || !twinNeighborNode.getBoard().equals(twinNode.getPrevious().getBoard())) {
                            twinPriorityQueue.insert(twinNeighborNode);
                        }
                    }
                }
            }
        }
    }

    public boolean isSolvable() {           // is the initial board solvable?
        return this.solvable;
    }

    public int moves() {                    // min number of moves to solve initial board; -1 if unsolvable
        return this.moves;
    }

    public Iterable<Board> solution() {     // sequence of boards in a shortest solution; null if unsolvable

        if(this.isSolvable()) {
            Stack<Board> solutionQueue = new Stack<Board>();

            Node node = solution;

            do {
                solutionQueue.push(node.getBoard());
                node = node.getPrevious();
            } while (node != null);

            return (Iterable<Board>) solutionQueue;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
