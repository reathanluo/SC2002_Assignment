package entity.effect;

import entity.combatant.Combatant;

// Applied when the player chooses the Defend action.
// Temporarily increases the combatant’s DEF by 10 while the effect is active.
// The bonus is applied right away and removed once the effect expires.
public class DefendEffect extends StatusEffect {
  private int defenseBonus;

  public DefendEffect(int duration) {
    super("Defend", duration);
    this.defenseBonus = 10;
  }

  public int getDefenseBonus() {
    return defenseBonus;
  }

  @Override
  public void onApply(Combatant target) {
    // Increase DEF immediately when the effect is applied
    target.setDefense(target.getDefense() + defenseBonus);
  }

  @Override
  public void onRemove(Combatant target) {
    // Restore DEF back to its original value after the effect ends
    target.setDefense(target.getDefense() - defenseBonus);
  }
}
