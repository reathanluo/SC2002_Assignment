package entity.combatant;

public class Goblin extends Enemy {

    public Goblin() {
        super("Goblin", 55, 35, 15, 25, new BasicEnemyAttackBehavior());
    }
}

