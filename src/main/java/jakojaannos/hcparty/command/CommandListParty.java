package jakojaannos.hcparty.command;

import com.google.common.base.Preconditions;
import jakojaannos.hcparty.api.IInviteManager;
import jakojaannos.hcparty.api.IParty;
import jakojaannos.hcparty.api.IPartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.translation.I18n;

import java.util.Collection;
import java.util.UUID;

public class CommandListParty extends CommandPartyBase {
    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.list.usage";
    }

    @Override
    protected void execute(MinecraftServer server, ICommandSender sender, String[] args, IPartyManager manager, IInviteManager inviteManager, UUID playerUuid) throws CommandException {
        StringBuilder builder = new StringBuilder();

        // Add list of party members (if in a party)
        if (manager.isInParty(playerUuid)) {
            // Add title
            builder.append(I18n.translateToLocal("commands.hcparty.list.text.party"));
            builder.append("\n");

            // Add members
            final IParty party = manager.getParty(playerUuid);
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
            final Collection<UUID> invited = inviteManager.getPendingRequests(party);
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
            final Collection<UUID> requests = inviteManager.getPendingRequests(party);
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
            final Collection<IParty> invites = inviteManager.getPendingInvites(playerUuid);
            if (!invites.isEmpty()) {
                builder.append("\n");
                appendTitle(builder, "commands.hcparty.list.text.invites");

                int index = 0;
                for (IParty party : invites) {
                    appendPlayerName(builder, index, getNameByUUID(server, party.getLeader()));
                    index++;
                }
            }
        }

        // Print out the message
        sender.sendMessage(new TextComponentTranslation(builder.toString()));
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
