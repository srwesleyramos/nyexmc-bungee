package br.com.nyexgaming.mc.bungee.commands;

import br.com.nyexgaming.mc.bungee.NyexPlugin;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Command;

public class ReloadCommand extends Command {

    public ReloadCommand() {
        super("gnyexgaming", "nyexgaming.command", "gnyex");

        this.setPermissionMessage("§4Nyex Bungee ⇝ §cVocê não possui permissão para este comando.");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        NyexPlugin plugin = (NyexPlugin) ProxyServer.getInstance().getPluginManager().getPlugin("NyexGaming");

        plugin.reload();

        sender.sendMessage(new TextComponent("§9Nyex Bungee ⇝ §fA operação foi executada, verifique o console para possíveis erros."));
    }
}
