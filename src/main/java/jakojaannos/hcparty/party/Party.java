package jakojaannos.hcparty.party;

import com.google.common.base.Preconditions;
import jakojaannos.api.hcparty.IParty;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.INBTSerializable;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class Party implements IParty, INBTSerializable<NBTTagCompound> {
    private List<PartyMember> members = new ArrayList<>();

    @Override
    public List<UUID> getMembers() {
        return members.stream()
                .map(member -> member.uuid)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isLeader(UUID playerUuid) {
        return members.stream()
                .anyMatch(member -> member.uuid == playerUuid && member.isLeader);
    }

    @Override
    public List<UUID> getLeaders() {
        return members.stream()
                .filter(member -> member.isLeader)
                .map(member -> member.uuid)
                .collect(Collectors.toList());
    }

    @Override
    public boolean isMember(UUID playerUuid) {
        return members.stream().anyMatch(member -> member.uuid == playerUuid);
    }


    @Override
    public void addMember(UUID playerUuid, boolean isLeader) {
        Preconditions.checkState(!isMember(playerUuid));
        members.add(new PartyMember(playerUuid, isLeader));
    }

    @Override
    public void removeMember(UUID playerUuid) {
        int index = -1;
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).uuid == playerUuid) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            members.remove(index);
        }
    }


    @Override
    public NBTTagCompound serializeNBT() {
        NBTTagCompound compound = new NBTTagCompound();

        // Write members
        NBTTagList list = new NBTTagList();
        for (PartyMember member : members) {
            list.appendTag(member.serializeNBT());
        }
        compound.setTag("members", list);

        return compound;
    }

    @Override
    public void deserializeNBT(NBTTagCompound nbt) {
        // Read members
        NBTTagList memberTags = nbt.getTagList("members", 10); // 10 => Compound
        for (int i = 0; i < memberTags.tagCount(); i++) {
            PartyMember member = new PartyMember();
            member.deserializeNBT(memberTags.getCompoundTagAt(i));
            members.add(member);
        }
    }


    private static class PartyMember implements INBTSerializable<NBTTagCompound> {
        UUID uuid;
        boolean isLeader;

        PartyMember() {
        }

        PartyMember(UUID playerUuid, boolean isLeader) {
            this.uuid = playerUuid;
            this.isLeader = isLeader;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound compound = new NBTTagCompound();
            compound.setUniqueId("uuid", uuid);
            compound.setBoolean("isLeader", isLeader);
            return compound;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            uuid = nbt.getUniqueId("uuid");
            isLeader = nbt.getBoolean("isLeader");
        }
    }
}
