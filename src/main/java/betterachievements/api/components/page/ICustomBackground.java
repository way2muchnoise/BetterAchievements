package betterachievements.api.components.page;

public interface ICustomBackground
{
    /**
     * Drawing the background of the achievement page
     * This is called after translating the origin to the top left corner of the background area
     * NOTE: You need to do your own scaling
     *
     * @param left      the x coord of the left side of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param top       the y coord of the top side of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param width     the width of the {@link net.minecraft.client.gui.achievement.GuiAchievement}
     * @param height    the height of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param z         the z level
     * @param scale     the current scale
     */
    void drawBackground(int left, int top, int width, int height, float z, float scale);
}
