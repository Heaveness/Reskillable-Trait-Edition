package codersafterdark.reskillable.common.commands;

import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.data.PlayerData;
import codersafterdark.reskillable.api.data.PlayerDataHandler;
import codersafterdark.reskillable.api.data.PlayerSkillInfo;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.toast.ToastHelper;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.MinecraftForge;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class CmdIncrementSkill extends CommandBase {
    @Nonnull
    @Override
    public String getName() {
        return "incrementskill";
    }

    @Nonnull
    @Override
    public String getUsage(@Nonnull ICommandSender sender) {
        return "reskillable.command.incrementskill.usage";
    }

    @Override
    public void execute(@Nonnull MinecraftServer server, @Nonnull ICommandSender sender, @Nonnull String[] args) throws CommandException {
        if (args.length == 0) {
            throw new CommandException("reskillable.command.invalid.missing.playerskill");
        }
        if (args.length == 1) {
            throw new CommandException("reskillable.command.invalid.missing.skill");
        }
        EntityPlayerMP player = getPlayer(server, sender, args[0]);
        args[1] = args[1].replaceAll(":", ".");
        String[] parts = args[1].split("\\.");
        ResourceLocation skillName = parts.length > 1 ? new ResourceLocation(parts[0], args[1].substring(parts[0].length() + 1)) : new ResourceLocation(args[1]);
        if (!ReskillableRegistries.SKILLS.containsKey(skillName)) {
            throw new CommandException("reskillable.command.invalid.skill", skillName);
        }
        int levels = 1;
        if (args.length > 2) {
            try {
                levels = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                throw new CommandException("reskillable.command.invalid.missing.level", args[2]);
            }
            if (levels < 1) {
                throw new CommandException("reskillable.command.invalid.belowmin", levels);
            }
        }
        Skill skill = ReskillableRegistries.SKILLS.getValue(skillName);
        PlayerData data = PlayerDataHandler.get(player);
        PlayerSkillInfo skillInfo = data.getSkillInfo(skill);
        int oldLevel = skillInfo.getLevel();
        int maxLevel = skill.getCap();
        if (oldLevel + levels > maxLevel) {
            levels = maxLevel - oldLevel;
            if (levels <= 0) {
                sender.sendMessage(new TextComponentTranslation("reskillable.command.fail.skillup", skillName, player.getDisplayName()));
                return;
            }
        }
        if (!MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Pre(player, skill, oldLevel + levels, oldLevel))) {
            skillInfo.setLevel(oldLevel + levels);
            data.saveAndSync();
            MinecraftForge.EVENT_BUS.post(new LevelUpEvent.Post(player, skill, skillInfo.getLevel(), oldLevel));
            sender.sendMessage(new TextComponentTranslation("reskillable.command.success.skillup", skillName, player.getDisplayName()));
            ToastHelper.sendSkillToast(player, skill, skillInfo.getLevel());
        } else {
            sender.sendMessage(new TextComponentTranslation("reskillable.command.fail.skillup", skillName, player.getDisplayName()));
        }
    }

    @Nonnull
    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos targetPos) {
        if (args.length == 0) {
            return Arrays.asList(server.getPlayerList().getOnlinePlayerNames());
        }
        if (args.length == 1) {
            String partialName = args[0];
            return Arrays.stream(server.getPlayerList().getOnlinePlayerNames()).filter(name -> name.startsWith(partialName)).collect(Collectors.toList());
        }
        if (args.length == 2) {
            String partial = args[1].replaceAll(":", ".");
            return ReskillableRegistries.SKILLS.getValuesCollection().stream().map(Skill::getKey).filter(skillName -> skillName.startsWith(partial)).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 2;
    }
}