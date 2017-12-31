package jakojaannos.hcparty.command;

import jakojaannos.hcparty.party.PartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

public class CommandCreateParty extends CommandPartyBase {
    CommandCreateParty(PartyManager manager) {
        super(manager);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.create.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args, PartyManager manager, UUID playerUuid) throws CommandException {
        // Make sure player is currently not in a party
        if (manager.isInParty(playerUuid)) {
            throw new CommandException("commands.hcparty.error.inparty");
        }

        // Validation done, create party
        manager.createParty(playerUuid);
        sender.sendMessage(new TextComponentTranslation("commands.hcparty.create.success"));
    }
}
