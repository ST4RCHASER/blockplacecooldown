package me.pluemkitsada.blockplacecooldown;
import java.util.HashMap;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerPlaceBlockListener implements org.bukkit.event.Listener
{
    Main instance;
    private HashMap<Player, Integer> cooldownTime;
    private HashMap<Player, BukkitRunnable> cooldownTask;

    public PlayerPlaceBlockListener(Main instance)
    {
        org.bukkit.Bukkit.getServer().getPluginManager().registerEvents(this, instance);
        this.instance = instance;
        cooldownTime = new HashMap();
        cooldownTask = new HashMap();
    }

    @org.bukkit.event.EventHandler(priority= EventPriority.HIGHEST)
    public void BlockPlaceEvent(final BlockPlaceEvent e) {
        int id = e.getBlockPlaced().getTypeId();
        final Player player = e.getPlayer();
        if (!instance.getConfig().contains("blocks." + id + "")) {
            return;
        }
        if (player.hasPermission("bpc.bypass.*") || player.hasPermission("bpc.bypass." + id)) {
            return;
        }
            if (instance.getConfig().getString("blocks." + id + "").equalsIgnoreCase(id + "")) {
                String message = instance.getConfig().getString("message.prefix").replaceAll("&", "§") + instance.getConfig().getString("message.denyplace").replace("<time>", String.valueOf(cooldownTime.get(e.getPlayer())));
                if (cooldownTime.containsKey(e.getPlayer())) {
                    e.getPlayer().sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', message));
                    e.setCancelled(true);
                    return;
                }
                cooldownTime.put(e.getPlayer(), Integer.valueOf(instance.getConfig().getInt("cooldown")));
                cooldownTask.put(e.getPlayer(), new BukkitRunnable() {
                    public void run() {
                        cooldownTime.put(e.getPlayer(), Integer.valueOf(((Integer) cooldownTime.get(e.getPlayer())).intValue() - 1));
                        if (((Integer) cooldownTime.get(e.getPlayer())).intValue() == 0) {
                            cooldownTime.remove(e.getPlayer());
                            cooldownTask.remove(e.getPlayer());
                            cancel();
                            String messageend = instance.getConfig().getString("message.prefix").replaceAll("&", "§") + instance.getConfig().getString("message.canplace");
                            e.getPlayer().sendMessage(org.bukkit.ChatColor.translateAlternateColorCodes('&', messageend));
                        }
                    }
                });
                ((BukkitRunnable) cooldownTask.get(e.getPlayer())).runTaskTimer(instance, 20L, 20L);
            }
    }
}
