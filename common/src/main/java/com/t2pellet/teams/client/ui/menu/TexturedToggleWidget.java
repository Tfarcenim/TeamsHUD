package com.t2pellet.teams.client.ui.menu;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;

public class TexturedToggleWidget extends Button {

    private final ResourceLocation texture;
    private final int u;
    private final int v;
    private final int hoveredVOffset;
    private final int textureWidth;
    private final int textureHeight;
    private final ToggleCondition condition;

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, ResourceLocation texture, ToggleCondition condition, OnPress pressAction) {
        this(x, y, width, height, u, v, height, texture, 256, 256, condition, pressAction);
    }

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, ToggleCondition condition, OnPress pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, 256, 256, condition, pressAction);
    }

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, ToggleCondition condition, OnPress pressAction) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, condition, pressAction, TextComponent.EMPTY);
    }

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, ToggleCondition condition, OnPress pressAction, Component text) {
        this(x, y, width, height, u, v, hoveredVOffset, texture, textureWidth, textureHeight, condition, pressAction, NO_TOOLTIP, text);
    }

    public TexturedToggleWidget(int x, int y, int width, int height, int u, int v, int hoveredVOffset, ResourceLocation texture, int textureWidth, int textureHeight, ToggleCondition condition, OnPress pressAction, OnTooltip tooltipSupplier, Component text) {
        super(x, y, width, height, text, pressAction, tooltipSupplier);
        this.condition = condition;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.hoveredVOffset = hoveredVOffset;
        this.texture = texture;
    }

    public void renderButton(PoseStack matrices, int mouseX, int mouseY, float delta) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, this.texture);
        int i = this.v;
        if (this.isHoveredOrFocused()) {
            i += this.hoveredVOffset;
        }
        int j = this.u;
        if (condition.isOn()) {
            j += this.width;
        }

        RenderSystem.enableDepthTest();
        blit(matrices, this.x, this.y, (float)j, (float)i, this.width, this.height, this.textureWidth, this.textureHeight);
        if (this.isHovered) {
            this.renderToolTip(matrices, mouseX, mouseY);
        }
    }

    @FunctionalInterface
    public interface ToggleCondition {
        boolean isOn();
    }
}
