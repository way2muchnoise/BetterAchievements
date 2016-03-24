package betterachievements.reference;

public class Reference
{
    // User friendly version of our mods name.
    public static final String NAME = "Better Achievements";

    // Internal mod name used for reference purposes and resource gathering.
    public static final String ID = "BetterAchievements";
    public static final String RESOURCE_ID = ID.toLowerCase();

    // Main version information that will be displayed in mod listing and for other purposes.
    public static final String VERSION_FULL = "@VERSION@";

    // proxy info
    public static final String SERVER_PROXY = "betterachievements.proxy.CommonProxy";
    public static final String CLIENT_PROXY = "betterachievements.proxy.ClientProxy";
    public static final String MOD_GUI_FACTORY = "betterachievements.gui.ModGuiFactory";
}
