import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.util.Stack;

public class MainGame {
    private int[][] grid;
    private int[] rowClues;
    private int[] colClues;
    private JButton[][] gridButtons;
    private JLabel[] rowSumsLabels;
    private JLabel[] colSumsLabels;
    private CellState[][] cellStates;
    private int gridSize;
    private String difficulty;
    private Stack<CellState[][]> undoStack;
    private Check check;

    public enum CellState {
        UNKNOWN, DELETED, KEPT
    }

    public MainGame(int gridSize, String difficulty) {
        this.gridSize = gridSize;
        this.difficulty = difficulty;
        this.undoStack = new Stack<>();
        this.check = new Check();

        Srand srand = new Srand(gridSize);
        this.grid = srand.getGrid();
        this.cellStates = srand.getCellStates();
        this.rowClues = srand.getRowSums();
        this.colClues = srand.getColSums();

        this.rowSumsLabels = new JLabel[gridSize];
        this.colSumsLabels = new JLabel[gridSize];
    }

    public JPanel createGrid() {
        JPanel gridPanel = new JPanel();
        gridPanel.setLayout(new GridLayout(gridSize + 1, gridSize + 1));
        gridPanel.setBackground(Color.BLACK);

        gridButtons = new JButton[gridSize][gridSize];

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                final int row = i;
                final int col = j;

                JButton button = new JButton(String.valueOf(grid[i][j]));
                button.setFont(new Font("Arial", Font.BOLD, 20));
                button.setBackground(Color.BLACK);
                button.setForeground(Color.WHITE);
                button.setBorder(new LineBorder(Color.DARK_GRAY, 1));
                button.addActionListener(e -> {
                    saveStateForUndo();
                    toggleCell(button, row, col);
                    updateSumLabels();
                });
                gridButtons[i][j] = button;
                gridPanel.add(button);
            }
            rowSumsLabels[i] = createSumLabel(rowClues[i], 0);
            gridPanel.add(rowSumsLabels[i]);
        }

        for (int j = 0; j < gridSize; j++) {
            colSumsLabels[j] = createSumLabel(colClues[j], 0);
            gridPanel.add(colSumsLabels[j]);
        }

        return gridPanel;
    }

    private JLabel createSumLabel(int total, int current) {
        JLabel label = new JLabel(total + " (" + current + ")", SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, 18));
        label.setForeground(Color.LIGHT_GRAY);
        return label;
    }

    private void updateSumLabels() {
        for (int i = 0; i < gridSize; i++) {
            int rowSum = 0;
            for (int j = 0; j < gridSize; j++) {
                if (cellStates[i][j] == CellState.KEPT) {
                    rowSum += grid[i][j];
                }
            }
            rowSumsLabels[i].setText(rowClues[i] + " (" + rowSum + ")");
        }

        for (int j = 0; j < gridSize; j++) {
            int colSum = 0;
            for (int i = 0; i < gridSize; i++) {
                if (cellStates[i][j] == CellState.KEPT) {
                    colSum += grid[i][j];
                }
            }
            colSumsLabels[j].setText(colClues[j] + " (" + colSum + ")");
        }
    }

    private void toggleCell(JButton button, int row, int col) {
        switch (cellStates[row][col]) {
            case UNKNOWN -> {
                cellStates[row][col] = CellState.DELETED;
                button.setBackground(Color.RED);
                button.setText("X");
            }
            case DELETED -> {
                cellStates[row][col] = CellState.KEPT;
                button.setBackground(Color.GREEN);
                button.setText(String.valueOf(grid[row][col]));
            }
            case KEPT -> {
                cellStates[row][col] = CellState.UNKNOWN;
                button.setBackground(Color.BLACK);
                button.setText(String.valueOf(grid[row][col]));
            }
        }
    }

    public boolean checkSolution() {
        return check.checkSolution(grid, cellStates, rowClues, colClues);
    }

    public void resetGrid() {
        saveStateForUndo();
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                cellStates[i][j] = CellState.UNKNOWN;
                gridButtons[i][j].setText(String.valueOf(grid[i][j]));
                gridButtons[i][j].setBackground(Color.BLACK);
                gridButtons[i][j].setEnabled(true);
            }
        }
        updateSumLabels();
    }

    public void undo() {
        if (!undoStack.isEmpty()) {
            CellState[][] previousState = undoStack.pop();
            for (int i = 0; i < gridSize; i++) {
                for (int j = 0; j < gridSize; j++) {
                    cellStates[i][j] = previousState[i][j];
                    updateButtonAppearance(gridButtons[i][j], previousState[i][j], i, j);
                }
            }
            updateSumLabels();
        }
    }

    private void saveStateForUndo() {
        CellState[][] currentState = new CellState[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            System.arraycopy(cellStates[i], 0, currentState[i], 0, gridSize);
        }
        undoStack.push(currentState);
    }

    private void updateButtonAppearance(JButton button, CellState state, int row, int col) {
        switch (state) {
            case DELETED -> {
                button.setBackground(Color.RED);
                button.setText("X");
            }
            case KEPT -> {
                button.setBackground(Color.GREEN);
                button.setText(String.valueOf(grid[row][col]));
            }
            case UNKNOWN -> {
                button.setBackground(Color.BLACK);
                button.setText(String.valueOf(grid[row][col]));
            }
        }
    }

    public int getGridSize() {
        return gridSize;
    }

    public String getDifficulty() {
        return difficulty;
    }
}