package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import java.util.ArrayList;
import java.util.List;

import com.tmtravlr.potioncore.potion.PotionArchery;
import com.tmtravlr.potioncore.potion.PotionIronSkin;
import com.tmtravlr.potioncore.potion.PotionMagicShield;
import com.tmtravlr.potioncore.potion.PotionReach;
import com.tmtravlr.potioncore.potion.PotionRecoil;

import codersafterdark.reskillable.common.util.talentskeletons.TimeBasedTalent;
import electroblob.wizardry.registry.WizardryPotions;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class TalentIndecisiveDrinker extends TimeBasedTalent {

	private static List<Potion> potions = new ArrayList<Potion>();
	
	static {
		potions.add(MobEffects.HASTE);
		potions.add(MobEffects.RESISTANCE);
		potions.add(PotionReach.INSTANCE);
		potions.add(MobEffects.JUMP_BOOST);
		potions.add(PotionRecoil.INSTANCE);
		potions.add(PotionIronSkin.INSTANCE);
		potions.add(MobEffects.INVISIBILITY);
		potions.add(PotionArchery.INSTANCE);
		potions.add(MobEffects.LUCK);
		potions.add(MobEffects.SPEED);
		potions.add(PotionMagicShield.INSTANCE);
		potions.add(WizardryPotions.ward);
		potions.add(WizardryPotions.empowerment);
	}
	
	public TalentIndecisiveDrinker() {
		super(30, new ResourceLocation(MOD_ID, "indecisiveDrinker"), 1, 2, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
	
	@Override
	protected void processPlayer(EntityPlayerMP player) {
		player.addPotionEffect(new PotionEffect(potions.get((int)(Math.random()*potions.size())), 15*20, 0, false, false));
	}
}
