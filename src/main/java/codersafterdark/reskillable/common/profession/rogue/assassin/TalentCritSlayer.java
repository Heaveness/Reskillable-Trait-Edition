package codersafterdark.reskillable.common.profession.rogue.assassin;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.common.potion.ReskillablePotion;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.CriticalHitEvent;
import net.minecraftforge.fml.common.eventhandler.Event;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentCritSlayer extends Talent {

    public TalentCritSlayer() {
        super(new ResourceLocation(MOD_ID, "crit_slayer"), 2, 4, new ResourceLocation(MOD_ID, "rogue"), new ResourceLocation(MOD_ID, "assassin"), 3, "profession|reskillable:rogue|13");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onCritical(CriticalHitEvent event) {
        EntityPlayer player = event.getEntityPlayer();
        if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this) &&
                !player.world.isRemote && event.getResult() == Event.Result.ALLOW) {
            player.addPotionEffect(new PotionEffect(ReskillablePotion.POTION_CRIT_SLAYER_EFFECT, 60, 0, true, false));
        }
    }
}
