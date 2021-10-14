package codersafterdark.reskillable.api.talent;

import codersafterdark.reskillable.common.Reskillable;
import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.RequirementHolder;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.common.registry.ReskillableSounds;
import electroblob.wizardry.registry.WizardrySounds;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.registries.IForgeRegistryEntry;
import org.apache.logging.log4j.Level;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.stream.Collectors;

public abstract class Talent extends IForgeRegistryEntry.Impl<Talent> implements Comparable<Talent> {
    private final String name;
    protected TalentConfig talentConfig;
    private ResourceLocation icon;
    private Profession parentProfession;
    private Profession.SubProfession subProfession;

    public Talent(ResourceLocation name, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, String... defaultRequirements) {
        this.name = name.toString().replace(":", ".");
        setRegistryName(name);
        this.talentConfig = ReskillableAPI.getInstance().getTalentConfig(name, x, y, cost, defaultRequirements);
        setParentProfession(professionName);
        setSubProfession(subProfessionName);
        setIcon(new ResourceLocation(name.getNamespace(), "textures/talents/" + getSubProfession().getUnformattedName() + "/" + name.getPath() + ".png"));
        //setIcon(new ResourceLocation(name.getNamespace(), "textures/talents/" + name.getPath() + ".png"));
    }

    public Profession.SubProfession getSubProfession() {
        return subProfession;
    }

    public void setSubProfession(ResourceLocation subProfession) {
        if (parentProfession != null && !parentProfession.getAllSubProfessions().isEmpty()) {
            Optional<Profession.SubProfession> subP = parentProfession.getAllSubProfessions().stream().filter(subprofession1 -> subprofession1.getUnformattedName().equals(subProfession.getPath())).findFirst();
            subP.ifPresent(profession -> this.subProfession = profession);
        }
    }

    @Nonnull
    public Profession getParentProfession() {
        return subProfession.getProfession();
    }

    protected void setParentProfession(ResourceLocation professionName) {
        if (parentProfession != null) {
            if (professionName != null && professionName.equals(parentProfession.getRegistryName())) {
                //The skill is already the parent profession
                return;
            }
            //Remove from old parent profession if there already is a parent profession
            parentProfession.getTalents().remove(this);
        }
        parentProfession = Objects.requireNonNull(ReskillableRegistries.PROFESSIONS.getValue(professionName));
        if (isEnabled()) {
            if (parentProfession.isEnabled()) {
                parentProfession.addTalent(this);
            } else {
                Reskillable.logger.log(Level.ERROR, getName() + " is enabled but the parent skill: " + parentProfession.getName() + " is disabled. Disabling: " + getName());
                this.talentConfig.setEnabled(false);
            }
        }
    }

    public RequirementHolder getRequirements() {
        return talentConfig.getRequirementHolder();
    }

    public String getKey() {
        return name;
    }

    public String getName() {
        return new TextComponentTranslation("reskillable.talent." + getKey()).getUnformattedComponentText();
    }

    public String getDescription() {
        return new TextComponentTranslation("reskillable.talent." + getKey() + ".desc").getUnformattedComponentText();
    }

    public void setCap(int cap) {talentConfig.setRankCap(cap);}

    public int getCap() {return talentConfig.getRankCap();}

    public ResourceLocation getIcon() {
        return icon;
    }

    protected void setIcon(ResourceLocation newIcon) {
        icon = newIcon;
    }

    public void onUnlock(EntityPlayer player) {}

    public void onLock(EntityPlayer player) {}

    public boolean hasSpikes() {return false;}

    public boolean isEnabled() {return talentConfig.isEnabled();}

    @Override
    public int compareTo(@Nonnull Talent o) {
        int profCmp = getParentProfession().compareTo(o.getParentProfession());
        if (profCmp == 0) {
            return getName().compareTo(o.getName());
        }
        return profCmp;
    }

    public int getCost() {
        return talentConfig.getCost();
    }

    public int getX() {
        return talentConfig.getX();
    }

    public int getY() {
        return talentConfig.getY();
    }

    public final TalentConfig getTalentConfig() {
        return talentConfig;
    }

}
