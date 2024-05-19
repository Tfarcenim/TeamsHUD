package com.t2pellet.teams.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.client.ui.toast.ToastInvited;
import com.t2pellet.teams.client.ui.toast.ToastRequested;
import com.t2pellet.teams.network.server.C2STeamJoinPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.Toast;
import org.lwjgl.glfw.GLFW;

import java.util.Optional;

public class TeamsKeys {

    public static class TeamsKey {
        @FunctionalInterface
        public interface OnPress {
            void execute(Minecraft client);
        }

        private TeamsKey(String keyName, int keyBind, OnPress action) {
            keyBinding = new KeyMapping(
                    keyName,
                    InputConstants.Type.KEYSYM,
                    keyBind,
                    "key.category.teams"
            );
            onPress = action;
        }

        public void register() {
            Services.PLATFORM.registerKeyBinding(Optional.of(keyBinding));
        }

        public String getLocalizedName() {
            return keyBinding.getTranslatedKeyMessage().getContents();
        }

        final KeyMapping keyBinding;
        final OnPress onPress;
    }

    public static final TeamsKey ACCEPT = new TeamsKey("key.teams.accept", GLFW.GLFW_KEY_RIGHT_BRACKET, client -> {
        var toastManager = client.getToasts();
        ToastInvited invited = toastManager.getToast(ToastInvited.class, Toast.NO_TOKEN);
        if (invited != null) {
            invited.respond();
            Services.PLATFORM.sendToServer(new C2STeamJoinPacket(invited.team));
        } else {
            ToastRequested requested = toastManager.getToast(ToastRequested.class, Toast.NO_TOKEN);
            if (requested != null) {
                requested.respond();
                Services.PLATFORM.sendToServer(new C2STeamJoinPacket(ClientTeam.INSTANCE.getName()));//todo?
            }
        }
    });

    public static final TeamsKey REJECT = new TeamsKey("key.teams.reject", GLFW.GLFW_KEY_LEFT_BRACKET, client -> {
        var toastManager = client.getToasts();
        ToastInvited toast = toastManager.getToast(ToastInvited.class, Toast.NO_TOKEN);
        if (toast != null) {
            toast.respond();
        } else {
            ToastRequested requested = toastManager.getToast(ToastRequested.class, Toast.NO_TOKEN);
            if (requested != null) {
                requested.respond();
            }
        }
    });

    public static final TeamsKey TOGGLE_HUD = new TeamsKey("key.teams.toggle_hud", GLFW.GLFW_KEY_B, client -> {
        TeamsHUDClient.compass.enabled = !TeamsHUDClient.compass.enabled;
        TeamsHUDClient.status.enabled = !TeamsHUDClient.status.enabled;
    });

    static final TeamsKey[] KEYS = {
            ACCEPT,
            REJECT,
            TOGGLE_HUD
    };

}
