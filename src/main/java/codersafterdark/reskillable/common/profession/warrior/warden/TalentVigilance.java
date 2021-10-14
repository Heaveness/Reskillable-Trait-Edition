package codersafterdark.reskillable.common.profession.warrior.warden;

import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentVigilance extends Talent {

    public TalentVigilance() {
        super(new ResourceLocation(MOD_ID, "vigilance"), 0, 2, new ResourceLocation(MOD_ID, "warrior"), new ResourceLocation(MOD_ID, "warden"),
                3, "profession|reskillable:warrior|19");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void playerTickHandler(TickEvent.PlayerTickEvent event) {
        EntityPlayer player = event.player;
        if (!PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {return;}
        if (event.phase != TickEvent.Phase.START) {return;}
        if (player.getHealth() >= player.getMaxHealth()) {return;}

        // block healing for 30 seconds after the player attacks;
        int seconds = 20;
        int nextHealingTime = player.getLastAttackedEntityTime() + 20*seconds;
        int sessionGameTime = player.ticksExisted;
        if (sessionGameTime < nextHealingTime) {return;}

        if (player.ticksExisted % 100 == 0) {
            //if (!player.world.isRemote) {
                //player.sendMessage(new TextComponentString("Enough time has passed."));
            //}
            player.heal(2.0F);
        }
    }
}

