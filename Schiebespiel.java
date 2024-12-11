import java.io.*;
import java.util.*;

public class Schiebespiel {

    static int moveCount = 0;
    static int[][] board;
    static int size;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int choice;

        while (true) {
            System.out.println("1 - New Game\n2 - Resume\n3 - Exit");
            choice = sc.nextInt();

            if (choice == 1) {
                Game();
            } else if (choice == 2) {
                ResumeSavedGame();
            } else if (choice == 3) {
                System.out.println("END!");
                break;
            } else {
                System.out.println("Invalid Input! End!");
            }
        }
    }

    public static void Game() {
        Scanner sc = new Scanner(System.in);
        Random rd = new Random();

        if (board == null) {
            System.out.println("Geben Sie die Größe des Spielfelds an: ");
            size = sc.nextInt();

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

            board = new int[size][size];

            // Shuffle the numbers
            ArrayList<Integer> numList = new ArrayList<>();
            for (int num : availableNums) {
                numList.add(num);
            }
            Collections.shuffle(numList, rd);

            // Fill the board with shuffled numbers
            int index = 0;
            for (int i = 0; i < size; i++) {
                for (int j = 0; j < size; j++) {
                    board[i][j] = numList.get(index++);
                }
            }

            // Print the initial board
            printBoard(board);
        }

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

            if (moveRow != -1 && moveCol != -1 &&
                    ((emptyRow == moveRow - 1 && emptyCol == moveCol) || (emptyRow == moveRow + 1 && emptyCol == moveCol) ||
                    (emptyCol == moveCol - 1 && emptyRow == moveRow) || (emptyCol == moveCol + 1 && emptyRow == moveRow))) {
                board[emptyRow][emptyCol] = move;
                board[moveRow][moveCol] = 0;
                moveCount++;
            } else {
                System.out.println("Ungültiger Zug. Versuchen Sie es erneut.");
            }

            printBoard(board);
            System.out.println("Züge: " + moveCount);

            if (isBoardInOrder(board)) {
                System.out.println("Sie haben das Spiel in " + moveCount + " Zügen gewonnen.");
                saveGameState();
                break;
            }

            if (askToSaveGame()) {
                saveGameState();
                System.out.println("Spiel gespeichert. Beenden.");
                break;
            }
        }

        // Reset the board and move count for a new game
        board = null;
        moveCount = 0;

        // Ask if the player wants to play again
        System.out.print("Replay (1 = yes/ 2 = no): ");
        int replay = sc.nextInt();
        if (replay == 1) {
            Game();
        } else {
            System.out.println("END!");
        }
    }

    public static boolean isBoardInOrder(int[][] board) {
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
        if (board == null) {
            System.out.println("Das Spielfeld ist nicht geladen.");
            return;
        }
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

    public static void saveGameState() {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("gameState.dat"))) {
            out.writeObject(board);
            out.writeInt(moveCount);
            out.writeInt(size);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ResumeSavedGame() {
        File file = new File("gameState.dat");
        if (!file.exists()) {
            System.out.println("Kein gespeicherter Spielstand gefunden.");
            return;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(file))) {
            board = (int[][]) in.readObject();
            moveCount = in.readInt();
            size = in.readInt();
            System.out.println("Spielstand geladen.");
            printBoard(board);
            Game(); // Continue the game without reinitializing the board
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static boolean askToSaveGame() {
        Scanner sc = new Scanner(System.in);
        System.out.println("Möchten Sie das Spiel speichern und beenden? (1 = ja / 2 = nein): ");
        int choice = sc.nextInt();
        return choice == 1;
    }
}
