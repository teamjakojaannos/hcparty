package jakojaannos.hcparty.command;

import jakojaannos.api.hcparty.IParty;
import jakojaannos.hcparty.party.Party;
import jakojaannos.hcparty.party.PartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

public class CommandJoinParty extends CommandPartyBase {
    CommandJoinParty(PartyManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.join.usage";
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index) {
        return index == 0;
    }

    @Override
    protected void execute(MinecraftServer server, ICommandSender sender, String[] args, PartyManager manager, UUID playerUuid) throws CommandException {
        if (args.length < 1) {
            throw new WrongUsageException(getUsage(sender));
        }
        UUID targetUuid = findUUIDByName(server, args[0]);

        // Make sure player is currently not in a party
        if (manager.isInParty(playerUuid)) {
            throw new CommandException("commands.hcparty.error.inparty");
        }

        // Make sure target player is in a party
        final IParty party = manager.getCurrentParty(targetUuid);
        if (party != null) {
            // Validation done, join party
            manager.getInviteManager().requestToJoinParty(playerUuid, party);
            sender.sendMessage(new TextComponentTranslation("commands.hcparty.join.success"));
        }
        else {
            throw new CommandException("commands.hcparty.join.error.notinparty");
        }
    }
}
