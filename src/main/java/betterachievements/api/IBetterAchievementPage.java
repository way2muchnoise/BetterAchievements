package betterachievements.api;


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
     * @param columnWidth the width of a column
     * @param rowHeight   the height of a row
     * @param z           the z level
     * @param scale       the current scale
     */
    void drawBackground(int columnWidth, int rowHeight, float z, float scale);

    /**
     * Set the zoom level on page load
     * The default minecraft value is 1.0F
     *
     * @return the startup scale
     */
    float setScaleOnLoad();

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
}
