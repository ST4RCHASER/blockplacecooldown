package me.pluemkitsada.blockplacecooldown;
import org.bukkit.command.CommandSender;

public class Main extends org.bukkit.plugin.java.JavaPlugin
{
    PlayerPlaceBlockListener BlockPlaceListener;

    public Main() {}

    public void onEnable()
    {
        org.bukkit.Bukkit.getServer().getLogger().info("BlockPlaceCooldown 1.3-a has been started!");
        org.bukkit.Bukkit.getServer().getLogger().info("by _StarChaser");
        getConfig().options().copyDefaults(true);
        saveDefaultConfig();
        PlayerPlaceBlockListener BlockPlaceListener = new PlayerPlaceBlockListener(this);
    }

    public boolean onCommand(CommandSender sender, org.bukkit.command.Command cmd, String label, String[] args) {
        if (!(sender instanceof org.bukkit.entity.Player)) {
            sender.sendMessage(getConfig().getString("message.onlyconsole").replaceAll("&", "§").replaceAll("%n", "\n"));
            return true;
        }
        org.bukkit.entity.Player p = (org.bukkit.entity.Player)sender;
        if (cmd.getName().equalsIgnoreCase("bpc")) {
            if (!sender.hasPermission("bpc.use")) {
                sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "" + getConfig().getString("Message.Permission").replaceAll("&", "§").replaceAll("<player>", p.getName()));
                return true;
            }
            if (args.length == 0) {
                sender.sendMessage("§e========= §dBlockPlaceCooldown 1.3-a §e=========");
                sender.sendMessage("§d/" + label + "§e- §fshow this page");
                sender.sendMessage("§d/" + label + " add <blockid> §e- §fAdd a block");
                sender.sendMessage("§d/" + label + " remove <blockid> §e- §fRemove a block");
                sender.sendMessage("§d/" + label + " message <message> §e- §fset message cooldown time");
                sender.sendMessage("§d/" + label + " prefix <message> §e- §fset message prefix");
                sender.sendMessage("§d/" + label + " reload §e- §fReload configuration file");
                return true;
            }
            if (args[0].equalsIgnoreCase("add")) {
                if (args.length == 1) {
                    sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "§cusage: /bpc add <blockid>");
                    return true;
                }
                getConfig().set("blocks." + args[1], args[1]);
                saveConfig();
                sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "" + getConfig().getString("message.added").replaceAll("&", "§").replaceAll("<blockid>", args[1]));
                return true;
            }
            if (args[0].equalsIgnoreCase("remove")) {
                if (args.length == 1) {
                    sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "§cusage: /bpc remove <blockid>");
                    return true;
                }
                getConfig().set("blocks." + args[1], null);
                saveConfig();
                sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "" + getConfig().getString("message.removed").replaceAll("&", "§").replaceAll("<blockid>", args[1]));
                return true;
            }
            if (args[0].equalsIgnoreCase("prefix")) {
                if (args.length == 1) {
                    sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "§cusage: /bpc prefix <message>");
                    return true;
                }
                StringBuilder str = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    str.append(args[i] + " ");
                }
                String Prefix = str.toString();
                getConfig().set("message.prefix", Prefix);
                saveConfig();
                sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "§aPrefix Message has been set: §c" + Prefix);
                return true;
            }
            if (args[0].equalsIgnoreCase("message")) {
                if (args.length == 1) {
                    sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "§cusage: /bpc message <message>");
                    sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") +"§c<message> = Message word");
                    return true;
                }
                StringBuilder str = new StringBuilder();
                for (int i = 1; i < args.length; i++) {
                    str.append(args[i] + " ");
                }
                String BlockPlace = str.toString();
                getConfig().set("message.denyplace", BlockPlace);
                saveConfig();
                sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "§aMessage has been set: §c" + BlockPlace);
                return true;
            }
            if ((args[0].equalsIgnoreCase("reload")) && (args.length == 1)) {
                reloadConfig();
                saveConfig();
                sender.sendMessage(getConfig().getString("message.prefix").replaceAll("&", "§") + "" + getConfig().getString("message.reload").replaceAll("&", "§"));
                return true;
            }
        }
        sender.sendMessage("§e========= §dBlockPlaceCooldown §e=========");
        sender.sendMessage("§d/" + label + "§e- §fshow this page");
        sender.sendMessage("§d/" + label + " add <blockid> §e- §fAdd a block");
        sender.sendMessage("§d/" + label + " remove <blockid> §e- §fRemove a block");
        sender.sendMessage("§d/" + label + " message <message> §e- §fset message cooldown time");
        sender.sendMessage("§d/" + label + " prefix <message> §e- §fset message prefix");
        sender.sendMessage("§d/" + label + " reload §e- §fReload configuration file");
        return true;
    }
}
