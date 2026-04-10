package control;

import entity.item.Item;
import entity.item.Potion;
import entity.item.PowerStone;
import entity.item.SmokeBomb;
import entity.item.FireFlask;

// factory class for creating objects in one place. Index corresponds to position in item selection menu (0 - potion, 1 - power stonem, 2 - smoke bomb)
public class ItemFactory {

    // return new item object for given menu index.
    public static Item createItem(int index) {
        switch (index) {
            case 0: return new Potion();
            case 1: return new PowerStone();
            case 2: return new SmokeBomb();
            case 3: return new FireFlask();
            default: throw new IllegalArgumentException("Unknown item type: " + index);
        }
    }
}
