package betterachievementsdemo.registry;

import betterachievements.api.util.ColourHelper;
import betterachievementsdemo.achivement.BetterAchievementsDemoAchievement;
import betterachievementsdemo.achivement.BetterAchievementsDemoPage;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementRegistry
{
    private static Achievement blue =
            new BetterAchievementsDemoAchievement("demo.blue", "Blue", 0, 0, Blocks.wool, null)
            {
                @Override
                public int recolourBackground(float greyScale)
                {
                    return ColourHelper.tone(ColourHelper.RGB(0, 0, 255), greyScale);
                }
            };
    private static Achievement red =
            new BetterAchievementsDemoAchievement("demo.red", "Red", 2, -1, Blocks.wool, blue)
            {
                @Override
                public int recolourBackground(float greyScale)
                {
                    return ColourHelper.tone(ColourHelper.RGB(255, 0, 0), greyScale);
                }
            };
    private static AchievementPage page1 =
            new BetterAchievementsDemoPage("demoPage1", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page2 =
            new BetterAchievementsDemoPage("demoPage2", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page3 =
            new BetterAchievementsDemoPage("demoPage3", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page4 =
            new BetterAchievementsDemoPage("demoPage4", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };

    private static AchievementPage page5 =
            new BetterAchievementsDemoPage("demoPage5", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };

    public static void registerAchievements()
    {
        blue.initIndependentStat().registerStat();
        red.registerStat();

        AchievementPage.registerAchievementPage(page1);
        AchievementPage.registerAchievementPage(page2);
        AchievementPage.registerAchievementPage(page3);
        AchievementPage.registerAchievementPage(page4);
        AchievementPage.registerAchievementPage(page5);
    }
}
