package betterachievements.handler;

import betterachievements.gui.GuiBetterAchievements;
import betterachievements.reference.Reference;
import betterachievements.util.ColourHelper;
import cpw.mods.fml.client.config.IConfigElement;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.common.config.Configuration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler
{
    public static Configuration config;

    public static void init(File file)
    {
        if (config == null)
        {
            config = new Configuration(file);
            loadConfig();
        }
    }

    @SubscribeEvent
    public void onConfigChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event)
    {
        if (event.modID.equalsIgnoreCase(Reference.ID))
        {
            loadConfig();
        }
    }

    private static void loadConfig()
    {
        String cantUnlock = config.getString(StatCollector.translateToLocal("betterachievements.config.cantUnlockArrowColour"), Configuration.CATEGORY_GENERAL, "#000000" , StatCollector.translateToLocal("betterachievements.config.cantUnlockArrowColour.desc"));
        String canUnlock = config.getString(StatCollector.translateToLocal("betterachievements.config.canUnlockArrowColour"), Configuration.CATEGORY_GENERAL, "#A0A0A0" , StatCollector.translateToLocal("betterachievements.config.canUnlockArrowColour.desc"));
        String unlocked = config.getString(StatCollector.translateToLocal("betterachievements.config.completeArrowColour"), Configuration.CATEGORY_GENERAL, "#00FF00" , StatCollector.translateToLocal("betterachievements.config.completeArrowColour.desc"));
        GuiBetterAchievements.colourCantUnlock = ColourHelper.RGB(cantUnlock);
        GuiBetterAchievements.colourCanUnlock = ColourHelper.RGB(canUnlock);
        GuiBetterAchievements.colourUnlocked = ColourHelper.RGB(unlocked);
    }

    @SuppressWarnings("unchecked")
    public static List<IConfigElement> getConfigElements()
    {
        List<IConfigElement> list = new ArrayList<IConfigElement>();
        list.addAll(new ConfigElement(config.getCategory(Configuration.CATEGORY_GENERAL)).getChildElements());
        return list;
    }
}
