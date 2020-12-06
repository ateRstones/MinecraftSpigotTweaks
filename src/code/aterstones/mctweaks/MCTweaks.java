package code.aterstones.mctweaks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemBreakEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

import code.aterstones.mctweaks.command.GMCommand;
import code.aterstones.mctweaks.util.CommandRegisterer;

public class MCTweaks extends JavaPlugin implements Listener {

    public static final int OFF_HAND_SLOT = 40; // Ref Spigot docs

    @Override
    public void onLoad() {
        getLogger().info("Helloooo~");
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        CommandRegisterer.registerCommand(this, "gm", new GMCommand());
    }

    @Override
    public void onDisable() {
        getLogger().info("Byee byee");
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage("§7" + event.getPlayer().getName() + " §a+");
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        event.setQuitMessage("§7" + event.getPlayer().getName() + " §4-");
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemBreak(PlayerItemBreakEvent event) {
        tryItemReplace(event.getPlayer(), event.getBrokenItem(), true);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onItemConsume(PlayerItemConsumeEvent event) {
        if(!event.isCancelled()) {
            tryItemReplace(event.getPlayer(), event.getItem());
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        if(!event.isCancelled()) {
            tryItemReplace(event.getPlayer(), event.getItemInHand());
        }
    }

    public void tryItemReplace(Player player, ItemStack is) {
        tryItemReplace(player, is, false);
    }

    public void tryItemReplace(Player player, ItemStack is, boolean simpleCheck) {
        System.out.println(is);
        if(is != null) {
            if(player.getGameMode() == GameMode.SURVIVAL && is.getAmount() - 1 == 0) {
                PlayerInventory inv = player.getInventory();
                int slot;
                if(is.equals(inv.getItemInMainHand())) {
                    slot = inv.getHeldItemSlot();
                } else if(is.equals(inv.getItemInOffHand())) {
                    slot = OFF_HAND_SLOT;
                } else {
                    return;
                }
                
                int replenishSlot = findItemOfSameType(inv, is, slot, simpleCheck);
                System.out.println("Slot return is " + replenishSlot);
                if(replenishSlot != -1) {
                    replaceItemInFuture(inv, slot, replenishSlot);
                }
            }
        }
    }

    public void replaceItemInFuture(final PlayerInventory inv, final int slot, final int replenishSlot) {
        Bukkit.getScheduler().runTaskLater(this, () -> {
            if(inv.getItem(slot) == null) {
                inv.setItem(slot, inv.getItem(replenishSlot));
                inv.setItem(replenishSlot, null);
            }
        }, 1);
    }

    public int findItemOfSameType(Inventory inventory, ItemStack stack, int ignoreSlot, boolean simpleCheck) {
        int slot = 0;
        for(ItemStack is : inventory.getContents()) {
            if(is != null && slot != ignoreSlot && (!simpleCheck ? stack.isSimilar(is) : (stack.getType() == is.getType()))) {
                return slot;
            }
            slot++;
        }

        return -1;
    }

}
