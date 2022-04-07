/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] randomizedQueue;
    private int numberOfItems;

    // construct an empty randomized queue
    public RandomizedQueue() {
        numberOfItems = 0;
        randomizedQueue = (Item[]) new Object[1];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return numberOfItems == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return numberOfItems;
    }

    private void resize(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < numberOfItems; i++) {
            copy[i] = randomizedQueue[i];
        }
        randomizedQueue = copy;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Can't add a null item!");
        }
        if (numberOfItems == randomizedQueue.length) {
            resize(2*randomizedQueue.length);
        }
        randomizedQueue[numberOfItems++] = item;
    }

    private void swap(int pointer1, int pointer2) {
        Item temp = randomizedQueue[pointer1];
        randomizedQueue[pointer1] = randomizedQueue[pointer2];
        randomizedQueue[pointer2] = temp;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException();
        }
        int randomPointer = StdRandom.uniform(0, numberOfItems);
        Item item = randomizedQueue[randomPointer];
        swap(randomPointer, numberOfItems-1);
        randomizedQueue[numberOfItems-1] = null;
        numberOfItems--;
        if (numberOfItems > 0 && numberOfItems == randomizedQueue.length/4) {
            resize(randomizedQueue.length/2);
        }
        return item;
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
           throw new java.util.NoSuchElementException();
        }
        int randomPointer = StdRandom.uniform(0, numberOfItems);
        Item item = randomizedQueue[randomPointer];
        return item;
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        private int i;
        private Item[] iterableRandomizedQueue;

        public RandomizedQueueIterator() {
            i = numberOfItems;
            iterableRandomizedQueue = (Item[]) new Object[i];
            for (int j = 0; j < i; j++) {
               iterableRandomizedQueue[j] = randomizedQueue[j];
            }
        }
        private void swap(int pointer1, int pointer2) {
            Item temp = iterableRandomizedQueue[pointer1];
            iterableRandomizedQueue[pointer1] = iterableRandomizedQueue[pointer2];
            iterableRandomizedQueue[pointer2] = temp;
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
           throw new UnsupportedOperationException();
        }

        public Item next() {
            if (i == 0) {
                throw new java.util.NoSuchElementException();
            }
            int randomPointer = StdRandom.uniform(0, i);
            swap(randomPointer, i-1);
            return iterableRandomizedQueue[--i];
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        randomizedQueue.enqueue(6);
        randomizedQueue.enqueue(5);
        randomizedQueue.enqueue(4);
        randomizedQueue.enqueue(7);
        for (Integer x : randomizedQueue) {
            StdOut.println(x);
        }
        StdOut.println("random sample -> " + randomizedQueue.sample());
        StdOut.println("removing -> " + randomizedQueue.dequeue());
        StdOut.println("random sample -> " + randomizedQueue.sample());
        StdOut.println("removing -> " + randomizedQueue.dequeue());
        StdOut.println("random sample -> " + randomizedQueue.sample());
        StdOut.println("removing -> " + randomizedQueue.dequeue());
        StdOut.println("random sample -> " + randomizedQueue.sample());
        StdOut.println("removing -> " + randomizedQueue.dequeue());
        StdOut.println(randomizedQueue.size());
        for (Integer x : randomizedQueue) {
            StdOut.println(x);
        }
    }
}
