package entity.item;

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

    @Override
    public void use(Player user, List<Combatant> targets) {
        for (Combatant target : targets) {
            target.addStatusEffect(new BurnEffect(3, 20));
            System.out.println("  " + user.getName() + " hurls a Fire Flask at " + target.getName() + "!");
        }
        markConsumed();
    }
}