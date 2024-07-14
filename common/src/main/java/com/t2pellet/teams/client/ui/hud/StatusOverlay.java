package com.t2pellet.teams.client.ui.hud;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.core.ModComponents;
import com.t2pellet.teams.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.List;

public class StatusOverlay {

    private static final ResourceLocation ICONS = TeamsHUD.id("textures/gui/hudicons.png");

    public boolean enabled = true;
    private final Minecraft client;
    private int offsetY = 0;

    public StatusOverlay() {
        this.client = Minecraft.getInstance();
    }

    public void render(GuiGraphics graphics) {
        offsetY = 0;
        List<ClientTeam.Teammate> teammates = ClientTeam.INSTANCE.getTeammates();
        int shown = 0;
        for (int i = 0; i < teammates.size() && shown < 4; ++i) {
            if (client.player.getUUID().equals(teammates.get(i).id)) {
                continue;
            }
            renderStatus(graphics, teammates.get(i));
            ++shown;
        }
    }

    private void renderStatus(GuiGraphics graphics, ClientTeam.Teammate teammate) {
        if (!Services.PLATFORM.getConfig().enableStatusHUD() || !enabled) return;

        // Dont render dead players
        if (teammate.getHealth() <= 0) return;
        
        int posX = (int) Math.round(client.getWindow().getGuiScaledWidth() * 0.003);
        int posY = client.getWindow().getGuiScaledHeight() / 4 - 5 + offsetY;

        // Health
        String health = String.valueOf(Math.round(teammate.getHealth()));
        graphics.blit(ICONS,posX + 20, posY, 0, 0, 9, 9);
        graphics.drawString(client.font, ModComponents.literal(health), posX + 32, posY, ChatFormatting.WHITE.getColor());

        // Hunger
        if (Services.PLATFORM.getConfig().showHunger()) {
            String hunger = String.valueOf(teammate.getHunger());
            graphics.blit(ICONS, posX + 46, posY, 9, 0, 9, 9);
            graphics.drawString(client.font, ModComponents.literal(hunger), posX + 58, posY, ChatFormatting.WHITE.getColor());
        }

        // Draw skin
        graphics.pose().pushPose();
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        graphics.blit(teammate.skin, posX + 4, client.getWindow().getGuiScaledHeight() / 2 - 34 + 2 * offsetY, 32, 32, 32, 32);
        graphics.pose().popPose();

        // Draw name
        graphics.drawString( client.font, Component.literal(teammate.name), (int) Math.round(client.getWindow().getGuiScaledWidth() * 0.002) + 20, posY - 15, ChatFormatting.WHITE.getColor());

        // Update count & offset
        offsetY += 46;
    }

}
