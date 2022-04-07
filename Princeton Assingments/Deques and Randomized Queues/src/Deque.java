/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {

    private Node first = null;
    private Node last = null;
    private int size;

    private class Node {
        Item item;
        Node next;
        Node previous;
    }
    // construct an empty deque
    public Deque() {
        size = 0;
    }

    // is the deque empty?
    public boolean isEmpty() {
        return size == 0;
    }

    // return the number of items on the deque
    public int size() {
        return size;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Can't add a null item!");
        }
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        first.next = oldFirst;
        if (isEmpty()) {
            last = first;
        } else {
            oldFirst.previous = first;
        }
        size++;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("Can't add a null item!");
        }
        Node oldLast = last;
        last = new Node();
        last.item = item;
        last.previous = oldLast;
        if (isEmpty()) {
            first = last;
        } else {
            oldLast.next = last;
        }
        size++;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty!");
        }
        if (first == last) {
            last = null;
        }
        Item item = first.item;
        first = first.next;
        if (first != null) {
            first.previous = null;
        }
        size--;
        return item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new java.util.NoSuchElementException("Deque is empty!");
        }
        if (first == last) {
            first = null;
        }
        Item item = last.item;
        last = last.previous;
        if (first != null) {
            last.next = null;
        }
        size--;
        return item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = first;
        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new java.util.NoSuchElementException("Next item is null!");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addLast(6);
        deque.addFirst(5);
        deque.addFirst(4);
        deque.addLast(7);
        StdOut.println("removing -> " + deque.removeFirst());
        StdOut.println("removing -> " + deque.removeFirst());
        StdOut.println("removing -> " + deque.removeFirst());
        StdOut.println("removing -> " + deque.removeLast());
        StdOut.println(deque.size());

        for (Integer x : deque) {
            StdOut.println(x);
        }
    }
}
