package entity.effect;

// Applied when Shield Bash lands successfully.
// While this effect is active, the target cannot perform any actions.
public class StunEffect extends StatusEffect {

  public StunEffect(int duration) {
    super("Stun", duration);
  }

  // Override this so the game knows the affected combatant must skip their turn.
  @Override
  public boolean preventsAction() {
    return true;
  }
}
