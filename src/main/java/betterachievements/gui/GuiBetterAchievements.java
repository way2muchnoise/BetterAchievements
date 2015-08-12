package betterachievements.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.stats.StatFileWriter;

public class GuiBetterAchievements extends GuiAchievements
{
    public GuiBetterAchievements(GuiScreen currentScreeen, StatFileWriter statFileWriter)
    {
        super(currentScreeen, statFileWriter);
    }
}
