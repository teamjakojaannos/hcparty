package jakojaannos.life.revival.capability.storage;

import jakojaannos.life.api.revival.capability.ISavior;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class SaviorStorage implements Capability.IStorage<ISavior> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<ISavior> capability, ISavior instance, EnumFacing side) {
        return new NBTTagCompound();
    }

    @Override
    public void readNBT(Capability<ISavior> capability, ISavior instance, EnumFacing side, NBTBase nbt) {

    }
}
