package jakojaannos.hcparty.command;

import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;
import net.minecraftforge.server.command.CommandTreeHelp;

public class CommandParty extends CommandTreeBase {
    @Override
    public String getName() {
        return "hcparty";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.usage";
    }

    @Override
    public int getRequiredPermissionLevel() {
        return 0;
    }

    public CommandParty() {
        addSubcommand(new CommandCreateParty());
        addSubcommand(new CommandJoinParty());
        addSubcommand(new CommandLeaveParty());
        addSubcommand(new CommandInviteParty());
        addSubcommand(new CommandListParty());
        addSubcommand(new CommandAcceptParty());
        addSubcommand(new CommandDeclineParty());
        addSubcommand(new CommandTreeHelp(this));
    }
}
