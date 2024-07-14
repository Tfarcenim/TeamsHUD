package com.t2pellet.teams.mixin;

import com.mojang.authlib.GameProfile;
import com.t2pellet.teams.TeamsHUD;
import com.t2pellet.teams.core.IHasTeam;
import com.t2pellet.teams.core.Team;
import com.t2pellet.teams.core.TeamDB;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.ServerPlayerGameMode;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.ProfilePublicKey;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player implements IHasTeam {
	@Shadow @Final public ServerPlayerGameMode gameMode;

	@Shadow public abstract ServerLevel serverLevel();

	@Unique
	private Team team;

	public ServerPlayerMixin(Level $$0, BlockPos $$1, float $$2, GameProfile $$3) {
		super($$0, $$1, $$2, $$3);
	}


	@Override
	public boolean hasTeam() {
		return team != null;
	}

	@Override
	public Team getTeam() {
		return team;
	}

	@Override
	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public boolean isTeammate(ServerPlayer other) {
		return team.equals(((IHasTeam) other).getTeam());
	}

	@Inject(at = @At(value = "TAIL"), method = "addAdditionalSaveData")
	private void writeCustomDataToNbt(CompoundTag nbt, CallbackInfo info) {
		if (team != null) {
			nbt.putString("playerTeam", team.getName());
		}
	}

	@Inject(at = @At(value = "TAIL"), method = "readAdditionalSaveData")
	private void readCustomDataFromNbt(CompoundTag nbt, CallbackInfo info) {
		if (team == null && nbt.contains("playerTeam")) {
			team = TeamDB.getOrMakeDefault(this.serverLevel().getServer()).getTeam(nbt.getString("playerTeam"));
			if (team == null || !team.hasPlayer(getUUID())) {
				team = null;
			}
		}
	}

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/server/level/ServerPlayer;getHealth()F",ordinal = 1), method = "doTick")
	private void playerTick(CallbackInfo info) {
		var player = (ServerPlayer) ((Object) this);
		TeamsHUD.onPlayerHealthUpdate(player,player.getHealth(),player.getFoodData().getFoodLevel());
	}

	@Override
	public boolean isSpectator() {
		return this.gameMode.getGameModeForPlayer() == GameType.SPECTATOR;
	}

	@Override
	public boolean isCreative() {
		return this.gameMode.getGameModeForPlayer() == GameType.CREATIVE;
	}
}
