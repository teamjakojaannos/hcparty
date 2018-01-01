package jakojaannos.hcparty.command;

import jakojaannos.hcparty.api.IInviteManager;
import jakojaannos.hcparty.api.IParty;
import jakojaannos.hcparty.api.IPartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

public class CommandInviteParty extends CommandPartyBase {
    @Override
    public String getName() {
        return "invite";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.invite.usage";
    }

    @Override
    protected void execute(MinecraftServer server, ICommandSender sender, String[] args, IPartyManager manager, IInviteManager inviteManager, UUID playerUuid) throws CommandException {
        // Validate the player name passed in as argument
        if (args.length < 1) {
            throw new WrongUsageException(getUsage(sender));
        }
        UUID targetUuid = findUUIDByName(server, args[0]);

        // Make sure target player is currently not in a party
        if (manager.isInParty(targetUuid)) {
            throw new CommandException("commands.hcparty.error.notinparty");
        }

        // Make sure inviting player is in a party
        final IParty party = manager.getParty(playerUuid);
        if (party != null) {
            // Validation done, invite to party
            inviteManager.addProposal(targetUuid, party, true);
            sender.sendMessage(new TextComponentTranslation("commands.hcparty.invite.success"));
        } else {
            throw new CommandException("commands.hcparty.error.notinparty");
        }
    }
}
