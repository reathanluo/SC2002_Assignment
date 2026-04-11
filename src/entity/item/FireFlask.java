package entity.item;

import entity.action.Action;
import entity.combatant.Combatant;
import entity.combatant.Player;
import entity.effect.BurnEffect;
import java.util.List;

public class FireFlask extends Item {

    public FireFlask() {
        super("Fire Flask");
    }

    @Override
    public String getDescription() {
        return "Hurl fire at one enemy. Burns for 20 damage/turn for 3 turns (bypasses DEF).";
    }

    // delegates target resolution to a single-target action so BattleEngine prompts the player to pick an enemy
    @Override
    public Action getTargetResolutionAction(Player user) {
        return new Action() {
            @Override public String getName() { return "Fire Flask"; }
            @Override public String getDescription(Combatant p) { return ""; }
            @Override public void execute(Combatant p, List<Combatant> t) {}
            @Override public boolean isAvailable(Combatant p) { return true; }
            @Override public boolean requiresTargetSelection() { return true; }
            @Override public boolean isAreaOfEffect() { return false; }
        };
    }

    @Override
    public void use(Player user, List<Combatant> targets) {
        for (Combatant target : targets) {
            target.addStatusEffect(new BurnEffect(3, 20));
        }
        markConsumed();
    }
}