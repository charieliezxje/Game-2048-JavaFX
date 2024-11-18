package org.example.game2048javafx;

import javafx.animation.ScaleTransition;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class View extends GridPane {
  private final int SIZE = 4;
  private final StackPane[][] tiles = new StackPane[SIZE][SIZE];
  private final Label scoreLabel = new Label("SCORE: 0");
  private final Label bestScoreLabel = new Label("BEST: 0");

  public View() {
    setHgap(10);
    setVgap(10);
    setAlignment(Pos.CENTER);
    setStyle("-fx-padding: 10; -fx-background-color: #bbada0;");
  }

  public void setupGrid() {
    Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/org/example/game2048javafx/fonts/Poppins-Bold.ttf"), 30);

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
  }

  public HBox getHeader() {
    Font poppinsBold = Font.loadFont(getClass().getResourceAsStream("/org/example/game2048javafx/fonts/Poppins-Bold.ttf"), 32);

    Label titleLabel = new Label("2048 Game");
    titleLabel.setFont(poppinsBold);
    titleLabel.setStyle(
      "-fx-font-size: 32px; " +
        "-fx-background-color: #edc22e; " +
        "-fx-text-fill: white; " +
        "-fx-padding: 15; " +
        "-fx-border-radius: 15; " +
        "-fx-background-radius: 15;");

    HBox header = new HBox(20, titleLabel, scoreLabel, bestScoreLabel);
    header.setAlignment(Pos.CENTER_LEFT);
    return header;
  }

  public Label getFooter() {
    return new Label("Join the numbers and get to the 2048 tile!");
  }

  public void updateUI(int[][] board, int score, int bestScore) {
    scoreLabel.setText("SCORE: " + score);
    bestScoreLabel.setText("BEST: " + bestScore);
    // Update tiles
    // Similar logic as in the original
  }
}
