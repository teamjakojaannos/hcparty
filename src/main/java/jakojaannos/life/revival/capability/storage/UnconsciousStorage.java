package jakojaannos.life.revival.capability.storage;

import jakojaannos.life.api.revival.capability.IUnconsciousHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class UnconsciousStorage implements Capability.IStorage<IUnconsciousHandler> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IUnconsciousHandler> capability, IUnconsciousHandler instance, EnumFacing side) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setInteger("duration", instance.getDuration());
        compound.setInteger("timer", instance.getTimer());
        return compound;
    }

    @Override
    public void readNBT(Capability<IUnconsciousHandler> capability, IUnconsciousHandler instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound) nbt;
        instance.setDuration(compound.getInteger("duration"));
        instance.setTimer(compound.getInteger("timer"));
    }
}
