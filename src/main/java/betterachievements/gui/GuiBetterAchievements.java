package betterachievements.gui;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.stats.StatFileWriter;

public class GuiBetterAchievements extends GuiAchievements
{
    public GuiBetterAchievements(GuiScreen currentScreen, StatFileWriter statFileWriter)
    {
        super(currentScreen, statFileWriter);
    }

    @Override
    public void drawWorldBackground(int p_146270_1_)
    {
        this.drawGradientRect(0, 0, this.width, this.height, -1072689136, -804253680);
    }
}
