package com.gildedgames.aether.level.gen;

import com.gildedgames.aether.level.biome.AetherBiomes;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.gen.BiomeSource;
import net.minecraft.util.maths.Vec2i;

import java.util.*;

public class AetherBiomeSource extends BiomeSource {

    private final Biome biome;
    private final double temperature;

    public AetherBiomeSource(double temperature) {
        biome = AetherBiomes.AETHER;
        this.temperature = temperature; 
    }

    @Override
    public Biome getBiomeInChunk(Vec2i pos) {
        return biome;
    }

    @Override
    public Biome getBiome(int x, int z) {
        return biome;
    }

    @Override
    public double getTemperature(int x, int z) {
        return temperature;
    }

    @Override
    public double[] getTemperatures(double[] temperatures, int x, int z, int xSize, int zSize) {
        if (temperatures == null || temperatures.length < xSize * zSize)
            temperatures = new double[xSize * zSize];
        Arrays.fill(temperatures, 0, xSize * zSize, temperature);
        return temperatures;
    }

    @Override
    public Biome[] getBiomes(Biome[] biomes, int x, int z, int xSize, int zSize) {
        if (biomes == null || biomes.length < xSize * zSize)
            biomes = new Biome[xSize * zSize];
        if (temperatureNoises == null || temperatureNoises.length < xSize * zSize) {
            temperatureNoises = new double[xSize * zSize];
            rainfallNoises = new double[xSize * zSize];
        }
        Arrays.fill(biomes, 0, xSize * zSize, biome);
        return biomes;
    }
}
