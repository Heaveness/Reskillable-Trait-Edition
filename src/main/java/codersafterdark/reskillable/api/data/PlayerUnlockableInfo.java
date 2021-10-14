package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.*;

public class PlayerUnlockableInfo extends PlayerSkillInfo {

    private static final String TAG_RANK = "rank";
    private static final String TAG_MODIFIERS = "modifiers";

    public final Unlockable unlockable;
    private int rank;
    private final List<AttributeModifier> unlockableAttributes = new ArrayList<>();

    public PlayerUnlockableInfo(Unlockable unlockable) {
        super(unlockable.getParentSkill());
        this.unlockable = unlockable;
        rank = 0;
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        rank = cmp.getInteger(TAG_RANK);

        unlockableAttributes.clear();
        NBTTagList modifiersCmp = cmp.getTagList(TAG_MODIFIERS, 10);

        for (int i = 0; i < modifiersCmp.tagCount(); i++) {
            NBTTagCompound attributeCmp = modifiersCmp.getCompoundTagAt(i);
            unlockableAttributes.add(SharedMonsterAttributes.readAttributeModifierFromNBT(attributeCmp));
        }
    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_RANK, rank);

        NBTTagList modifiersCmp = new NBTTagList();
        for (AttributeModifier a : unlockableAttributes) {
            modifiersCmp.appendTag(SharedMonsterAttributes.writeAttributeModifierToNBT(a));
        }

        cmp.setTag(TAG_MODIFIERS, modifiersCmp);
    }

    public int getRank() {
        if (rank > unlockable.getCap()) {
            rank = unlockable.getCap();
        }
        return rank;
    }

    public void setRank(int rank) {
        int interval = super.skill.getSkillPointInterval();
        spendSkillPoints(rank/interval - this.rank/interval);
        this.rank = rank;
    }

    public List<AttributeModifier> getUnlockableAttributes() {
        return unlockableAttributes;
    }

    public boolean isCapped() {
        return rank >= unlockable.getCap();
    }

    public int getLevelUpCost() {
        return unlockable.getCost();
    }

    public void levelUp() {
        rank++;
    }

    public void addAttributeModifier(IAttributeInstance attributeInstance, AttributeModifier modifier) {
        unlockableAttributes.add(modifier);
        attributeInstance.applyModifier(modifier);
    }

    public void removeUnlockableAttribute(IAttributeInstance attributeInstance) {
        for (AttributeModifier modifier : unlockableAttributes) {
            if (modifier.getName().equals(attributeInstance.getAttribute().getName())) {
                attributeInstance.removeModifier(modifier);
            }
        }
        unlockableAttributes.clear();
    }

}
