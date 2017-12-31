package jakojaannos.hcparty.command;

import com.google.common.base.Preconditions;
import jakojaannos.api.hcparty.IParty;
import jakojaannos.hcparty.party.Party;
import jakojaannos.hcparty.party.PartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.translation.I18n;

import java.util.Collection;
import java.util.UUID;

public class CommandListParty extends CommandPartyBase {
    CommandListParty(PartyManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.list.usage";
    }

    @Override
    protected void execute(MinecraftServer server, ICommandSender sender, String[] args, PartyManager manager, UUID playerUuid) throws CommandException {
        StringBuilder builder = new StringBuilder();

        // Add list of party members (if in a party)
        if (manager.isInParty(playerUuid)) {
            // Add title
            builder.append(I18n.translateToLocal("commands.hcparty.list.text.party"));
            builder.append("\n");

            // Add members
            final IParty party = manager.getCurrentParty(playerUuid);
            Preconditions.checkNotNull(party);
            for (UUID uuid : party.getMembers()) {
                // Prefix leader with ' L ', normal party members with ' - '
                if (party.isLeader(uuid)) {
                    builder.append(" L ");
                } else {
                    builder.append(" - ");
                }

                builder.append(getNameByUUID(server, uuid));
                builder.append("\n");
            }

            // Add list of invited players
            final Collection<UUID> invited = manager.getInviteManager().getPendingRequests(party);
            if (!invited.isEmpty()) {
                builder.append("\n");
                appendTitle(builder, "commands.hcparty.list.text.invited");

                int index = 0;
                for (UUID uuid : invited) {
                    appendPlayerName(builder, index, getNameByUUID(server, uuid));
                    index++;
                }
            }

            // Add list of players requesting to join
            final Collection<UUID> requests = manager.getInviteManager().getPendingRequests(party);
            if (!requests.isEmpty()) {
                builder.append("\n");
                appendTitle(builder, "commands.hcparty.list.text.requests");

                int index = 0;
                for (UUID uuid : requests) {
                    appendPlayerName(builder, index, getNameByUUID(server, uuid));
                    index++;
                }
            }
        } else {
            builder.append(I18n.translateToLocal("commands.hcparty.list.text.notinparty"));
            builder.append("\n");

            // Add list of pending invites
            final Collection<IParty> invites = manager.getInviteManager().getPendingInvites(playerUuid);
            if (!invites.isEmpty()) {
                builder.append("\n");
                appendTitle(builder, "commands.hcparty.list.text.invites");

                int index = 0;
                for (IParty party : invites) {
                    appendPlayerName(builder, index, getNameByUUID(server, party.getLeaders().get(0)));
                    index++;
                }
            }
        }
    }


    private String getNameByUUID(MinecraftServer server, UUID uuid) {
        return server.getPlayerList().getPlayerByUUID(uuid).getDisplayNameString();
    }

    private void appendTitle(StringBuilder builder, String translationKey) {
        builder.append(I18n.translateToLocal(translationKey));
        builder.append("\n");
    }

    private void appendPlayerName(StringBuilder builder, int index, String name) {
        builder.append(" ");
        builder.append(index);
        builder.append(". ");
        builder.append(name);
        builder.append("\n");
    }
}
