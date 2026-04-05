import boundary.CLIGameUI;
import boundary.GameUI;
import control.GameController;

// entry point of game, sets up GameUI and hands control to GameController
public class Main {
  public static void main(String[] args) {
    GameUI ui = new CLIGameUI();
    GameController controller = new GameController(ui);
    controller.startGame();
  }
}
