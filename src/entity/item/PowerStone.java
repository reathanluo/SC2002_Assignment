package entity.item;

import entity.action.Action;
import entity.combatant.Combatant;
import entity.combatant.Player;
import java.util.List;

// fires the player's special skill for free, cooldown stays exactly as it was
public class PowerStone extends Item {

  public PowerStone() {
    super("Power Stone");
  }

  @Override
  public String getDescription() {
    return "Trigger your special skill once for free (cooldown unchanged).";
  }

  // the special skill knows whether it hits one target or all of them,
  // so just let it handle target resolution rather than duplicating that logic here
  @Override
  public Action getTargetResolutionAction(Player user) {
    return user.getSpecialSkill();
  }

  @Override
  public void use(Player user, List<Combatant> targets) {
    // execute() triggers the cooldown internally, so save the pre-decrement
    // cooldown and write it back after, that way the stone has zero net
    // effect on cooldown (undoing both the skill's triggerCooldown and the
    // decrement that BattleEngine already applied this turn).
    int savedCooldown = user.getPreDecrementCooldown();
    user.getSpecialSkill().execute(user, targets);
    user.setSpecialSkillCooldown(savedCooldown);
    markConsumed();
  }
}
