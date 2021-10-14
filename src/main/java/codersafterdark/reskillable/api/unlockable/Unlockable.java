package codersafterdark.reskillable.api.unlockable;

import codersafterdark.reskillable.common.Reskillable;
import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.skill.Skill;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import java.util.*;

public abstract class Unlockable extends IForgeRegistryEntry.Impl<Unlockable> implements Comparable<Unlockable> {
    private final String name;
    protected UnlockableConfig unlockableConfig;
    private ResourceLocation icon;
    private Skill parentSkill;

    public Unlockable(ResourceLocation name, int x, int y, ResourceLocation skillName, int cost, String... defaultRequirements) {
        this.name = name.toString().replace(":", ".");
        setRegistryName(name);
        setIcon(new ResourceLocation(name.getNamespace(), "textures/unlockables/" + name.getPath() + ".png"));
        this.unlockableConfig = ReskillableAPI.getInstance().getTraitConfig(name, x, y, cost, defaultRequirements);
        setParentSkill(skillName);
    }

    @Nonnull
    public Skill getParentSkill() {
        return parentSkill;
    }

    protected void setParentSkill(ResourceLocation skillName) {
        if (parentSkill != null) {
            if (skillName != null && skillName.equals(parentSkill.getRegistryName())) {
                //The skill is already the parent skill
                return;
            }
            //Remove from old parent skill if there already is a parent skill
            parentSkill.getUnlockables().remove(this);
        }
        parentSkill = Objects.requireNonNull(ReskillableRegistries.SKILLS.getValue(skillName));
        if (isEnabled()) {
            if (parentSkill.isEnabled()) {
                parentSkill.addUnlockable(this);
            } else {
                Reskillable.logger.log(Level.ERROR, getName() + " is enabled but the parent skill: " + parentSkill.getName() + " is disabled. Disabling: " + getName());
                this.unlockableConfig.setEnabled(false);
            }
        }
    }

    public RequirementHolder getRequirements() {
        return unlockableConfig.getRequirementHolder();
    }

    public String getKey() {
        return name;
    }

    public String getName() {
        return new TextComponentTranslation("reskillable.unlock." + getKey()).getUnformattedComponentText();
    }

    public String getDescription() {
        return new TextComponentTranslation("reskillable.unlock." + getKey() + ".desc").getUnformattedComponentText();
    }

    public void setCap(int cap) {unlockableConfig.setRankCap(cap);}

    public int getCap() {return unlockableConfig.getRankCap();}

    public ResourceLocation getIcon() {
        return icon;
    }

    protected void setIcon(ResourceLocation newIcon) {
        icon = newIcon;
    }

    public void onUnlock(EntityPlayer player) {
    }

    public void onLock(EntityPlayer player) {
    }

    public boolean hasSpikes() {
        return false;
    }

    public boolean isEnabled() {
        return unlockableConfig.isEnabled();
    }

    @Override
    public int compareTo(@Nonnull Unlockable o) {
        int skillCmp = getParentSkill().compareTo(o.getParentSkill());
        if (skillCmp == 0) {
            return getName().compareTo(o.getName());
        }
        return skillCmp;
    }

    public int getCost() {
        return unlockableConfig.getCost();
    }

    public int getX() {
        return unlockableConfig.getX();
    }

    public int getY() {
        return unlockableConfig.getY();
    }

    public final UnlockableConfig getUnlockableConfig() {
        return unlockableConfig;
    }
}