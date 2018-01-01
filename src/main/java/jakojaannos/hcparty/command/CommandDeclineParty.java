package jakojaannos.hcparty.command;

import com.google.common.base.Preconditions;
import jakojaannos.hcparty.api.IInviteManager;
import jakojaannos.hcparty.api.IParty;
import jakojaannos.hcparty.api.IPartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;
import java.util.UUID;

public class CommandDeclineParty extends CommandPartyBase {
    @Override
    public String getName() {
        return "decline";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.decline.usage";
    }

    @Override
    protected void execute(MinecraftServer server, ICommandSender sender, String[] args, IPartyManager manager, IInviteManager inviteManager, UUID playerUuid) throws CommandException {
        // Parse index from arguments
        int index = 0;
        if (args.length > 0) {
            index = parseInt(args[0], 0);
        }

        // In a party, rejecting requests
        if (manager.isInParty(playerUuid)) {
            final IParty party = manager.getParty(playerUuid);
            Preconditions.checkNotNull(party);

            // Only party leader can decline requests
            // TODO: Make this configurable
            if (!party.isLeader(playerUuid)) {
                throw new CommandException("commands.hcparty.error.notleader");
            }

            List<UUID> requests = inviteManager.getPendingRequests(party);
            if (index < requests.size()) {
                inviteManager.rejectProposal(requests.get(index), party);
                sender.sendMessage(new TextComponentTranslation("commands.hcparty.decline.successrequest"));
            } else {
                throw new CommandException("commands.hcparty.error.invalidid");
            }
        }
        // Not in a party, rejecting invites
        else {
            List<IParty> invites = inviteManager.getPendingInvites(playerUuid);
            if (index < invites.size()) {
                inviteManager.rejectProposal(playerUuid, invites.get(index));
                sender.sendMessage(new TextComponentTranslation("commands.hcparty.decline.successinvite"));
            } else {
                throw new CommandException("commands.hcparty.error.invalidid");
            }
        }
    }
}
