/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;


public class Permutation {
    public static void main(String[] args) {
        RandomizedQueue<String> randomizedQueue = new RandomizedQueue<>();
        int wordCounter = 0;
        while (!StdIn.isEmpty() && Integer.parseInt(args[0]) > 0) {
            String word = StdIn.readString();
            if (wordCounter ==  Integer.parseInt(args[0])) {
                randomizedQueue.dequeue();
                wordCounter--;
            }
            randomizedQueue.enqueue(word);
            wordCounter++;
        }
        for (String word: randomizedQueue) {
            StdOut.println(word);
        }
    }
}
