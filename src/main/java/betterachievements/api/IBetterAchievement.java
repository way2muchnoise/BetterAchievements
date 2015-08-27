package betterachievements.api;


/**
 * Implemented on {@link net.minecraft.stats.Achievement}
 * Used for more in depth control
 */
public interface IBetterAchievement
{
    /**
     * Recolour the background of the {@link net.minecraft.stats.Achievement} icon in the {@link net.minecraft.client.gui.achievement.GuiAchievement}
     *
     * @param greyScale the current gray scale
     *
     * @return the int colour you want the background to be
     */
    int recolourBackground(float greyScale);

    /**
     * Is there any special Icon rendering
     *
     * @return if true renderIcon will be executed, if false the regular item rendering will be done
     */
    boolean hasSpecialIconRenderer();

    /**
     * Any special render code
     *
     * @param top  top of the achievement icon
     * @param left left of the achievement icon
     */
    void renderIcon(int top, int left);
}
