package betterachievements.api.components.page;

import net.minecraft.stats.Achievement;

public interface ICustomPosition
{
    /**
     * Set position on load located at given {@link Achievement}
     *
     * @return an {@link Achievement} to set position if null previous position will be kept
     */
    Achievement setPositionOnLoad();
}
