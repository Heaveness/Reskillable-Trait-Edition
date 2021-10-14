package codersafterdark.reskillable.common.profession.mage.alchemist;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

import com.tmtravlr.potioncore.potion.PotionExtension;

import codersafterdark.reskillable.common.util.talentskeletons.TimeBasedTalent;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;

public class TalentTrueExtension extends TimeBasedTalent {

	public TalentTrueExtension() {
		super(10, new ResourceLocation(MOD_ID, "trueExtension"), 1, 1, new ResourceLocation(MOD_ID, "mage"), new ResourceLocation(MOD_ID, "alchemist"), 3, "profession|reskillable:mage|13");
	}
	
	@Override
	protected boolean shouldPeformAction(EntityPlayerMP player, Timer t) {
		return super.shouldPeformAction(player, t) && !player.isPotionActive(PotionExtension.INSTANCE);
	}

	@Override
	protected void processPlayer(EntityPlayerMP player) {
		player.addPotionEffect(new PotionEffect(PotionExtension.INSTANCE, 100, 1, false, false));
	}
}
