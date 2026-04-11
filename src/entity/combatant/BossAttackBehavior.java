package entity.combatant;

import entity.effect.StunEffect;
import entity.effect.BurnEffect;

// Boss attack behavior with random chance to trigger special attacks each turn.
// Normal attack:  50% chance
// Critical hit:   30% chance — deals 1.5x attack damage
// Stun attack:    10% chance — deals normal damage + stuns for 1 turn
// Fire breath:    10% chance — deals light damage + applies Burn for 3 turns
public class BossAttackBehavior implements EnemyBehavior {

    private static final double CRIT_CHANCE  = 0.30;
    private static final double STUN_CHANCE  = 0.10;
    private static final double FIRE_CHANCE  = 0.10;
    // remaining 50% = normal attack

    @Override
    public String execute(Combatant self, Combatant target) {
        double roll = Math.random();

        if (roll < FIRE_CHANCE) {
            // Fire breath: light damage + burn for 3 turns (15 dmg/turn)
            target.takeDamage(self.getAttack() / 2);
            target.addStatusEffect(new BurnEffect(3, 15));
            return "  *** " + self.getName() + " breathes FIRE — " + target.getName() + " is BURNING! ***";

        } else if (roll < FIRE_CHANCE + STUN_CHANCE) {
            // Crushing blow: normal damage + stun for 1 turn
            target.takeDamage(self.getAttack());
            target.addStatusEffect(new StunEffect(1));
            return "  *** " + self.getName() + " uses CRUSHING BLOW — " + target.getName() + " is STUNNED! ***";

        } else if (roll < FIRE_CHANCE + STUN_CHANCE + CRIT_CHANCE) {
            // Critical hit: 1.5x damage
            int critDamage = (int)(self.getAttack() * 1.5);
            target.takeDamage(critDamage);
            return "  *** " + self.getName() + " lands a CRITICAL HIT! ***";

        } else {
            // Normal attack
            target.takeDamage(self.getAttack());
            return null;
        }
    }
}