package codersafterdark.reskillable.common.item;

import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.common.core.ReskillableSounds;
import dynamicswordskills.api.SkillRegistry;
import dynamicswordskills.entity.DSSPlayerInfo;
import dynamicswordskills.skills.SkillBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.*;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Loader;

import javax.annotation.Nonnull;
import java.util.Collection;
import java.util.Iterator;

public class ItemScrollRespec extends ReskillableItem {

    public ItemScrollRespec() {
        super("scroll_respec");
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> onItemRightClick(World world, EntityPlayer player, @Nonnull EnumHand hand) {
        ItemStack stack = player.getHeldItem(hand);
        PlayerData data = PlayerDataHandler.get(player);
        Collection<PlayerSkillInfo> allSkills = data.getAllSkillInfo();
        StringBuilder failedSkills = new StringBuilder();
        Iterator<PlayerSkillInfo> var8 = allSkills.iterator();

        if (world.isRemote) {
            while (var8.hasNext()) {
                PlayerSkillInfo skillInfo = (PlayerSkillInfo)var8.next();
                int oldLevel = skillInfo.getLevel();
                if (!MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Pre(player, skillInfo.skill, 1, oldLevel))) {
                    skillInfo.setLevel(1);
                    skillInfo.respec();
                    MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, skillInfo.skill, 1, oldLevel));
                    //ToastHelper.sendSkillToast(player, skillInfo.skill, 1);
                } else {
                    failedSkills.append(skillInfo.skill.getName()).append(", ");
                }
            }
            data.saveAndSync();

            if (Loader.isModLoaded("dynamicswordskills")) {
                DSSPlayerInfo playerSkills = DSSPlayerInfo.get(player);
                for (SkillBase skill : SkillRegistry.getValues()) {
                    playerSkills.removeSkill(skill.getRegistryName().toString());
                }
            }

            if (failedSkills.length() == 0) {
                player.sendMessage(new TextComponentTranslation("reskillable.misc.scroll.message.success"));
            } else {
                player.sendMessage(new TextComponentTranslation("reskillable.misc.scroll.message.fail"));
            }
        }

        SoundEvent chest = SoundEvent.REGISTRY.getObject(new ResourceLocation("block.chest.open"));
        SoundEvent sfx = ReskillableSounds.getSound("reskillable:use_scroll_respec", chest);
        world.playSound(null, player.posX, player.posY, player.posZ, sfx, SoundCategory.PLAYERS, 1F, (float) (0.7 + Math.random() * 0.4));

        stack.shrink(1);
        return ActionResult.newResult(EnumActionResult.SUCCESS, stack);
    }

}
