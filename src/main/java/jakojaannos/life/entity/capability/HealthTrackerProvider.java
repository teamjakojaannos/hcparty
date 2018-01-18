package jakojaannos.life.entity.capability;

import jakojaannos.life.api.entity.capability.IHealthTracker;
import jakojaannos.life.init.ModCapabilities;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class HealthTrackerProvider implements ICapabilitySerializable<NBTTagCompound> {
    private final IHealthTracker tracker;

    public HealthTrackerProvider() {
        this.tracker = new HealthTracker();
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing) {
        return capability == ModCapabilities.HEALTH_TRACKER;
    }

    @Nullable
    @Override
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing) {
        return capability == ModCapabilities.HEALTH_TRACKER ? (T) tracker : null;
    }

    @Override
    public NBTTagCompound serializeNBT() {
        return (NBTTagCompound) ModCapabilities.HEALTH_TRACKER.writeNBT(tracker, null);
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        ModCapabilities.HEALTH_TRACKER.readNBT(tracker, null, nbt);
    }
}
