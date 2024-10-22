package com.boyonk.smokesignals.network.s2c.play;

import com.boyonk.smokesignals.SmokeSignals;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import net.minecraft.block.Block;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

import java.util.Map;

public record SyncParticlesS2CPacket(Map<Block, ParticleEffect> map) implements CustomPayload {

	public static final Id<SyncParticlesS2CPacket> ID = new Id<>(Identifier.of(SmokeSignals.NAMESPACE, "sync_particles"));
	private static final PacketCodec<RegistryByteBuf, Map<Block, ParticleEffect>> MAP_PACKET_CODEC = PacketCodecs.map(Object2ObjectArrayMap::new, PacketCodecs.registryValue(RegistryKeys.BLOCK), ParticleTypes.PACKET_CODEC);
	public static final PacketCodec<RegistryByteBuf, SyncParticlesS2CPacket> CODEC = MAP_PACKET_CODEC.xmap(SyncParticlesS2CPacket::new, SyncParticlesS2CPacket::map);

	@Override
	public Id<? extends CustomPayload> getId() {
		return ID;
	}
}
