package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.HashSet;
import java.util.Set;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;
import yeelp.scalingfeast.init.SFPotion;

public class TalentSoftHearted extends AbstractPotionEffectBasedTalent {

	static Set<PotionEffectBasedAttributeModifier> mods = new HashSet<PotionEffectBasedAttributeModifier>();
	static {
		mods.add(new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "SoftHearted Speed Bonus", SFPotion.softstomach, 10.0, 2));
	}
	
	public TalentSoftHearted() {
		super(mods, new ResourceLocation(MOD_ID, "softHearted"), 2, 1, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
}
