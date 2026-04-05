package entity.combatant;

import entity.action.Action;
import entity.action.ArcaneBlast;

public class Wizard extends Player {
    private int bonusAttack; // extra ATK accumulated through kills this battle
    private final Action specialSkill;

    public Wizard() {
        super("Wizard", 200, 50, 10, 20);
        this.bonusAttack = 0;
        this.specialSkill = new ArcaneBlast(this);
    }

    public int getBonusAttack() {
        return bonusAttack;
    }

    public void addBonusAttack(int amount) {
        bonusAttack += amount;
    }

    public void resetBonusAttack() {
        bonusAttack = 0;
    }

    // Overrides base getAttack() to include the bonus ATK on top of the base stat
    @Override
    public int getAttack() {
        return super.getAttack() + bonusAttack;
    }

    @Override
    public Action getSpecialSkill() {
        return specialSkill;
    }

    // Called by ArcaneBlast each time enemy is killed, stacks +10 ATK per kill.
    @Override
    public void onKill() {
        addBonusAttack(10);
    }
}
