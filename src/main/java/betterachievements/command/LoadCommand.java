package betterachievements.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

public class LoadCommand implements ISubCommand {
    @Override
    public String getCommandName() {
        return "load";
    }

    @Override
    public void handleCommand(ICommandSender sender, String[] arguments) throws CommandException {

    }
}
