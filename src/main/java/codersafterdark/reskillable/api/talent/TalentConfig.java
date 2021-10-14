package codersafterdark.reskillable.api.talent;

import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.unlockable.AutoUnlocker;

public class TalentConfig {
    private boolean enabled = true;
    private int x = 1;
    private int y = 1;
    private int cost = 1;
    private int rankCap = 1;
    private RequirementHolder requirementHolder = RequirementHolder.realEmpty();

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
        //AutoUnlocker.recheckUnlockables();
    }

    public RequirementHolder getRequirementHolder() {
        return requirementHolder;
    }

    public void setRequirementHolder(RequirementHolder requirementHolder) {
        this.requirementHolder = requirementHolder;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setRankCap(int cap) {this.rankCap = cap;}

    public int getRankCap() {return rankCap;}

}
