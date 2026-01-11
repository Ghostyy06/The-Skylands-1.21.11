package net.ghostyy.world;

import net.ghostyy.Skylands;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.minecraft.world.dimension.DimensionType;

import java.awt.*;

public class SkylandsWorlds {

    public class Dim {

        public static final RegistryKey<World> SKYLANDS = register("skylands");

        private static RegistryKey<World> register(String name) {
            return RegistryKey.of(RegistryKeys.WORLD, Identifier.of(Skylands.MOD_ID, name));
        }
    }

    public class Type {

        public static final RegistryKey<DimensionType> SKYLANDS = register("skylands");

        private static RegistryKey<DimensionType> register(String name) {
            return RegistryKey.of(RegistryKeys.DIMENSION_TYPE, Identifier.of(Skylands.MOD_ID, name));
        }
    }
}
