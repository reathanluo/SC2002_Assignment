package com.arena.control;

import com.arena.entity.combatant.Enemy;
import com.arena.entity.combatant.Goblin;
import com.arena.entity.combatant.Player;
import com.arena.entity.combatant.Warrior;
import com.arena.entity.combatant.Wizard;
import com.arena.entity.combatant.Wolf;
import com.arena.entity.item.Item;
import com.arena.boundary.GameUI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// To setup game, loading screen, character,item,difficulty selection
// Setup -> Battle -> Reply, loop until player exit
public class GameController {
    private GameUI ui;
    private BattleEngine battleEngine;
    // previous settings saved, so player can "reply with same settings"
    private int lastPlayerIndex;
    private Difficulty lastDifficulty;
    private List<Integer> lastItemIndices;

    public GameController(GameUI ui) {
        this.ui = ui;
    }

    // main loop, run full game from setup to results, and ask for reply
    public void startGame() {
        boolean running = true;
        while (running) {
            // pass objects for reference so stats are displayed without hard-coding
            ui.displayLoadingScreen(
                Arrays.asList(new Warrior(), new Wizard()),
                Arrays.asList((Enemy) new Goblin(), new Wolf())
            );

            lastPlayerIndex = selectPlayerIndex();
            Player player = PlayerFactory.createPlayer(lastPlayerIndex);

            lastItemIndices = selectItemIndices();
            for (int index : lastItemIndices) {
                player.addItem(ItemFactory.createItem(index));
            }

            lastDifficulty = selectDifficulty();

            runBattle(player, lastDifficulty);

            running = handleReplay();
        }
    }

    // set up and run a battle, and show results
    private void runBattle(Player player, Difficulty difficulty) {
        Level level = LevelFactory.createLevel(difficulty);
        TurnOrderStrategy strategy = new SpeedBasedTurnOrder();
        battleEngine = new BattleEngine(player, level, strategy, ui);
        battleEngine.startBattle();
        showResults();
    }

    // prompt player if they want to replay after battle ends. 
    private boolean handleReplay() {
        while (true) {
            int choice = ui.getReplayChoice();
            switch (choice) {
                case 1: // replay with same settings, using saved data from previous game
                    Player freshPlayer = PlayerFactory.createPlayer(lastPlayerIndex);
                    for (int index : lastItemIndices) {
                        freshPlayer.addItem(ItemFactory.createItem(index));
                    }
                    runBattle(freshPlayer, lastDifficulty);
                    break; // stay in this loop to ask again after the battle
                case 2: // start a new game with new settings, by breaking out to startGame() loop
                    return true;
                case 3: // exit the game
                default:
                    ui.displayExitMessage();
                    return false;
            }
        }
    }

    private int selectPlayerIndex() {
        List<String> playerTypes = Arrays.asList("Warrior", "Wizard");
        return ui.getPlayerChoice(playerTypes);
    }

    private List<Integer> selectItemIndices() {
        List<String> itemNames = Arrays.asList("Potion", "Power Stone", "Smoke Bomb");

        ui.displayItemSelectionPrompt(1, 2);
        int choice1 = ui.getItemChoice(itemNames);
        ui.displayItemSelectionPrompt(2, 2);
        int choice2 = ui.getItemChoice(itemNames);

        return Arrays.asList(choice1, choice2);
    }

    private Difficulty selectDifficulty() {
        List<String> descriptions = new ArrayList<>();
        for (Difficulty d : Difficulty.values()) descriptions.add(LevelFactory.getDescription(d)); // get level descriptions from LevelFactory
        int choice = ui.getDifficultyChoice(descriptions);
        Difficulty[] values = Difficulty.values();
        return (choice >= 0 && choice < values.length) ? values[choice] : Difficulty.EASY;
    }

    private void showResults() {
        if (battleEngine.isPlayerVictory()) {
            ui.displayVictory(battleEngine.getPlayer(), battleEngine.getRoundCount());
        } else {
            ui.displayDefeat(battleEngine.getEnemies(), battleEngine.getRoundCount());
        }
    }
}
