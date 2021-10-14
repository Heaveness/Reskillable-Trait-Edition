package codersafterdark.reskillable.api.data;

import codersafterdark.reskillable.api.ReskillableAPI;
import codersafterdark.reskillable.api.ReskillableRegistries;
import codersafterdark.reskillable.api.event.LevelUpEvent;
import codersafterdark.reskillable.api.event.LockUnlockableEvent;
import codersafterdark.reskillable.api.event.UnlockUnlockableEvent;
import codersafterdark.reskillable.api.profession.Profession;
import codersafterdark.reskillable.api.requirement.Requirement;
import codersafterdark.reskillable.api.requirement.RequirementCache;
import codersafterdark.reskillable.api.skill.Skill;
import codersafterdark.reskillable.api.talent.Talent;
import codersafterdark.reskillable.api.talent.TalentActive;
import codersafterdark.reskillable.api.unlockable.Ability;
import codersafterdark.reskillable.api.unlockable.IAbilityEventHandler;
import codersafterdark.reskillable.api.unlockable.Unlockable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.entity.living.EnderTeleportEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.RightClickBlock;
import net.minecraftforge.event.world.BlockEvent.HarvestDropsEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;

import java.lang.ref.WeakReference;
import java.util.*;
import java.util.function.Consumer;

public class PlayerData {
    private static final String TAG_SKILLS_CMP = "SkillLevels";
    private static final String TAG_PROFESSIONS_CMP = "ProfessionLevels";
    private static final String TAG_TALENTS_CMP = "TalentRanks";
    private static final String TAG_UNLOCKABLES_CMP = "UnlockableRanks";
    private static final String TAG_UNLOCKED_PROFESSIONS = "Professions";
    private final boolean client;
    private final RequirementCache requirementCache;
    public WeakReference<EntityPlayer> playerWR;
    private Map<Profession, PlayerProfessionInfo> professionInfo = new HashMap<>();
    private Map<Skill, PlayerSkillInfo> skillInfo = new HashMap<>();
    private Map<Talent, PlayerTalentInfo> talentInfo = new HashMap<>();
    private Map<Unlockable, PlayerUnlockableInfo> unlockableInfo = new HashMap<>();
    private List<Profession> unlockedProfessions = new ArrayList<>();

    public PlayerData(EntityPlayer player) {
        playerWR = new WeakReference<>(player);
        client = player.getEntityWorld().isRemote;
        requirementCache = RequirementCache.getCache(player);

        ReskillableRegistries.PROFESSIONS.getValuesCollection().forEach(p -> professionInfo.put(p, new PlayerProfessionInfo(p)));
        ReskillableRegistries.SKILLS.getValuesCollection().forEach(s -> skillInfo.put(s, new PlayerSkillInfo(s)));
        ReskillableRegistries.TALENTS.getValuesCollection().forEach(t -> talentInfo.put(t, new PlayerTalentInfo(t)));
        ReskillableRegistries.UNLOCKABLES.getValuesCollection().forEach(u -> unlockableInfo.put(u, new PlayerUnlockableInfo(u)));

        load();
    }

    public void unlockProfession(Profession p) {unlockedProfessions.add(p);}

    public List<Profession> getUnlockedProfessions() {return this.unlockedProfessions;}

    public PlayerProfessionInfo getProfessionInfo(Profession p) {return professionInfo.get(p);}

    public PlayerSkillInfo getSkillInfo(Skill s) {
        return skillInfo.get(s);
    }

    public PlayerTalentInfo getTalentInfo(Talent t) {return talentInfo.get(t);}

    public PlayerUnlockableInfo getUnlockableInfo(Unlockable u) {return unlockableInfo.get(u);}

    public Collection<PlayerSkillInfo> getAllSkillInfo() {
        return skillInfo.values();
    }

    public Collection<PlayerProfessionInfo> getAllProfessionInfo() {return professionInfo.values();}

