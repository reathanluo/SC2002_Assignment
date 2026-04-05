package entity.combatant;

import entity.action.Action;
import entity.action.ShieldBash;

public class Warrior extends Player {
    private final Action specialSkill;

    public Warrior() {
        super("Warrior", 260, 40, 20, 30);
        this.specialSkill = new ShieldBash(this);
    }

    @Override
    public Action getSpecialSkill() {
        return specialSkill;
    }
}
