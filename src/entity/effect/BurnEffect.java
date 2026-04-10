package entity.effect;

import entity.combatant.Combatant;

public class BurnEffect extends StatusEffect {

    private int damagePerTurn;

    public BurnEffect(int duration, int damagePerTurn) {
        super("Burn", duration);
        this.damagePerTurn = damagePerTurn;
    }

    public int getDamagePerTurn() {
        return damagePerTurn;
    }

    @Override
    public void onApply(Combatant target) {
        System.out.println("  [" + target.getName() + " is now BURNING for " + damagePerTurn + " damage/turn!]");
    }

    public void tick(Combatant target) {
        int newHp = Math.max(0, target.getHp() - damagePerTurn);
        target.setHp(newHp);
        System.out.println("  [BURN] " + target.getName() + " takes " + damagePerTurn + " burn damage! (" + target.getHp() + " HP remaining)");
        if (!target.isAlive()) {
            System.out.println("  " + target.getName() + " burned to death!");
        }
    }
}