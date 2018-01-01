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

public class CommandAcceptParty extends CommandPartyBase {
    @Override
    public String getName() {
        return "accept";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.accept.usage";
    }

    @Override
    protected void execute(MinecraftServer server, ICommandSender sender, String[] args, IPartyManager manager, IInviteManager inviteManager, UUID playerUuid) throws CommandException {
        // Parse index from arguments
        int index = 0;
        if (args.length > 0) {
            index = parseInt(args[0], 0);
        }

        // In a party, accepting requests
        if (manager.isInParty(playerUuid)) {
            final IParty party = manager.getParty(playerUuid);
            Preconditions.checkNotNull(party);

            // Only party leader can accept requests
            // TODO: Make this configurable
            if (!party.isLeader(playerUuid)) {
                throw new CommandException("commands.hcparty.error.notleader");
            }

            List<UUID> requests = inviteManager.getPendingRequests(party);
            if (requests.size() == 0) {
                throw new CommandException("commands.hcparty.accept.error.empty");
            }

            if (index < requests.size()) {
                UUID target = requests.get(index);
                inviteManager.acceptProposal(target, party);
                sender.sendMessage(new TextComponentTranslation("commands.hcparty.accept.successrequest"));
            } else {
                throw new CommandException("commands.hcparty.error.invalidid");
            }
        }
        // Not in a party, accepting invites
        else {
            List<IParty> invites = inviteManager.getPendingInvites(playerUuid);
            if (invites.size() == 0) {
                throw new CommandException("commands.hcparty.accept.error.empty");
            }

            if (index < invites.size()) {
                inviteManager.acceptProposal(playerUuid, invites.get(index));
                sender.sendMessage(new TextComponentTranslation("commands.hcparty.accept.successinvite"));
            } else {
                throw new CommandException("commands.hcparty.error.invalidid");
            }
        }
    }
}
