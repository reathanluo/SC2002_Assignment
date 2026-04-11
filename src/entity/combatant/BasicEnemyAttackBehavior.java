package entity.combatant;

public class BasicEnemyAttackBehavior implements EnemyBehavior {
    //enemy performs attack only
    @Override
    public String execute(Combatant self, Combatant target) {
        target.takeDamage(self.getAttack());
        return null;
    }
}
