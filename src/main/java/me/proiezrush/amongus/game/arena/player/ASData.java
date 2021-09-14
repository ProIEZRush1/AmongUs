package me.proiezrush.amongus.game.arena.player;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

public class ASData {

    private final Player p;
    public ASData(AS as) {
        this.p = as.getPlayer();
    }

    private ItemStack[] inventory;
    private ItemStack[] armor;
    private GameMode gameMode;
    private double health;
    private double maxHealth;
    private int foodLevel;
    private int level;
    private float exp;
    private boolean allowFlight;
    private boolean isFlying;
    private Location location;

    public void clear() {
        this.inventory = p.getInventory().getContents();
        this.armor = p.getInventory().getArmorContents();
        this.gameMode = p.getGameMode();
        this.health = p.getHealth();
        this.maxHealth = p.getMaxHealth();
        this.foodLevel = p.getFoodLevel();
        this.level = p.getLevel();
        this.exp = p.getExp();
        this.allowFlight = p.getAllowFlight();
        this.isFlying = p.isFlying();
        this.location = p.getLocation();

        p.getInventory().clear();
        p.getInventory().setArmorContents(null);
        p.setGameMode(GameMode.SURVIVAL);
        p.setHealth(20);
        p.setMaxHealth(20);
        p.setFoodLevel(20);
        p.setLevel(0);
        p.setExp(0);
        p.setAllowFlight(false);
        p.setFlying(false);
    }

    public void restore() {
        p.getInventory().setContents(inventory);
        p.getInventory().setArmorContents(armor);
        p.setGameMode(gameMode);
        p.setHealth(health);
        p.setMaxHealth(maxHealth);
        p.setFoodLevel(foodLevel);
        p.setLevel(level);
        p.setExp(exp);
        p.setAllowFlight(allowFlight);
        p.setFlying(isFlying);
        p.teleport(location);
        for (PotionEffect potionEffect : p.getActivePotionEffects()) {
            p.removePotionEffect(potionEffect.getType());
        }
    }

}
