package jakojaannos.life.entity.capability;

import jakojaannos.life.api.entity.capability.IHealthTracker;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class HealthTrackerStorage implements Capability.IStorage<IHealthTracker> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IHealthTracker> capability, IHealthTracker instance, EnumFacing side) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("logLength", instance.getBacklogLength());
        return compound;
    }

    @Override
    public void readNBT(Capability<IHealthTracker> capability, IHealthTracker instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound) nbt;
        instance.setBacklogLength(compound.getInteger("logLength"));
    }
}
