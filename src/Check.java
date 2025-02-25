import javax.swing.*;

public class Check {
    public boolean checkSolution(int[][] grid, MainGame.CellState[][] cellStates, int[] rowClues, int[] colClues) {
        boolean isCorrect = isSolutionCorrect(grid, cellStates, rowClues, colClues);
        if (isCorrect) {
            JOptionPane.showMessageDialog(null, "Correct Solution!");
        } else {
            JOptionPane.showMessageDialog(null, "Incorrect Solution!");
        }
        return isCorrect;
    }

    public boolean isSolutionCorrect(int[][] grid, MainGame.CellState[][] cellStates, int[] rowClues, int[] colClues) {
        int[] currentRowSums = calculateRowSums(grid, cellStates);
        int[] currentColSums = calculateColSums(grid, cellStates);

        for (int i = 0; i < rowClues.length; i++) {
            if (currentRowSums[i] != rowClues[i]) {
                return false;
            }
        }

        for (int j = 0; j < colClues.length; j++) {
            if (currentColSums[j] != colClues[j]) {
                return false;
            }
        }

        return true;
    }

    private int[] calculateRowSums(int[][] grid, MainGame.CellState[][] cellStates) {
        int[] sums = new int[grid.length];
        for (int i = 0; i < grid.length; i++) {
            int sum = 0;
            for (int j = 0; j < grid[i].length; j++) {
                if (cellStates[i][j] == MainGame.CellState.KEPT) {
                    sum += grid[i][j];
                }
            }
            sums[i] = sum;
        }
        return sums;
    }

    private int[] calculateColSums(int[][] grid, MainGame.CellState[][] cellStates) {
        int[] sums = new int[grid[0].length];
        for (int j = 0; j < grid[0].length; j++) {
            int sum = 0;
            for (int i = 0; i < grid.length; i++) {
                if (cellStates[i][j] == MainGame.CellState.KEPT) {
                    sum += grid[i][j];
                }
            }
            sums[j] = sum;
        }
        return sums;
    }
}