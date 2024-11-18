package org.example.game2048javafx;

public class Controller {
  private final Model model;
  private final View view;

  public Controller(Model model, View view) {
    this.model = model;
    this.view = view;
  }

  public void startGame() {
    model.resetBoard();
    model.addRandomTile();
    model.addRandomTile();
    view.updateUI(model.getBoard(), model.getScore(), model.getBestScore());
  }

  public void handleKeyPress(String direction) {
    if (model.move(direction)) {
      model.addRandomTile();
      view.updateUI(model.getBoard(), model.getScore(), model.getBestScore());
      if (model.checkGameOver()) {
        System.out.println("Game Over!");
      }
    }
  }
}
