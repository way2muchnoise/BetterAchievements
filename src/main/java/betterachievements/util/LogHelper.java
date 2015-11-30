package betterachievements.util;

import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Loader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogHelper
{
    private Logger log;
    private boolean debug;

    public LogHelper(String id)
    {
        log = LogManager.getLogger(id);
    }

    public static LogHelper instance()
    {
        return new LogHelper(Loader.instance().activeModContainer().getModId());
    }

    public void debug(Object obj)
    {
        if (debug)
            log.info(obj);
        else
            log.debug(obj);
    }

    public void info(Object obj)
    {
        log.info(obj);
    }

    public void warn(Object obj)
    {
        log.warn(obj);
    }

    public void crash(Exception e, String message)
    {
        FMLCommonHandler.instance().raiseException(e, message, true);
    }

    public void error(Exception e, String message)
    {
        FMLCommonHandler.instance().raiseException(e, message, false);
    }

    public void setDebug(boolean debug)
    {
        this.debug = debug;
    }
}
