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

    // called by BattleEngine on enemey's turn.
    public void performAction(Combatant target) {
        if (behavior != null) {
            behavior.execute(this, target);
        }
    }
}
