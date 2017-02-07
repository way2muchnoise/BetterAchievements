package betterachievements.api.components.achievement;

public interface ICustomBackgroundColour {
    /**
     * Recolour the background of the {@link net.minecraft.stats.Achievement} icon in the {@link betterachievements.gui.GuiBetterAchievements}
     *
     * @param greyScale the current gray scale
     * @return the int colour you want the background to be
     */
    int recolourBackground(float greyScale);
}
