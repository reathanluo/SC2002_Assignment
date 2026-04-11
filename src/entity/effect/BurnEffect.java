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
        // message is displayed by BattleEngine via the UI
    }

    // applies burn damage and returns a message describing the result
    public String tick(Combatant target) {
        int newHp = Math.max(0, target.getHp() - damagePerTurn);
        target.setHp(newHp);
        StringBuilder msg = new StringBuilder();
        msg.append("  [BURN] ").append(target.getName()).append(" takes ").append(damagePerTurn)
           .append(" burn damage! (").append(target.getHp()).append(" HP remaining)");
        if (!target.isAlive()) {
            msg.append("\n  ").append(target.getName()).append(" burned to death!");
        }
        return msg.toString();
    }
}