package entity.effect;

import entity.combatant.Combatant;

// Base class for all status effects that last for a limited number of turns.
// Each effect tracks how long it stays active on a combatant.
public abstract class StatusEffect {
  private String name;
  private int duration;

  public StatusEffect(String name, int duration) {
    this.name = name;
    this.duration = duration;
  }

  public String getName() {
    return name;
  }

  public int getDuration() {
    return duration;
  }

  // Reduce the remaining duration by 1 at the start of the combatant's turn.
  public void decrementDuration() {
    if (duration > 0) {
      duration--;
    }
  }

  // Check if the effect has run out of time and should be removed.
  public boolean isExpired() {
    return duration <= 0;
  }

  // Override this in effects like Stun if the combatant should skip their turn.
  public boolean preventsAction() {
    return false;
  }

  // Override this in effects like Defend if incoming damage should be blocked.
  public boolean negatesDamage() {
    return false;
  }

  // Runs immediately after the effect is applied to a combatant.
  // Useful for temporary stat boosts (for example increasing DEF).
  public void onApply(Combatant target) {}

  // Runs when the effect expires and gets removed.
  // Use this to undo anything changed earlier in onApply().
  public void onRemove(Combatant target) {}
}
