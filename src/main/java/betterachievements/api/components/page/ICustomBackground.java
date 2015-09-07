package betterachievements.api.components.page;

public interface ICustomBackground
{
    /**
     * Drawing the background of the achievement page
     *
     * @param left      the x coord of the left side of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param top       the y coord of the top side of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param width     the width of the {@link net.minecraft.client.gui.achievement.GuiAchievement}
     * @param height    the height of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param offsetX   the border width on the x axis
     * @param offsetY   the border width on the y axis
     * @param z         the z level
     * @param scale     the current scale
     */
    void drawBackground(int left, int top, int width, int height, int offsetX, int offsetY, float z, float scale);
}
