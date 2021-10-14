package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.HashSet;
import java.util.Set;

import com.tmtravlr.potioncore.PotionCoreAttributes;

import net.minecraft.util.ResourceLocation;
import yeelp.scalingfeast.init.SFPotion;

public class TalentIronHearted extends AbstractPotionEffectBasedTalent {

	static Set<PotionEffectBasedAttributeModifier> mods = new HashSet<PotionEffectBasedAttributeModifier>();
	static {
		mods.add(new PotionEffectBasedAttributeModifier(PotionCoreAttributes.DAMAGE_RESISTANCE, "IronHearted Damage Resistance Bonus", SFPotion.ironstomach, 10.0, 2));
	}
	
	public TalentIronHearted() {
		super(mods, new ResourceLocation(MOD_ID, "ironHearted"), 2, 3, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
}
