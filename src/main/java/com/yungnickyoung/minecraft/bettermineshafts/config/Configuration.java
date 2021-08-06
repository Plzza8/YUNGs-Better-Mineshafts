package com.yungnickyoung.minecraft.bettermineshafts.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;

@Config(name = "bettermineshafts-fabric-1_17")
public class Configuration implements ConfigData {
    public double mineshaftSpawnRate = .003;

    @ConfigEntry.Gui.Tooltip(count = 2)
    public int minY = 17;

    @ConfigEntry.Gui.Tooltip
    public int maxY = 37;

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip
    public SpawnRatesConfig spawnRates = new SpawnRatesConfig();

    @ConfigEntry.Gui.CollapsibleObject
    @ConfigEntry.Gui.Tooltip(count = 3)
    public OresConfig ores = new OresConfig();
}
