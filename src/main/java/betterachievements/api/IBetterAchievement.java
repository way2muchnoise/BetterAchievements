package betterachievements.api;


/**
 * Implemented on {@link net.minecraft.stats.Achievement}
 * Used for more in depth control
 */
public interface IBetterAchievement
{
    /**
     * Has custom background colour for the {@link net.minecraft.stats.Achievement}
     *
     * @return
     */
    boolean recolourBackground();

    /**
     * Recolour the background of the {@link net.minecraft.stats.Achievement} icon in the {@link betterachievements.gui.GuiBetterAchievements}
     *
     * @param greyScale the current gray scale
     *
     * @return the int colour you want the background to be
     */
    int recolourBackground(float greyScale);

    /**
     * Is there any special Icon rendering
     *
     * @return if true {@link #renderIcon(int, int)} will be executed, if false the regular item rendering will be done
     */
    boolean hasSpecialIconRenderer();

    /**
     * Any special render code executed if {@link #hasSpecialIconRenderer()} is true
     *
     * @param top  top of the achievement icon
     * @param left left of the achievement icon
     */
    void renderIcon(int top, int left);
}
