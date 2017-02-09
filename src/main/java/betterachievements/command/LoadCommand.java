package betterachievements.command;

import betterachievements.handler.AchievementHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.world.WorldServer;

public class LoadCommand implements ISubCommand {
    @Override
    public String getCommandName() {
        return "load";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) throws CommandException {
        if (!sender.getEntityWorld().isRemote && sender.getEntityWorld() instanceof WorldServer) {
            AchievementHandler.getInstance().loadAchievements((WorldServer) sender.getEntityWorld());
        }
    }
}
