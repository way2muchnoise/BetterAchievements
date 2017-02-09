package betterachievements.command;

import betterachievements.handler.AchievementHandler;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class SaveCommand implements ISubCommand {

    @Override
    public String getCommandName() {
        return "save";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) throws CommandException {
        if (!sender.getEntityWorld().isRemote) {
            AchievementHandler.getInstance().saveAchievements(sender.getEntityWorld());
        }
    }
}
