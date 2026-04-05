package control;

import entity.combatant.Enemy;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// holds all data necessary for one playthrough of a difficulty
public class Level {
    private Difficulty difficulty;
    private String description; // text used in the difficulty selection menu
    private List<Enemy> initialSpawn; 
    private List<Enemy> backupSpawn;
    private boolean backupSpawned; // tracks whether the backup wave has already arrived

    public Level(Difficulty difficulty, String description, List<Enemy> initialSpawn, List<Enemy> backupSpawn) {
        this.difficulty = difficulty;
        this.description = description;
        this.initialSpawn = initialSpawn;
        this.backupSpawn = (backupSpawn != null) ? backupSpawn : Collections.emptyList();
        this.backupSpawned = false;
    }

    public Difficulty getDifficulty() { return difficulty; }
    public String getDescription() { return description; }
    public List<Enemy> getInitialSpawn() { return Collections.unmodifiableList(initialSpawn); }
    public List<Enemy> getBackupSpawn() { return Collections.unmodifiableList(backupSpawn); }
    public boolean isBackupSpawned() { return backupSpawned; }

    public boolean hasBackupSpawn() {
        return backupSpawn != null && !backupSpawn.isEmpty();
    }

    // marks the backup wave as spawned.
    public void triggerBackupSpawn() {
        this.backupSpawned = true;
    }
}
