package com.t2pellet.teams.network;

import com.t2pellet.teams.network.client.*;
import com.t2pellet.teams.network.server.*;
import com.t2pellet.teams.platform.PhysicalSide;
import com.t2pellet.teams.platform.Platform;
import com.t2pellet.teams.platform.Services;

public class CommonPacketHandler {

    public static void registerPackets() {
        Services.PLATFORM.registerServerMessage(C2STeamCreatePacket.ID, C2STeamCreatePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamRequestPacket.ID, C2STeamRequestPacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamKickPacket.ID, C2STeamKickPacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamLeavePacket.ID, C2STeamLeavePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamInvitePacket.ID, C2STeamInvitePacket::new);
        Services.PLATFORM.registerServerMessage(C2STeamJoinPacket.ID, C2STeamJoinPacket::new);
        if (Services.PLATFORM.getPlatform() == Platform.FORGE || Services.PLATFORM.getPhysicalSide() == PhysicalSide.CLIENT) {
            registerClientPackets();
        }
    }

    public static void registerClientPackets() {
        Services.PLATFORM.registerClientMessage(S2CTeamPlayerDataPacket.ID, S2CTeamPlayerDataPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamDataPacket.ID, S2CTeamDataPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamRequestedPacket.ID, S2CTeamRequestedPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamClearPacket.ID, S2CTeamClearPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInvitedPacket.ID, S2CTeamInvitedPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInviteSentPacket.ID, S2CTeamInviteSentPacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamUpdatePacket.ID, S2CTeamUpdatePacket::new);
        Services.PLATFORM.registerClientMessage(S2CTeamInitPacket.ID, S2CTeamInitPacket::new);
    }
}
