package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import com.fantasticsource.dynamicstealth.server.Attributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentAura extends Talent {

    public TalentAura() {
        super(new ResourceLocation(MOD_ID, "aura"), 0, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "profession|reskillable:warrior|6", "reskillable:defense|12");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent.Post event) {
        if (event.getTalent() instanceof TalentAura) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.world.isRemote) {
                PlayerTalentInfo info = PlayerDataHandler.get(player).getTalentInfo(this);
                IAttributeInstance AttributeThreat = player.getEntityAttribute(Attributes.THREATGEN_ATTACK);
                IAttributeInstance visionCone = player.getEntityAttribute(Attributes.SIGHT);
                AttributeModifier modifierSight = new AttributeModifier("dynamicstealth.sight", 500.0, 0);
                AttributeModifier modifierThreat = new AttributeModifier("dynamicstealth.threatGenAttackedBySame", 100.0, 0);
                info.addAttributeModifier(AttributeThreat, modifierThreat);
                info.addAttributeModifier(visionCone, modifierSight);
                PlayerDataHandler.get(player).saveAndSync();
            }
        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent.Post event) {
        if (event.getTalent() instanceof TalentAura) {
            EntityPlayer player = event.getEntityPlayer();
            PlayerTalentInfo info = PlayerDataHandler.get(player).getTalentInfo(this);
            IAttributeInstance threatFromAttacks = player.getEntityAttribute(Attributes.THREATGEN_ATTACK);
            IAttributeInstance visionCone = player.getEntityAttribute(Attributes.SIGHT);
            info.removeTalentAttribute(threatFromAttacks);
            info.removeTalentAttribute(visionCone);
            PlayerDataHandler.get(player).saveAndSync();
        }
    }
}
