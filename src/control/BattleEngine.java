package control;

import entity.action.*;
import entity.combatant.*;
import entity.item.Item;
import boundary.GameUI;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// core class of battle system, runs the round loop, and sets up turn order, executes player and enemey actions,
// checks win/loss conditions each turn
public class BattleEngine {
    private Player player;
    private List<Enemy> enemies; // all enemies ever spawned this run (including eliminated)
    private Level level;
    private TurnOrderStrategy turnOrderStrategy;
    private GameUI ui;
    private int roundCount;
    private boolean battleOver;
    // Tracks how many of each enemy type have been labeled so labels are given
    // correctly even when backup enemies are added (eg: initial: Goblin A/B, backup: Goblin C).
    private Map<String, Integer> enemyLabelCounters = new HashMap<>();

    public BattleEngine(Player player, Level level, TurnOrderStrategy turnOrderStrategy, GameUI ui) {
        this.player = player;
        this.level = level;
        this.enemies = new ArrayList<>(level.getInitialSpawn());
        assignLabels(this.enemies); // give each enemy a unique letter suffix (Goblin A, Wolf B, etc)
        this.turnOrderStrategy = turnOrderStrategy;
        this.ui = ui;
        this.roundCount = 0;
        this.battleOver = false;
    }

    // runs rounds in a loop until the battle is over (player dead or all enemies defeated).
    public void startBattle() {
        while (!battleOver) {
            processRound();
        }
    }

    // One full round: determine turn order, process each combatant's turn in order,
    // check for battle end after every turn, then display the end-of-round summary.
    public void processRound() {
        roundCount++;
        ui.displayRoundStart(roundCount);

        List<Combatant> allAlive = getAllAliveCombatants();
        List<Combatant> turnOrder = turnOrderStrategy.determineTurnOrder(allAlive);
        ui.displayTurnOrder(turnOrder);

        for (Combatant combatant : turnOrder) {
            // A combatant may have died earlier this round (eg: from AoE damage), skip if so.
            if (!combatant.isAlive()) continue;

            processTurn(combatant);

            if (checkBattleEnd()) {
                battleOver = true;
                return; // end the round early, no point continuing after battle is decided
            }
        }

        ui.displayRoundEnd(player, enemies, roundCount);
    }

    // Handles one combatant's turn, tick effects, check stun, then action.
    private void processTurn(Combatant combatant) {
        // Tick status effects first, an effect that expires here won't block this turn.
        combatant.applyStatusEffects();

        if (!combatant.isAlive()) return;

        if (combatant.isStunned()) {
            ui.displayActionResult(combatant.getName() + " is STUNNED , turn skipped!");
            return;
        }

        if (combatant instanceof Player) {
            ((Player) combatant).decrementCooldown(); // count down special skill cooldown each turn
            ui.displayPlayerTurnStart((Player) combatant);
            processPlayerTurn((Player) combatant);
        } else if (combatant instanceof Enemy) {
            processEnemyTurn((Enemy) combatant);
        }
    }

    // show the action menu and loop until the player select an action.
    private void processPlayerTurn(Player player) {
        while (true) {
            List<Action> availableActions = buildAvailableActions(player);

            int actionIndex = ui.getActionChoice(availableActions);
            Action chosenAction = availableActions.get(actionIndex);

            // choosing "Use Item" action will open the item submenu.
            // Replace it with a real ItemAction once the player picks a specific item.
            if (chosenAction.getName().equals("Use Item")) {
                chosenAction = selectItem(player);
                if (chosenAction == null) continue; // player cancelled, go back to action menu
            }

            ui.displayActionContext(chosenAction.getName(), chosenAction.getDescription(player));

            List<Combatant> targets = resolveTargets(chosenAction);
            if (targets == null) continue; // player cancelled at target selection, go back to action menu

            // Save HP values before execute() so we can report what changed afterward.
            // Some actions (eg: ArcaneBlast) hit multiple targets, so we check all of them.
            List<Enemy> aliveEnemies = getAliveEnemies();
            int[] hpBefore = new int[aliveEnemies.size()];
            for (int i = 0; i < aliveEnemies.size(); i++) hpBefore[i] = aliveEnemies.get(i).getHp();
            int playerHpBefore = player.getHp();

            chosenAction.execute(player, targets);

            displayPostActionResults(aliveEnemies, hpBefore, playerHpBefore);
            break;
        }
    }

    // Compares post action HP to the pre action saved HP and prints what changed
    private void displayPostActionResults(List<Enemy> snapshot, int[] hpBefore, int playerHpBefore) {
        for (int i = 0; i < snapshot.size(); i++) {
            Enemy e = snapshot.get(i);
            int before = hpBefore[i];
            if (!e.isAlive()) {
                ui.displayActionResult("  " + e.getName() + ": " + before + " - 0 HP - ELIMINATED!");
            } else if (e.getHp() < before) {
                ui.displayActionResult("  " + e.getName() + ": " + before + " - " + e.getHp() + " HP (-" + (before - e.getHp()) + ")");
            }
        }
        // show HP gain if the player healed (eg: from a Potion).
        int playerHpAfter = player.getHp();
        if (playerHpAfter > playerHpBefore) {
            ui.displayActionResult("  " + player.getName() + ": " + playerHpBefore + " - " + playerHpAfter + " HP (+" + (playerHpAfter - playerHpBefore) + ")");
        }
    }

