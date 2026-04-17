package boundary;

import entity.action.Action;
import entity.combatant.Combatant;
import entity.combatant.Enemy;
import entity.combatant.Player;
import entity.effect.StatusEffect;
import entity.item.Item;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// handles user input and output operations. 
public class CLIGameUI implements GameUI {
  private Scanner scanner;

  public CLIGameUI() {
    this.scanner = new Scanner(System.in);
  }

  @Override
  public void displayLoadingScreen(List<Player> players, List<Enemy> enemyTypes) {
    System.out.println("========================================");
    System.out.println("       TURN-BASED COMBAT ARENA");
    System.out.println("========================================");
    System.out.println();
    System.out.println("--- Players ---");
    for (Player p : players) {
      System.out.printf("%-8s | HP: %-3d | ATK: %-2d | DEF: %-2d | SPD: %-2d | Skill: %s%n",
          p.getName(), p.getMaxHp(), p.getAttack(), p.getDefense(), p.getSpeed(),
          p.getSpecialSkill().getName());
    }
    System.out.println();
    System.out.println("--- Enemies ---");
    for (Enemy e : enemyTypes) {
      System.out.printf("%-8s | HP: %-3d | ATK: %-2d | DEF: %-2d | SPD: %-2d%n",
          e.getName(), e.getMaxHp(), e.getAttack(), e.getDefense(), e.getSpeed());
    }
    System.out.println("========================================");
    System.out.println();
  }

  @Override
  public int getPlayerChoice(List<String> playerTypes) {
    System.out.println("Choose your character:");
    for (int i = 0; i < playerTypes.size(); i++) {
      System.out.println("  " + (i + 1) + ". " + playerTypes.get(i));
    }
    return readChoice(1, playerTypes.size()) - 1;
  }

  @Override
  public int getItemChoice(List<String> itemNames) {
    System.out.println("Choose an item:");
    for (int i = 0; i < itemNames.size(); i++) {
      System.out.println("  " + (i + 1) + ". " + itemNames.get(i));
    }
    return readChoice(1, itemNames.size()) - 1;
  }

  @Override
  public int getDifficultyChoice(List<String> descriptions) {
    System.out.println("Select difficulty:");
    for (int i = 0; i < descriptions.size(); i++) {
      System.out.println("  " + (i + 1) + ". " + descriptions.get(i));
    }
    return readChoice(1, descriptions.size()) - 1;
  }

  @Override
  public void displayPlayerStats(Player player) {
    System.out.println(player.getName() + " | HP: " + player.getHp() + "/" + player.getMaxHp()
        + " | ATK: " + player.getAttack() + " | DEF: " + player.getDefense()
        + " | SPD: " + player.getSpeed()
        + " | Cooldown: " + player.getSpecialSkillCooldown());
  }

  @Override
  public void displayPlayerTurnStart(Player player) {
    System.out.println("==================== YOUR TURN ====================");
    System.out.println("  " + player.getName()
        + "  |  HP: " + player.getHp() + "/" + player.getMaxHp()
        + "  |  ATK: " + player.getAttack()
        + "  |  DEF: " + player.getDefense()
        + "  |  SPD: " + player.getSpeed());

    int cd = player.getSpecialSkillCooldown();
    System.out.println("  Special Skill Cooldown: " + (cd == 0 ? "Ready!" : cd + " turn(s)"));

    StringBuilder items = new StringBuilder("  Items: ");
    List<Item> inv = player.getInventory();
    if (inv.isEmpty()) {
      items.append("none");
    } else {
      for (int i = 0; i < inv.size(); i++) {
        Item it = inv.get(i);
        if (i > 0)
          items.append("  |  ");
        items.append(it.getName());
        if (it.isConsumed())
          items.append(" (used)");
      }
    }
    System.out.println(items);

    List<StatusEffect> effects = player.getStatusEffects();
    if (!effects.isEmpty()) {
      StringBuilder fx = new StringBuilder("  Effects: ");
      for (StatusEffect e : effects) {
        fx.append("[").append(e.getName()).append(" ").append(e.getDuration()).append("t] ");
      }
      System.out.println(fx);
    }

    System.out.println("===================================================");
  }

  @Override
  public void displayEnemyAttack(Enemy enemy, Player player, int damage, boolean blocked) {
    System.out.println("--- " + enemy.getName() + "'s Turn (HP: "
        + enemy.getHp() + "/" + enemy.getMaxHp() + ") ---");
    if (blocked) {
      System.out.println("  Attacks " + player.getName()
          + " - BLOCKED by Smoke Bomb! (0 damage) - HP: "
          + player.getHp() + "/" + player.getMaxHp());
    } else {
      System.out.println("  Attacks " + player.getName() + " for " + damage
          + " damage! - HP: " + player.getHp() + "/" + player.getMaxHp());
    }
    System.out.println();
  }

  @Override
  public void displayActionContext(String actionName, String description) {
    System.out.println();
    System.out.println("[ " + actionName + " ]");
    System.out.println("  " + description);
  }

  @Override
  public void displayEnemyStats(List<Enemy> enemies) {
    for (Enemy e : enemies) {
      if (e.isAlive()) {
        System.out.println("  " + e.getName() + " | HP: " + e.getHp() + "/" + e.getMaxHp()
            + " | ATK: " + e.getAttack() + " | DEF: " + e.getDefense());
      } else {
        System.out.println("  " + e.getName() + " | ELIMINATED");
      }
    }
  }

  @Override
  public void displayRoundStart(int roundNumber) {
    System.out.println();
    System.out.println("===== Round " + roundNumber + " =====");
  }

