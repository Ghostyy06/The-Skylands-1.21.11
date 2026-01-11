package net.ghostyy.effect;

import net.ghostyy.Skylands;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;

public class SkylandsEffects {

    public static final RegistryEntry<StatusEffect> PREVENT_FALL_DAMAGE = register("prevent_fall_damage", new PreventFallDamageEffect(StatusEffectCategory.BENEFICIAL, 7673343));

    private static RegistryEntry<StatusEffect> register(String name, StatusEffect effect) {
        return Registry.registerReference(Registries.STATUS_EFFECT, Identifier.of(Skylands.MOD_ID, name), effect);
    }

    public static void init() {};

}