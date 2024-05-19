package com.t2pellet.teams.client.ui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.teams.TeamsHUDFabric;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.platform.Config;
import com.t2pellet.teams.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public class CompassOverlay extends GuiComponent {

    private static final int HUD_WIDTH = 182;
    private static final int HUD_HEIGHT = 5;

    private static final int MIN_DIST = 12;
    private static final int MAX_DIST = 128;
    private static final float MIN_SCALE = 0.2f;
    private static final float MAX_SCALE = 0.4f;
    private static final float MIN_ALPHA = 0.4f;

    public boolean enabled = true;
    private final Minecraft client;
    private boolean isShowing = false;

    public CompassOverlay() {
        this.client = Minecraft.getInstance();
    }

    public boolean isShowing() {
        return isShowing;
    }

    public void render(PoseStack matrices) {
        if (!Services.PLATFORM.getConfig().getConfigEntry(Config.enableCompassHUD).getAsBoolean() || !enabled) {
            isShowing = false;
            return;
        }

        // Render heads
        boolean renderedAnyHead = false;
        float minScale = 1.0F;
        for (var teammate : ClientTeam.INSTANCE.getTeammates()) {
            if (client.player.getUUID().equals(teammate.id)) continue;
            Player player = client.level.getPlayerByUUID(teammate.id);
            if (player != null) {
                double rotationHead = caculateRotationHead();
                float scaleFactor = calculateScaleFactor(player);
                if (scaleFactor < minScale) minScale = scaleFactor;
                double renderFactor = calculateRenderFactor(player, rotationHead);
                renderHUDHead(matrices, teammate.skin, scaleFactor, renderFactor);
                renderedAnyHead = true;
            }
        }

        // Render bar
        if (ClientTeam.INSTANCE.isInTeam() && !ClientTeam.INSTANCE.isTeamEmpty() && renderedAnyHead) {
            RenderSystem.setShaderTexture(0, GUI_ICONS_LOCATION);
            var x = (client.getWindow().getGuiScaledWidth() - HUD_WIDTH) / 2;
            var y = 5 + HUD_HEIGHT / 2;
            float alpha = (1 - minScale) * (1 - MIN_ALPHA) + MIN_ALPHA;
            RenderSystem.enableBlend();
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alpha);
            blit(matrices, x, y, 0, 74, HUD_WIDTH, HUD_HEIGHT);
            RenderSystem.disableBlend();
            isShowing = true;
        } else {
            isShowing = false;
        }
    }

    private double caculateRotationHead() {
        double rotationHead = client.player.getYHeadRot() % 360;
        if (rotationHead > 180) {
            rotationHead = rotationHead - 360;
        } else if (rotationHead < -180) {
            rotationHead = 360 + rotationHead;
        }
        return rotationHead;
    }

    private float calculateScaleFactor(Player player) {
        double diffPosX = player.position().x - client.player.position().x;
        double diffPosZ = player.position().z - client.player.position().z;
        double magnitude =  Math.sqrt(diffPosX * diffPosX + diffPosZ * diffPosZ);

        if (magnitude >= MAX_DIST) {
            return 1;
        } else if (magnitude <= MIN_DIST) {
            return 0;
        } else {
            return (float) ((magnitude - MIN_DIST) / (MAX_DIST - MIN_DIST));
        }
    }

    private double calculateRenderFactor(Player player, double rotationHead) {
        double diffPosX = player.position().x - client.player.position().x;
        double diffPosZ = player.position().z - client.player.position().z;
        double magnitude = Math.sqrt(diffPosX * diffPosX + diffPosZ * diffPosZ);
        diffPosX /= magnitude;
        diffPosZ /= magnitude;
        double angle = Math.atan(diffPosZ / diffPosX) * 180 / Math.PI + 90;
        if (diffPosX >= 0) {
            angle -= 180;
        }
        double renderFactor = (angle - rotationHead) / 180;
        if (renderFactor > 1) {
            renderFactor = renderFactor - 2;
        }
        if (renderFactor < -1) {
            renderFactor = 2 + renderFactor;
        }
        return renderFactor;
    }

    private void renderHUDHead(PoseStack matrices, ResourceLocation skin, float scaleFactor, double renderFactor) {
        RenderSystem.setShaderTexture(0, skin);
        int scaledWidth = client.getWindow().getGuiScaledWidth();
        int x = (int) (scaledWidth / 2 - HUD_WIDTH / 4 + renderFactor * HUD_WIDTH / 2 + 41);
        int y = 5 + HUD_HEIGHT + 4;
        float sizeFactor = scaleFactor * (MAX_SCALE - MIN_SCALE) + MIN_SCALE;
        float alphaFactor = (1 - scaleFactor) * (1 - MIN_ALPHA) + MIN_ALPHA;
        matrices.pushPose();
        RenderSystem.enableBlend();
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, alphaFactor);
        matrices.scale(sizeFactor, sizeFactor, sizeFactor);
        if (1 - Math.abs(renderFactor) < Math.min(alphaFactor, 0.6f)) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, (float) (1 - Math.abs(renderFactor)));
            blit(matrices, Math.round(x / sizeFactor), Math.round(y / sizeFactor), 32, 32, 32, 32);
        } else {
            blit(matrices, Math.round(x / sizeFactor), Math.round(y / sizeFactor), 32, 32, 32, 32);
        }
        RenderSystem.disableBlend();
        matrices.popPose();
    }



}
