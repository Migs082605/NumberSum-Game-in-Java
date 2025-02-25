import java.util.Random;
import java.util.HashSet;
import java.util.Set;

public class Srand {
    private final int SIZE;
    private int[][] grid;
    private MainGame.CellState[][] cellStates;
    private int[] rowSums;
    private int[] colSums;

    public Srand(int size) {
        this.SIZE = size;
        generatePuzzle();
    }

    private void generatePuzzle() {
        Random rand = new Random();
        grid = new int[SIZE][SIZE];
        cellStates = new MainGame.CellState[SIZE][SIZE];
        rowSums = new int[SIZE];
        colSums = new int[SIZE];

        boolean[][] solution = new boolean[SIZE][SIZE];
        Set<String> uniqueSolutions = new HashSet<>();

        while (true) {
            fillGridWithRandomNumbers(rand);
            determineSolutionAndClues(rand, solution);
            if (hasUniqueSolution(solution, uniqueSolutions)) {
                break;
            }
        }
    }

    private void fillGridWithRandomNumbers(Random rand) {
        for (int row = 0; row < SIZE; row++) {
            for (int col = 0; col < SIZE; col++) {
                grid[row][col] = rand.nextInt(9) + 1;
                cellStates[row][col] = MainGame.CellState.UNKNOWN;
            }
        }
    }

    private void determineSolutionAndClues(Random rand, boolean[][] solution) {
        for (int row = 0; row < SIZE; row++) {
            int rowSum = 0;
            for (int col = 0; col < SIZE; col++) {
                if (rand.nextBoolean()) {
                    solution[row][col] = true;
                    rowSum += grid[row][col];
                } else {
                    solution[row][col] = false;
                }
            }
            rowSums[row] = rowSum;
        }
        for (int col = 0; col < SIZE; col++) {
            int colSum = 0;
            for (int row = 0; row < SIZE; row++) {
                if (solution[row][col]) {
                    colSum += grid[row][col];
                }
            }
            colSums[col] = colSum;
        }
    }

    private boolean hasUniqueSolution(boolean[][] solution, Set<String> uniqueSolutions) {
        String solutionKey = generateSolutionKey(solution);
        if (uniqueSolutions.contains(solutionKey)) {
            return false;
        }
        uniqueSolutions.add(solutionKey);
        return true;
    }

    private String generateSolutionKey(boolean[][] solution) {
        StringBuilder keyBuilder = new StringBuilder();
        for (boolean[] row : solution) {
            for (boolean cell : row) {
                keyBuilder.append(cell ? "1" : "0").append(",");
            }
        }
        return keyBuilder.toString();
    }

    public int[][] getGrid() {
        return grid;
    }

    public MainGame.CellState[][] getCellStates() {
        return cellStates;
    }

    public int[] getRowSums() {
        return rowSums;
    }

    public int[] getColSums() {
        return colSums;
    }
}