    public Collection<PlayerTalentInfo> getAllTalentInfo() {return talentInfo.values();}

    public Collection<PlayerUnlockableInfo> getAllUnlockableInfo() {return unlockableInfo.values();}

    public boolean hasAnyAbilities() {
        return !getAllAbilities().isEmpty();
    }

    public boolean hasActiveTalents() {
        return !getAllActiveTalents().isEmpty();
    }

    public boolean hasSecondaryProfession() {return !(getUnlockedProfessions().size() <= 1);}

    public Set<Ability> getAllAbilities() {
        Set<Ability> set = new TreeSet<>();
        skillInfo.values().forEach(info -> info.addAbilities(set));
        return set;
    }

    public Set<TalentActive> getAllActiveTalents() {
        Set<TalentActive> set = new TreeSet<>();
        professionInfo.values().forEach(info -> info.addActiveTalents(set));
        return set;
    }

    public boolean matchStats(RequirementHolder holder) {
        return playerWR.get() == null || holder.getRequirements().stream().allMatch(this::requirementAchieved);
    }

    //helper method to access the requirement cache
    public boolean requirementAchieved(Requirement requirement) {
        return getRequirementCache().requirementAchieved(requirement);
    }

    public final RequirementCache getRequirementCache() {
        return requirementCache;
    }

    public void load() {
        if (!client) {
            EntityPlayer player = playerWR.get();

            if (player != null) {
                NBTTagCompound cmp = PlayerDataHandler.getDataCompoundForPlayer(player);
                loadFromNBT(cmp);
            }
        }
    }

    public void save() {
        if (!client) {
            EntityPlayer player = playerWR.get();

            if (player != null) {
                NBTTagCompound cmp = PlayerDataHandler.getDataCompoundForPlayer(player);
                saveToNBT(cmp);
            }
        }
    }

    public void sync() {
        if (!client) {
            EntityPlayer player = playerWR.get();
            ReskillableAPI.getInstance().syncPlayerData(player, this);
        }
    }

    public void saveAndSync() {
        save();
        sync();
    }

    public void loadFromNBT(NBTTagCompound cmp) {
        NBTTagCompound skillsCmp = cmp.getCompoundTag(TAG_SKILLS_CMP);
        NBTTagCompound profsCmp = cmp.getCompoundTag(TAG_PROFESSIONS_CMP);
        NBTTagCompound talentsCmp = cmp.getCompoundTag(TAG_TALENTS_CMP);
        NBTTagCompound unlockablesCmp = cmp.getCompoundTag(TAG_UNLOCKABLES_CMP);
        NBTTagCompound unlockedProfessions = cmp.getCompoundTag(TAG_UNLOCKED_PROFESSIONS);
        for (PlayerSkillInfo info : skillInfo.values()) {
            String key = info.skill.getKey();
            if (skillsCmp.hasKey(key)) {
                NBTTagCompound infoCmp = skillsCmp.getCompoundTag(key);
                info.loadFromNBT(infoCmp);
            }
        }
        for (PlayerProfessionInfo info : professionInfo.values()) {
            String key = info.profession.getKey();
            if (profsCmp.hasKey(key)) {
                NBTTagCompound infoCmp = profsCmp.getCompoundTag(key);
                info.loadFromNBT(infoCmp);
            }
        }
        for (PlayerTalentInfo info : talentInfo.values()) {
            String key = info.talent.getKey();
            if (talentsCmp.hasKey(key)) {
                NBTTagCompound infoCmp = talentsCmp.getCompoundTag(key);
                info.loadFromNBT(infoCmp);
            }
        }
        for (PlayerUnlockableInfo info : unlockableInfo.values()) {
            String key = info.unlockable.getKey();
            if (unlockablesCmp.hasKey(key)) {
                NBTTagCompound infoCmp = unlockablesCmp.getCompoundTag(key);
                info.loadFromNBT(infoCmp);
            }
        }

        this.unlockedProfessions.clear();
        for (String s : unlockedProfessions.getKeySet()) {
            Optional.ofNullable(ReskillableRegistries.PROFESSIONS.getValue(new ResourceLocation(s.replace(".", ":"))))
                    .ifPresent(this.unlockedProfessions::add);
        }

    }

