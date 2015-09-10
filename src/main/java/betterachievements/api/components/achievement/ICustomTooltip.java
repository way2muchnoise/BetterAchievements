package betterachievements.api.components.achievement;

import net.minecraft.stats.StatFileWriter;

public interface ICustomTooltip
{
    /**
     * Render your own tooltip
     *
     * @param mouseX mouse X position
     * @param mouseY mouse Y position
     * @param statFileWriter the {@link StatFileWriter} this can be used to gather info related to the {@link net.minecraft.stats.Achievement}
     */
    void renderTooltip(int mouseX, int mouseY, StatFileWriter statFileWriter);
}