    // Builds the list of actions the player can choose this turn.
    // "Use Item" only appears if there's at least one unconsumed item.
    // The special skill only appears when its cooldown has reached 0.
    private List<Action> buildAvailableActions(Player player) {
        List<Action> actions = new ArrayList<>();
        actions.add(new BasicAttack());
        actions.add(new Defend());

        if (player.hasItems()) {
            actions.add(new ItemAction(null)); // placeholder, replaced in processPlayerTurn
        }

        Action skill = player.getSpecialSkill();
        if (skill.isAvailable(player)) {
            actions.add(skill);
        }

        return actions;
    }

    // prompts the player to pick an item from their unconsumed inventory.
    // returns null if player cancels.
    private ItemAction selectItem(Player player) {
        List<Item> usableItems = new ArrayList<>();
        for (Item item : player.getInventory()) {
            if (!item.isConsumed()) usableItems.add(item);
        }
        int itemIndex = ui.getItemUseChoice(usableItems);
        if (itemIndex == -1) return null; // cancelled
        return new ItemAction(usableItems.get(itemIndex));
    }

    // determines the target list for an action before it is executed.
    // For ItemAction, some items (eg: PowerStone) delegate target resolution to
    // their underlying special skill, so this method recursively call that skill as the action.
    // returns null if the player cancels target selection.
    private List<Combatant> resolveTargets(Action action) {
        if (action instanceof ItemAction) {
            Action delegate = ((ItemAction) action).getTargetResolutionDelegate(player);
            if (delegate != null) return resolveTargets(delegate); // eg: PowerStone -> special skill
        }

        List<Combatant> targets = new ArrayList<>();

        if (action.isAreaOfEffect()) {
            targets.addAll(getAliveEnemies()); // hit everything alive
        } else if (action.requiresTargetSelection()) {
            List<Enemy> aliveEnemies = getAliveEnemies();
            int targetIndex = ui.getTargetChoice(aliveEnemies);
            if (targetIndex == -1) return null; // cancelled
            targets.add(aliveEnemies.get(targetIndex));
        }
        // else, no targets needed (eg: Defend applies to the performer only)

        return targets;
    }

    // checks if the player has damage negation before the attack lands,
    // then captures the damage by comparing HP before and after.
    private void processEnemyTurn(Enemy enemy) {
        boolean blocked = player.hasDamageNegation();
        int hpBefore = player.getHp();
        enemy.performAction(player);
        int damage = hpBefore - player.getHp(); // 0 if blocked
        ui.displayEnemyAttack(enemy, player, damage, blocked);
    }

    // returns true if the battle should end, either the player is dead,
    // or all enemies are defeated and no backup wave remains.
    private boolean checkBattleEnd() {
        if (!player.isAlive()) {
            return true;
        }
        if (areAllEnemiesDefeated()) {
            if (level.hasBackupSpawn() && !level.isBackupSpawned()) {
                spawnBackup(); // backup wave spawned after initial enemies are dead
                return false;
            }
            return true;
        }
        return false;
    }

    // add backup enemies to the fight. enemy suffix labels continue from where the initial spawn left off
    // because enemyLabelCounters is shared across both waves
    private void spawnBackup() {
        level.triggerBackupSpawn();
        List<Enemy> backup = level.getBackupSpawn();
        enemies.addAll(backup);
        assignLabels(backup);
        ui.displayActionResult("Backup enemies have arrived!");
    }

    // appends a letter suffix (eg: Goblin A, Wolf B) to each enemy's name within their type group
    // Uses a shared counter map so initial and backup spawns get consistent naming.
    private void assignLabels(List<Enemy> newEnemies) {
        for (Enemy e : newEnemies) {
            String base = e.getName(); // "Goblin" or "Wolf" before any label is added
            int idx = enemyLabelCounters.getOrDefault(base, 0);
            e.setName(base + " " + (char)('A' + idx)); // 0 = 'A', 1 = 'B', 2 = 'C', etc.
            enemyLabelCounters.put(base, idx + 1);
        }
    }

    private boolean areAllEnemiesDefeated() {
        for (Enemy e : enemies) {
            if (e.isAlive()) return false;
        }
        return true;
    }

    private List<Combatant> getAllAliveCombatants() {
        List<Combatant> alive = new ArrayList<>();
        if (player.isAlive()) alive.add(player);
        for (Enemy e : enemies) {
            if (e.isAlive()) alive.add(e);
        }
        return alive;
    }

    private List<Enemy> getAliveEnemies() {
        List<Enemy> alive = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e.isAlive()) alive.add(e);
        }
        return alive;
    }

    public boolean isPlayerVictory() {
        return player.isAlive() && areAllEnemiesDefeated();
    }

    public Player getPlayer() { return player; }
    public List<Enemy> getEnemies() { return Collections.unmodifiableList(enemies); }
    public int getRoundCount() { return roundCount; }
    public boolean isBattleOver() { return battleOver; }
}
