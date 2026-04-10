package entity.combatant;

public class Boss extends Enemy {

    public Boss() {
        super("Dragon Boss", 200, 70, 25, 20, new BossAttackBehavior());
    }
}
