package entity.combatant;

// interface for implementing different enemy attack behaviors (Strategy pattern)
public interface EnemyBehavior {
    // returns a special message to display (eg: "CRITICAL HIT!"), or null for default attacks
    String execute(Combatant self, Combatant target);
}

