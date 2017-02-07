package betterachievements.api.util;

import betterachievements.util.LogHelper;

public class ColourHelper
{
    /**
     * Convert to integer RGBA value
     * Uses 255 as A value
     *
     * @param r integer red
     * @param g integer green
     * @param b integer blue
     *
     * @return single integer representation of the given ints
     */
    public static int RGB(int r, int g, int b)
    {
        return RGBA(r, g, b, 255);
    }

    /**
     * Convert to integer RGBA value
     *
     * @param r integer red
     * @param g integer green
     * @param b integer blue
     * @param a integer alpha
     *
     * @return single integer representation of the given ints
     */
    public static int RGBA(int r, int g, int b, int a)
    {
        return ((a & 255) << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

    /**
     * Convert to integer RGBA value
     * Uses 1.0F as A value
     *
     * @param red float red
     * @param green float green
     * @param blue float blue
     *
     * @return single integer representation of the given floats
     */
    public static int RGB(float red, float green, float blue)
    {
        return RGBA((int) (red * 255), (int) (green * 255), (int) (blue * 255), 255);
    }

    /**
     * Convert to integer RGBA value
     *
     * @param red float red
     * @param green float green
     * @param blue float blue
     * @param alpha float alpha
     *
     * @return single integer representation of the given floats
     */
    public static int RGB(float red, float green, float blue, float alpha)
    {
        return RGBA((int) (red * 255), (int) (green * 255), (int) (blue * 255), (int) (alpha * 255));
    }

    /**
     * Convert an #RRGGBB value to a int colour
     *
     * @param colour the #RRGGBB value
     * @return the int colour value or an {@link java.lang.IllegalArgumentException} if a mal formed input is given
     */
    public static int RGB(String colour)
    {
        if (!colour.startsWith("#") || !(colour.length() == 7)) throw new IllegalArgumentException("Use #RRGGBB format");
        return RGB(Integer.parseInt(colour.substring(1, 3), 16), Integer.parseInt(colour.substring(3, 5), 16), Integer.parseInt(colour.substring(5, 7), 16));
    }

    /**
     * Blends given int colours
     *
     * @param colours an amount of colours
     * @return the mix int colour value or an IllegalArgumentException if colours is empty
     */
    public static int blend(int... colours)
    {
        if (colours.length < 1)
            throw new IllegalArgumentException();

        int[] alphas = new int[colours.length];
        int[] reds = new int[colours.length];
        int[] greens = new int[colours.length];
        int[] blues = new int[colours.length];

        for (int i = 0; i < colours.length; i++)
        {
            alphas[i] = (colours[i] >> 24 & 0xff);
            reds[i] = ((colours[i] & 0xff0000) >> 16);
            greens[i] = ((colours[i] & 0xff00) >> 8);
            blues[i] = (colours[i] & 0xff);
        }

        float a, r, g, b;
        a = r = g = b = 0;
        float ratio = 1.0F / colours.length;

        for (int alpha : alphas)
            a += alpha * ratio;

        for (int red : reds)
            r += red * ratio;

        for (int green : greens)
            g += green * ratio;

        for (int blue : blues)
            b += blue * ratio;

        return ((int) a) << 24 | ((int) r) << 16 | ((int) g) << 8 | ((int) b);
    }

    /**
     * Tone a int colour
     * bigger then 1 will tone up, less then 1 will tone down
     * @param colour colour in int form
     * @param scale scale as float
     * @return the toned colour
     */
    public static int tone(int colour, float scale)
    {
        float r = (colour >> 16) & 255;
        float g = (colour >> 8) & 255;
        float b = colour & 255;
        return RGB(r * scale, g * scale, b * scale);
    }

    /**
     * Blend colour with given grey scale
     *
     * @param colour colour in int form
     * @param greyScale grayScale as float
     * @return the toned colour
     */
    public static int blendWithGreyScale(int colour, float greyScale)
    {
        return ColourHelper.blend(colour, ColourHelper.RGB(greyScale, greyScale, greyScale));
    }

    /**
     * Gives a colour based of {@link System#currentTimeMillis()} and given params
     *
     * @param freqR frequency of the reds
     * @param freqG frequency of the greens
     * @param freqB frequency of the blues
     * @param phaseR phase shift red
     * @param phaseG phase shift green
     * @param phaseB phase shift blue
     * @param center center value
     * @param width width of colour range
     * @param length change rate
     * @return an int colour
     */
    public static int getRainbowColour(float freqR, float freqG, float freqB, float phaseR, float phaseG, float phaseB, float center, float width, float length)
    {
        double i = (System.currentTimeMillis() * 2 * Math.PI) / (double) length;
        double r = Math.sin(freqR*i + phaseR) * width + center;
        double g = Math.sin(freqG*i + phaseG) * width + center;
        double b = Math.sin(freqB*i + phaseB) * width + center;
        return RGB((int)r, (int)g, (int)b);
    }

    /**
     * Short had for parsing array of params
     *
     * @param params all parameters for {@link #getRainbowColour(float, float, float, float, float, float, float, float, float)}
     * @return an int rainbow colour
     */
    public static int getRainbowColour(float[] params)
    {
        return getRainbowColour(params[0], params[1], params[2], params[3], params[4], params[5], params[6], params[7], params[8]);
    }

    /**
     * Create settings for rainbow colour
     *
     * @param colourCode a string representation of the rainbow settings
     * @return an array containing parameters for {@link #getRainbowColour(float, float, float, float, float, float, float, float, float)}
     */
    public static float[] getRainbowSettings(String colourCode)
    {
        String[] splitted = colourCode.split(";");
        float[] result = {0.3F, 0.3F, 0.3F, 0, 2, 4, 128, 127, 50};
        for (int i = 1; i < splitted.length; i++)
        {
            try
            {
                result[i-1] = Float.parseFloat(splitted[i]);
            } catch (NumberFormatException e)
            {
                LogHelper.instance().error(e, "Parsing error while creating rainbow settings");
            }
        }
        return result;
    }
}
