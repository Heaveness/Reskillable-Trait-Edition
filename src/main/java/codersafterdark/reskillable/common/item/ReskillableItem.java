package codersafterdark.reskillable.common.item;

import codersafterdark.reskillable.client.render.IModelRegister;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ReskillableItem extends Item implements IModelRegister {
    public ReskillableItem(String name) {
        setCreativeTab(CreativeTabs.MISC);
        setRegistryName(name);
        setTranslationKey(name);
    }

    @SideOnly(Side.CLIENT)
    @Override
    public void registerModels() {
        ModelLoader.setCustomModelResourceLocation(this, 0, new ModelResourceLocation(getRegistryName(), "inventory"));
    }
}
