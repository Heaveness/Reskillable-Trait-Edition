package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.HashSet;
import java.util.Set;

import com.tmtravlr.potioncore.potion.PotionMagicShield;

import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.util.ResourceLocation;

public class TalentAlchemicDrive extends AbstractPotionEffectBasedTalent {

	static Set<PotionEffectBasedAttributeModifier> mods = new HashSet<PotionEffectBasedAttributeModifier>();
	static {
		mods.add(new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.MOVEMENT_SPEED, "AlchemicDrive Speed Boost", PotionMagicShield.INSTANCE, 10.0, 2));
		mods.add(new PotionEffectBasedAttributeModifier(SharedMonsterAttributes.ATTACK_SPEED, "AlchemicDrive Attack Speed Boost", PotionMagicShield.INSTANCE, 25.0, 2));
	}
	protected TalentAlchemicDrive() {
		super(mods, new ResourceLocation(MOD_ID, "alchemicDrive"), 1, 3, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
}
