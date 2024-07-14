package com.t2pellet.teams.mixin;

import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.t2pellet.teams.core.ModTeam;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.MessageArgument;
import net.minecraft.network.chat.PlayerChatMessage;
import net.minecraft.server.commands.TeamMsgCommand;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.scores.PlayerTeam;
import net.minecraft.world.scores.Team;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

@Mixin(TeamMsgCommand.class)
public abstract class TeamMessageCommandMixin {

    @Shadow private static void sendMessage(CommandSourceStack pSource, Entity pSender, PlayerTeam pTeam, List<ServerPlayer> pTeamMembers, PlayerChatMessage pChatMessage) {
        throw new AssertionError();
    }

    //private static synthetic lambda$register$2(Lcom/mojang/brigadier/context/CommandContext;)I throws com/mojang/brigadier/exceptions/CommandSyntaxException
    @Inject(method = "*(Lcom/mojang/brigadier/context/CommandContext;)I",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/Entity;getTeam()Lnet/minecraft/world/scores/Team;"),
            locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private static void fixMsgCommand(CommandContext<CommandSourceStack> ctx, CallbackInfoReturnable<Integer> cir, CommandSourceStack commandSource, Entity entity) throws CommandSyntaxException {
        Team team = entity.getTeam();
        if (team instanceof ModTeam modTeam) {
            List<ServerPlayer> list = commandSource.getServer().getPlayerList().getPlayers().stream().filter((p_288679_) -> p_288679_ == entity || p_288679_.getTeam() == modTeam).toList();
            if (!list.isEmpty()) {
                MessageArgument.resolveChatMessage(ctx, "message", (p_248180_) -> {
                    sendMessage(commandSource, entity, modTeam.getScoreboardTeam(), list, p_248180_);
                });
            }
            cir.setReturnValue(list.size());
        }
    }
}
