package net.ghostyy.mixin;

import net.fabricmc.loader.impl.lib.sat4j.core.Vec;
import net.ghostyy.effect.SkylandsEffects;
import net.ghostyy.world.SkylandsWorlds;
import net.minecraft.block.Blocks;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends Entity {

    public PlayerEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    private static final RegistryKey<World> TARGET_DIMENSION = SkylandsWorlds.Dim.SKYLANDS;

    @Inject(method = "tickMovement", at = @At("TAIL"))
    private void tickMovement(CallbackInfo info) {
        if (getEntityWorld() instanceof ServerWorld) {
            RegistryKey<World> registryKey = getEntityWorld().getRegistryKey() == TARGET_DIMENSION ? World.OVERWORLD : TARGET_DIMENSION;
            ServerWorld serverWorld = ((ServerWorld) getEntityWorld()).getServer().getWorld(registryKey);

            if (getBlockPos() != null) {
                assert serverWorld != null;
                if ((serverWorld.getRegistryKey() == TARGET_DIMENSION ? getY() >= 322.5 : getY() <= -32)) {
                    teleportToDimension(serverWorld, this);
                    if (serverWorld.getRegistryKey() == TARGET_DIMENSION) {
                    }
                }
            }
        }
    }

    private void createSpawnPlatform(ServerWorld world) {
        BlockPos blockPos = new BlockPos((int) getX(), 32, (int) getZ());
        int i = blockPos.getX();
        int j = blockPos.getY() - 2;
        int k = blockPos.getZ();
        BlockPos.iterate(i - 2, j + 1, k - 2, i + 2, j + 3, k + 2).forEach(pos -> world.setBlockState(pos, Blocks.AIR.getDefaultState()));
        BlockPos.iterate(i - 2, j, k - 2, i + 2, j, k + 2).forEach(pos -> world.setBlockState(pos, Blocks.COBBLESTONE.getDefaultState()));
    }

    private void teleportToDimension(ServerWorld world, Entity player) {
        int teleportY = world.getRegistryKey() == World.OVERWORLD ? 320 : 32;
        double teleportVelY = world.getRegistryKey() == World.OVERWORLD ? player.getVelocity().y : .5;
        if (!world.isClient()) {
            MinecraftServer server = world.getServer();
            if (server != null) {
                if (player instanceof ServerPlayerEntity) {
                    ServerPlayerEntity serverPlayer = (ServerPlayerEntity) player;
                    if (!serverPlayer.isGliding() && !serverPlayer.isSpectator()) {
                        serverPlayer.addStatusEffect(new StatusEffectInstance(SkylandsEffects.PREVENT_FALL_DAMAGE, 200, 0, true, false, false));
                        serverPlayer.teleportTo(new TeleportTarget(world, new Vec3d(getX(), teleportY, getZ()), new Vec3d(player.getVelocity().x, teleportVelY, player.getVelocity().z), getYaw(), getPitch(), TeleportTarget.NO_OP));
                        if (world.getRegistryKey() == TARGET_DIMENSION) {
                            createSpawnPlatform(world);
                        }
                    }
                }
            }
        }
    }
}