  @Override
  public void displayTurnOrder(List<Combatant> turnOrder) {
    System.out.print("Turn order: ");
    for (int i = 0; i < turnOrder.size(); i++) {
      Combatant c = turnOrder.get(i);
      System.out.print(c.getName() + " (SPD " + c.getSpeed() + ")");
      if (i < turnOrder.size() - 1)
        System.out.print(" -> ");
    }
    System.out.println();
  }

  @Override
  public int getActionChoice(List<Action> availableActions) {
    System.out.println();
    System.out.println("Choose action:");
    for (int i = 0; i < availableActions.size(); i++) {
      System.out.println("  " + (i + 1) + ". " + availableActions.get(i).getName());
    }
    return readChoice(1, availableActions.size()) - 1;
  }

  @Override
  public int getTargetChoice(List<Enemy> enemies) {
    System.out.println();
    System.out.println("Choose target:");
    for (int i = 0; i < enemies.size(); i++) {
      Enemy e = enemies.get(i);
      System.out.println("  " + (i + 1) + ". " + e.getName()
          + " (HP: " + e.getHp() + "/" + e.getMaxHp() + ")");
    }
    System.out.println("  0. Cancel");
    int choice = readChoice(0, enemies.size());
    return (choice == 0) ? -1 : choice - 1;
  }

  @Override
  public int getItemUseChoice(List<Item> items) {
    System.out.println();
    System.out.println("Choose item to use:");
    for (int i = 0; i < items.size(); i++) {
      System.out.println("  " + (i + 1) + ". " + items.get(i).getName()
          + " - " + items.get(i).getDescription());
    }
    System.out.println("  0. Cancel");
    int choice = readChoice(0, items.size());
    return (choice == 0) ? -1 : choice - 1;
  }

  @Override
  public void displayActionResult(String message) {
    System.out.println(message);
  }

  @Override
  public void displayStatusEffects(Combatant combatant) {
    List<StatusEffect> effects = combatant.getStatusEffects();
    if (effects != null && !effects.isEmpty()) {
      System.out.print("  Active effects on " + combatant.getName() + ": ");
      for (StatusEffect e : effects) {
        System.out.print("[" + e.getName() + " " + e.getDuration() + "t] ");
      }
      System.out.println();
    }
  }

  @Override
  public void displayRoundEnd(Player player, List<Enemy> enemies, int roundNumber) {
    System.out.print("End of Round " + roundNumber + ": ");
    System.out.print(player.getName() + " HP: " + player.getHp() + "/" + player.getMaxHp());

    for (Enemy e : enemies) {
      if (!e.isAlive()) {
        System.out.print(" | " + e.getName() + ": ELIMINATED");
      } else {
        System.out.print(" | " + e.getName() + " HP: " + e.getHp());
        if (e.isStunned()) System.out.print(" [STUNNED]");
      }
    }

    // to count remaining uses per item type
    // find each unqiue item, and count how many is not consumed
    List<String> seen = new ArrayList<>();
    for (Item it : player.getInventory()) {
      if (!seen.contains(it.getName())) {
        seen.add(it.getName());
        int count = 0;
        for (Item i2 : player.getInventory()) {
          if (i2.getName().equals(it.getName()) && !i2.isConsumed()) count++;
        }
        System.out.print(" | " + it.getName() + ": " + count);
      }
    }

    System.out.println(" | Special Skills Cooldown: " + player.getSpecialSkillCooldown() + " rounds");
      System.out.println();
    System.out.println("--------------------------------------------"); 
  }

  @Override
  public void displayVictory(Player player, int totalRounds) {
    System.out.println();
    System.out.println("========================================");
    System.out.println("  Congratulations, you have defeated all your enemies.");

    StringBuilder line = new StringBuilder("  Remaining HP: " + player.getHp() + "/" + player.getMaxHp()
        + " | Total Rounds: " + totalRounds);

    // append remaining (unconsumed) count per item type
    List<String> seen = new ArrayList<>();
    for (Item it : player.getInventory()) {
      if (!seen.contains(it.getName())) {
        seen.add(it.getName());
        int count = 0;
        for (Item i2 : player.getInventory()) {
          if (i2.getName().equals(it.getName()) && !i2.isConsumed()) count++;
        }
        line.append(" | Remaining ").append(it.getName()).append(": ").append(count);
      }
    }

    System.out.println(line);
    System.out.println("========================================");
  }

  @Override
  public void displayDefeat(List<Enemy> remainingEnemies, int totalRounds) {
    int remaining = 0;
    for (Enemy e : remainingEnemies) {
      if (e.isAlive())
        remaining++;
    }
    System.out.println();
    System.out.println("========================================");
    System.out.println("  Defeated. Don't give up, try again!");
    System.out.println("  Enemies remaining: " + remaining
        + " | Total Rounds Survived: " + totalRounds);
    System.out.println("========================================");
  }

  @Override
  public int getReplayChoice() {
    System.out.println();
    System.out.println("What would you like to do?");
    System.out.println("  1. Replay with same settings");
    System.out.println("  2. Start a new game");
    System.out.println("  3. Exit");
    return readChoice(1, 3);
  }

  @Override
  public void displayItemSelectionPrompt(int itemNumber, int totalItems) {
    if (itemNumber == 1) {
      System.out.println("Pick " + totalItems + " items (duplicates allowed):");
    }
    System.out.println("Item " + itemNumber + ":");
  }

  @Override
  public void displayExitMessage() {
    System.out.println("Thanks for playing!");
  }

  // reads menu input choices. reprompt on bad input
  private int readChoice(int min, int max) {
    while (true) {
      System.out.print("> ");
      try {
        int choice = Integer.parseInt(scanner.nextLine().trim());
        if (choice >= min && choice <= max) {
          return choice;
        }
      } catch (NumberFormatException ignored) {
      }
      System.out.println("Invalid input. Please enter a number between " + min + " and " + max + ".");
    }
  }
}
