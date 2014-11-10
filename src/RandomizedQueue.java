import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by rishee on 11/7/14.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] items;
    private int N;                          // size of queue

    public RandomizedQueue() {               // construct an empty randomized queue

        this.items = (Item[]) new Object[1];
        this.N = 0;
    }

    public boolean isEmpty() {                // is the queue empty?

        return (this.N == 0);
    }

    public int size() {                       // return the number of items on the queue

        return this.N;
    }

    public void enqueue(Item item) {          // add the item

        this.checkNull(item);

        if (this.items.length == this.N)
            this.resize(N * 2);

        this.items[this.N++] = item;
    }

    public Item dequeue() {                   // delete and return a random item

        this.checkEmpty();

        int index = StdRandom.uniform(this.N);
        Item item = this.items[index];

        this.items[index] = this.items[this.N - 1];
        N--;

        if ( this.N < this.items.length / 4 ) {
            this.resize(this.items.length / 2);
        }

        return item;
    }

    public Item sample() {                     // return (but do not delete) a random item

        this.checkEmpty();
        return this.items[StdRandom.uniform(this.N)];
    }

    public Iterator<Item> iterator() {        // return an independent iterator over items in random order
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private Item[] copy;
        private int index;

        public RandomizedQueueIterator() {
            copy = (Item[]) new Object[N];
            for(int i = 0; i < N; i++)
                copy[i] = items[i];
            StdRandom.shuffle(copy);
            index = 0;
        }

        public boolean hasNext() {
            return index < N;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext())
                throw new NoSuchElementException();

            return copy[index++];
        }
    }

    private void resize(int capacity) {

        Item[] copy = (Item[]) new Object[capacity];
        for(int a = 0; a < this.N; a++) {
            copy[a] = this.items[a];
        }
        this.items = copy;
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

        RandomizedQueue<String> q = new RandomizedQueue<String>();
        String item = StdIn.readString();
        while (!item.equals("exit")) {
            if (item.equals("-"))
                StdOut.print(q.dequeue() + " ");
            else if (item.equals("list")) {
                for (String s : q)
                    StdOut.println(s);
            } else
                q.enqueue(item);
            item = StdIn.readString();
        }
        StdOut.println("(" + q.size() + " left on queue)");
    }

}
