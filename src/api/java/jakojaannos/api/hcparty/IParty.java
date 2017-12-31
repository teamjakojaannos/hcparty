package jakojaannos.api.hcparty;

import java.util.List;
import java.util.UUID;

public interface IParty {

    List<UUID> getMembers();

    List<UUID> getLeaders();

    boolean isMember(UUID playerUuid);

    boolean isLeader(UUID playerUuid);

    void addMember(UUID playerUuid, boolean isLeader);

    void removeMember(UUID playerUuid);
}
