package com.arena.control;

import com.arena.entity.combatant.*;
import java.util.Arrays;
import java.util.Collections;

// factory class for creating Level objects, defines enemy lists and level descriptions
public class LevelFactory {

    // Retrieve description for a particular difficulty level
    public static String getDescription(Difficulty difficulty) {
        return createLevel(difficulty).getDescription();
    }

    // return new Level object for the selected difficulty
    public static Level createLevel(Difficulty difficulty) {
        switch (difficulty) {
            case EASY:
                return new Level(difficulty, "Easy   - 3 Goblins",
                    Arrays.asList(new Goblin(), new Goblin(), new Goblin()),
                    Collections.emptyList());
            case MEDIUM:
                return new Level(difficulty, "Medium - 1 Goblin + 1 Wolf | Backup: 2 Wolves",
                    Arrays.asList(new Goblin(), new Wolf()),
                    Arrays.asList(new Wolf(), new Wolf()));
            case HARD:
                return new Level(difficulty, "Hard   - 2 Goblins | Backup: 1 Goblin + 2 Wolves",
                    Arrays.asList(new Goblin(), new Goblin()),
                    Arrays.asList(new Goblin(), new Wolf(), new Wolf()));
            default:
                throw new IllegalArgumentException("Unknown difficulty: " + difficulty);
        }
    }
}
