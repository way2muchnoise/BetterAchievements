package betterachievements.api.components.page;

public interface ICustomBackground
{
    /**
     * Drawing the background of the achievement page
     *
     * @param left the x coord of the left side of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param top   the y coord of the top side of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param z           the z level
     * @param scale       the current scale
     */
    void drawBackground(int left, int top, float z, float scale);
}
