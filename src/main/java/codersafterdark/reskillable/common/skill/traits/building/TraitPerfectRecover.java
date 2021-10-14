package codersafterdark.reskillable.common.skill.traits.building;

import codersafterdark.reskillable.api.unlockable.Trait;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import static codersafterdark.reskillable.common.lib.LibMisc.MOD_ID;

public class TraitPerfectRecover extends Trait {
    private Item glowstone;
    private Item lantern;

    public TraitPerfectRecover() {
        super(new ResourceLocation(MOD_ID, "perfect_recover"), 1, 1, new ResourceLocation(MOD_ID, "building"),
                4, "reskillable:building|8", "reskillable:gathering|4", "reskillable:mining|6");
    }

    @Override
    public void onBlockDrops(HarvestDropsEvent event) {
        if (event.getState().getBlock() == Blocks.GLOWSTONE) {
            if (!event.getDrops().stream().map(stack -> stack.getItem() == this.getGlowstone()).reduce(false, (a, b) -> a || b)) {
                event.getDrops().removeIf(s -> s.getItem() == Items.GLOWSTONE_DUST);
                event.getDrops().add(new ItemStack(Items.GLOWSTONE_DUST, 4));
            }
        } else if (event.getState().getBlock() == Blocks.SEA_LANTERN) {
            if (!event.getDrops().stream().map(stack -> stack.getItem() == this.getSeaLantern()).reduce(false, (a, b) -> a || b)) {
                event.getDrops().removeIf(s -> s.getItem() == Items.PRISMARINE_CRYSTALS);
                event.getDrops().add(new ItemStack(Items.PRISMARINE_CRYSTALS, 5));
                event.getDrops().add(new ItemStack(Items.PRISMARINE_SHARD, 4));
            }
        }
    }

    private Item getGlowstone() {
        if (glowstone == null) {
            glowstone = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:glowstone"));
        }
        return glowstone;
    }

    private Item getSeaLantern() {
        if (lantern == null) {
            lantern = ForgeRegistries.ITEMS.getValue(new ResourceLocation("minecraft:sea_lantern"));
        }
        return lantern;
    }
}