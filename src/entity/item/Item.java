package entity.item;

import entity.action.Action;
import entity.combatant.Combatant;
import entity.combatant.Player;
import java.util.List;

// Base class for items the player can use during a fight.
// Once used, an item isn't removed, it just gets flagged so the UI
// can grey it out and the player can't pick it again.
public abstract class Item {
  private String name;
  private boolean consumed;

  public Item(String name) {
    this.name = name;
    this.consumed = false;
  }

  public String getName() {
    return name;
  }

  public boolean isConsumed() {
    return consumed;
  }

  // call this at the end of use() to lock the item from being used again
  protected void markConsumed() {
    this.consumed = true;
  }

  public abstract String getDescription();

  // where the actual item logic lives — subclasses handle their own effects
  // and should always call markConsumed() when done
  public abstract void use(Player user, List<Combatant> targets);

  // most items don't need this, but some (like PowerStone) piggyback off
  // an existing skill's targeting logic rather than rolling their own
  public Action getTargetResolutionAction(Player user) {
    return null;
  }
}
