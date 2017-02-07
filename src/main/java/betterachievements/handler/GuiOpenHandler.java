package betterachievements.handler;

import betterachievements.gui.GuiAchievementsOld;
import betterachievements.gui.GuiBetterAchievements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.ReflectionHelper;

import java.lang.reflect.Field;

public class GuiOpenHandler {
    private static Field prevScreen, currentPage;

    static {
        try {
            prevScreen = ReflectionHelper.findField(GuiAchievements.class, "parentScreen", "field_146562_a");
            prevScreen.setAccessible(true);
            currentPage = ReflectionHelper.findField(GuiAchievements.class, "currentPage");
            currentPage.setAccessible(true);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event) {
        // Do nothing if I want to open the old GUI
        if (event.getGui() instanceof GuiAchievementsOld)
            return;

        if (event.getGui() instanceof GuiAchievements) {
            event.setCanceled(true);
            try {
                Minecraft.getMinecraft().displayGuiScreen(new GuiBetterAchievements((GuiScreen) prevScreen.get(event.getGui()), (Integer) currentPage.get(event.getGui()) + 1));
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
