package jakojaannos.life.revival.capability.storage;

import jakojaannos.life.api.revival.capabilities.IRevivable;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

import javax.annotation.Nullable;

public class RevivableStorage implements Capability.IStorage<IRevivable> {
    @Nullable
    @Override
    public NBTBase writeNBT(Capability<IRevivable> capability, IRevivable instance, EnumFacing side) {

        return null;
    }

    @Override
    public void readNBT(Capability<IRevivable> capability, IRevivable instance, EnumFacing side, NBTBase nbt) {
        null;
    }
}
