package com.t2pellet.teams.client.ui.menu;

import com.t2pellet.teams.TeamsHUD;
import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.teams.client.core.ClientTeamDB;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class TeamsLonelyScreen extends TeamsScreen {

    static final int WIDTH = 256;
    static final int HEIGHT = 166;
    private static final ResourceLocation TEXTURE = new ResourceLocation(TeamsHUD.MODID, "textures/gui/screen_background.png");
    private static final Component CREATE_TEXT = new TranslatableComponent("teams.menu.create");
    private static final Component LONELY_TEXT = new TranslatableComponent("teams.menu.lonely.alone");
    private static final Component GO_BACK_TEXT = new TranslatableComponent("teams.menu.return");

    public TeamsLonelyScreen(Screen parent) {
        super(parent, new TranslatableComponent("teams.menu.lonely.title"));
    }

    @Override
    protected void init() {
        super.init();
        // Team Entries
        int yPos = y + 12;
        for (String team : ClientTeamDB.INSTANCE.getOnlineTeams()) {
            var entry = new TeamEntry(team, this.width / 2 - 122, yPos);
            addRenderableWidget(entry);
            addWidget(entry.joinButton);
            yPos += 24;
        }
        // Menu buttons
        addRenderableWidget(new Button(this.width / 2 - 106, y + HEIGHT - 30, 100, 20, CREATE_TEXT, button -> {
            minecraft.setScreen(new TeamsCreateScreen(this));
        }));
        addRenderableWidget(new Button(this.width / 2 + 6, y + HEIGHT - 30, 100, 20, GO_BACK_TEXT, button -> {
            minecraft.setScreen(parent);
        }));
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        super.render(matrices, mouseX, mouseY, delta);
        if (ClientTeamDB.INSTANCE.getOnlineTeams().isEmpty()) {
            int textWidth = font.width(LONELY_TEXT);
            int textHeight   = font.lineHeight;
            font.draw(matrices, LONELY_TEXT, (int) ((this.width - textWidth) / 2), y + 24 - (int) (textHeight / 2), Color.BLACK.getRGB());
        }
    }

    public void refresh() {
        minecraft.setScreen(new TeamsLonelyScreen(parent));
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
        return 1.0F;
    }
}
