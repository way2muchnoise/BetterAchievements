package betterachievements.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.stats.StatFileWriter;

public class GuiAchievementsOld extends GuiAchievements
{
    public GuiAchievementsOld(GuiScreen currentScreen, StatFileWriter statFileWriter)
    {
        super(currentScreen, statFileWriter);
    }
}
