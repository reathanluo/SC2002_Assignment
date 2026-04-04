package com.arena.entity.action;

import com.arena.entity.combatant.Combatant;
import com.arena.entity.combatant.Player;
import com.arena.entity.effect.StunEffect;
import java.util.List;

// Warrior's special skill (deals standard ATK damage to one enemy and sturns them for 2 turns)
// Cooldown: 3 turns after use.
public class ShieldBash extends SpecialSkill {

  public ShieldBash(Player owner) {
    super(owner);
  }

  @Override
  public String getName() {
    return "Shield Bash";
  }

  @Override
  public String getDescription(Combatant performer) {
    return "Deals " + performer.getAttack() + " damage to one enemy and stuns them for 2 turns. (Cooldown: 3 turns)";
  }

  @Override
  public void execute(Combatant performer, List<Combatant> targets) {
    Combatant target = targets.get(0);
    target.takeDamage(performer.getAttack());
    target.addStatusEffect(new StunEffect(3));
    triggerCooldown();
  }

  @Override
  public boolean requiresTargetSelection() {
    return true;
  }

  @Override
  public boolean isAreaOfEffect() {
    return false;
  }
}
