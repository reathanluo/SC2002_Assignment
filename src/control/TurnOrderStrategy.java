package com.arena.control;

import com.arena.entity.combatant.Combatant;
import java.util.List;

// interface for implementing logic of deciding the order combatants act each round.
public interface TurnOrderStrategy {
    // take list of alive combatants and returns them in correct acting order
    List<Combatant> determineTurnOrder(List<Combatant> combatants);
}
