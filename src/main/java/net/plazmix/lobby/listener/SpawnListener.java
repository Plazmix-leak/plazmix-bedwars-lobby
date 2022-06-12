package net.plazmix.lobby.listener;

import lombok.RequiredArgsConstructor;
import net.plazmix.core.PlazmixCoreApi;
import net.plazmix.event.PlazmixExperienceChangeEvent;
import net.plazmix.utility.leveling.LevelingUtil;
import net.plazmix.utility.player.PlazmixUser;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.*;
import org.bukkit.inventory.meta.FireworkMeta;

@RequiredArgsConstructor
public final class SpawnListener implements Listener {

    @EventHandler
    public void onBlockFromTo(BlockFromToEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockIgnite(BlockIgniteEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockSpread(BlockSpreadEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onBlockBurn(BlockBurnEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPhysicsEvent(BlockPhysicsEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onArmorStandEdit(PlayerArmorStandManipulateEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDeath(PlayerDeathEvent event) {
        event.setDeathMessage(null);
    }

    @EventHandler
    public void onLeavesDecay(LeavesDecayEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityCombust(EntityCombustEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityBreed(EntityBreedEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityChangeBlock(EntityChangeBlockEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityBlockForm(EntityBlockFormEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getClickedBlock() != null && event.getClickedBlock().getType().equals(Material.ENDER_CHEST)) {
            return;
        }

        if (event.hasItem() && event.getItem().getType().equals(Material.BOW)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onCreatureSpawn(CreatureSpawnEvent event) {
        if (event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.CUSTOM)) {
            return;
        }

        event.setCancelled(true);
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {

        if (!(event.getDamager() instanceof Projectile)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {

        if (!event.getCause().equals(EntityDamageEvent.DamageCause.ENTITY_ATTACK)) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Bukkit.dispatchCommand(event.getPlayer(), "spawn");

        PlazmixUser plazmixUser = PlazmixUser.of(event.getPlayer());

        if (plazmixUser.handle() == null) {
            return;
        }

        plazmixUser.handle().setLevel(plazmixUser.getLevel());
        plazmixUser.handle().setExp((float) plazmixUser.getExperience() / plazmixUser.getMaxExperience());


        if (!PlazmixCoreApi.GROUP_API.isDefault(plazmixUser.getName())) {

            plazmixUser.handle().setAllowFlight(true);

            plazmixUser.handle().playSound(event.getPlayer().getLocation(), Sound.ENTITY_ITEM_PICKUP, 1, 1);

            Firework firework = plazmixUser.handle().getWorld().spawn(plazmixUser.handle().getLocation(), Firework.class);
            FireworkMeta fireworkMeta = firework.getFireworkMeta();

            fireworkMeta.setPower(3);
            fireworkMeta.addEffect(FireworkEffect.builder()
                    .with(FireworkEffect.Type.STAR)

                    .withColor(Color.PURPLE)
                    .withColor(Color.WHITE)

                    .build());

            firework.setFireworkMeta(fireworkMeta);
        }

        plazmixUser.handle().playSound(plazmixUser.handle().getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1, 1);
        plazmixUser.handle().sendTitle("§d§lBedWars", "§fФантастические бои на парящих островах");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage(null);
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {

        if (event.getTo().getY() <= 0) {
            Bukkit.dispatchCommand(event.getPlayer(), "spawn");
        }
    }

    @EventHandler
    public void onPlayerFood(FoodLevelChangeEvent event) {
        event.setCancelled(true);
    }

    @EventHandler
    public void onExpChange(PlazmixExperienceChangeEvent event) {
        PlazmixUser plazmixUser = PlazmixUser.of(event.getPlayer());

        int currentExp = event.getCurrentExp();
        int level = LevelingUtil.getLevel(currentExp);

        plazmixUser.handle().setLevel(level);
        plazmixUser.handle().setExp((float) ((level > 1 ? currentExp - LevelingUtil.getTotalExpToLevel(level) : currentExp) / LevelingUtil.getExpFromLevelToNext(level)));

    }
}
