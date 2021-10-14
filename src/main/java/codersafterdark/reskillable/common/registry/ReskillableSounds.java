package codersafterdark.reskillable.common.registry;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public final class ReskillableSounds {

    public static SoundCategory TALENTS;
    public static final SoundEvent TALENT_BLINK = createSound("talent.blink");


    private ReskillableSounds() {
    }

        public static SoundEvent createSound(String name) {
        return createSound("reskillable", name);
    }

    public static SoundEvent createSound(String modID, String name) {
        return (new SoundEvent(new ResourceLocation(modID, name))).setRegistryName(name);
    }

    @SubscribeEvent
    public static void register(RegistryEvent.Register<SoundEvent> event) {
        event.getRegistry().register(TALENT_BLINK);
    }

}
