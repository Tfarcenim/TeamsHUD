package com.t2pellet.teams.network;

import com.t2pellet.teams.TeamsHUD;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class Packet {

    protected CompoundTag tag;

    Packet(FriendlyByteBuf byteBuf) {
        this.tag = byteBuf.readNbt();
    }

    Packet() {
        tag = new CompoundTag();
    }

    public void encode(FriendlyByteBuf byteBuf) {
        byteBuf.writeNbt(tag);
    }

    public abstract void execute();


    public static class PacketKey<T extends Packet> {

        private Class<T> clazz;
        private ResourceLocation id;

        public PacketKey(Class<T> clazz, String id) {
            this.clazz = clazz;
            this.id = new ResourceLocation(TeamsHUD.MODID, id);
        }

        public Class<T> getClazz() {
            return clazz;
        }

        public ResourceLocation getId() {
            return id;
        }
    }
}
