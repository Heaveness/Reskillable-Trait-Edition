package codersafterdark.reskillable.common.core;

import codersafterdark.reskillable.common.advancement.ReskillableAdvancements;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import codersafterdark.reskillable.api.unlockable.AutoUnlocker;
import codersafterdark.reskillable.common.commands.ReskillableCmd;
import codersafterdark.reskillable.base.ConfigHandler;
import codersafterdark.reskillable.base.LevelLockHandler;
import codersafterdark.reskillable.common.loot.LootConditionRequirement;
import codersafterdark.reskillable.common.network.PacketHandler;
import codersafterdark.reskillable.common.potion.ReskillablePotion;
import codersafterdark.reskillable.common.skill.attributes.AttributeCritChance;
import codersafterdark.reskillable.common.skill.attributes.AttributeCritDamage;
import codersafterdark.reskillable.common.skill.attributes.AttributeDamageResist;
import codersafterdark.reskillable.common.skill.attributes.ReskillableAttributes;
import codersafterdark.reskillable.common.util.DamageSourceUtil;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.DamageSource;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.storage.loot.conditions.LootConditionManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;

public class CommonProxy {

    public static DamageSource dmgSourceBleed = DamageSourceUtil.newType("reskillable.bleed").setDamageBypassesArmor();

    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(PlayerDataHandler.EventHandler.class);
        MinecraftForge.EVENT_BUS.register(LevelLockHandler.class);
        MinecraftForge.EVENT_BUS.register(RequirementCache.class);
        ReskillableSounds.preInit();
        ConfigHandler.init(event.getSuggestedConfigurationFile());
        PacketHandler.preInit();
        LootConditionManager.registerCondition(new LootConditionRequirement.Serializer());
        ReskillableAdvancements.preInit();
        new ReskillableAttributes().register();
        MinecraftForge.EVENT_BUS.register(new AttributeCritChance());
        MinecraftForge.EVENT_BUS.register(new AttributeCritDamage());
        MinecraftForge.EVENT_BUS.register(new AttributeDamageResist());
        ReskillablePotion.registerPotions();
    }

    public void init(FMLInitializationEvent event) {
        if (ConfigHandler.config.hasChanged()) {
            ConfigHandler.config.save();
        }
    }

    public void postInit(FMLPostInitializationEvent event) {
        LevelLockHandler.setupLocks();
        RequirementCache.registerDirtyTypes();
    }

    public void serverStarting(FMLServerStartingEvent event) {
        event.registerServerCommand(new ReskillableCmd());
        AutoUnlocker.setUnlockables();
        MinecraftForge.EVENT_BUS.register(AutoUnlocker.class);
    }

    public AdvancementProgress getPlayerAdvancementProgress(EntityPlayer entityPlayer, Advancement advancement) {
        return ((EntityPlayerMP) entityPlayer).getAdvancements().getProgress(advancement);
    }

    public EntityPlayer getClientPlayer() {
        return null;
    }

    public String getLocalizedString(String string) {
        return string;
    }
}