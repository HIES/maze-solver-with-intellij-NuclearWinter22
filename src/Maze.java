import java.awt.Color;
import java.io.File;
import java.util.ArrayList;
import java.util.Scanner;


public class Maze_With_Extensions {
    private Cell[][] board;
    private final int DELAY = 200;

    public Maze_With_Extensions(int rows, int cols, int[][] map) {
        StdDraw.setXscale(0, cols);
        StdDraw.setYscale(0, rows);
        board = new Cell[rows][cols];
        //grab number of rows to invert grid system with StdDraw (lower-left, instead of top-left)
        int height = board.length - 1;
        for (int r = 0; r < rows; r++)
            for (int c = 0; c < cols; c++) {
                board[r][c] = map[r][c] == 1 ? new Cell(c , height - r, 0.5, false) : new Cell(c, height - r, 0.5, true);
            }
    }

    public void draw() {
        for (int r = 0; r < board.length; r++)
            for (int c = 0; c < board[r].length; c++){
                Cell cell = board[r][c];
                StdDraw.setPenColor(cell.getColor());
                StdDraw.filledSquare(cell.getX(), cell.getY(), cell.getRadius());
            }
        StdDraw.show();
    }

    public boolean findPath(int row, int col) {
        boolean isComplete = false;
        if (isValid(row, col)) {
            board[row][col].setColor(Color.BLUE);
            draw();
            StdDraw.pause(DELAY);
            board[row][col].visitCell();
            if (isExit(row, col)) isComplete = true;
            else if (findPath(row, col+1) || findPath(row, col-1) || findPath(row+1, col) || findPath(row-1, col)) isComplete = true;
        }
        if (isComplete) {
            board[row][col].becomePath();
            draw();
            StdDraw.pause(DELAY);
        }
        return isComplete;
    }

    private boolean isValid(int row, int col) {
        if (row < board.length && row >= 0 && col < board[0].length && col >= 0) {
            return !board[row][col].isWall() && !board[row][col].isVisited();
        } else return false;
    }

    private boolean isExit(int row, int col) {
        return row == board.length - 1 && col == board[0].length - 1;
    }

    private static int[][] loadMaze(String fileName) throws Exception {
        ArrayList<String[]> mazeRows = new ArrayList<>();
        File mazeFile = new File("input/"+ fileName + ".csv");
        Scanner inputObject = new Scanner(mazeFile);
        while (inputObject.hasNextLine()) {
            String [] row = inputObject.nextLine().split(",");
            mazeRows.add(row);
        }
        inputObject.close();
        int[][] maze = new int[mazeRows.size()][mazeRows.get(0).length];
        for (int i = 0; i < maze.length; i++) {
            for (int y = 0; y < maze[0].length; y++) {
                maze[i][y] = Integer.parseInt(mazeRows.get(i)[y]);
            }
        }
        return maze;
    }

    public static void main(String[] args) throws Exception {
        int[][] maze = loadMaze(args[0]);
        Maze_With_Extensions geerid = new Maze_With_Extensions(maze.length, maze[0].length, maze);
        geerid.draw();
        geerid.findPath(0, 0);
        geerid.draw();
    }
}
