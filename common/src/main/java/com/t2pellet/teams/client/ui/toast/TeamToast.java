package com.t2pellet.teams.client.ui.toast;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.t2pellet.teams.platform.MultiloaderConfig;
import com.t2pellet.teams.platform.Services;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.toasts.Toast;
import net.minecraft.client.gui.components.toasts.ToastComponent;
import net.minecraft.client.renderer.GameRenderer;


public abstract class TeamToast implements Toast {

    public final String team;
    private boolean firstDraw = true;
    private long firstDrawTime;

    public TeamToast(String team) {
        this.team = team;
    }

    public abstract String title();

    public abstract String subTitle();

    @Override
    public Visibility render(PoseStack matrices, ToastComponent manager, long startTime) {
        if (firstDraw) {
            firstDrawTime = startTime;
            firstDraw = false;
        }

        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderTexture(0, TEXTURE);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        manager.blit(matrices, 0, 0, 0, 64, this.width(), this.height());
        manager.getMinecraft().font.draw(matrices, title(), 22, 7, ChatFormatting.WHITE.getColor());
        manager.getMinecraft().font.draw(matrices, subTitle(), 22, 18, -16777216);

        return startTime - firstDrawTime < Services.PLATFORM.getConfig().toastDuration() * 1000L && team != null ? Visibility.SHOW : Visibility.HIDE;    }
}
