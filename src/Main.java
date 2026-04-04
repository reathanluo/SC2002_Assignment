package com.arena;

import com.arena.boundary.CLIGameUI;
import com.arena.boundary.GameUI;
import com.arena.control.GameController;

// entry point of game, sets up GameUI and hands control to GameController
public class Main {
  public static void main(String[] args) {
    GameUI ui = new CLIGameUI();
    GameController controller = new GameController(ui);
    controller.startGame();
  }
}
