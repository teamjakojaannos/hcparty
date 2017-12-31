package jakojaannos.hcparty.command;

import com.google.common.base.Preconditions;
import jakojaannos.api.hcparty.IParty;
import jakojaannos.hcparty.party.Party;
import jakojaannos.hcparty.party.PartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.List;
import java.util.UUID;

public class CommandDeclineParty extends CommandPartyBase {
    CommandDeclineParty(PartyManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "decline";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.decline.usage";
    }

    @Override
    protected void execute(MinecraftServer server, ICommandSender sender, String[] args, PartyManager manager, UUID playerUuid) throws CommandException {
        // Parse index from arguments
        int index = 0;
        if (args.length > 0) {
            index = parseInt(args[0], 0);
        }

        // In a party, accepting requests
        if (manager.isInParty(playerUuid)) {
            final IParty party = manager.getCurrentParty(playerUuid);
            Preconditions.checkNotNull(party);

            // Only party leader can decline requests
            // TODO: Make this configurable
            if (!party.isLeader(playerUuid)) {
                throw new CommandException("commands.hcparty.error.notleader");
            }

            List<UUID> requests = manager.getInviteManager().getPendingRequests(party);
            if (index < requests.size()) {
                manager.getInviteManager().removeRequest(party, index);
                sender.sendMessage(new TextComponentTranslation("commands.hcparty.decline.successrequest"));
            }
            else {
                throw new CommandException("commands.hcparty.error.invalidid");
            }
        }
        // Not in a party, accepting invites
        else {
            List<IParty> invites = manager.getInviteManager().getPendingInvites(playerUuid);
            if (index < invites.size()) {
                manager.getInviteManager().removeInvite(playerUuid, index);
                sender.sendMessage(new TextComponentTranslation("commands.hcparty.decline.successinvite"));
            }
            else {
                throw new CommandException("commands.hcparty.error.invalidid");
            }
        }
    }
}
