package jakojaannos.hcparty.command;

import jakojaannos.hcparty.api.IPartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

public class CommandCreateParty extends CommandPartyBase {
    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.create.usage";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args, IPartyManager manager, UUID playerUuid) throws CommandException {
        // Make sure player is currently not in a party
        if (manager.isInParty(playerUuid)) {
            throw new CommandException("commands.hcparty.error.inparty");
        }

        // Validation done, create party
        manager.createParty(playerUuid);
        sender.sendMessage(new TextComponentTranslation("commands.hcparty.create.success"));
    }
}
