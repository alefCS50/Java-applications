/*
My name is Allan Efendic
I live in Stockholm, Sweden

My project is called Slider-puzzle solver

The slider puzzle is a puzzle game oftentimes found on PC desktops.
It is a game where you have a certain amount of numbers in small squares(tiles).
One of these squares is empty, making it possible to slide the numbers to different positions in the puzzle.
The goal of the game is to sort all the numbers in the puzzle,
 the smallest in the top-left edge, the biggest in the bottom to the right.

My program is written in Java and uses two classes to solve the puzzle using AI.
The first class that I constructed was the class for the actual board(puzzle)
with different methods that are useful when solving this puzzle.
The board constructor takes a two-dimensional array and that array is turned into a
one-dimensional array for easier computation later on in the methods.

Some methods in the Board class that are very important are isGoal, neighbors and twin.
 The isGoal method determines whether the current board is solved or not.
  The neighbors method returns an iterable containing all the neighbors to a particular board.
  A neighbor to a board is created when you simply slide the one of the tiles in the original board one step.
  The twin method returns the twin to the original board.
  The twin to a particular board is obtained by simply exchanging the positions of two tiles in the board with each other.
  The reason this method is important is that if the twin to a board is solvable,
   that means that the original board is unsolvable.
  This method can therefore help us calculate if a particular board will be solvable or not.
 */


import edu.princeton.cs.algs4.Queue;

import java.util.ArrayList;

public class Board {
    private int n;
    private int[] board;
    private Queue<Board> q;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] tiles) {


        if (tiles == null) {
            throw new IllegalArgumentException();
        }
        if (tiles.length != tiles[0].length) throw new IllegalArgumentException();

        n = tiles.length;
        board = new int[n * n];

        //set value to board
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                board[get1d(i, j)] = tiles[i][j];
            }
        }

    }


    // string representation of this board
    public String toString() {
        StringBuilder str = new StringBuilder();

        str.append(n + "\n");
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                str.append(String.format("%2d ", board[get1d(i, j)]));
            }
            str.append("\n");
        }
        return str.toString();
    }


    // board dimension n
    public int dimension() {
        return n;
    }

    // number of tiles out of place
    public int hamming() {
        int counter = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != i + 1 && board[i] != 0) {
                counter += 1;
            }
        }
        return counter;
    }

    // sum of Manhattan distances between tiles and goal
    public int manhattan() {
        int sum = 0;
        int x1 = 0;
        int x2 = 0;
        int y1 = 0;
        int y2 = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] != i + 1 && board[i] != 0) {
                x1 = get2d(i)[0];
                y1 = get2d(i)[1];

                x2 = get2d(board[i] - 1)[0];
                y2 = get2d(board[i] - 1)[1];

                sum = sum + (Math.abs(x1 - x2) + Math.abs(y1 - y2));
            }
        }
        return sum;
    }


    // is this board the goal board?
    public boolean isGoal() {
        for (int i = 0; i < board.length - 1; i++) {
            if (board[i] != i + 1) return false;
        }
        return true;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (this == y) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;

        Board that = (Board) y;
        for (int i = 0; i < n * n; i++) {
            if (this.board[i] != that.board[i]) return false;
        }
        return true;

    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        q = new Queue<Board>();
        ArrayList<Integer> temp = new ArrayList<>();
        int holeIndex;
        int[] holeGrid = new int[2];
        int col = -1;
        int row = -1;
        for (int i = 0; i < board.length; i++) {
            if (this.board[i] == 0) {

                //get coordinates of hole
                holeIndex = i;
                col = get2d(i)[0];
                row = get2d(i)[1];


                //left
                if (possibility(row - 1, col)) {
                    int up = get1d(row - 1, col);
                    temp.add(up);
                }
                //right
                if (possibility(row + 1, col)) {
                    int down = get1d(row + 1, col);
                    temp.add(down);
                }
                //up
                if (possibility(row, col - 1)) {
                    int left = get1d(row, col - 1);
                    temp.add(left);
                }
                //down
                if (possibility(row, col + 1)) {
                    int right = get1d(row, col + 1);
                    temp.add(right);
                }

            }
        }

        for (Integer item : temp) {
            int[] temp1d = board.clone();
            int[][] temp2d;
            exch(temp1d, get1d(row, col), item);

            temp2d = get2dArr(temp1d, n);
            Board copyB = new Board(temp2d);
            q.enqueue(copyB);
        }

        return q;
    }


    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        int[] twin1d = board.clone();
        int[][] twin2d;

        //exchanging index 1-2, 1-3 or 2-3
        for (int index = 0; index < n; index++) {
            if (twin1d[index] != 0 && twin1d[index + 1] != 0) {
                exch(twin1d, index, index + 1);
                break;
            } else if (twin1d[index] != 0 && twin1d[index + 2] != 0) {
                exch(twin1d, index, index + 2);
                break;
            }
        }
        twin2d = get2dArr(twin1d, n);
        return new Board(twin2d);

    }

    //helper methods
    private int get1d(int row, int col) {
        return ((col) % n) + (n * (row));
    }

    private int[] get2d(int num) {
        int[] tod2 = new int[2];
        tod2[0] = num % n;
        tod2[1] = num / n;
        return tod2;
    }

    private boolean possibility(int col, int row) {
        if (row < 0 || row >= n || col < 0 || col >= n) return false;
        return true;
    }

    private void exch(int[] copy, int a, int b) {
        int swap = copy[b];
        copy[b] = copy[a];
        copy[a] = swap;
    }

    private static int[][] get2dArr(int[] oneD, int width) {
        //get back to 2d
        int count = 0;
        int[][] temp2d = new int[width][width];
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < width; y++) {
                if (count == oneD.length) break;
                temp2d[x][y] = oneD[count];
                count++;
            }
        }
        return temp2d;
    }

}
