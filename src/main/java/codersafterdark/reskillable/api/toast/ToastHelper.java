package codersafterdark.reskillable.api.toast;

import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import codersafterdark.reskillable.common.network.*;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ToastHelper {
    //This technically could work for any IToast but as a helper method might as well make sure it is an AbstractToast
    //in case we decide to modify this method to do more things
    @SideOnly(Side.CLIENT)
    public static void sendToast(AbstractToast toast) {
        Minecraft.getMinecraft().getToastGui().add(toast);
    }

    @SideOnly(Side.CLIENT)
    public static void sendUnlockableToast(Unlockable u) {
        if (u != null) {
            sendToast(new UnlockableToast(u));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void sendProfessionToast(Profession profession, int level) {
        if (profession != null) {
            sendToast(new ProfessionToast(profession, level));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void sendTalentToast(Talent t) {
        if (t != null) {
            sendToast(new TalentToast(t));
        }
    }

    @SideOnly(Side.CLIENT)
    public static void sendSkillToast(Skill skill, int level) {
        if (skill != null) {
            sendToast(new SkillToast(skill, level));
        }
    }

    public static void sendUnlockableToast(EntityPlayerMP player, Unlockable u) {
        if (u != null) {
            PacketHandler.INSTANCE.sendTo(new UnlockableToastPacket(u.getRegistryName()), player);
        }
    }

    public static void sendTalentToast(EntityPlayerMP player, Talent t) {
        if (t != null) {
            PacketHandler.INSTANCE.sendTo(new TalentToastPacket(t.getRegistryName()), player);
        }
    }

    public static void sendSkillToast(EntityPlayerMP player, Skill skill, int level) {
        if (skill != null) {
            PacketHandler.INSTANCE.sendTo(new SkillToastPacket(skill.getRegistryName(), level), player);
        }
    }

    public static void sendProfessionToast(EntityPlayerMP player, Profession profession, int level) {
        if (profession != null) {
            PacketHandler.INSTANCE.sendTo(new ProfessionToastPacket(profession.getRegistryName(), level), player);
        }
    }
}