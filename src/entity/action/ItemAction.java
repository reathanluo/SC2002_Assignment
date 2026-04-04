package com.arena.entity.action;

import com.arena.entity.combatant.Combatant;
import com.arena.entity.combatant.Player;
import com.arena.entity.item.Item;
import java.util.List;

// action for using a particular item. 
public class ItemAction implements Action {
    private Item item;

    public ItemAction(Item item) {
        this.item = item;
    }

    public Item getItem() { return item; }

    // Item like PowerStone need to get targets, like a SpecialSkill. This method returns the
    // specific action so BattleEngine can reuse the targeting logic. Returns null if item handles target itself 
    // or need no targets
    public Action getTargetResolutionDelegate(Player player) {
        return item.getTargetResolutionAction(player);
    }

    @Override
    public String getName() {
        return (item != null) ? "Use Item: " + item.getName() : "Use Item";
    }

    @Override
    public String getDescription(Combatant performer) {
        return (item != null) ? item.getDescription() : "Use an item from your inventory.";
    }

    @Override
    public void execute(Combatant performer, List<Combatant> targets) {
        item.use((Player) performer, targets);
    }

    @Override
    public boolean isAvailable(Combatant performer) {
        if (performer instanceof Player) {
            return ((Player) performer).hasItems();
        }
        return false;
    }

    @Override
    public boolean requiresTargetSelection() {
        return false;
    }

    @Override
    public boolean isAreaOfEffect() {
        return false;
    }
}
