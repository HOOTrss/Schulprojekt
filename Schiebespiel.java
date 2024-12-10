package Def;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;
import java.util.Scanner;

public class Schiebespiel {
	 
    static int moveCount = 0;
    
    public static void main(String[] args) {    
 
        Scanner sc = new Scanner(System.in);
        Random rd = new Random();
 
        System.out.println("Geben Sie die Größe des Spielfelds an: ");
        int size = sc.nextInt();
 
        if (size == 1) {
            System.out.println("WIN");
            return;
        }
 
        int totalNums = size * size;
        int[] availableNums = new int[totalNums];
        for (int i = 0; i < totalNums - 1; i++) {
            availableNums[i] = i + 1;
        }
        availableNums[totalNums - 1] = 0; // 0 represents the empty field
 
        int[][] board = new int[size][size];
 
        ArrayList<Integer> numList = new ArrayList<>();
        for (int num : availableNums) {
            numList.add(num);
        }
        Collections.shuffle(numList, rd);
 
        int index = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                board[i][j] = numList.get(index++);
            }
        }
        printBoard(board);
 
        while (true) {
            System.out.println("Geben Sie die Zahl des Feldes ein, das Sie bewegen möchten:");
            int move = sc.nextInt();
 
            int emptyRow = -1, emptyCol = -1;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] == 0) {
                        emptyRow = i;
                        emptyCol = j;
                        break;
                    }
                }
            }
 
            int moveRow = -1, moveCol = -1;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    if (board[i][j] == move) {
                        moveRow = i;
                        moveCol = j;
                        break;
                    }
                }
            }
 
            if (moveRow != -1 && moveCol != -1) {
                boolean isAdjacent = false;
                // Check if the move is directly above or below the empty field
                if ((emptyRow == moveRow - 1 && emptyCol == moveCol) || (emptyRow == moveRow + 1 && emptyCol == moveCol)) {
                    isAdjacent = true;
                }
                // Check if the move is directly to the left or right of the empty field
                if ((emptyCol == moveCol - 1 && emptyRow == moveRow) || (emptyCol == moveCol + 1 && emptyRow == moveRow)) {
                    isAdjacent = true;
                }
                if (isAdjacent) {
                    board[emptyRow][emptyCol] = move;
                    board[moveRow][moveCol] = 0;
                    moveCount++;
                } else {
                    System.out.println("Ungültiger Zug. Versuchen Sie es erneut.");
                }
            }
 
            printBoard(board);
            System.out.println("Züge: " + moveCount);
            if (isBoardInOrder(board)) {
                System.out.println("Sie haben das Spiel in " + moveCount + " Zügen gewonnen.");
                break;
            }
        }
    }
 
    public static boolean isBoardInOrder(int[][] board) {
        int size = board.length;
        int expectedValue = 1;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (i == size - 1 && j == size - 1) {
                    return board[i][j] == 0;
                }
                if (board[i][j] != expectedValue++) {
                    return false;
                }
            }
        }
        return true;
    }
 
    public static void printBoard(int[][] board) {
        for (int[] row : board) {
            for (int num : row) {
                if (num == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(num + " ");
                }
            }
            System.out.println();
        }
    }
}

// EDITED BY ME
    
