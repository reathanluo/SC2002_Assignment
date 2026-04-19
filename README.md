# Turn-Based Combat Arena

A command-line turn-based combat game built in Java for the SC2002 Assignment

## Table of Contents

- [Overview](#overview)
- [Features](#features)
- [Architecture](#architecture)
- [How to Play](#how-to-play)
- [Game Mechanics](#game-mechanics)
- [Design Patterns](#design-patterns)
- [Project Structure](#project-structure)

## Overview

Players choose a character class, select items, and battle through waves of enemies across four difficulty tiers. Combat is turn-based with a speed-based turn order system, status effects, special skills with cooldowns, and consumable items.

## Features

- **2 Playable Classes** — Warrior and Wizard, each with a unique special skill
- **4 Difficulty Levels** — Easy, Medium, Hard, and Nightmare, with varying enemy compositions and backup waves
- **4 Consumable Items** — Potion, Power Stone, Smoke Bomb, and Fire Flask
- **Status Effect System** — Burn (damage over time), Stun (skip turn), Defend (DEF boost), and Smoke Bomb (damage immunity)
- **Boss Fight** — Dragon Boss with randomised special attacks including critical hits, stuns, and fire breath
- **Replay System** — Replay with the same settings or start fresh after each battle

## Architecture

The project follows a **Boundary–Control–Entity (BCE)** architecture:

| Layer | Package | Responsibility |
|-------|---------|---------------|
| **Boundary** | `boundary` | User input/output (`GameUI`, `CLIGameUI`) |
| **Control** | `control` | Game flow, battle logic, factories (`GameController`, `BattleEngine`, `Level`, factories) |
| **Entity** | `entity` | Domain objects — combatants, actions, items, status effects |

## How to Play

### Prerequisites

- Java JDK 11 or above

### Compile & Run

```bash
# From the project root directory
javac -d out src/Main.java src/**/*.java
java -cp out Main
```

### Game Flow

1. **View Stats** — A loading screen displays all player and enemy stats
2. **Choose Character** — Select Warrior or Wizard
3. **Pick Items** — Choose 2 items (duplicates allowed) from 4 options
4. **Select Difficulty** — Pick from Easy, Medium, Hard, or Nightmare
5. **Battle** — Fight through enemy waves using attacks, items, and special skills
6. **Results** — View victory/defeat summary, then choose to replay or exit

## Game Mechanics

### Player Classes

| Class | HP | ATK | DEF | SPD | Special Skill |
|-------|-----|-----|-----|-----|---------------|
| **Warrior** | 260 | 40 | 20 | 30 | Shield Bash — ATK damage + 2-turn stun |
| **Wizard** | 200 | 50 | 10 | 20 | Arcane Blast — ATK damage to ALL enemies, +10 ATK per kill |

### Enemies

| Enemy | HP | ATK | DEF | SPD | Behaviour |
|-------|-----|-----|-----|-----|-----------|
| **Goblin** | 55 | 35 | 15 | 25 | Basic attack |
| **Wolf** | 40 | 45 | 5 | 35 | Basic attack |
| **Dragon Boss** | 200 | 70 | 25 | 20 | 50% normal, 30% crit (1.5×), 10% stun, 10% fire breath |

### Items

| Item | Effect |
|------|--------|
| **Potion** | Restores 100 HP (capped at max) |
| **Power Stone** | Triggers special skill for free without affecting cooldown |
| **Smoke Bomb** | Negates all incoming damage for 2 turns |
| **Fire Flask** | Burns one enemy for 20 damage/turn for 3 turns (bypasses DEF) |

### Difficulty Levels

| Difficulty | Initial Spawn | Backup Wave |
|------------|--------------|-------------|
| **Easy** | 3 Goblins | — |
| **Medium** | 1 Goblin + 1 Wolf | 2 Wolves |
| **Hard** | 2 Goblins | 1 Goblin + 2 Wolves |
| **Nightmare** | 1 Goblin + 1 Wolf | Dragon Boss |

### Combat Rules

- **Turn Order** — Determined by SPD stat (highest goes first)
- **Damage** — `max(0, attacker.ATK − target.DEF)`
- **Special Skills** — 3-turn cooldown after use
- **Status Effects** — Tick at the start of the affected combatant's turn; expired effects are removed automatically
- **Backup Waves** — Spawn after all initial enemies are eliminated (if applicable)

## Design Patterns

| Pattern | Application |
|---------|-------------|
| **Strategy** | `EnemyBehavior` — swappable enemy AI; `TurnOrderStrategy` — swappable turn-ordering logic |
| **Factory Method** | `PlayerFactory`, `ItemFactory`, `LevelFactory` — centralised object creation |
| **BCE** | Three-layer package architecture separating UI, logic, and domain |
| **Template Method** | `StatusEffect` lifecycle hooks (`onApply`, `onRemove`) overridden by subclasses |

## Project Structure

```
src/
├── Main.java                          # Entry point
├── boundary/
│   ├── GameUI.java                    # UI interface
│   └── CLIGameUI.java                 # CLI implementation
├── control/
│   ├── GameController.java            # Game lifecycle controller
│   ├── BattleEngine.java              # Combat loop controller
│   ├── Level.java                     # Level configuration data
│   ├── Difficulty.java                # Difficulty enum
│   ├── LevelFactory.java             # Level creation factory
│   ├── PlayerFactory.java            # Player creation factory
│   ├── ItemFactory.java              # Item creation factory
│   ├── TurnOrderStrategy.java        # Turn order interface
│   └── SpeedBasedTurnOrder.java      # Speed-based turn order
└── entity/
    ├── combatant/
    │   ├── Combatant.java             # Abstract base for all combatants
    │   ├── Player.java                # Abstract player with inventory
    │   ├── Warrior.java               # Tank class (ShieldBash)
    │   ├── Wizard.java                # DPS class (ArcaneBlast)
    │   ├── Enemy.java                 # Abstract enemy with behaviour
    │   ├── Goblin.java                # Basic enemy
    │   ├── Wolf.java                  # Fast enemy
    │   ├── Boss.java                  # Dragon Boss
    │   ├── EnemyBehavior.java         # Enemy AI interface
    │   ├── BasicEnemyAttackBehavior.java  # Standard attack AI
    │   └── BossAttackBehavior.java    # Boss multi-attack AI
    ├── action/
    │   ├── Action.java                # Action interface
    │   ├── BasicAttack.java           # Single-target attack
    │   ├── Defend.java                # DEF boost action
    │   ├── SpecialSkill.java          # Abstract skill with cooldown
    │   ├── ShieldBash.java            # Warrior's special (stun)
    │   ├── ArcaneBlast.java           # Wizard's special (AoE)
    │   └── ItemAction.java            # Item-to-Action adapter
    ├── effect/
    │   ├── StatusEffect.java          # Abstract timed effect
    │   ├── BurnEffect.java            # Damage over time
    │   ├── DefendEffect.java          # Temp DEF boost
    │   ├── StunEffect.java            # Skip turn
    │   └── SmokeBombEffect.java       # Damage immunity
    └── item/
        ├── Item.java                  # Abstract consumable item
        ├── Potion.java                # Heals 100 HP
        ├── PowerStone.java            # Free special skill use
        ├── SmokeBomb.java             # 2-turn damage immunity
        └── FireFlask.java             # Burns target 20 dmg/turn
```
