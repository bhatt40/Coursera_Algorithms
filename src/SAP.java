/**
 * Created by rishee on 11/10/14.
 */
public class SAP {

    private Digraph G;
    private BreadthFirstDirectedPaths bfsV;
    private BreadthFirstDirectedPaths bfsW;
    private int shortestDistance;
    private int shortestAncestor;


    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.G = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {

        this.getShortestPath(v, w);

        return this.shortestDistance;
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {

        this.getShortestPath(v, w);

        return this.shortestAncestor;

    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {

        this.getShortestPath(v, w);

        return this.shortestDistance;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
    {
        this.getShortestPath(v, w);

        return this.shortestAncestor;
    }

    private void getShortestPath(int v, int w) {

        this.bfsV = new BreadthFirstDirectedPaths(G, v);
        this.bfsW = new BreadthFirstDirectedPaths(G, w);

        this.shortestDistance = Integer.MAX_VALUE;
        this.shortestAncestor = -1;
        int distance;

        for (int a = 0; a < G.V(); a++)
        {
            if (this.bfsV.hasPathTo(a) & this.bfsW.hasPathTo(a)) {
                distance = this.bfsV.distTo(a) + this.bfsW.distTo(a);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    this.shortestAncestor = a;
                }
            }
        }

        if (this.shortestAncestor == -1)
            this.shortestDistance = -1;
    }

    private void getShortestPath(Iterable<Integer> v, Iterable<Integer> w) {

        this.bfsV = new BreadthFirstDirectedPaths(G, v);
        this.bfsW = new BreadthFirstDirectedPaths(G, w);

        this.shortestDistance = Integer.MAX_VALUE;
        this.shortestAncestor = -1;
        int distance;

        for (int a = 0; a < G.V(); a++)
        {
            if (this.bfsV.hasPathTo(a) & this.bfsW.hasPathTo(a)) {
                distance = this.bfsV.distTo(a) + this.bfsW.distTo(a);
                if (distance < shortestDistance) {
                    shortestDistance = distance;
                    this.shortestAncestor = a;
                }
            }
        }

        if (this.shortestAncestor == -1)
            this.shortestDistance = -1;
    }

    // do unit testing of this class
    public static void main(String[] args) {

        In in = new In(args[0]);
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        while (!StdIn.isEmpty()) {
            int v = StdIn.readInt();
            int w = StdIn.readInt();
            int length   = sap.length(v, w);
            int ancestor = sap.ancestor(v, w);
            StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
        }
    }
}