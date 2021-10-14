package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerTalentInfo;
import codersafterdark.reskillable.api.event.LockTalentEvent;
import codersafterdark.reskillable.api.event.UnlockTalentEvent;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.IAttributeInstance;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentWardenNovice extends Talent {
    IAttribute damageResist = ReskillableAttributes.DAMAGE_RESIST;

    public TalentWardenNovice() {
        super(new ResourceLocation(MOD_ID, "warden_novice"), 1, 4, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "profession|reskillable:warrior|6");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onUnlock(UnlockTalentEvent.Post event) {
        if (event.getTalent() instanceof TalentWardenNovice) {
            EntityPlayer player = event.getEntityPlayer();
            if (!player.world.isRemote) {
                PlayerTalentInfo info = PlayerDataHandler.get(player).getTalentInfo(this);
                IAttributeInstance AttributeResist = player.getEntityAttribute(damageResist);
                AttributeModifier modifier = new AttributeModifier("reskillable.damageResistance", 5.0D, 0);
                info.addAttributeModifier(AttributeResist, modifier);
                PlayerDataHandler.get(player).saveAndSync();
            }
        }
    }

    @SubscribeEvent
    public void onLock(LockTalentEvent.Post event) {
        if (event.getTalent() instanceof TalentWardenNovice) {
            EntityPlayer player = event.getEntityPlayer();
            IAttributeInstance AttributeCrit = player.getEntityAttribute(damageResist);
            PlayerTalentInfo info = PlayerDataHandler.get(player).getTalentInfo(this);
            info.removeTalentAttribute(AttributeCrit);
            PlayerDataHandler.get(player).saveAndSync();
        }
    }
}
