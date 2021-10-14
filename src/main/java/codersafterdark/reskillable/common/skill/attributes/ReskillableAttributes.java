package codersafterdark.reskillable.common.skill.attributes;

import codersafterdark.reskillable.common.lib.LibMisc;
import net.minecraft.entity.ai.attributes.AbstractAttributeMap;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReskillableAttributes {

    public static IAttribute CRIT_CHANCE = new RangedAttribute(null,
            LibMisc.MOD_ID + ".critChance",
            5.0, 0, 100.0).setShouldWatch(true);

    public static IAttribute CRIT_DAMAGE = new RangedAttribute(null,
            LibMisc.MOD_ID + ".critDamage",
            125.0, 100.0, 200.0).setShouldWatch(true);

    public static IAttribute DAMAGE_RESIST = new RangedAttribute(null,
            LibMisc.MOD_ID + ".damageResistance",
            0.0, 0.0, 100.0).setShouldWatch(true);

    @SubscribeEvent
    public void onPlayerConstruction(EntityEvent.EntityConstructing event) {
        if (event.getEntity() instanceof EntityPlayer) {
            AbstractAttributeMap plAttributes = ((EntityPlayer) event.getEntity()).getAttributeMap();
            plAttributes.registerAttribute(CRIT_CHANCE).setBaseValue(5.0);
            plAttributes.registerAttribute(CRIT_DAMAGE).setBaseValue(125.0);
            plAttributes.registerAttribute(DAMAGE_RESIST).setBaseValue(0.0);
        }
    }

    public void register() {
        MinecraftForge.EVENT_BUS.register(this);
    }

}
