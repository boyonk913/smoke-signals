package com.boyonk.smokesignals.client;

import com.boyonk.smokesignals.SmokeSignals;
import com.boyonk.smokesignals.particle.ColoredSmokeParticleEffect;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.CampfireSmokeParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleFactory;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import org.jetbrains.annotations.Nullable;

public class SmokeSignalsClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
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
			particle.setColor(parameters.color().getX(), parameters.color().getY(), parameters.color().getZ());
			particle.setSprite(this.spriteProvider);
			return particle;
		}
	}
}