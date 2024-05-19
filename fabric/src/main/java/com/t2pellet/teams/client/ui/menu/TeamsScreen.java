package com.t2pellet.teams.client.ui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.teams.client.core.ClientTeam;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public abstract class TeamsScreen extends Screen {

    public final Screen parent;
    protected int x;
    protected int y;
    protected boolean inTeam;

    public TeamsScreen(Screen parent, Component title) {
        super(title);
        this.parent = parent;
        inTeam = ClientTeam.INSTANCE.isInTeam();
    }

    @Override
    protected void init() {
        x = (width - getWidth()) / 2;
        y = (height - getHeight()) / 2;
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        renderBackground(matrices);
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, getBackgroundTexture());
        matrices.pushPose();
        matrices.scale(getBackgroundScale(), getBackgroundScale(), getBackgroundScale());
        blit(matrices, (int) (x / getBackgroundScale()), (int) (y / getBackgroundScale()), 0, 0, (int) (getWidth() / getBackgroundScale()), (int) (getHeight() / getBackgroundScale()));
        matrices.popPose();
        super.render(matrices, mouseX, mouseY, delta);
    }

    protected abstract int getWidth();

    protected abstract int getHeight();

    protected abstract ResourceLocation getBackgroundTexture();

    protected abstract float getBackgroundScale();

}
