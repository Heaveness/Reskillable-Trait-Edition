package codersafterdark.reskillable.common.profession.gatherer.seafarer;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.talent.Talent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityMountEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TalentSailor extends Talent {

    public TalentSailor() {
        super(new ResourceLocation(MOD_ID, "sailing_sailor"), 1, 2, new ResourceLocation(MOD_ID, "gatherer"), new ResourceLocation(MOD_ID, "seafarer"),
                3, "profession|reskillable:gatherer|19");
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onRide(EntityMountEvent event) {
        if (event.getEntityMounting() instanceof EntityPlayer && event.getEntityBeingMounted() != null && event.getEntityBeingMounted() instanceof EntityBoat) {
            EntityPlayer player = (EntityPlayer) event.getEntityMounting();
            if (PlayerDataHandler.get(player).getProfessionInfo(getParentProfession()).isUnlocked(this)) {
                EntityBoat boat = (EntityBoat) event.getEntityBeingMounted();
                if (event.isMounting() && boat.isBeingRidden()) {
                }
            }
        }

    }

}
