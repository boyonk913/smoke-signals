package com.boyonk.smokesignals.particle;

import com.boyonk.smokesignals.SmokeSignals;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

public record ColoredSmokeParticleEffect(Vector3f color, int maxAge) implements ParticleEffect {

	public ColoredSmokeParticleEffect(DyeColor color, int maxAge) {
		this(Vec3d.unpackRgb(color.getEntityColor()).toVector3f(), maxAge);
	}


	public static final MapCodec<ColoredSmokeParticleEffect> CODEC = RecordCodecBuilder.mapCodec(instance ->
			instance.group(
					Codecs.VECTOR_3F
							.fieldOf("color")
							.forGetter(ColoredSmokeParticleEffect::color),
					Codec.INT
							.fieldOf("max_age")
							.forGetter(ColoredSmokeParticleEffect::maxAge)
			).apply(instance, ColoredSmokeParticleEffect::new)
	);

	public static final PacketCodec<RegistryByteBuf, ColoredSmokeParticleEffect> PACKET_CODEC = PacketCodec.tuple(
			PacketCodecs.VECTOR3F, ColoredSmokeParticleEffect::color,
			PacketCodecs.INTEGER, ColoredSmokeParticleEffect::maxAge,
			ColoredSmokeParticleEffect::new
	);

	@Override
	public ParticleType<?> getType() {
		return SmokeSignals.COLORED_CAMPFIRE_SMOKE;
	}


}
