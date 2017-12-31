package jakojaannos.hcparty.command;

import jakojaannos.hcparty.party.PartyManager;
import net.minecraft.command.ICommandSender;
import net.minecraftforge.server.command.CommandTreeBase;

public class CommandParty extends CommandTreeBase {
    @Override
    public String getName() {
        return "hcparty";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.usage";
    }

    public CommandParty(PartyManager manager) {
        addSubcommand(new CommandCreateParty(manager));
        addSubcommand(new CommandJoinParty(manager));
        addSubcommand(new CommandLeaveParty(manager));
        addSubcommand(new CommandInviteParty(manager));
        addSubcommand(new CommandListParty(manager));
        addSubcommand(new CommandAcceptParty(manager));
        addSubcommand(new CommandDeclineParty(manager));
    }
}
