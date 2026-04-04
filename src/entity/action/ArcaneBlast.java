package com.arena.entity.action;

import com.arena.entity.combatant.Combatant;
import com.arena.entity.combatant.Player;
import java.util.List;

// Wizard's special skill (deals BasicAttack damage to ALL enemies at once)
// each kill also grants Wizard +10 ATK (stacks) for rest of the battle
// Cooldown: 3 turns after use.
public class ArcaneBlast extends SpecialSkill {

  public ArcaneBlast(Player owner) {
    super(owner);
  }

  @Override
  public String getName() {
    return "Arcane Blast";
  }

  @Override
  public String getDescription(Combatant performer) {
    return "Deals " + performer.getAttack() + " damage to ALL enemies. Each kill grants +10 ATK until end of level. (Cooldown: 3 turns)";
  }

  @Override
  public void execute(Combatant performer, List<Combatant> targets) {
    for (Combatant target : targets) {
      // check alive status before dealing damage
      boolean wasAlive = target.isAlive();
      target.takeDamage(performer.getAttack());
      if (wasAlive && !target.isAlive()) {
        owner.onKill(); // Wizard gains +10 ATK for each kill
      }
    }
    triggerCooldown();
  }

  @Override
  public boolean requiresTargetSelection() {
    return false;
  }

  @Override
  public boolean isAreaOfEffect() {
    return true;
  }
}
