package entity.combatant;

import entity.effect.StunEffect;

public class BossAttackBehavior implements EnemyBehavior {

    private static final double CRIT_CHANCE = 0.30;
    private static final double STUN_CHANCE = 0.10;

    @Override
    public void execute(Combatant self, Combatant target) {
        double roll = Math.random();

        if (roll < STUN_CHANCE) {
            target.takeDamage(self.getAttack());
            target.addStatusEffect(new StunEffect(1));
            System.out.println("  *** " + self.getName() + " uses CRUSHING BLOW — " + target.getName() + " is STUNNED! ***");

        } else if (roll < STUN_CHANCE + CRIT_CHANCE) {
            int critDamage = (int)(self.getAttack() * 1.5);
            target.takeDamage(critDamage);
            System.out.println("  *** " + self.getName() + " lands a CRITICAL HIT! ***");

        } else {
            target.takeDamage(self.getAttack());
        }
    }
}
