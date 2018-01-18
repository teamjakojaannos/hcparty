package jakojaannos.life.revival.capability.storage;

import jakojaannos.life.api.revival.capability.IBleedoutHandler;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class BleedoutStorage implements Capability.IStorage<IBleedoutHandler> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IBleedoutHandler> capability, IBleedoutHandler instance, EnumFacing side) {
        NBTTagCompound compound = new NBTTagCompound();
        compound.setFloat("health", instance.getBleedoutHealth());
        compound.setInteger("counter", instance.getBleedoutCount());
        compound.setInteger("timer", instance.getBleedoutTime());
        return compound;
    }

    @Override
    public void readNBT(Capability<IBleedoutHandler> capability, IBleedoutHandler instance, EnumFacing side, NBTBase nbt) {
        NBTTagCompound compound = (NBTTagCompound) nbt;
        instance.setBleedoutHealth(compound.getFloat("health"));
        instance.setBleedoutCount(compound.getInteger("counter"));
        instance.setBleedoutTime(compound.getInteger("time"));
    }
}
