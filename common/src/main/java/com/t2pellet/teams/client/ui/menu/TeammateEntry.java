package com.t2pellet.teams.client.ui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.core.ClientTeam;
import com.t2pellet.teams.network.server.C2STeamKickPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

import java.awt.*;

public class TeammateEntry extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {

    static final int WIDTH = 244;
    static final int HEIGHT = 24;
    private static final ResourceLocation TEXTURE = new ResourceLocation(TeamsHUD.MODID, "textures/gui/screen_background.png");

    private ImageButton kickButton;
    private TexturedToggleWidget favButton;
    private Minecraft client;
    private TeamsMainScreen parent;
    private ClientTeam.Teammate teammate;
    private int x;
    private int y;

    public TeammateEntry(TeamsMainScreen parent, ClientTeam.Teammate teammate, int x, int y, boolean local) {
        this.client = Minecraft.getInstance();
        this.parent = parent;
        this.teammate = teammate;
        this.x = x;
        this.y = y;
        if (!local) {
            this.favButton = new TexturedToggleWidget(x + WIDTH - 12, y + 8, 8, 8, 0, 190, TEXTURE, () -> {
                return ClientTeam.INSTANCE.isFavourite(teammate);
            }, button -> {
                if (ClientTeam.INSTANCE.isFavourite(teammate)) {
                    ClientTeam.INSTANCE.removeFavourite(teammate);
                } else {
                    ClientTeam.INSTANCE.addFavourite(teammate);
                }
            });
        }
        if (ClientTeam.INSTANCE.hasPermissions()) {
            this.kickButton = new ImageButton(x + WIDTH - 24, y + 8, 8, 8, 16, 190, TEXTURE, button -> {
                Services.PLATFORM.sendToServer(new C2STeamKickPacket(ClientTeam.INSTANCE.getName(), teammate.id));
                ClientTeam.INSTANCE.removePlayer(teammate.id);
            });
        }
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        // Background
        renderBackground(matrices);
        // Head
        float scale = 0.5F;
        matrices.pushPose();
        matrices.scale(scale, scale, scale);
        RenderSystem.setShaderTexture(0, teammate.skin);
        blit(matrices, (int) ((x + 4) / scale), (int) ((y + 4) / scale), 32, 32, 32, 32);
        matrices.popPose();
        // Nameplate
        client.font.draw(matrices, teammate.name, x + 24, y + 12 - (int) (client.font.lineHeight / 2), Color.BLACK.getRGB());
        // Buttons
        if (favButton != null) {
            favButton.render(matrices, mouseX, mouseY, delta);
        }
        if (kickButton != null) {
            kickButton.render(matrices, mouseX, mouseY, delta);
        }
    }

    private void renderBackground(PoseStack matrices) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        blit(matrices, x, y, 0, 166, WIDTH, HEIGHT);
    }


    @Override
    public NarrationPriority narrationPriority() {
        return NarrationPriority.FOCUSED;
    }

    @Override
    public void updateNarration(NarrationElementOutput builder) {
        // TODO : implement this
    }

    public ImageButton getKickButton() {
        return kickButton;
    }

    public TexturedToggleWidget getFavButton() {
        return favButton;
    }
}
