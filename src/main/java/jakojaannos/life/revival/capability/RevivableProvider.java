package jakojaannos.life.revival.capability;

import jakojaannos.life.api.revival.IRevivable;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class RevivableProvider implements net.minecraftforge.common.capabilities.ICapabilityProvider {
    private final IRevivable instance;

    public RevivableProvider(EntityPlayer player) {
        this.instance = new RevivableImpl(player);
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ModCapabilities.REVIVABLE;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return (T) this.instance;
    }
}
