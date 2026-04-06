package entity.effect;

// Applied when the player uses a Smoke Bomb.
// While active, the combatant takes no damage from enemy attacks.
public class SmokeBombEffect extends StatusEffect {

  public SmokeBombEffect(int duration) {
    super("Smoke Bomb Invulnerability", duration);
  }

  // Override this so the game knows incoming damage should be ignored
  // while the Smoke Bomb effect is active.
  @Override
  public boolean negatesDamage() {
    return true;
  }
}
