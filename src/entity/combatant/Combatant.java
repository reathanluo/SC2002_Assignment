package entity.combatant;


import entity.effect.StatusEffect;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// abstract class for every combatant
// holds stats (HP, ATK, DEF, SPD) and manages active status effects
public abstract class Combatant {
    private String name;
    private int hp;
    private int maxHp;
    private int attack;
    private int defense;
    private int speed;
    private boolean alive;
    private List<StatusEffect> statusEffects;

    public Combatant(String name, int hp, int attack, int defense, int speed) {
        this.name = name;
        this.hp = hp;
        this.maxHp = hp;
        this.attack = attack;
        this.defense = defense;
        this.speed = speed;
        this.alive = true;
        this.statusEffects = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getAttack() {
        return attack;
    }

    public int getDefense() {
        return defense;
    }

    public int getSpeed() {
        return speed;
    }

    public boolean isAlive() {
        return alive;
    }

    public List<StatusEffect> getStatusEffects() {
        return Collections.unmodifiableList(statusEffects);
    }

    public void setHp(int hp) {
        this.hp = Math.max(0, Math.min(hp, maxHp));
        if (this.hp == 0) {
            this.alive = false;
        }
    }

    public void setAttack(int attack) {
        this.attack = attack;
    }

    public void setDefense(int defense) {
        this.defense = defense;
    }

    // applies incoming attack damage reduced by DEF, does nothing if damage-negating effect like smoke is active
    public void takeDamage(int incomingAttack) {
        if (hasDamageNegation())
            return;
        int actualDamage = Math.max(0, incomingAttack - this.defense);
        setHp(this.hp - actualDamage);
    }

    public void heal(int amount) {
        setHp(this.hp + amount);
    }

    // add status effect and applies any stat changes it carries
    public void addStatusEffect(StatusEffect effect) {
        this.statusEffects.add(effect);
        effect.onApply(this); // eg: DefendEffect increases DEF stat immediately
    }

    public void removeStatusEffect(StatusEffect effect) {
        this.statusEffects.remove(effect);
    }

    // called at start of combatant's turn, checks all active effects and removes expired ones.
   public void applyStatusEffects() {
        for (int i = statusEffects.size() - 1; i >= 0; i--) {
            StatusEffect effect = statusEffects.get(i);

            if (effect instanceof entity.effect.BurnEffect) {
                ((entity.effect.BurnEffect) effect).tick(this);
            }

            effect.decrementDuration();
            if (effect.isExpired()) {
                effect.onRemove(this);
                statusEffects.remove(i);
            }
        }
    }

    // returns true if any active effect is flagging that this combatant cannot act.
    public boolean isStunned() {
        for (StatusEffect effect : statusEffects) {
            if (effect.preventsAction()) return true;
        }
        return false;
    }

    // return true if any active effect is blocking all incoming damage
    public boolean hasDamageNegation() {
        for (StatusEffect effect : statusEffects) {
            if (effect.negatesDamage()) return true;
        }
        return false;
    }
}