    public void saveToNBT(NBTTagCompound cmp) {
        NBTTagCompound skillsCmp = new NBTTagCompound();
        NBTTagCompound profsCmp = new NBTTagCompound();
        NBTTagCompound talentsCmp = new NBTTagCompound();
        NBTTagCompound unlockablesCmp = new NBTTagCompound();
        NBTTagCompound unlockedProfessions = new NBTTagCompound();

        for (PlayerSkillInfo info : skillInfo.values()) {
            String key = info.skill.getKey();
            NBTTagCompound infoCmp = new NBTTagCompound();
            info.saveToNBT(infoCmp);
            skillsCmp.setTag(key, infoCmp);
        }

        for (PlayerProfessionInfo info: professionInfo.values()) {
            String key = info.profession.getKey();
            NBTTagCompound infoCmp = new NBTTagCompound();
            info.saveToNBT(infoCmp);
            profsCmp.setTag(key, infoCmp);
        }

        for (PlayerTalentInfo info: talentInfo.values()) {
            String key = info.talent.getKey();
            NBTTagCompound infoCmp = new NBTTagCompound();
            info.saveToNBT(infoCmp);
            talentsCmp.setTag(key, infoCmp);
        }

        for (PlayerUnlockableInfo info: unlockableInfo.values()) {
            String key = info.unlockable.getKey();
            NBTTagCompound infoCmp = new NBTTagCompound();
            info.saveToNBT(infoCmp);
            unlockablesCmp.setTag(key, infoCmp);
        }

        for (Profession p : this.unlockedProfessions) {
            String key = p.getKey();
            unlockedProfessions.setBoolean(key, true);
        }

        cmp.setTag(TAG_PROFESSIONS_CMP, profsCmp);
        cmp.setTag(TAG_SKILLS_CMP, skillsCmp);
        cmp.setTag(TAG_TALENTS_CMP, talentsCmp);
        cmp.setTag(TAG_UNLOCKABLES_CMP, unlockablesCmp);
        cmp.setTag(TAG_UNLOCKED_PROFESSIONS, unlockedProfessions);
    }

    // Event Handlers

    public void tickPlayer(PlayerTickEvent event) {
        forEachEventHandler(h -> h.onPlayerTick(event));
    }

    public void blockDrops(HarvestDropsEvent event) {
        forEachEventHandler(h -> h.onBlockDrops(event));
    }

    public void mobDrops(LivingDropsEvent event) {
        forEachEventHandler(h -> h.onMobDrops(event));
    }

    public void breakSpeed(BreakSpeed event) {
        forEachEventHandler(h -> h.getBreakSpeed(event));
    }

    public void attackMob(LivingHurtEvent event) {
        forEachEventHandler(h -> h.onAttackMob(event));
    }

    public void hurt(LivingHurtEvent event) {
        forEachEventHandler(h -> h.onHurt(event));
    }

    public void rightClickBlock(RightClickBlock event) {
        forEachEventHandler(h -> h.onRightClickBlock(event));
    }

    public void enderTeleport(EnderTeleportEvent event) {
        forEachEventHandler(h -> h.onEnderTeleport(event));
    }

    public void killMob(LivingDeathEvent event) {
        forEachEventHandler(h -> h.onKillMob(event));
    }

    public void forEachEventHandler(Consumer<IAbilityEventHandler> consumer) {
        skillInfo.values().forEach(info -> info.forEachEventHandler(consumer));
        professionInfo.values().forEach(info -> info.forEachEventHandler(consumer));
        talentInfo.values().forEach(info -> info.forEachEventHandler(consumer));
        unlockableInfo.values().forEach(info -> info.forEachEventHandler(consumer));
    }
}