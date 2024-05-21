package com.t2pellet.teams.client.ui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.client.ui.toast.ToastRequest;
import com.t2pellet.teams.network.server.C2STeamRequestPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;

public class TeamEntry extends GuiComponent implements Widget, GuiEventListener, NarratableEntry {

    static final int WIDTH = 244;
    static final int HEIGHT = 24;
    private static final ResourceLocation TEXTURE = new ResourceLocation(TeamsHUD.MODID, "textures/gui/screen_background.png");

    public final ImageButton joinButton;
    private Minecraft client;
    private String team;
    private int x;
    private int y;

    public TeamEntry(String team, int x, int y) {
        this.client = Minecraft.getInstance();
        this.team = team;
        this.x = x;
        this.y = y;
        this.joinButton = new ImageButton(x + WIDTH - 24, y + 8, 8, 8, 24, 190, TEXTURE, button -> {
            Services.PLATFORM.sendToServer(new C2STeamRequestPacket(team));
            client.getToasts().addToast(new ToastRequest(team));
            client.setScreen(null);
        });
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        // Background
        renderBackground(matrices);
        // Name
        client.font.draw(matrices, team, x + 8, y + 12 - (int) (client.font.lineHeight / 2), ChatFormatting.BLACK.getColor());
        // Buttons
        joinButton.render(matrices, mouseX, mouseY, delta);
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
}
