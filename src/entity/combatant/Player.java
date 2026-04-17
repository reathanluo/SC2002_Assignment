package entity.combatant;


import entity.action.Action;
import entity.item.Item;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// abstract player class to implement specific classes
public abstract class Player extends Combatant {
    private List<Item> inventory;
    private int specialSkillCooldown;
    private int preDecrementCooldown;

    public Player(String name, int hp, int attack, int defense, int speed) {
        super(name, hp, attack, defense, speed);
        this.inventory = new ArrayList<>();
        this.specialSkillCooldown = 0;
        this.preDecrementCooldown = 0;
    }

    public List<Item> getInventory() {
        return Collections.unmodifiableList(inventory);
    }

    public int getSpecialSkillCooldown() {
        return specialSkillCooldown;
    }

    public void setSpecialSkillCooldown(int cooldown) {
        this.specialSkillCooldown = cooldown;
    }

    public void decrementCooldown() {
        preDecrementCooldown = specialSkillCooldown;
        if (specialSkillCooldown > 0) {
            specialSkillCooldown--;
        }
    }

    public int getPreDecrementCooldown() {
        return preDecrementCooldown;
    }

    public void addItem(Item item) {
        inventory.add(item);
    }

    public void removeItem(Item item) {
        inventory.remove(item);
    }

    // returns true if inventory contains at least one item not used yet
    public boolean hasItems() {
        for (Item item : inventory) {
            if (!item.isConsumed())
                return true;
        }
        return false;
    }

    public abstract Action getSpecialSkill();

    // called when player kills an enemy during special skill
    public void onKill() {
    }
}
