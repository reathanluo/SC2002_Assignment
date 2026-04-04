package com.arena.entity.action;

import com.arena.entity.combatant.Combatant;
import com.arena.entity.combatant.Player;

// abstract class for player special skills with 3-turn cooldown.

public abstract class SpecialSkill implements Action {
    protected static final int COOLDOWN_TURNS = 3;
    protected final Player owner; // player object this skill belongs to

    protected SpecialSkill(Player owner) {
        this.owner = owner;
    }

    // available only when cooldown has counted down to 0
    @Override
    public boolean isAvailable(Combatant performer) {
        return owner.getSpecialSkillCooldown() == 0;
    }

    // set cooldown back to full, called by subclass after execution of special skill
    protected void triggerCooldown() {
        owner.setSpecialSkillCooldown(COOLDOWN_TURNS);
    }
}
