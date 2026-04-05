package entity.action;

import entity.combatant.Combatant;
import entity.effect.DefendEffect;
import java.util.List;

// raises performer's DEF by 10 for current turn and the next
public class Defend implements Action {

  @Override
  public String getName() {
    return "Defend";
  }

  @Override
  public String getDescription(Combatant performer) {
    return "Boost DEF by 10 for this turn and the next. (Current DEF: "
        + performer.getDefense() + " - " + (performer.getDefense() + 10) + ")";
  }

  @Override
  public void execute(Combatant performer, List<Combatant> targets) {
    // add defend effect for 2 turns
    performer.addStatusEffect(new DefendEffect(2));
  }

  @Override
  public boolean isAvailable(Combatant performer) {
    return true;
  }

  @Override
  public boolean requiresTargetSelection() {
    return false;
  }

  @Override
  public boolean isAreaOfEffect() {
    return false;
  }
}
