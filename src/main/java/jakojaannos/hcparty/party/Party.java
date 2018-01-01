package jakojaannos.hcparty.party;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import jakojaannos.hcparty.api.IParty;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

class Party implements IParty, INBTSerializable<NBTTagCompound> {
    private byte id;

    private List<UUID> members = new ArrayList<>();
    private int leaderIndex = 0;

    Party(byte id, UUID leader) {
        this.id = id;
        addMember(leader);
    }

    @Override
    public byte getId() {
        return id;
    }

    @Override
    public ImmutableList<UUID> getMembers() {
        return ImmutableList.copyOf(members);
    }

    @Override
    public UUID getLeader() {
        Preconditions.checkElementIndex(leaderIndex, members.size());
        return members.get(leaderIndex);
    }

    @Override
    public void setLeader(UUID playerUuid) {
        if (isMember(playerUuid)) {
            leaderIndex = members.indexOf(playerUuid);
        }
    }

    @Override
    public void addMember(UUID playerUuid) {
        if (isMember(playerUuid)) {
            return;
        }

        members.add(playerUuid);
        PartyManager.INSTANCE.onPlayerAdded(playerUuid, this);
    }

    @Override
    public void removeMember(UUID playerUuid) {
        if (!isMember(playerUuid)) {
            return;
        }

        boolean wasLeader = isLeader(playerUuid);
        members.remove(playerUuid);

        PartyManager.INSTANCE.onPlayerRemoved(playerUuid, this, wasLeader);
    }


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        // Write members
        NBTTagList list = new NBTTagList();
        for (UUID uuid : members) {
            NBTTagCompound memberCompound = new NBTTagCompound();
            memberCompound.setUniqueId("uuid", uuid);
            list.appendTag(memberCompound);
        }
        compound.setTag("members", list);

        // Write other info
        compound.setInteger("leader", leaderIndex);
        compound.setByte("partyid", id);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        // Read members
        NBTTagList memberTags = nbt.getTagList("members", 10); // 10 => Compound
        for (int i = 0; i < memberTags.tagCount(); i++) {
            members.add(memberTags.getCompoundTagAt(i).getUniqueId("uuid"));
        }

        // Read other info
        id = nbt.getByte("partyid");
        leaderIndex = nbt.getInteger("leader");
    }


    @Override
    public int hashCode() {
        return id;
    }

    @Override
    public boolean equals(Object obj) {
        return obj != null && obj instanceof IParty && ((IParty) obj).getId() == this.id;
    }
}