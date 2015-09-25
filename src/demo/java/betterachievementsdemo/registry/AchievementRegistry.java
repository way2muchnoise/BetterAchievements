package betterachievementsdemo.registry;

import betterachievements.api.util.ColourHelper;
import betterachievementsdemo.betterachivements.DemoAchievement;
import betterachievementsdemo.betterachivements.DemoPage;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.AchievementPage;

public class AchievementRegistry
{
    private static Achievement blue =
            new DemoAchievement("demo.blue", "Blue", 0, 0, Blocks.wool, null)
            {
                @Override
                public int recolourBackground(float greyScale)
                {
                    return ColourHelper.blendWithGreyScale(ColourHelper.RGB(0, 0, 255), greyScale);
                }
            };
    private static Achievement red =
            new DemoAchievement("demo.red", "Red", 2, -1, Blocks.wool, blue)
            {
                @Override
                public int recolourBackground(float greyScale)
                {
                    return ColourHelper.blendWithGreyScale(ColourHelper.RGB(255, 0, 0), greyScale);
                }
            };
    private static Achievement rainbow =
            new DemoAchievement("demo.rainbow", "Rainbow", 2, 1, Items.nether_star, red)
            {
                @Override
                public int recolourBackground(float greyScale)
                {
                    return ColourHelper.blendWithGreyScale(
                            ColourHelper.getRainbowColour(0.3F, 0.3F, 0.3F, 0, 2, 4, 128, 127, 50),
                            greyScale);
                }
            };
    private static AchievementPage page1 =
            new DemoPage("demoPage1", blue, red, rainbow)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page2 =
            new DemoPage("demoPage2", blue, red, rainbow)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page3 =
            new DemoPage("demoPage3", blue, red, rainbow)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };
    private static AchievementPage page4 =
            new DemoPage("demoPage4", blue, red, rainbow)
            {
                @Override
                public ItemStack getPageIcon()
                {
                    return new ItemStack(Blocks.diamond_block);
                }
            };

    private static AchievementPage page5 =
            new AchievementPage("demoPage5", blue, red, rainbow);

    public static void registerAchievements()
    {
        blue.initIndependentStat().registerStat();
        red.registerStat();
        rainbow.setSpecial().registerStat();

        AchievementPage.registerAchievementPage(page1);
        AchievementPage.registerAchievementPage(page2);
        AchievementPage.registerAchievementPage(page3);
        AchievementPage.registerAchievementPage(page4);
        AchievementPage.registerAchievementPage(page5);
    }
}
