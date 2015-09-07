package betterachievements.util;

public class ColourHelper
{
    public static int RGBA(int r, int g, int b, int a)
    {
        return (a << 24) | ((r & 255) << 16) | ((g & 255) << 8) | (b & 255);
    }

    public static int RGB(int red, int green, int blue)
    {
        return RGBA(red, green, blue , 255);
    }

    public static int RGB(String colour)
    {
        if (!colour.startsWith("#") || !(colour.length() == 7)) throw new IllegalArgumentException("Use #RRGGBB format");
        return RGB(Integer.parseInt(colour.substring(1, 3), 16), Integer.parseInt(colour.substring(3, 5), 16), Integer.parseInt(colour.substring(5, 7), 16));
    }
}
