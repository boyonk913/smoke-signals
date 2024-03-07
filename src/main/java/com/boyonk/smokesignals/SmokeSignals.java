package com.boyonk.smokesignals;

import com.boyonk.smokesignals.particle.ColoredSmokeParticleEffect;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.internal.Streams;
import com.google.gson.stream.JsonReader;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleType;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.PathUtil;
import net.minecraft.util.Util;
import net.minecraft.util.dynamic.Codecs;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;

public class SmokeSignals implements ModInitializer {

	public static final String NAMESPACE = "smoke_signals";
	public static final Logger LOGGER = LoggerFactory.getLogger("Smoke Signals");
	public static final Gson GSON = (new GsonBuilder()).setPrettyPrinting().create();

	public static final Codec<Map<Block, ParticleEffect>> MAP_CODEC = Codecs.validate(Codec.unboundedMap(Registries.BLOCK.getCodec(), ParticleTypes.TYPE_CODEC), map -> map.isEmpty() ? DataResult.error(() -> "Map can't be empty!") : DataResult.success(map));
	private static Map<Block, ParticleEffect> BLOCK_TO_SMOKE = createDefaultMap();

	public static final ParticleType<ColoredSmokeParticleEffect> COLORED_CAMPFIRE_SMOKE = new ParticleType<>(true, ColoredSmokeParticleEffect.PARAMETERS_FACTORY) {
		@Override
		public Codec<ColoredSmokeParticleEffect> getCodec() {
			return ColoredSmokeParticleEffect.CODEC;
		}
	};


	@Override
	public void onInitialize() {
		Registry.register(Registries.PARTICLE_TYPE, new Identifier(NAMESPACE, "colored_campfire_smoke"), COLORED_CAMPFIRE_SMOKE);

		Path path = FabricLoader.getInstance().getConfigDir().resolve(NAMESPACE + ".json");

		if (Files.isRegularFile(path)) {
			try (JsonReader reader = new JsonReader(Files.newBufferedReader(path, StandardCharsets.UTF_8))) {
				reader.setLenient(false);
				JsonElement json = Streams.parse(reader);

				BLOCK_TO_SMOKE = Util.getResult(MAP_CODEC.parse(JsonOps.INSTANCE, json), JsonParseException::new);
			} catch (JsonParseException exception) {
				LOGGER.error("Couldn't parse config {}", path.getFileName(), exception);
			} catch (IOException exception) {
				LOGGER.error("Couldn't access config {}", path.getFileName(), exception);
			}
		} else {
			try {
				PathUtil.createDirectories(path.getParent());
				try (Writer writer = Files.newBufferedWriter(path, StandardCharsets.UTF_8)) {
					GSON.toJson(Util.getResult(MAP_CODEC.encodeStart(JsonOps.INSTANCE, BLOCK_TO_SMOKE), IOException::new), writer);
				}
			} catch (IOException exception) {
				LOGGER.error("Failed to save config {}", path, exception);
			}
		}
	}

	public static @Nullable ParticleEffect getSmoke(BlockState state) {
		return BLOCK_TO_SMOKE.get(state.getBlock());
	}

	private static Map<Block, ParticleEffect> createDefaultMap() {
		Map<Block, ParticleEffect> map = new LinkedHashMap<>();
		map.put(Blocks.HAY_BLOCK, ParticleTypes.CAMPFIRE_SIGNAL_SMOKE);
		map.put(Blocks.WHITE_WOOL, new ColoredSmokeParticleEffect(DyeColor.WHITE, 280));
		map.put(Blocks.ORANGE_WOOL, new ColoredSmokeParticleEffect(DyeColor.ORANGE, 280));
		map.put(Blocks.MAGENTA_WOOL, new ColoredSmokeParticleEffect(DyeColor.MAGENTA, 280));
		map.put(Blocks.LIGHT_BLUE_WOOL, new ColoredSmokeParticleEffect(DyeColor.LIGHT_BLUE, 280));
		map.put(Blocks.YELLOW_WOOL, new ColoredSmokeParticleEffect(DyeColor.YELLOW, 280));
		map.put(Blocks.LIME_WOOL, new ColoredSmokeParticleEffect(DyeColor.LIME, 280));
		map.put(Blocks.PINK_WOOL, new ColoredSmokeParticleEffect(DyeColor.PINK, 280));
		map.put(Blocks.GRAY_WOOL, new ColoredSmokeParticleEffect(DyeColor.GRAY, 280));
		map.put(Blocks.LIGHT_GRAY_WOOL, new ColoredSmokeParticleEffect(DyeColor.LIGHT_GRAY, 280));
		map.put(Blocks.CYAN_WOOL, new ColoredSmokeParticleEffect(DyeColor.CYAN, 280));
		map.put(Blocks.PURPLE_WOOL, new ColoredSmokeParticleEffect(DyeColor.PURPLE, 280));
		map.put(Blocks.BLUE_WOOL, new ColoredSmokeParticleEffect(DyeColor.BLUE, 280));
		map.put(Blocks.BROWN_WOOL, new ColoredSmokeParticleEffect(DyeColor.BROWN, 280));
		map.put(Blocks.GREEN_WOOL, new ColoredSmokeParticleEffect(DyeColor.GREEN, 280));
		map.put(Blocks.RED_WOOL, new ColoredSmokeParticleEffect(DyeColor.RED, 280));
		map.put(Blocks.BLACK_WOOL, new ColoredSmokeParticleEffect(DyeColor.BLACK, 280));

		return map;
	}

}
