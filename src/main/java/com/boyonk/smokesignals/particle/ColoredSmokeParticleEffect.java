package com.boyonk.smokesignals.particle;

import com.boyonk.smokesignals.SmokeSignals;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.particle.AbstractDustParticleEffect;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.util.DyeColor;
import net.minecraft.util.math.Vec3f;
import net.minecraft.util.registry.Registry;

import java.util.Locale;

public record ColoredSmokeParticleEffect(Vec3f color, int maxAge) implements ParticleEffect {

	public ColoredSmokeParticleEffect(DyeColor color, int maxAge) {
		this(new Vec3f(color.getColorComponents()[0], color.getColorComponents()[1], color.getColorComponents()[2]), maxAge);
	}


	public static final Codec<ColoredSmokeParticleEffect> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Vec3f.CODEC
							.fieldOf("color")
							.forGetter(ColoredSmokeParticleEffect::color),
					Codec.INT
							.fieldOf("max_age")
							.forGetter(ColoredSmokeParticleEffect::maxAge)
			).apply(instance, ColoredSmokeParticleEffect::new)
	);

	public static final Factory<ColoredSmokeParticleEffect> PARAMETERS_FACTORY = new Factory<>() {

		@Override
		public ColoredSmokeParticleEffect read(ParticleType<ColoredSmokeParticleEffect> type, StringReader reader) throws CommandSyntaxException {
			Vec3f color = AbstractDustParticleEffect.readColor(reader);
			reader.expect(' ');
			int maxAge = reader.readInt();

			return new ColoredSmokeParticleEffect(color, maxAge);
		}

		@Override
		public ColoredSmokeParticleEffect read(ParticleType<ColoredSmokeParticleEffect> type, PacketByteBuf buf) {
			return new ColoredSmokeParticleEffect(AbstractDustParticleEffect.readColor(buf), buf.readVarInt());
		}
	};

	@Override
	public ParticleType<?> getType() {
		return SmokeSignals.COLORED_CAMPFIRE_SMOKE;
	}

	@Override
	public void write(PacketByteBuf buf) {
		buf.writeFloat(this.color.getX());
		buf.writeFloat(this.color.getY());
		buf.writeFloat(this.color.getZ());
		buf.writeVarInt(this.maxAge);
	}

	@Override
	public String asString() {
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %s", Registry.PARTICLE_TYPE.getId(this.getType()), this.color.getX(), this.color.getY(), this.color.getZ(), this.maxAge);
	}


}
