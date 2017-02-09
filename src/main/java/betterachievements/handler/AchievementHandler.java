package betterachievements.handler;

import betterachievements.util.LogHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.stats.Achievement;
import net.minecraft.stats.AchievementList;
import net.minecraft.stats.StatisticsManagerServer;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraft.world.WorldServerMulti;
import net.minecraftforge.event.entity.player.AchievementEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.util.*;

public class AchievementHandler {
    private final Map<UUID, Set<Achievement>> playerAchievementMap;
    private final Set<UUID> currentItrs;
    private static final String TO_UNLOCK = "toUnlockAchievements.dat";
    private static final String ALL = "allAchievements.dat";
    private static AchievementHandler instance;

    public static AchievementHandler getInstance() {
        if (instance == null)
            instance = new AchievementHandler();
        return instance;
    }

    private AchievementHandler() {
        this.playerAchievementMap = new HashMap<>();
        this.currentItrs = new HashSet<>();
    }

    @SubscribeEvent
    public void onAchievementUnlocked(AchievementEvent event) {
        if (event.getEntityPlayer() instanceof EntityPlayerMP) {
            StatisticsManagerServer stats = ((EntityPlayerMP) event.getEntityPlayer()).getStatFile();
            if (!stats.canUnlockAchievement(event.getAchievement()))
                addAchievementToMap(event.getEntityPlayer().getUniqueID(), event.getAchievement());
            if (!this.currentItrs.contains(event.getEntityPlayer().getUniqueID()))
                tryUnlock((EntityPlayerMP) event.getEntityPlayer());
        }
    }

    public void addAchievementToMap(UUID uuid, Achievement achievement) {
        Set<Achievement> achievements = this.playerAchievementMap.get(uuid);
        if (achievements == null) achievements = new HashSet<>();
        achievements.add(achievement);
        this.playerAchievementMap.put(uuid, achievements);
    }

    public void tryUnlock(EntityPlayerMP player) {
        this.currentItrs.add(player.getUniqueID());
        Set<Achievement> achievements = this.playerAchievementMap.get(player.getUniqueID());
        boolean doItr = achievements != null;
        while (doItr) {
            doItr = false;
            Iterator<Achievement> itr = achievements.iterator();
            while (itr.hasNext()) {
                Achievement current = itr.next();
                if (player.getStatFile().canUnlockAchievement(current)) {
                    player.addStat(current);
                    itr.remove();
                    doItr = true;
                }
            }
        }
        this.currentItrs.remove(player.getUniqueID());
    }

    public void saveAchievements(World world) {
        dumpAchievementData(new File(world.getSaveHandler().getWorldDirectory(), ALL), getAchievements(world));
    }

    public void dumpAchievementData(File worldFolder) {
        dumpAchievementData(new File(worldFolder, TO_UNLOCK), this.playerAchievementMap);
    }

    public void dumpAchievementData(File file, Map<UUID, Set<Achievement>> playerAchievementMap) {
        List<String> lines = new ArrayList<>();
        for (Map.Entry<UUID, Set<Achievement>> entry : playerAchievementMap.entrySet()) {
            StringBuilder sb = new StringBuilder();
            sb.append(entry.getKey().toString()).append("->");
            for (Achievement achievement : entry.getValue())
                sb.append(achievement.statId).append(",");
            lines.add(sb.toString());
        }
        try {
            Files.write(file.toPath(), lines, Charset.defaultCharset());
        } catch (IOException e) {
            LogHelper.instance().error(e, "couldn't write " + TO_UNLOCK);
        }
    }

    public void compat(String worldName, File worldFolder) {
        File file = new File(ConfigHandler.getConfigDir(), worldName.replaceAll("[^a-zA-Z0-9.-]", "_") + " " + TO_UNLOCK);
        if (file.exists()) {
            try {
                File newLocation = new File(worldFolder, TO_UNLOCK);
                Files.move(file.toPath(), newLocation.toPath());
            } catch (IOException e) {
                LogHelper.instance().error(e, "couldn't copy old toUnlockAchievements");
            }
        }
    }

    public void loadAchievements(WorldServer world) {
        constructFromData(new File(world.getSaveHandler().getWorldDirectory(), ALL), this.playerAchievementMap);
        for (EntityPlayer player : world.playerEntities) {
            if (player instanceof EntityPlayerMP) {
                tryUnlock((EntityPlayerMP) player);
            }
        }
    }

    public void constructFromData(File worldFolder) {
        playerAchievementMap.clear();
        constructFromData(new File(worldFolder, TO_UNLOCK), this.playerAchievementMap);
    }

    private void constructFromData(File file, Map<UUID, Set<Achievement>> playerAchievementMap) {
        try {
            if (!file.exists()) return;

            Map<String, Achievement> achievementMap = new HashMap<>();
            for (Achievement achievement : AchievementList.ACHIEVEMENTS)
                achievementMap.put(achievement.statId, achievement);

            List<String> lines = Files.readAllLines(file.toPath(), Charset.defaultCharset());
            for (String line : lines) {
                String[] splitted = line.split("->");
                if (splitted.length != 2) continue;
                UUID uuid;
                try {
                    uuid = UUID.fromString(splitted[0]);
                } catch (IllegalArgumentException e) {
                    LogHelper.instance().error(e, "bad uuid \"" + splitted[0] + "\" in " + TO_UNLOCK);
                    continue;
                }
                Set<Achievement> achievementSet = new HashSet<>();
                for (String sAchievement : splitted[1].split(",")) {
                    Achievement achievement = achievementMap.get(sAchievement);
                    if (achievement == null) continue;
                    achievementSet.add(achievement);
                }
                playerAchievementMap.put(uuid, achievementSet);
            }
        } catch (IOException e) {
            LogHelper.instance().error(e, "couldn't read " + TO_UNLOCK);
        }
    }

    private Map<UUID, Set<Achievement>> getAchievements(World world) {
        Map<UUID, Set<Achievement>> map = new HashMap<>();
        for (EntityPlayer player : world.playerEntities) {
            Set<Achievement> achievements = new HashSet<>();
            for (Achievement achievement : AchievementList.ACHIEVEMENTS) {
                if (player.hasAchievement(achievement)) {
                    achievements.add(achievement);
                }
            }
            map.put(player.getUniqueID(), achievements);
        }
        return map;
    }
}
