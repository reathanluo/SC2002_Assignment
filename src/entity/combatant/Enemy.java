package entity.combatant;

// abstract class for all enemy classes
public abstract class Enemy extends Combatant {
    private EnemyBehavior behavior;

    public Enemy(String name, int hp, int attack, int defense, int speed, EnemyBehavior behavior) {
        super(name, hp, attack, defense, speed);
        this.behavior = behavior;
    }

    public EnemyBehavior getBehavior() {
        return behavior;
    }

    // called by BattleEngine on enemy's turn. Returns special message or null.
    public String performAction(Combatant target) {
        if (behavior != null) {
            return behavior.execute(this, target);
        }
        return null;
    }
}
