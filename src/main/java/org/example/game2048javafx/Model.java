package org.example.game2048javafx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Model {
  private final int SIZE = 4;
  private final int[][] board = new int[SIZE][SIZE];
  private int score = 0;
  private int bestScore = 0;

  public int[][] getBoard() {
    return board;
  }

  public int getScore() {
    return score;
  }

  public int getBestScore() {
    return bestScore;
  }

  public void resetBoard() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        board[row][col] = 0;
      }
    }
    score = 0;
  }

  public void addRandomTile() {
    List<int[]> emptySpaces = new ArrayList<>();
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        if (board[row][col] == 0) {
          emptySpaces.add(new int[] {row, col});
        }
      }
    }
    if (!emptySpaces.isEmpty()) {
      Random rand = new Random();
      int[] space = emptySpaces.get(rand.nextInt(emptySpaces.size()));
      board[space[0]][space[1]] = rand.nextDouble() < 0.9 ? 2 : 4;
    }
  }

  public boolean move(String direction) {
    boolean moved = false;
    for (int i = 0; i < SIZE; i++) {
      int[] line = getLine(direction, i);
      int[] merged = mergeLine(line);
      if (!arrayEquals(line, merged)) moved = true;
      setLine(direction, i, merged);
    }
    return moved;
  }

  private int[] getLine(String direction, int index) {
    int[] line = new int[SIZE];
    for (int i = 0; i < SIZE; i++) {
      switch (direction) {
        case "UP" -> line[i] = board[i][index];
        case "DOWN" -> line[i] = board[SIZE - 1 - i][index];
        case "LEFT" -> line[i] = board[index][i];
        case "RIGHT" -> line[i] = board[index][SIZE - 1 - i];
      }
    }
    return line;
  }

  private void setLine(String direction, int index, int[] line) {
    for (int i = 0; i < SIZE; i++) {
      switch (direction) {
        case "UP" -> board[i][index] = line[i];
        case "DOWN" -> board[SIZE - 1 - i][index] = line[i];
        case "LEFT" -> board[index][i] = line[i];
        case "RIGHT" -> board[index][SIZE - 1 - i] = line[i];
      }
    }
  }

  private int[] mergeLine(int[] line) {
    int[] merged = new int[SIZE];
    int pos = 0;

    for (int i = 0; i < SIZE; i++) {
      if (line[i] == 0) continue;
      if (pos > 0 && merged[pos - 1] == line[i]) {
        merged[pos - 1] *= 2;
        score += merged[pos - 1];
      } else {
        merged[pos++] = line[i];
      }
    }
    if (score > bestScore) bestScore = score;
    return merged;
  }

  private boolean arrayEquals(int[] a, int[] b) {
    for (int i = 0; i < SIZE; i++) {
      if (a[i] != b[i]) return false;
    }
    return true;
  }

  public boolean checkGameOver() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        if (board[row][col] == 0) return false;
        if (row < SIZE - 1 && board[row][col] == board[row + 1][col]) return false;
        if (col < SIZE - 1 && board[row][col] == board[row][col + 1]) return false;
      }
    }
    return true;
  }
}
