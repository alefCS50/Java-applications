/*
The Solver class is the class containing the Main method
and in this class we have all the methods used to solve a particular puzzle.
This method uses the A* algorithm for solving the slider puzzle.
The constructor takes a Board as its argument and the class also contains a private class called SearchNode
which is used for keeping track of the moves performed on the board, and it is also useful for determining
the moves to be made on the board in order to solve the puzzle.


We insert the original Searchnode (with the original board)
 into a priority queue and then add all the neighbors of that board to the priority queue as well (in form of Searchnodes).
 Then we delete the Searchnode with minimum "priority" the priority is calculated with the manhattan distance.
 We repeat this procedure until the Searchnode deleted from the priority queue is the solved board.

 However, we must perform this same procedure on the twin-board as well.
 If the twin board is solved before the original board, that means that the original board is unsolvable.
 */


import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;

public class Solver {

    private MinPQ<SearchNode> pq;
    private ArrayList<Board> path;
    private int moveCount = 0;
    private SearchNode first;
    private SearchNode firstTwin;
    private MinPQ<SearchNode> pqTwin;
    private ArrayList<SearchNode> pathTwin;
    private int moveCountTwin;
    private boolean firstSolved;
    private boolean firstTwinSolved;
    private Board initial;


    private class SearchNode implements Comparable<SearchNode> {
        private Board current = null;
        private int moves = 0;
        private SearchNode prev = null;
        private int priority = 0;

        public SearchNode(Board board, int steps, SearchNode previous) {
            current = board;
            prev = previous;
            moves = steps;
            priority = steps + current.manhattan();
        }

        public int compareTo(SearchNode that) {
            if (this.priority > that.priority) return +1;
            if (this.priority < that.priority) return -1;
            return 0;
        }
    }

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        this.initial = initial;

        //create queue and path list
        pq = new MinPQ<SearchNode>();
        path = new ArrayList<Board>();

        //queue and path list Twin
        pqTwin = new MinPQ<SearchNode>();


        //add first
        pq.insert(new SearchNode(initial, 0, null));
        firstSolved = false;
        //ad firstTwin
        pqTwin.insert(new SearchNode(initial.twin(), 0, null));
        firstTwinSolved = false;

        //loop
        while (!pq.min().current.isGoal() && !pqTwin.min().current.isGoal()) {

            //delete min and add onto path
            SearchNode min = pq.min();
            if (min != null) {
                pq.delMin();
                path.add(min.current);
                moveCount++;
                firstSolved = min.current.isGoal();
            }
            //same but twin
            SearchNode minTwin = pqTwin.min();
            if (minTwin != null) {
                pqTwin.delMin();
                firstTwinSolved = minTwin.current.isGoal();
            }

            //add neighbors in queue

            for (Board neighbor : min.current.neighbors()) {
                if (min.moves == 0) {
                    pq.insert(new SearchNode(neighbor, min.moves + 1, min));
                } else if (!neighbor.equals(min.prev.current)) {
                    pq.insert(new SearchNode(neighbor, min.moves + 1, min));
                }

            }


            //add neighbors in queue twin
            moveCountTwin++;
            for (Board neighbor : minTwin.current.neighbors()) {
                if (minTwin.moves == 0) {
                    pqTwin.insert(new SearchNode(neighbor, minTwin.moves + 1, minTwin));
                } else if (!neighbor.equals(minTwin.prev.current)) {
                    pqTwin.insert(new SearchNode(neighbor, minTwin.moves + 1, minTwin));
                }
            }

        }
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        if (firstTwinSolved == true) return false;
        return true;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!isSolvable()) return -1;
        return pq.min().moves;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> Sol = new Stack<>();
        SearchNode last = pq.min();
        while (last.prev != null) {
            Sol.push(last.current);
            last = last.prev;
        }
        Sol.push(initial);
        return Sol;
    }


    /* The main method takes the name of an
     input file as a command-line argument
      and prints the minimum number of moves
      to solve the puzzle and a corresponding solution.
      It also prints all the steps from the original board to the solved one.

       The first number in the input file is the length of the board
       It has to be specified for the program to work
     */
    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] tiles = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                tiles[i][j] = in.readInt();
        Board initial = new Board(tiles);

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

