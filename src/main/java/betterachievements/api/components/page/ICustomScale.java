package betterachievements.api.components.page;

public interface ICustomScale {
    /**
     * Resets the current scale to a given one on page open
     *
     * @return true to set scale to the one provided by {@link #resetScaleOnLoad()}
     */
    boolean resetScaleOnLoad();

    /**
     * Set the zoom level on page load will reset current scale if {@link #resetScaleOnLoad()} is true
     * Will always be loaded when page is loaded first
     * The default minecraft value is 1.0F
     *
     * @return the startup scale
     */
    float setScale();

    /**
     * Highest possible scale value
     * The default minecraft value is 2.0F
     *
     * @return max scale level
     */
    float getMaxScale();

    /**
     * Lowest possible scale value
     * The default minecraft value is 1.0F
     *
     * @return min scale level
     */
    float getMinScale();
}
