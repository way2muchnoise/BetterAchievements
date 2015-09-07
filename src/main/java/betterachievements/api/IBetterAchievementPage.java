package betterachievements.api;


import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;

/**
 * Implemented on {@link net.minecraftforge.common.AchievementPage}
 * Used for more in depth control
 */
public interface IBetterAchievementPage
{
    /**
     * Has custom draw background method
     * @return true is {@link #drawBackground(int, int, float, float)} is implemented
     */
    boolean hasCustomBackGround();

    /**
     * Drawing the background of the achievement page if {@link #hasCustomBackGround()} is true
     * Can be left unimplemented if {@link #hasCustomBackGround()} is false
     *
     * @param left the x coord of the left side of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param top   the y coord of the top side of the {@link betterachievements.gui.GuiBetterAchievements}
     * @param z           the z level
     * @param scale       the current scale
     */
    void drawBackground(int left, int top, float z, float scale);

    /**
     * Resets the current scale to a given one on page open
     *
     * @return true to set scale to the one provided by {@link #setScaleOnLoad()}
     */
    boolean setScaleOnLoad();

    /**
     * Set the zoom level on page load
     * The default minecraft value is 1.0F
     *
     * @return the startup scale
     */
    float setScale();

    /**
     * Set position on load located at given {@link Achievement}
     *
     * @return an {@link Achievement} to set position if null previous position will be kept
     */
    Achievement setPositionOnLoad();

    /**
     * Highest possible zoom value
     *
     * @return max zoom level
     */
    float getMaxZoom();

    /**
     * Lowest possible zoom value
     *
     * @return min zoom level
     */
    float getMinZoom();

    /**
     * Get the {@link ItemStack} to be displayed on the tab
     *
     * @return if null one will be picked for the page
     */
    ItemStack getPageIcon();

    /**
     * If true {@link #getColourForUnlockedArrow()}, {@link #getColourForCanUnlockArrow()} ()} and {@link #getColourForCantUnlockArrow()} ()}
     * will be called
     *
     * @return true if you have custom colours
     */
    boolean hasCustomArrowColours();

    /**
     * The integer value of the arrow for unlocked achievements
     * Use {@link betterachievements.api.util.ColourHelper#RGB(String)} to use #RRGGBB format to integer
     *
     * @return the integer value of the arrow
     */
    int getColourForUnlockedArrow();

    /**
     * The integer value of the arrow for achievements that can be unlocked
     * Use {@link betterachievements.api.util.ColourHelper#RGB(String)} to use #RRGGBB format to integer
     *
     * @return the integer value of the arrow
     */
    int getColourForCanUnlockArrow();

    /**
     * The integer value of the arrow for achievements that can't be unlocked yet
     * Use {@link betterachievements.api.util.ColourHelper#RGB(String)} to use #RRGGBB format to integer
     *
     * @return the integer value of the arrow
     */
    int getColourForCantUnlockArrow();
}
