package com.t2pellet.teams.client.ui.hud;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.platform.Config;
import com.t2pellet.teams.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;
import java.util.List;

public class StatusOverlay extends GuiComponent {

    private static final ResourceLocation ICONS = new ResourceLocation(TeamsHUD.MODID, "textures/gui/hudicons.png");

    public boolean enabled = true;
    private final Minecraft client;
    private int offsetY = 0;

    public StatusOverlay() {
        this.client = Minecraft.getInstance();
    }

    public void render(PoseStack matrices) {
        offsetY = 0;
        List<ClientTeam.Teammate> teammates = ClientTeam.INSTANCE.getTeammates();
        int shown = 0;
        for (int i = 0; i < teammates.size() && shown < 4; ++i) {
            if (client.player.getUUID().equals(teammates.get(i).id)) {
                continue;
            }
            renderStatus(matrices, teammates.get(i));
            ++shown;
        }
    }

    private void renderStatus(PoseStack matrices, ClientTeam.Teammate teammate) {
        if (!Services.PLATFORM.getConfig().getConfigEntry(Config.enableStatusHUD).getAsBoolean() || !enabled) return;

        // Dont render dead players
        if (teammate.getHealth() <= 0) return;
        
        int posX = (int) Math.round(client.getWindow().getGuiScaledWidth() * 0.003);
        int posY = client.getWindow().getGuiScaledHeight() / 4 - 5 + offsetY;

        // Health
        String health = String.valueOf(Math.round(teammate.getHealth()));
        RenderSystem.setShaderTexture(0, ICONS);
        blit(matrices, posX + 20, posY, 0, 0, 9, 9);
        drawString(matrices, client.font, new TextComponent(health), posX + 32, posY, Color.WHITE.getRGB());

        // Hunger
        String hunger = String.valueOf(teammate.getHunger());
        RenderSystem.setShaderTexture(0, ICONS);
        blit(matrices, posX + 46, posY, 9, 0, 9, 9);
        drawString(matrices, client.font, new TextComponent(hunger), posX + 58, posY, Color.WHITE.getRGB());

        // Draw skin
        RenderSystem.setShaderTexture(0, teammate.skin);
        matrices.pushPose();
        matrices.scale(0.5F, 0.5F, 0.5F);
        blit(matrices, posX + 4, client.getWindow().getGuiScaledHeight() / 2 - 34 + 2 * offsetY, 32, 32, 32, 32);
        matrices.popPose();

        // Draw name
        drawString(matrices, client.font, new TextComponent(teammate.name), (int) Math.round(client.getWindow().getGuiScaledWidth() * 0.002) + 20, posY - 15, Color.WHITE.getRGB());

        // Update count & offset
        offsetY += 46;
    }

}
