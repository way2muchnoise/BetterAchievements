package betterachievements.api.components.achievement;

public interface ICustomIconRenderer
{
    /**
     * Custom icon rendering of the {@link net.minecraft.stats.Achievement} on the {@link betterachievements.gui.GuiBetterAchievements}
     *
     * @param top  top of the achievement icon
     * @param left left of the achievement icon
     */
    void renderIcon(int top, int left);
}
