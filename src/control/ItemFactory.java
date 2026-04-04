package com.arena.control;

import com.arena.entity.item.Item;
import com.arena.entity.item.Potion;
import com.arena.entity.item.PowerStone;
import com.arena.entity.item.SmokeBomb;

// factory class for creating objects in one place. Index corresponds to position in item selection menu (0 - potion, 1 - power stonem, 2 - smoke bomb)
public class ItemFactory {

    // return new item object for given menu index.
    public static Item createItem(int index) {
        switch (index) {
            case 0: return new Potion();
            case 1: return new PowerStone();
            case 2: return new SmokeBomb();
            default: throw new IllegalArgumentException("Unknown item type: " + index);
        }
    }
}
