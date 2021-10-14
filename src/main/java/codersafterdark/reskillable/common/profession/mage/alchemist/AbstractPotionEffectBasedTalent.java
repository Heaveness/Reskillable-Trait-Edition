package codersafterdark.reskillable.common.profession.mage.alchemist;

import java.util.HashSet;
import java.util.Set;

import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.PotionEvent.PotionAddedEvent;
import net.minecraftforge.event.entity.living.PotionEvent.PotionExpiryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public abstract class AbstractPotionEffectBasedTalent extends Talent {

	protected static class PotionEffectBasedAttributeModifier {
		private IAttribute attribute;
		private AttributeModifier modifier;
		private Potion potionTrigger;

		PotionEffectBasedAttributeModifier(IAttribute affectAttribute, String name, Potion applicableEffect, double amount, int operation) {
			attribute = affectAttribute;
			potionTrigger = applicableEffect;
			modifier = new AttributeModifier(name, amount, operation);
		}

		public void setModifierIfEffectActive(EntityPlayerMP player) {
			if(player.isPotionActive(potionTrigger)) {
				IAttributeInstance instance = player.getEntityAttribute(attribute);
				AttributeModifier mod = instance.getModifier(modifier.getID());
				if(mod == null) {
					instance.applyModifier(modifier);
				}
			}
		}

		public void removeModifierIfEffectIsNotActive(EntityPlayerMP player) {
			if(!player.isPotionActive(potionTrigger)) {
				IAttributeInstance instance = player.getEntityAttribute(attribute);
				AttributeModifier mod = instance.getModifier(modifier.getID());
				if(mod != null) {
					instance.removeModifier(modifier);
				}
			}
		}
	}

	private Set<PotionEffectBasedAttributeModifier> mods = new HashSet<PotionEffectBasedAttributeModifier>();

	protected AbstractPotionEffectBasedTalent(Set<PotionEffectBasedAttributeModifier> mods, ResourceLocation name, int x, int y, ResourceLocation professionName, ResourceLocation subProfessionName, int cost, String... defaultRequirements) {
		super(name, x, y, professionName, subProfessionName, cost, defaultRequirements);
		this.mods = mods;
		MinecraftForge.EVENT_BUS.register(this);
	}

	@SubscribeEvent
	public void onPotionApply(PotionAddedEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayerMP) {
			mods.forEach((pmod) -> pmod.setModifierIfEffectActive((EntityPlayerMP)event.getEntityLiving()));
		}
	}
	
	@SubscribeEvent
	public void onPotionExpiry(PotionExpiryEvent event) {
		if(event.getEntityLiving() instanceof EntityPlayerMP) {
			mods.forEach((pmod) -> pmod.removeModifierIfEffectIsNotActive((EntityPlayerMP)event.getEntityLiving()));
		}
	}
}
