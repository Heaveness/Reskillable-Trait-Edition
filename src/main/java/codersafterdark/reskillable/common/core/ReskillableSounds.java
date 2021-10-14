package codersafterdark.reskillable.common.core;

import codersafterdark.reskillable.common.lib.LibMisc;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ReskillableSounds {

    public static SoundEvent respec;

    public static void preInit() {
        respec = register("use_scroll_respec");

        MinecraftForge.EVENT_BUS.register(ReskillableSounds.class);
    }

    public static SoundEvent register(String name) {
        ResourceLocation loc = new ResourceLocation(LibMisc.MOD_ID, name);

        return new SoundEvent(loc).setRegistryName(loc);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(respec);
    }

    public static SoundEvent getSound(String sound, SoundEvent fallback) {
        SoundEvent attempt = SoundEvent.REGISTRY.getObject(new ResourceLocation(sound));
        return attempt == null ? fallback : attempt;
    }
}
