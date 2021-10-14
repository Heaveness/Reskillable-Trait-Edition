package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

import java.util.ArrayList;
import java.util.List;

public class PlayerTalentInfo extends PlayerProfessionInfo {

    private static final String TAG_RANK = "rank";
    private static final String TAG_MODIFIERS = "modifiers";

    public final Talent talent;
    private int rank;
    private final List<AttributeModifier> talentAttributes = new ArrayList<>();

    public PlayerTalentInfo(Talent talent) {
        super(talent.getParentProfession());
        this.talent = talent;
        rank = 0;
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        rank = cmp.getInteger(TAG_RANK);

        talentAttributes.clear();
        NBTTagList modifiersCmp = cmp.getTagList(TAG_MODIFIERS, 10);

        for (int i = 0; i < modifiersCmp.tagCount(); i++) {
            NBTTagCompound attributeCmp = modifiersCmp.getCompoundTagAt(i);
            talentAttributes.add(SharedMonsterAttributes.readAttributeModifierFromNBT(attributeCmp));
        }
    }

    public void saveToNBT(NBTTagCompound cmp) {
        cmp.setInteger(TAG_RANK, rank);

        NBTTagList modifiersCmp = new NBTTagList();
        for (AttributeModifier a : talentAttributes) {
            modifiersCmp.appendTag(SharedMonsterAttributes.writeAttributeModifierToNBT(a));
        }

        cmp.setTag(TAG_MODIFIERS, modifiersCmp);
    }

    public int getRank() {
        if (rank > talent.getCap()) {
            rank = talent.getCap();
        }
        return rank;
    }

    public void setRank(int rank) {
        int interval = super.profession.getSkillPointInterval();
        spendSkillPoints(rank/interval - this.rank/interval);
        this.rank = rank;
    }

    public List<AttributeModifier> getTalentAttributes() {return talentAttributes;}

    public boolean isCapped() {
        return rank >= talent.getCap();
    }

    public int getLevelUpCost() {
        return talent.getCost();
    }

    public void levelUp() {
        rank++;
    }

    public void addAttributeModifier(IAttributeInstance attributeInstance, AttributeModifier modifier) {
        talentAttributes.add(modifier);
        attributeInstance.applyModifier(modifier);
    }

    public void removeTalentAttribute(IAttributeInstance attributeInstance) {
        for (AttributeModifier modifier : talentAttributes) {
            if (modifier.getName().equals(attributeInstance.getAttribute().getName())) {
                attributeInstance.removeModifier(modifier);
            }
        }
        talentAttributes.clear();
    }

}
