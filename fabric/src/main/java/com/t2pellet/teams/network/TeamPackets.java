package com.t2pellet.teams.network;

import com.t2pellet.teams.network.packets.*;
import com.t2pellet.teams.network.packets.toasts.TeamInviteSentPacket;
import com.t2pellet.teams.network.packets.toasts.TeamInvitedPacket;
import com.t2pellet.teams.network.packets.toasts.TeamUpdatePacket;

public class TeamPackets {

    private TeamPackets() {
    }

    // Client
    public static final Packet.PacketKey<TeamPlayerDataPacket> TEAM_PLAYERDATA_PACKET = new Packet.PacketKey<>(TeamPlayerDataPacket.class, "message_team_playerdata");
    public static final Packet.PacketKey<TeamInvitedPacket> TEAM_INVITED_PACKET = new Packet.PacketKey<>(TeamInvitedPacket.class, "message_team_invited");
    public static final Packet.PacketKey<TeamClearPacket> TEAM_CLEAR_PACKET = new Packet.PacketKey<>(TeamClearPacket.class, "message_team_clear");
    public static final Packet.PacketKey<TeamInitPacket> TEAM_INIT_PACKET = new Packet.PacketKey<>(TeamInitPacket.class, "message_team_init");
    public static final Packet.PacketKey<TeamInviteSentPacket> TEAM_INVITE_SENT_PACKET = new Packet.PacketKey<>(TeamInviteSentPacket.class, "message_team_invite_sent");
    public static final Packet.PacketKey<TeamUpdatePacket> TEAM_UPDATE_PACKET = new Packet.PacketKey<>(TeamUpdatePacket.class, "message_team_update");
    public static final Packet.PacketKey<TeamDataPacket> TEAM_DATA_PACKET = new Packet.PacketKey<>(TeamDataPacket.class, "message_team_data_packet");
    public static final Packet.PacketKey<TeamRequestedPacket> TEAM_REQUESTED_PACKET = new Packet.PacketKey<>(TeamRequestedPacket.class, "message_team_requested");

    // Server
    public static final Packet.PacketKey<TeamJoinPacket> TEAM_JOIN_PACKET = new Packet.PacketKey<>(TeamJoinPacket.class, "message_team_join");
    public static final Packet.PacketKey<TeamKickPacket> TEAM_KICK_PACKET = new Packet.PacketKey<>(TeamKickPacket.class, "message_team_kick");
    public static final Packet.PacketKey<TeamLeavePacket> TEAM_LEAVE_PACKET = new Packet.PacketKey<>(TeamLeavePacket.class, "message_team_leave");
    public static final Packet.PacketKey<TeamInvitePacket> TEAM_INVITE_PACKET = new Packet.PacketKey<>(TeamInvitePacket.class, "message_team_invite");
    public static final Packet.PacketKey<TeamCreatePacket> TEAM_CREATE_PACKET = new Packet.PacketKey<>(TeamCreatePacket.class, "message_team_create");
    public static final Packet.PacketKey<TeamRequestPacket> TEAM_REQUEST_PACKET = new Packet.PacketKey<>(TeamRequestPacket.class, "message_team_request");

}
