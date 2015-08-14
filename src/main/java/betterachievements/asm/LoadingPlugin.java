package betterachievements.asm;

import codechicken.core.launch.DepLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

import java.util.Map;

@IFMLLoadingPlugin.TransformerExclusions({"betterachievements.asm."})
public class LoadingPlugin implements IFMLLoadingPlugin
{
    public LoadingPlugin()
    {
        DepLoader.load();
    }

    @Override
    public String[] getASMTransformerClass()
    {
        return new String[]{"betterachievements.asm.BetterAchievementTransformer"};
    }

    @Override
    public String getModContainerClass()
    {
        return null;
    }

    @Override
    public String getSetupClass()
    {
        return null;
    }

    @Override
    public void injectData(Map<String, Object> data)
    {

    }

    @Override
    public String getAccessTransformerClass()
    {
        return null;
    }
}
