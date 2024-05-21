package com.t2pellet.teams.client.ui.menu;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.ImageButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class TexturedToggleWidget extends ImageButton {


    private final Supplier<Boolean> booleanSupplier;

    public TexturedToggleWidget(int pX, int pY, int pWidth, int pHeight, int u, int v, ResourceLocation pResourceLocation, Button.OnPress pOnPress, Supplier<Boolean> booleanSupplier) {
        this(pX, pY, pWidth, pHeight, u, v, pHeight, pResourceLocation, 256, 256, pOnPress,booleanSupplier);
    }

    public TexturedToggleWidget(int pX, int pY, int pWidth, int pHeight, int pXTexStart, int pYTexStart, int pYDiffTex, ResourceLocation pResourceLocation, Button.OnPress pOnPress,Supplier<Boolean> supplier) {
        this(pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, 256, 256, pOnPress,supplier);
    }

    public TexturedToggleWidget(int pX, int pY, int pWidth, int pHeight, int pXTexStart, int pYTexStart, int pYDiffTex, ResourceLocation pResourceLocation, int pTextureWidth, int pTextureHeight, Button.OnPress pOnPress,Supplier<Boolean> supplier) {
        this(pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, pTextureWidth, pTextureHeight, pOnPress, CommonComponents.EMPTY,supplier);
    }

    public TexturedToggleWidget(int pX, int pY, int pWidth, int pHeight, int pXTexStart, int pYTexStart, int pYDiffTex, ResourceLocation pResourceLocation, int pTextureWidth, int pTextureHeight, Button.OnPress pOnPress, Component pMessage,Supplier<Boolean> supplier) {
        super(pX, pY, pWidth, pHeight, pXTexStart, pYTexStart, pYDiffTex, pResourceLocation, pTextureWidth, pTextureHeight, pOnPress, pMessage);
        this.booleanSupplier = supplier;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        int j = xTexStart;
        if (booleanSupplier.get()) {
            j+=width;
        }
            this.renderTexture(pGuiGraphics, this.resourceLocation, this.getX(), this.getY(), this.xTexStart, this.yTexStart,
                    this.yDiffTex, j, this.height, this.textureWidth, this.textureHeight);
    }
}
