package jakojaannos.life.revival.capability;

import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import jakojaannos.life.api.revival.capability.IRevivable;
import jakojaannos.life.api.revival.capability.ISavior;
import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Provides IRevivable, ISavior, IBleedoutHandler and IUnconsciousHandler capabilities to players.
 */
public class PlayerCapabilityProvider implements ICapabilitySerializable<NBTTagCompound> {
    private final IRevivable revivable;
    private final ISavior savior;
    private final IBleedoutHandler bleedoutHandler;
    private final IUnconsciousHandler unconsciousHandler;

    public PlayerCapabilityProvider(EntityPlayer player) {
        revivable = new PlayerRevivable(player);
        savior = new PlayerSavior(player);
        bleedoutHandler = new PlayerBleedoutHandler(player);
        unconsciousHandler = new PlayerUnconsciousHandler();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ModCapabilities.REVIVABLE
                || capability == ModCapabilities.SAVIOR
                || capability == ModCapabilities.BLEEDOUT_HANDLER
                || capability == ModCapabilities.UNCONSCIOUS_HANDLER;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        if (capability == ModCapabilities.REVIVABLE) {
            return (T) revivable;
        } else if (capability == ModCapabilities.SAVIOR) {
            return (T) savior;
        } else if (capability == ModCapabilities.BLEEDOUT_HANDLER) {
            return (T) bleedoutHandler;
        } else if (capability == ModCapabilities.UNCONSCIOUS_HANDLER) {
            return (T) unconsciousHandler;
        } else {
            return null;
        }
    }

    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setTag("revivable", ModCapabilities.REVIVABLE.writeNBT(revivable, null));
        compound.setTag("savior", ModCapabilities.SAVIOR.writeNBT(savior, null));
        compound.setTag("bleedout", ModCapabilities.BLEEDOUT_HANDLER.writeNBT(bleedoutHandler, null));
        compound.setTag("unconscious", ModCapabilities.UNCONSCIOUS_HANDLER.writeNBT(unconsciousHandler, null));
        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ModCapabilities.REVIVABLE.readNBT(revivable, null, nbt);
        ModCapabilities.SAVIOR.readNBT(savior, null, nbt);
        ModCapabilities.BLEEDOUT_HANDLER.readNBT(bleedoutHandler, null, nbt);
        ModCapabilities.UNCONSCIOUS_HANDLER.readNBT(unconsciousHandler, null, nbt);
    }
}
