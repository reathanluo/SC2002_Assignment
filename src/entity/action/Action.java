package entity.action;

import entity.combatant.Combatant;
import java.util.List;

// interface for implementing battle actions (attack, defend, item, special skills)
public interface Action {
  String getName();

  // return description of action shown to player
  String getDescription(Combatant performer);

  // carry out an action by the performer on the targets
  void execute(Combatant performer, List<Combatant> targets);

  // return false if action cannot be used now (cooldown)
  boolean isAvailable(Combatant performer);

  // whether BattleEngine should ask player for targets before executing
  boolean requiresTargetSelection();

  // whether action can hit all enemies alive
  boolean isAreaOfEffect();
}
