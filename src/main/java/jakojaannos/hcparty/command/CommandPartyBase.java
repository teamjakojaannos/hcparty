package jakojaannos.hcparty.command;

import com.google.common.base.Preconditions;
import jakojaannos.hcparty.api.IInviteManager;
import jakojaannos.hcparty.api.IPartyManager;
import net.minecraft.command.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;

import java.util.UUID;

public abstract class CommandPartyBase extends CommandBase {
    @Override
    public final void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
        // Find player who sent this command
        final Entity senderEntity = sender.getCommandSenderEntity();
        if (!(senderEntity instanceof EntityPlayer)) {
            throw new WrongUsageException("commands.hcparty.error.notplayer");
        }
        final EntityPlayer senderPlayer = (EntityPlayer) senderEntity;

        Preconditions.checkNotNull(IPartyManager.INSTANCE);
        Preconditions.checkNotNull(IInviteManager.INSTANCE);
        execute(server, sender, args, IPartyManager.INSTANCE, IInviteManager.INSTANCE, getUUID(senderPlayer));
    }

    protected abstract void execute(MinecraftServer server, ICommandSender sender, String[] args, IPartyManager manager, IInviteManager inviteManager, UUID playerUuid) throws CommandException;

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
