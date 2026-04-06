package entity.item;

import entity.combatant.Combatant;
import entity.combatant.Player;
import entity.effect.SmokeBombEffect;
import java.util.List;

// slaps a damage immunity effect on the player for 2 turns, current turn counts as one
public class SmokeBomb extends Item {

  public SmokeBomb() {
    super("Smoke Bomb");
  }

  @Override
  public String getDescription() {
    return "Negate all incoming damage for this turn and the next.";
  }

  @Override
  public void use(Player user, List<Combatant> targets) {
    // SmokeBombEffect handles the actual damage negation, just tell it how many turns
    user.addStatusEffect(new SmokeBombEffect(2));
    markConsumed();
  }
}
