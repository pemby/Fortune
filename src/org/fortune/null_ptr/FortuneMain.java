package org.fortune.null_ptr;

import java.util.ArrayList;
import java.util.Iterator;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;

public class FortuneMain extends JavaPlugin implements Listener {

	MainData data = new MainData(new FortuneParserGen(), new ArrayList<String>());

	public FortuneMain() {

		this.data.runPlugin = false;
		this.data.firstrun = true;
		this.data.loginFortune = this.getConfig().getStringList("loginFortuneOn");
		this.data.knownPlayers = this.getConfig().getStringList("NotifiedPlayers");

	}

	public boolean isFirstrun() {
		return this.data.firstrun;
	}

	public boolean isRunPlugin() {
		return this.data.runPlugin;
	}

	public void loadConfiguration() {
		// final String path = "Options.FortuneLoginOn";
		// this.getConfig().addDefault(path, "True");
		// this.getConfig().options().copyDefaults(true);
		this.saveConfig();
	}

	@Override
	public boolean onCommand(final CommandSender sender, final Command cmd, final String label, final String[] args) {
		boolean rval = false;

		final boolean isAPlayer = sender instanceof Player;
		if (label.equalsIgnoreCase("lfortune")) {
			if (!isAPlayer) {
				this.sendNonPlayerErrorMessage(sender);
			} else {

				final Player player = (Player) sender;
				final String playerName = player.getName();
				final boolean inList = this.data.loginFortune.contains(playerName);

				if (inList) {
					// toggle so reverse state
					this.setRunPlugin(false);
					player.sendMessage(this.sendLogFortOffMessage(player));
					// final Integer index =
					// this.loginFortune.indexOf(playerName);
					// this.loginFortune.remove(index);

					for (final Iterator<String> iter = this.data.loginFortune.listIterator(); iter.hasNext();) {
						final String a = iter.next();
						if (a.equals(playerName)) {
							iter.remove();
						}
					}

				} else {

					// toggle so reverse state
					this.setRunPlugin(true);
					player.sendMessage(this.sendLogFortOnMessage(player));
					this.data.loginFortune.add(playerName);

				}

				rval = true;
			}

		}

		if (label.equalsIgnoreCase("fortune")) {
			if (!isAPlayer) {
				this.sendNonPlayerErrorMessage(sender);

			} else {
				final Player player = (Player) sender;
				player.sendMessage(this.sendFortune());
			}
		}
		return rval;

	}

	@Override
	public void onDisable() {
		this.getLogger().info("Plug-in disabled");
		this.getConfig().set("loginFortuneOn", this.data.loginFortune);
		this.getConfig().set("NotifiedPlayers", this.data.knownPlayers);
		this.saveConfig();

	}

	@Override
	public void onEnable() {
		this.getServer().getPluginManager().registerEvents(this, this);
		this.loadConfiguration();
		this.getLogger().info("Plug-in enabled");

	}

	@EventHandler
	public void onJoin(final PlayerJoinEvent e) {

		final String playerName = e.getPlayer().getName();
		if (!(this.data.knownPlayers.contains(playerName))) {

			e.setJoinMessage(
					"NOTICE: Fortune has been installed on this server! You can either type /fortune to read a fortune, or type /lfortune to have a fortune be read to you on every login! ");
			this.data.knownPlayers.add(playerName);
		}

		if (this.data.loginFortune.contains(playerName)) {
			e.setJoinMessage(this.sendFortune());

		}

	}

	private String sendFortune() {
		return ChatColor.LIGHT_PURPLE + "Fortune: " + ChatColor.AQUA + this.data.alist.getRandomFortune();
	}

	private String sendLogFortOffMessage(final Player player) {
		return "Login Fortunes Turned " + ChatColor.LIGHT_PURPLE + "OFF! " + ChatColor.WHITE + " for "
				+ ChatColor.DARK_AQUA + player.getPlayerListName();
	}

	private String sendLogFortOnMessage(final Player player) {
		return "Login Fortunes Turned " + ChatColor.LIGHT_PURPLE + "ON!" + ChatColor.WHITE + " for "
				+ ChatColor.DARK_AQUA + player.getPlayerListName();
	}

	private void sendNonPlayerErrorMessage(final CommandSender sender) {
		sender.sendMessage(ChatColor.RED + "You must be a player to use this command!");
	}

	public void setFirstrun(final boolean firstrun) {
		this.data.firstrun = firstrun;
	}

	public void setRunPlugin(final boolean runPlugin) {
		this.data.runPlugin = runPlugin;
	}

}
