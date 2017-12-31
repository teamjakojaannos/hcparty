package jakojaannos.hcparty.command;

import jakojaannos.hcparty.party.PartyManager;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public abstract class CommandPartyBase extends CommandBase {
    private final PartyManager manager;

    CommandPartyBase(PartyManager manager) {
        this.manager = manager;
    }

    @Override
    public final void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // Find player who sent this command
        final Entity senderEntity = sender.getCommandSenderEntity();
        if (!(senderEntity instanceof EntityPlayer)) {
            throw new WrongUsageException("commands.hcparty.error.notplayer");
        }
        final EntityPlayer senderPlayer = (EntityPlayer) senderEntity;

        execute(server, sender, args, manager, getUUID(senderPlayer));
    }

    protected abstract void execute(MinecraftServer server, ICommandSender sender, String[] args, PartyManager manager, UUID playerUuid) throws CommandException;

    UUID findUUIDByName(MinecraftServer server, String name) throws CommandException {
        EntityPlayer target = server.getPlayerList().getPlayerByUsername(name);

        if (target == null) {
            throw new PlayerNotFoundException("commands.hcparty.error.playernotfound");
        }

        return getUUID(target);
    }

    UUID getUUID(EntityPlayer player) {
        return player.getUniqueID();
    }
}
