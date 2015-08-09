package betterachievements.api;

import net.minecraft.client.Minecraft;

/**
 * This will be used to render the background of a {@link net.minecraftforge.common.AchievementPage}
 */
public interface IAchievementPageRenderer
{
    /**
     * Drawing the background of the achievement page
     * @param mc current {@link Minecraft}
     * @param z the z level
     * @param scale the current scale
     * @param columnWidth the width of a column
     * @param rowHeight the height of a row
     */
    void drawBackground(Minecraft mc, float z, float scale, int columnWidth, int rowHeight);

    /**
     * Set the zoom level on page load
     * @return the startup scale
     */
    float setScaleOnLoad();

    /**
     * Highest possible zoom value
     * @return max zoom out level
     */
    float getMaxZoomOut();

    /**
     * Lowest possible zoom value
     * @return max zoom in level
     */
    float getMaxZoomIn();
}
