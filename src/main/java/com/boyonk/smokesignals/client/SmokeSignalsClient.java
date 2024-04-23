package com.boyonk.smokesignals.client;

import com.boyonk.smokesignals.SmokeSignals;
import com.boyonk.smokesignals.network.s2c.play.SyncParticlesS2CPacket;
import com.boyonk.smokesignals.particle.ColoredSmokeParticleEffect;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayConnectionEvents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.particle.ParticleEffect;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class SmokeSignalsClient implements ClientModInitializer {

	private static @Nullable Map<Block, ParticleEffect> clientBlockToSmoke;

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(SyncParticlesS2CPacket.ID, (payload, context) -> {
			SmokeSignals.LOGGER.info("Received smoke data from server!");
			if (clientBlockToSmoke == null) clientBlockToSmoke = SmokeSignals.blockToSmoke;
			SmokeSignals.blockToSmoke = payload.map();
		});

		ClientPlayConnectionEvents.DISCONNECT.register((handler, client) -> {
			if (clientBlockToSmoke == null) return;
			SmokeSignals.blockToSmoke = clientBlockToSmoke;
			clientBlockToSmoke = null;
		});

		ParticleFactoryRegistry.getInstance().register(SmokeSignals.COLORED_CAMPFIRE_SMOKE, Factory::new);
	}

	public static class Factory implements ParticleFactory<ColoredSmokeParticleEffect> {

		private final SpriteProvider spriteProvider;

		public Factory(SpriteProvider spriteProvider) {
			this.spriteProvider = spriteProvider;
		}


		@Nullable
		@Override
		public Particle createParticle(ColoredSmokeParticleEffect parameters, ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
			CampfireSmokeParticle particle = new CampfireSmokeParticle(world, x, y, z, velocityX, velocityY, velocityZ, true);
			particle.setAlpha(0.95f);
			particle.setMaxAge(world.getRandom().nextInt(50) + parameters.maxAge());
			particle.setColor(parameters.color().x, parameters.color().y, parameters.color().z);
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}
}