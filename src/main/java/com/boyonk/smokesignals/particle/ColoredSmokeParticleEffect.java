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
import net.minecraft.registry.Registries;
import net.minecraft.util.DyeColor;
import net.minecraft.util.dynamic.Codecs;
import org.joml.Vector3f;

import java.util.Locale;

public record ColoredSmokeParticleEffect(Vector3f color, int maxAge) implements ParticleEffect {

	public ColoredSmokeParticleEffect(DyeColor color, int maxAge) {
		this(new Vector3f(color.getColorComponents()[0],color.getColorComponents()[1],color.getColorComponents()[2]), maxAge);
	}



	public static final Codec<ColoredSmokeParticleEffect> CODEC = RecordCodecBuilder.create(instance ->
			instance.group(
					Codecs.VECTOR_3F
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
			Vector3f color = AbstractDustParticleEffect.readColor(reader);
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
		buf.writeFloat(this.color.x());
		buf.writeFloat(this.color.y());
		buf.writeFloat(this.color.z());
		buf.writeVarInt(this.maxAge);
	}

	@Override
	public String asString() {
		return String.format(Locale.ROOT, "%s %.2f %.2f %.2f %s", Registries.PARTICLE_TYPE.getId(this.getType()), this.color.x(), this.color.y(), this.color.z(), this.maxAge);
	}


}
