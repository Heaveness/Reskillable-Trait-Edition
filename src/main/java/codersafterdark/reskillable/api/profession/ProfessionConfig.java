package codersafterdark.reskillable.api.profession;

import java.util.Map;

public class ProfessionConfig {
    private boolean enabled = true;
    private boolean levelButton = true;
    private int levelCap = 32;
    private int skillPointInterval = 2;
    private int baseLevelCost = 3;
    private Map<Integer, Integer> levelStaggering;

    public boolean isEnabled() {return enabled;}

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public boolean hasLevelButton() {
        return levelButton;
    }

    public void setLevelButton(boolean button) {
        this.levelButton = button;
    }

    public int getLevelCap() {
        return levelCap;
    }

    public void setLevelCap(int levelCap) {
        this.levelCap = levelCap;
    }

    public int getSkillPointInterval() {
        return skillPointInterval;
    }

    public void setSkillPointInterval(int skillPointInterval) {
        this.skillPointInterval = skillPointInterval;
    }

    public int getBaseLevelCost() {
        return baseLevelCost;
    }

    public void setBaseLevelCost(int baseLevelCost) {
        this.baseLevelCost = baseLevelCost;
    }

    public Map<Integer, Integer> getLevelStaggering() {
        return levelStaggering;
    }

    public void setLevelStaggering(Map<Integer, Integer> levelStaggering) {
        this.levelStaggering = levelStaggering;
    }

}
