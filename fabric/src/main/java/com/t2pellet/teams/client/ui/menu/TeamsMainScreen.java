package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.PacketHandler;
import com.t2pellet.teams.network.packets.TeamLeavePacket;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class TeamsMainScreen extends TeamsScreen {

    static final int WIDTH = 267;
    static final int HEIGHT = 183;
    private static final ResourceLocation TEXTURE = new ResourceLocation(TeamsHUD.MODID, "textures/gui/screen_background.png");
    private static final Component INVITE_TEXT = new TranslatableComponent("teams.menu.invite");
    private static final Component LEAVE_TEXT = new TranslatableComponent("teams.menu.leave");
    private static final Component GO_BACK_TEXT = new TranslatableComponent("teams.menu.return");

    public TeamsMainScreen(Screen parent) {
        super(parent, new TranslatableComponent("teams.menu.title"));
    }

    @Override
    protected void init() {
        super.init();
        int yPos = y + 12;
        int xPos = x + (WIDTH - com.t2pellet.teams.client.ui.menu.TeammateEntry.WIDTH) / 2;
        // Add player buttons
        for (var teammate : ClientTeam.INSTANCE.getTeammates()) {
            boolean local = minecraft.player.getUUID().equals(teammate.id);
            var entry = new TeammateEntry(this, teammate, xPos, yPos, local);
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
        addRenderableWidget(new Button(this.width / 2  - 125, y + HEIGHT - 30, 80, 20, LEAVE_TEXT, button -> {
            PacketHandler.INSTANCE.sendToServer(new TeamLeavePacket(minecraft.player.getUUID()));
            minecraft.setScreen(new TeamsLonelyScreen(parent));
        }));
        addRenderableWidget(new Button(this.width / 2  - 40, y + HEIGHT - 30, 80, 20, INVITE_TEXT, button -> {
            minecraft.setScreen(new TeamsInviteScreen(this));
        }));
        addRenderableWidget(new Button(this.width / 2  + 45, y + HEIGHT - 30, 80, 20, GO_BACK_TEXT, button -> {
            minecraft.setScreen(parent);
        }));
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
