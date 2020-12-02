package code.aterstones.mctweaks;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.plugin.java.JavaPlugin;

public class MCTweaks extends JavaPlugin implements Listener {

    @Override
    public void onLoad() {
        getLogger().info("Helloooo~");
    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
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
    
    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        System.out.println(event.getClass().getSimpleName());
        System.out.println(event.getAction());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBlockPlace(BlockPlaceEvent event) {
        System.out.println(event.getClass().getSimpleName());
        final Player player = event.getPlayer();
        if(!event.isCancelled() && player.getGameMode() == GameMode.SURVIVAL) {
            ItemStack stack = event.getItemInHand();
            final PlayerInventory inv = player.getInventory();
            final boolean offHand = event.getHand() == EquipmentSlot.OFF_HAND;
            final int heltItemSlot = inv.getHeldItemSlot();
            final int slot = findItemOfSameType(inv, stack, offHand ? -1 : heltItemSlot);
            if(slot != -1) {
                Bukkit.getScheduler().runTaskLater(this, () -> {
                    ItemStack newIs = inv.getItem(slot);

                    if(offHand) {
                        if(inv.getItem(EquipmentSlot.OFF_HAND) == null) {
                            inv.setItem(EquipmentSlot.OFF_HAND, newIs);
                            inv.setItem(slot, null);
                        }
                    } else {
                        if(inv.getItem(heltItemSlot) == null) {
                            inv.setItem(heltItemSlot, newIs);
                            inv.setItem(slot, null);
                        }
                    }
                }, 1);
            }
        }
    }

    public int findItemOfSameType(Inventory inventory, ItemStack stack, int ignoreSlot) {
        int slot = 0;
        for(ItemStack is : inventory.getContents()) {
            if(is != null && slot != ignoreSlot && is != stack && is.getType() == stack.getType() && is.getData() == stack.getData()) {
                return slot;
            }
            slot++;
        }

        return -1;
    }

}
