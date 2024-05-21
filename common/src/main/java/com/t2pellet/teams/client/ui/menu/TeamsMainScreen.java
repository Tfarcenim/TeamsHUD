package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.core.ModComponents;
import com.t2pellet.teams.network.server.C2STeamLeavePacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;

public class TeamsMainScreen extends TeamsScreen {

    static final int WIDTH = 267;
    static final int HEIGHT = 183;
    private static final ResourceLocation TEXTURE = new ResourceLocation(TeamsHUD.MODID, "textures/gui/screen_background.png");

    public TeamsMainScreen(Screen parent) {
        super(parent, ModComponents.TEAMS_MENU_TITLE);
    }

    @Override
    protected void init() {
        super.init();
        int yPos = y + 12;
        int xPos = x + (WIDTH - com.t2pellet.teams.client.ui.menu.TeammateEntry.WIDTH) / 2;
        // Add player buttons
        for (var teammate : ClientTeam.INSTANCE.getTeammates()) {
            boolean local = minecraft.player.getUUID().equals(teammate.id);
            var entry = new TeammateEntry(teammate, xPos, yPos, local);
            addRenderableWidget(entry);
            if (entry.getFavButton() != null) {
                addWidget(entry.getFavButton());
            }
            if (entry.getKickButton() != null) {
                addWidget(entry.getKickButton());
            }
            yPos += 24;
        }
        // Add menu buttons
        addRenderableWidget(Button.builder(ModComponents.LEAVE_TEXT,button -> {
            Services.PLATFORM.sendToServer(new C2STeamLeavePacket());
            minecraft.setScreen(new TeamsLonelyScreen(parent));
        }).bounds(this.width / 2  - 125, y + HEIGHT - 30, 80, 20).build());
        addRenderableWidget(Button.builder(ModComponents.INVITE_TEXT,button -> minecraft.setScreen(new TeamsInviteScreen(this)))
                .bounds(this.width / 2  - 40, y + HEIGHT - 30, 80, 20).build());
        addRenderableWidget(Button.builder(ModComponents.GO_BACK_TEXT, button -> minecraft.setScreen(parent)).bounds(this.width / 2  + 45, y + HEIGHT - 30, 80, 20).build());
    }

    @Override
    protected int getWidth() {
        return WIDTH;
    }

    @Override
    protected int getHeight() {
        return HEIGHT;
    }

    @Override
    protected ResourceLocation getBackgroundTexture() {
        return TEXTURE;
    }

    @Override
    protected float getBackgroundScale() {
        return 1.1F;
    }

    public void refresh() {
        if (!ClientTeam.INSTANCE.isInTeam()) {
            minecraft.setScreen(parent);
        } else {
            minecraft.setScreen(new TeamsMainScreen(parent));
        }
    }

}
