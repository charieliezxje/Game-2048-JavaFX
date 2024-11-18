package org.example.game2048javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class Main extends Application {
  @Override
  public void start(Stage primaryStage) {
    Game2048 game = new Game2048();

    BorderPane root = new BorderPane();
    root.setTop(game.getHeader());    // Add header
    root.setCenter(game);             // Add game grid
    root.setBottom(game.getFooter()); // Add footer label

    Scene scene = new Scene(root, 550, 650);
    scene.getStylesheets().add(getClass().getResource("styles.css").toExternalForm()); // Load CSS

    primaryStage.setTitle("Play 2048!");
    primaryStage.setScene(scene);
    primaryStage.show();

    game.requestFocus(); // Focus on the game grid
    game.startGame();    // Start the game
  }

  public static void main(String[] args) {
    launch(args);
  }
}
