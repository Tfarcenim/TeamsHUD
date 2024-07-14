package com.t2pellet.teams.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.LiteralMessage;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.t2pellet.teams.core.IHasTeam;
import com.t2pellet.teams.core.ModComponents;
import com.t2pellet.teams.core.ModTeam;
import com.t2pellet.teams.core.TeamDB;
import com.t2pellet.teams.network.client.S2CTeamInviteSentPacket;
import com.t2pellet.teams.platform.Services;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class TeamCommand {

    private TeamCommand() {
    }

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(literal("teams")
                .then(literal("create")
                        .then(argument("name", StringArgumentType.string())
                                .executes(TeamCommand::createTeam)))
                .then(literal("invite")
                        .then(argument("player", EntityArgument.player())
                                .executes(TeamCommand::invitePlayer)))
                .then(literal("leave")
                        .executes(TeamCommand::leaveTeam))
                .then(literal("kick")
                        .then(argument("player", EntityArgument.player())
                            .requires(source -> source.hasPermission(2))
                            .executes(TeamCommand::kickPlayer)))
                .then(literal("remove")
                        .then(argument("name", StringArgumentType.string())
                            .requires(source -> source.hasPermission(3))
                            .suggests(TeamSuggestions.TEAMS)
                            .executes(TeamCommand::removeTeam)))
                .then(literal("info")
                        .then(argument("name", StringArgumentType.string())
                                .suggests(TeamSuggestions.TEAMS)
                                .executes(TeamCommand::getTeamInfo)))
                .then(literal("list")
                        .executes(TeamCommand::listTeams)));

    }

    private static int createTeam(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String name = ctx.getArgument("name", String.class);
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        try {
            TeamDB.getOrMakeDefault(player.server).addTeam(name, player);
        } catch (ModTeam.TeamException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(e.getMessage())).create();
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int invitePlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        ServerPlayer newPlayer = EntityArgument.getPlayer(ctx, "player");
        ModTeam team = ((IHasTeam) player).getTeam();
        if (team == null) {
            throw new SimpleCommandExceptionType(ModComponents.translatable("teams.error.notinteam", player.getName().getString())).create();
        }
        try {
            get(ctx).invitePlayerToTeam(newPlayer, team);
            Services.PLATFORM.sendToClient(new S2CTeamInviteSentPacket(team.getName(), newPlayer.getName().getString()), player);
        } catch (ModTeam.TeamException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(e.getMessage())).create();
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int leaveTeam(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer player = ctx.getSource().getPlayerOrException();
        try {
            get(ctx).removePlayerFromTeam(player);
        } catch (ModTeam.TeamException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(e.getMessage())).create();
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int kickPlayer(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        ServerPlayer otherPlayer = EntityArgument.getPlayer(ctx, "player");
        try {
            get(ctx).removePlayerFromTeam(otherPlayer);
        } catch (ModTeam.TeamException e) {
            throw new SimpleCommandExceptionType(new LiteralMessage(e.getMessage())).create();
        }
        return Command.SINGLE_SUCCESS;
    }

    private static int removeTeam(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String name = ctx.getArgument("name", String.class);
        ModTeam team = TeamDB.getOrMakeDefault(ctx.getSource().getServer()).getTeam(name);
        if (team == null) {
            throw new SimpleCommandExceptionType(ModComponents.translatable("teams.error.invalidteam", name)).create();
        }
        get(ctx).removeTeam(team);
        ctx.getSource().sendSuccess(() -> ModComponents.translatable("teams.success.remove", name), false);
        return Command.SINGLE_SUCCESS;
    }

    private static int listTeams(CommandContext<CommandSourceStack> ctx) {
        ctx.getSource().sendSuccess(() -> ModComponents.translatable("teams.success.list"), false);
        get(ctx).getTeams().forEach(team -> ctx.getSource().sendSuccess(() -> ModComponents.literal(team.getName()), false));
        return Command.SINGLE_SUCCESS;
    }

    private static int getTeamInfo(CommandContext<CommandSourceStack> ctx) throws CommandSyntaxException {
        String name = ctx.getArgument("name", String.class);
        ModTeam team = get(ctx).getTeam(name);
        if (team == null) {
            throw new SimpleCommandExceptionType(ModComponents.translatable("teams.error.invalidteam", name)).create();
        }
        ctx.getSource().sendSuccess(() -> ModComponents.translatable("teams.success.info", name), false);
        team.getOnlinePlayers().forEach(player -> ctx.getSource().sendSuccess(player::getName, false));
        return Command.SINGLE_SUCCESS;
    }

    private static TeamDB get(CommandContext<CommandSourceStack> context) {
        return TeamDB.getOrMakeDefault(context.getSource().getServer());
    }

}
