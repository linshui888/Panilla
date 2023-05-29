package com.ruinscraft.panilla.bukkit;

import com.ruinscraft.panilla.api.EnchantmentCompat;
import com.ruinscraft.panilla.api.IEnchantments;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class BukkitEnchantments implements IEnchantments {

    private static final boolean is_legacy;

    static {
        is_legacy = Bukkit.getVersion().contains("1.12") || Bukkit.getVersion().contains("1.8");
    }

    @Override
    public int getMaxLevel(EnchantmentCompat enchCompat) {
        Enchantment bukkitEnchantment = getBukkitEnchantment(enchCompat);
        if (bukkitEnchantment == null) {
            return Integer.MAX_VALUE; // unknown enchantment
        }
        return bukkitEnchantment.getMaxLevel();
    }

    @Override
    public int getStartLevel(EnchantmentCompat enchCompat) {
        Enchantment bukkitEnchantment = getBukkitEnchantment(enchCompat);
        if (bukkitEnchantment == null) {
            return Integer.MAX_VALUE; // unknown enchantment
        }
        return bukkitEnchantment.getStartLevel();
    }

    @Override
    public boolean conflicting(EnchantmentCompat enchCompat, EnchantmentCompat _enchCompat) {
        Enchantment bukkitEnchantment = getBukkitEnchantment(enchCompat);
        Enchantment _bukkitEnchantment = getBukkitEnchantment(_enchCompat);
        if (bukkitEnchantment == null || _bukkitEnchantment == null) {
            return false; // unknown enchantment
        }
        return bukkitEnchantment.conflictsWith(_bukkitEnchantment);
    }

    private Enchantment getBukkitEnchantment(EnchantmentCompat enchCompat) {
        Enchantment bukkitEnchantment = null;
        if (is_legacy) {
            bukkitEnchantment = Enchantment.getByName(enchCompat.legacyName);
        } else {
            try {
                Method getByKey = Enchantment.class.getDeclaredMethod("getByKey", NamespacedKey.class);
                bukkitEnchantment = (Enchantment) getByKey.invoke(null,
                        new NamespacedKey(enchCompat.namedKey.split(":")[0],
                                enchCompat.namedKey.split(":")[1]));
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            }
        }
        return bukkitEnchantment;
    }

}
