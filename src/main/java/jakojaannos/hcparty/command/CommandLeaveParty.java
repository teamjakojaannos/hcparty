package jakojaannos.hcparty.command;

import jakojaannos.hcparty.api.IPartyManager;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentTranslation;

import java.util.UUID;

public class CommandLeaveParty extends CommandPartyBase {
    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getUsage(ICommandSender sender) {
        return "commands.hcparty.leave.usage";
    }

    @Override
    protected void execute(MinecraftServer server, ICommandSender sender, String[] args, IPartyManager manager, UUID playerUuid) throws CommandException {
        // Make sure player is currently in a party
        if (!manager.isInParty(playerUuid)) {
            throw new CommandException("commands.hcparty.error.notinparty");
        }

        // Validation done, leave party
        manager.removePlayerFromParty(playerUuid);
        sender.sendMessage(new TextComponentTranslation("commands.hcparty.leave.success"));
    }
}
