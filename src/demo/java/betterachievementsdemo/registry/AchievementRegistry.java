package betterachievementsdemo.registry;

import betterachievements.api.BetterAchievement;
import betterachievements.api.BetterAchievementPage;
import betterachievementsdemo.reference.Colours;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementRegistry
{
    private static Achievement blue =
            new BetterAchievement("demo.blue", "Blue", 0, 0, Blocks.wool, null)
            {
                @Override
                public boolean recolourBackground()
                {
                    return true;
                }

                @Override
                public int recolourBackground(float greyScale)
                {
                    return Colours.BLUE;
                }
            };
    private static Achievement red =
            new BetterAchievement("demo.red", "Red", 2, -1, Blocks.wool, blue)
            {
                @Override
                public boolean recolourBackground()
                {
                    return true;
                }

                @Override
                public int recolourBackground(float greyScale)
                {
                    return Colours.tone(Colours.RED, greyScale);
                }
            };
    private static AchievementPage page1 =
            new BetterAchievementPage("demoPage1", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page2 =
            new BetterAchievementPage("demoPage2", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page3 =
            new BetterAchievementPage("demoPage3", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page4 =
            new BetterAchievementPage("demoPage4", blue, red)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };

    private static AchievementPage page5 =
            new BetterAchievementPage("demoPage5", blue, red)
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
