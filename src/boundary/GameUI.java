package boundary;

import entity.action.Action;
import entity.combatant.Combatant;
import entity.combatant.Enemy;
import entity.combatant.Player;
import entity.item.Item;
import java.util.List;

// interface for UI (input/output) implementations. 
public interface GameUI {
    void displayLoadingScreen(List<Player> players, List<Enemy> enemyTypes);
    int getPlayerChoice(List<String> playerTypes);
    int getItemChoice(List<String> itemNames);
    void displayItemSelectionPrompt(int itemNumber, int totalItems);
    int getDifficultyChoice(List<String> descriptions);
    void displayPlayerStats(Player player);
    void displayEnemyStats(List<Enemy> enemies);
    void displayRoundStart(int roundNumber);
    void displayTurnOrder(List<Combatant> turnOrder);
    void displayPlayerTurnStart(Player player);
    void displayEnemyAttack(Enemy enemy, Player player, int damage, boolean blocked);
    void displayActionContext(String actionName, String description);
    int getActionChoice(List<Action> availableActions);
    int getTargetChoice(List<Enemy> enemies);
    int getItemUseChoice(List<Item> items);
    void displayActionResult(String message);
    void displayStatusEffects(Combatant combatant);
    void displayRoundEnd(Player player, List<Enemy> enemies, int roundNumber);
    void displayVictory(Player player, int totalRounds);
    void displayDefeat(List<Enemy> remainingEnemies, int totalRounds);
    int getReplayChoice();
    void displayExitMessage();
}
