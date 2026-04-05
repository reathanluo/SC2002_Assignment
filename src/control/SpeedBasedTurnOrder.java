package control;

import entity.combatant.Combatant;
import java.util.ArrayList;
import java.util.List;

// sort combatants by SPD stat in descending order so fastest go first
public class SpeedBasedTurnOrder implements TurnOrderStrategy {

    @Override
    public List<Combatant> determineTurnOrder(List<Combatant> combatants) {
        List<Combatant> sorted = new ArrayList<>(combatants);
        // sort combatants by SPD stat in descending orer
        sorted.sort((a, b) -> b.getSpeed() - a.getSpeed());
        return sorted;
    }
}
