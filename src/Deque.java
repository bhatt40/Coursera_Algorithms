import java.util.NoSuchElementException;
import java.util.Iterator;

/**
 * Created by rishee on 11/7/14.
 */
public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node next;
        private Node previous;

        protected Node(Item item) {
            this.item = item;
            this.next = null;
            this.previous = null;
        }
    }

    private int N;                          // size of Deque
    private Node first;                     // first node in Deque
    private Node last;                      // last node in Deque

    public Deque() {                          // construct an empty deque
        this.first = null;
        this.last = null;
        N = 0;
    }

    public boolean isEmpty() {                // is the deque empty?

        return (N == 0);
    }

    public int size() {                        // return the number of items on the deque

        return N;
    }

    public void addFirst(Item item) {         // insert the item at the front

        this.checkNull(item);

        Node oldFirst = this.first;
        Node newFirst = new Node(item);

        // Update new Node
        newFirst.next = oldFirst;

        // Update old Node
        if (oldFirst != null)
            oldFirst.previous = newFirst;

        // Update Deque
        this.first = newFirst;
        this.N++;

        if (N == 1)
            this.last = this.first;
    }

    public void addLast(Item item) {          // insert the item at the end

        this.checkNull(item);

        Node oldLast = this.last;
        Node newLast = new Node(item);

        // Update new Node
        newLast.previous = oldLast;

        // Update old Node
        if (oldLast != null)
            oldLast.next = newLast;

        // Update Deque
        this.last = newLast;
        this.N++;

        if (N == 1)
            this.first = this.last;
    }

    public Item removeFirst() {               // delete and return the item at the front

        this.checkEmpty();

        Node newFirst = this.first.next;
        Node oldFirst = this.first;

        // Update new Node
        if (newFirst != null)
            newFirst.previous = null;

        // Update Deque
        this.first = newFirst;
        this.N--;

        return oldFirst.item;
    }

    public Item removeLast() {                // delete and return the item at the end

        this.checkEmpty();

        Node newLast = this.last.previous;
        Node oldLast = this.last;

        // Update new Node
        if (newLast != null)
            newLast.next = null;

        // Update Deque
        this.last = newLast;
        this.N--;

        return oldLast.item;
    }

    public Iterator<Item> iterator() {       // return an iterator over items in order from front to end

        return new ListIterator();
    }

    // an iterator, doesn't implement remove() since it's optional
    private class ListIterator implements Iterator<Item> {
        private Node current = first;
        public boolean hasNext()  { return current != null;                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    private void checkNull(Item item) {
        if (item == null)
            throw new NullPointerException();
    }

    private void checkEmpty() {
        if(N == 0)
            throw new NoSuchElementException();
    }

    public static void main(String[] args) {  // unit testing

        Deque<String> q = new Deque<String>();
        String item = StdIn.readString();
        while (!item.equals("exit"))
        {
            if (item.equals("-"))
                StdOut.print(q.removeLast() + " ");
            else if (item.equals("list"))
            {
                for (String s: q)
                    StdOut.println(s);
            }
            else
                q.addFirst(item);
            item = StdIn.readString();
        }
        StdOut.println("(" + q.size() + " left on queue)");

    }
}

