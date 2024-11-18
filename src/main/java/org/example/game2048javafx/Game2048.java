package org.example.game2048javafx;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game2048 extends GridPane {
  private final int SIZE = 4;
  private final int[][] board = new int[SIZE][SIZE];
  private final StackPane[][] tiles = new StackPane[SIZE][SIZE];

  private int score = 0;
  private int bestScore = 0;

  private final Label scoreLabel = new Label("SCORE: 0");
  private final Label bestScoreLabel = new Label("BEST: 0");
  private final Label footerLabel = new Label("Join the numbers and get to the 2048 tile!");
  private final Label gameOverLabel = new Label("Game Over! Try Again?");

  public Game2048() {
    setHgap(10);
    setVgap(10);
    setAlignment(Pos.CENTER);
    setStyle("-fx-padding: 10; -fx-background-color: #bbada0;");

    Font poppinsRegular = Font.loadFont(getClass().getResourceAsStream("/org/example/game2048javafx/fonts/Poppins-Regular.ttf"), 18);
    Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/org/example/game2048javafx/fonts/Poppins-Bold.ttf"), 30);

    scoreLabel.setFont(poppinsRegular);
    bestScoreLabel.setFont(poppinsRegular);
    footerLabel.setFont(poppinsRegular);
    footerLabel.setStyle("-fx-text-fill: #776e65;");
    gameOverLabel.setFont(poppinsBold);
    gameOverLabel.setStyle("-fx-text-fill: red; -fx-font-size: 24px;");
    gameOverLabel.setVisible(false);

    // Initialize grid tiles
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        StackPane stack = new StackPane();
        Rectangle bg = new Rectangle(100, 100);
        bg.setArcWidth(15);
        bg.setArcHeight(15);
        bg.setFill(Color.web("#cdc1b4"));

        Text text = new Text();
        text.setFont(poppinsBold);
        text.setFill(Color.web("#776e65"));
        stack.getChildren().addAll(bg, text);
        tiles[row][col] = stack;
        add(stack, col, row);
      }
    }

    // Handle key presses
    setOnKeyPressed(event -> {
      boolean moved = false;

      switch (event.getCode()) {
        case UP -> moved = move("UP");
        case DOWN -> moved = move("DOWN");
        case LEFT -> moved = move("LEFT");
        case RIGHT -> moved = move("RIGHT");
      }

      if (moved) {
        addRandomTile(); // Add exactly one tile after a valid move
        updateUIWithAnimations();
        checkGameOver();
      }
    });
  }

  // Create the header (title and scores)
  public HBox getHeader() {
    Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/org/example/game2048javafx/fonts/Poppins-Bold.ttf"), 32);

    // 2048 Title Box
    Label titleLabel = new Label("2048 Game");
    titleLabel.setFont(poppinsBold);
    titleLabel.setStyle(
      "-fx-font-size: 32px; " +
        "-fx-background-color: #edc22e; " +
        "-fx-text-fill: white; " +
        "-fx-padding: 15; " +
        "-fx-border-radius: 15; " +
        "-fx-background-radius: 15;");
    titleLabel.setAlignment(Pos.CENTER);
    titleLabel.setMinWidth(150);
    titleLabel.setMinHeight(60);

    // Score Box
    scoreLabel.setStyle(
      "-fx-font-size: 18px; " +
        "-fx-background-color: #eee4da; " +
        "-fx-text-fill: #333333; " +
        "-fx-padding: 10; " +
        "-fx-border-radius: 15; " +
        "-fx-background-radius: 15;");
    scoreLabel.setAlignment(Pos.CENTER);
    scoreLabel.setMinWidth(100);

    // Best Score Box
    bestScoreLabel.setStyle(
      "-fx-font-size: 18px; " +
        "-fx-background-color: #eee4da; " +
        "-fx-text-fill: #333333; " +
        "-fx-padding: 10; " +
        "-fx-border-radius: 15; " +
        "-fx-background-radius: 15;");
    bestScoreLabel.setAlignment(Pos.CENTER);
    bestScoreLabel.setMinWidth(100);

    HBox header = new HBox(20, titleLabel, new Region(), scoreLabel, bestScoreLabel);
    header.setAlignment(Pos.CENTER_LEFT);
    header.setStyle("-fx-padding: 10;");
    HBox.setHgrow(new Region(), Priority.ALWAYS);
    return header;
  }

  // Create the footer with buttons
  public VBox getFooter() {
    Font poppinsRegular = Font.loadFont(getClass().getResourceAsStream("/org/example/game2048javafx/fonts/Poppins-Regular.ttf"), 18);

    // New Game Button
    Button newGameButton = new Button("New Game");
    newGameButton.setFont(poppinsRegular);
    newGameButton.setStyle("-fx-padding: 10; -fx-background-color: #8f7a66; -fx-text-fill: white; -fx-background-radius: 15;");
    newGameButton.setOnAction(event -> {
      startGame();
      requestFocus(); // Ensure focus returns to the grid after button click
      gameOverLabel.setVisible(false);
    });

    // Exit Game Button
    Button exitGameButton = new Button("Exit Game");
    exitGameButton.setFont(poppinsRegular);
    exitGameButton.setStyle("-fx-padding: 10; -fx-background-color: #8f7a66; -fx-text-fill: white; -fx-background-radius: 15;");
    exitGameButton.setOnAction(event -> System.exit(0));

    HBox buttonContainer = new HBox(10, newGameButton, exitGameButton);
    buttonContainer.setAlignment(Pos.CENTER);

    VBox footer = new VBox(10, footerLabel, gameOverLabel, buttonContainer);
    footer.setAlignment(Pos.CENTER);
    footer.setStyle("-fx-padding: 10;");
    return footer;
  }

  // Start the game
  public void startGame() {
    resetBoard();
    addRandomTile();
    addRandomTile(); // Always start with exactly two tiles
    updateUI();
  }

  private void resetBoard() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        board[row][col] = 0;
      }
    }
    score = 0;
    updateScores();
  }

  private void addRandomTile() {
    List<int[]> emptySpaces = new ArrayList<>();
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        if (board[row][col] == 0) {
          emptySpaces.add(new int[]{row, col});
        }
      }
    }

    if (!emptySpaces.isEmpty()) {
      Random rand = new Random();
      int[] space = emptySpaces.get(rand.nextInt(emptySpaces.size()));
      board[space[0]][space[1]] = rand.nextDouble() < 0.9 ? 2 : 4;
    }
  }

  private void updateUI() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        int value = board[row][col];
        StackPane tile = tiles[row][col];
        Rectangle bg = (Rectangle) tile.getChildren().get(0);
        Text text = (Text) tile.getChildren().get(1);

        if (value == 0) {
          text.setText("");
          bg.setFill(Color.web("#cdc1b4"));
        } else {
          text.setText(String.valueOf(value));
          text.setFill(value > 4 ? Color.web("#f9f6f2") : Color.web("#776e65"));
          bg.setFill(Color.web(getTileColor(value)));
        }
      }
    }
  }

  private void updateUIWithAnimations() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        int value = board[row][col];
        StackPane tile = tiles[row][col];
        Rectangle bg = (Rectangle) tile.getChildren().get(0);
        Text text = (Text) tile.getChildren().get(1);

        if (value == 0) {
          text.setText("");
          bg.setFill(Color.web("#cdc1b4"));
        } else {
          text.setText(String.valueOf(value));
          text.setFill(value > 4 ? Color.web("#f9f6f2") : Color.web("#776e65"));
          bg.setFill(Color.web(getTileColor(value)));

          ScaleTransition scaleTransition = new ScaleTransition(Duration.millis(200), tile);
          scaleTransition.setFromX(0.9);
          scaleTransition.setFromY(0.9);
          scaleTransition.setToX(1.0);
          scaleTransition.setToY(1.0);
          scaleTransition.play();
        }
      }
    }
  }

  private String getTileColor(int value) {
    return switch (value) {
      case 2 -> "#eee4da";
      case 4 -> "#ede0c8";
      case 8 -> "#f2b179";
      case 16 -> "#f59563";
      case 32 -> "#f67c5f";
      case 64 -> "#f65e3b";
      case 128 -> "#edcf72";
      case 256 -> "#edcc61";
      case 512 -> "#edc850";
      case 1024 -> "#edc53f";
      case 2048 -> "#edc22e";
      default -> "#3c3a32";
    };
  }

  private boolean move(String direction) {
    boolean moved = false;

    for (int i = 0; i < SIZE; i++) {
      int[] line = getLine(direction, i);
      int[] merged = mergeLine(line);
      if (!arrayEquals(line, merged)) moved = true;
      setLine(direction, i, merged);
    }

    if (moved) {
      updateScores();
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
        merged[pos - 1] *= 2; // Merge tiles
        score += merged[pos - 1];
      } else {
        merged[pos++] = line[i];
      }
    }

    return merged;
  }

  private boolean arrayEquals(int[] a, int[] b) {
    for (int i = 0; i < SIZE; i++) {
      if (a[i] != b[i]) return false;
    }
    return true;
  }

  private void updateScores() {
    scoreLabel.setText("SCORE: " + score);
    if (score > bestScore) {
      bestScore = score;
      bestScoreLabel.setText("BEST: " + bestScore);
    }
  }

  private void checkGameOver() {
    for (int row = 0; row < SIZE; row++) {
      for (int col = 0; col < SIZE; col++) {
        if (board[row][col] == 0) return; // Empty space exists
        if (row < SIZE - 1 && board[row][col] == board[row + 1][col]) return; // Mergeable vertically
        if (col < SIZE - 1 && board[row][col] == board[row][col + 1]) return; // Mergeable horizontally
      }
    }
    gameOverLabel.setVisible(true);
  }
}
