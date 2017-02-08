package betterachievements.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;

import java.util.ArrayList;
import java.util.List;

public interface ISubCommand {

    default int getPermissionLevel() {
        return 3;
    }

    String getCommandName();

    void handleCommand(ICommandSender sender, String[] arguments) throws CommandException;

    default List<String> addTabCompletionOptions(ICommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    default boolean isVisible(ICommandSender sender) {
        return getPermissionLevel() <= 0 || CommandHandler.isOwnerOrOp(sender);
    }

    default int[] getSyntaxOptions(ICommandSender sender){
        return new int[]{0};
    }
}