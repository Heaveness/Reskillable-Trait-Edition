package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.talent.TalentActive;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

public class PlayerProfessionInfo {
    private static final String TAG_LEVEL = "level";
    private static final String TAG_SKILL_POINTS = "skillPoints";
    private static final String TAG_TALENTS = "talents";

    public final Profession profession;

    private int level;
    private int skillPoints;
    private List<Talent> talents = new ArrayList<>();

    public PlayerProfessionInfo(Profession profession) {
        this.profession = profession;
        level = 0;
        respec();
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        level = cmp.getInteger(TAG_LEVEL);
        skillPoints = cmp.getInteger(TAG_SKILL_POINTS);

        talents.clear();
        NBTTagCompound talentsCmp = cmp.getCompoundTag(TAG_TALENTS);

        for (String s : talentsCmp.getKeySet()) {
            Optional.ofNullable(ReskillableRegistries.TALENTS.getValue(new ResourceLocation(s.replace(".", ":"))))
                    .ifPresent(talents::add);
        }
    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_LEVEL, level);
        cmp.setInteger(TAG_SKILL_POINTS, skillPoints);

        NBTTagCompound talentsCmp = new NBTTagCompound();
        for (Talent t : talents) {
            talentsCmp.setBoolean(t.getKey(), true);
        }
        cmp.setTag(TAG_TALENTS, talentsCmp);
    }

    public int getLevel() {
        if (level > profession.getCap()) {
            level = profession.getCap();
        }

        return level;
    }

    public void setLevel(int level) {
        int interval = profession.getSkillPointInterval();
        skillPoints += level / interval - this.level / interval;
        this.level = level;
    }

    public int getRank() {
        return profession.getRank(level);
    }

    public int getSkillPoints() {return this.skillPoints;}

    public void spendSkillPoints(int amount) {
        skillPoints -= amount;
    }

    public int getProfessionPoints() {
        return skillPoints;
    }

    public boolean isCapped() {
        return level >= profession.getCap();
    }

    public int getLevelUpCost() {
        return profession.getLevelUpCost(level);
    }

    public boolean isUnlocked(Talent t) {return talents.contains(t);}

    //TODO decide if this should just call setLevel(level + 1);
    public void levelUp() {
        level++;
        if (level % profession.getSkillPointInterval() == 0) {
            skillPoints++;
        }
    }

    public void addActiveTalents(Set<TalentActive> activeTalents) {
        for (Talent t : talents) {
            if (t instanceof TalentActive) {
                activeTalents.add((TalentActive) t);
            }
        }
    }

    public void lock(Talent t, EntityPlayer p) {
        skillPoints += t.getCost();
        talents.remove(t);
        t.onLock(p);
    }

    public void unlock(Talent u, EntityPlayer p) {
        skillPoints -= u.getCost();
        talents.add(u);
        PlayerDataHandler.get(p).getTalentInfo(u).levelUp();
        u.onUnlock(p);
    }

    public void respec() {
        talents.clear();
        skillPoints = level / profession.getSkillPointInterval();
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        talents.forEach(t -> {
            if (t.isEnabled() && t instanceof IAbilityEventHandler) {
                consumer.accept((IAbilityEventHandler) t);
            }
        });
    }
}
