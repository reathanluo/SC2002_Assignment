package entity.combatant;

public class BasicEnemyAttackBehavior implements EnemyBehavior {
    //enemy performs attack only
    @Override
    public void execute(Combatant self, Combatant target) {
        target.takeDamage(self.getAttack());
    }
}
