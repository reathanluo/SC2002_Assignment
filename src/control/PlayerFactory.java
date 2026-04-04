package com.arena.control;

import com.arena.entity.combatant.Player;
import com.arena.entity.combatant.Warrior;
import com.arena.entity.combatant.Wizard;

// factory class for creating Player objects, index corresponds to position in selection menu
public class PlayerFactory {

    // returns chosen player character of the index
    public static Player createPlayer(int index) {
        switch (index) {
            case 0: return new Warrior();
            case 1: return new Wizard();
            default: throw new IllegalArgumentException("Unknown player type: " + index);
        }
    }
}
