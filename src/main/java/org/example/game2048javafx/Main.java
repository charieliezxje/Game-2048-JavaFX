package org.example.game2048javafx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Objects;

public class Main extends Application {

  @Override
  public void start(Stage primaryStage) {
    // Set the favicon
    primaryStage.getIcons().add(
      new Image(Objects.requireNonNull(getClass().getResourceAsStream("/favicon.png")))
    );

    showWelcomeScene(primaryStage);
  }

  private void showWelcomeScene(Stage stage) {
    VBox welcomeLayout = new VBox(20);
    welcomeLayout.setStyle(
      "-fx-padding: 20; -fx-alignment: center; -fx-background-color: #bbada0;");

    Text title = new Text("Welcome to 2048!");
    title.setFont(Font.font("Poppins", 40));
    title.setStyle("-fx-font-family: 'Poppins'; -fx-text-fill: #edc22e;");

    Button startButton = new Button("Start Game");
    startButton.setStyle(
      "-fx-padding: 10; -fx-background-color: #8f7a66; -fx-text-fill: white; -fx-font-size: 20px; -fx-background-radius: 10; -fx-font-family: 'Poppins';");
    startButton.setOnAction(event -> showGameScene(stage));

    startButton.setOnMouseEntered(
      event ->
        startButton.setStyle(
          "-fx-padding: 10; -fx-background-color: #736352; -fx-text-fill: white; -fx-font-size: 20px; -fx-background-radius: 10; -fx-font-family: 'Poppins'; -fx-cursor: hand;"));
    startButton.setOnMouseExited(
      event ->
        startButton.setStyle(
          "-fx-padding: 10; -fx-background-color: #8f7a66; -fx-text-fill: white; -fx-font-size: 20px; -fx-background-radius: 10; -fx-font-family: 'Poppins';"));

    welcomeLayout.getChildren().addAll(title, startButton);
    Scene welcomeScene = new Scene(welcomeLayout, 550, 700);

    stage.setScene(welcomeScene);
    stage.setTitle("2048 - Welcome");
    stage.show();
  }

  private void showGameScene(Stage stage) {
    Game2048 game = new Game2048();

    BorderPane root = new BorderPane();
    root.setTop(game.getHeader()); // Add header
    root.setCenter(game); // Add game grid
    root.setBottom(game.getFooter()); // Add footer

    Scene gameScene = new Scene(root, 550, 700);
    gameScene
      .getStylesheets()
      .add(getClass().getResource("styles.css").toExternalForm()); // Load CSS

    gameScene.setOnKeyPressed(event -> game.fireEvent(event)); // Ensure focus remains on grid

    stage.setScene(gameScene);
    stage.setTitle("Play 2048!");
    stage.show();

    game.requestFocus(); // Focus on the game grid
    game.startGame(); // Start the game
  }

  private void showLosingScene(Stage stage) {
    VBox losingLayout = new VBox(20);
    losingLayout.setStyle("-fx-background-color: #bbada0; -fx-padding: 20; -fx-alignment: center;");

    Text losingText = new Text("Game Over");
    losingText.setFont(Font.font("Poppins", 40));
    losingText.setStyle("-fx-fill: red; -fx-font-weight: bold;");

    Button retryButton = new Button("Retry");
    retryButton.setStyle(
      "-fx-padding: 10; -fx-background-color: #8f7a66; -fx-text-fill: white; -fx-font-size: 20px; -fx-background-radius: 10;");
    retryButton.setOnAction(event -> showGameScene(stage));

    Button exitButton = new Button("Exit");
    exitButton.setStyle(
      "-fx-padding: 10; -fx-background-color: #8f7a66; -fx-text-fill: white; -fx-font-size: 20px; -fx-background-radius: 10;");
    exitButton.setOnAction(event -> System.exit(0));

    losingLayout.getChildren().addAll(losingText, retryButton, exitButton);
    Scene losingScene = new Scene(losingLayout, 550, 700);

    stage.setScene(losingScene);
    stage.setTitle("2048 - Game Over");
  }

  public static void main(String[] args) {
    launch(args);
  }
}
