package entity.item;

import entity.combatant.Combatant;
import entity.combatant.Player;
import java.util.List;

// heals the player for 100 HP, capped at their max so no overflow
public class Potion extends Item {
  private int healAmount;

  public Potion() {
    super("Potion");
    this.healAmount = 100;
  }

  public int getHealAmount() {
    return healAmount;
  }

  @Override
  public String getDescription() {
    return "Restore 100 HP (up to max HP).";
  }

  @Override
  public void use(Player user, List<Combatant> targets) {
    // heal handles the cap logic, so just pass the amount straight through
    user.heal(healAmount);
    markConsumed();
  }
}
