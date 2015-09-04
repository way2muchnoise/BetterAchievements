package betterachievements.handler;

import betterachievements.gui.GuiAchievementsOld;
import betterachievements.gui.GuiBetterAchievements;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraftforge.client.event.GuiOpenEvent;

import java.lang.reflect.Field;

public class GuiOpenHandler
{
    private static Field prevScreen, currentPage;

    static
    {
        try
        {
            prevScreen = GuiAchievements.class.getDeclaredField("field_146562_a");
            prevScreen.setAccessible(true);
            currentPage = GuiAchievements.class.getDeclaredField("currentPage");
            currentPage.setAccessible(true);
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        // Do nothing if I want to open the old GUI
        if (event.gui instanceof GuiAchievementsOld)
            return;

        if (event.gui instanceof GuiAchievements)
        {
            event.setCanceled(true);
            try
            {
                Minecraft.getMinecraft().displayGuiScreen(new GuiBetterAchievements((GuiScreen)prevScreen.get(event.gui), (Integer)currentPage.get(event.gui) + 1));
            } catch (IllegalAccessException e)
            {
                throw new RuntimeException(e);
            }
        }
    }
}
