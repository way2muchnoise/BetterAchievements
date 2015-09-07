package betterachievements.api.util;

import cpw.mods.fml.common.event.FMLInterModComms;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.AchievementPage;

/**
 * Used to send an IMC message containing the {@link ItemStack} to render on the tab
 * in {@link betterachievements.gui.GuiBetterAchievements} of the given {@link AchievementPage}
 * Can be used as replacement for {@link betterachievements.api.components.page.ICustomIcon}
 */
public class IMCHelper
{
    private static final String MOD_ID = "BetterAchievements";

    public static void sendIconForPage(String pageName, ItemStack itemStack)
    {
        FMLInterModComms.sendMessage(MOD_ID, pageName, itemStack);
    }

    public static void sendIconForPage(AchievementPage page, ItemStack itemStack)
    {
        sendIconForPage(page.getName(), itemStack);
    }

    public static void sendIconForPage(String pageName, Item item)
    {
        sendIconForPage(pageName, new ItemStack(item));
    }

    public static void sendIconForPage(AchievementPage page, Item item)
    {
        sendIconForPage(page.getName(), new ItemStack(item));
    }

    public static void sendIconForPage(String pageName, Block block)
    {
        sendIconForPage(pageName, new ItemStack(block));
    }

    public static void sendIconForPage(AchievementPage page, Block block)
    {
        sendIconForPage(page.getName(), new ItemStack(block));
    }
}
