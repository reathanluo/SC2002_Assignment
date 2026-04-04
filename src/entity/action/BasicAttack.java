package com.arena.entity.action;

import com.arena.entity.combatant.Combatant;
import java.util.List;

// basic single-target attack, deals damage equal to the performer's ATK stat, reduced by target's DEF stat
public class BasicAttack implements Action {

  @Override
  public String getName() {
    return "Basic Attack";
  }

  @Override
  public String getDescription(Combatant performer) {
    return "Deals " + performer.getAttack() + " damage to one enemy (reduced by target DEF).";
  }

  @Override
  public void execute(Combatant performer, List<Combatant> targets) {
    Combatant target = targets.get(0);
    target.takeDamage(performer.getAttack());
  }

  @Override
  public boolean isAvailable(Combatant performer) {
    return true;
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
