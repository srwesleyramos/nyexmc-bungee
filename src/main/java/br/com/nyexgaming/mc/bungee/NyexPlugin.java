package br.com.nyexgaming.mc.bungee;

import br.com.nyexgaming.mc.bungee.commands.ReloadCommand;
import br.com.nyexgaming.mc.bungee.controller.Controller;
import br.com.nyexgaming.sdk.NyexGaming;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.config.Configuration;
import net.md_5.bungee.config.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

public class NyexPlugin extends Plugin {

    private Controller controller;

    @Override
    public void onEnable() {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b------------------------------------------------"));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b                                                "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §d           Nyex Gaming - BungeeCord             "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §d                   V 1.0.0                      "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b                                                "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §e ⇝ Loja: §fhttps://nyexgaming.com.br/           "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §e ⇝ Discord: §fhttps://nyexgaming.com.br/discord "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b                                                "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b------------------------------------------------"));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b                                                "));

        this.reload();

        this.getProxy().getPluginManager().registerCommand(this, new ReloadCommand());

        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b                                                "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b------------------------------------------------"));
    }

    @Override
    public void onDisable() {
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b------------------------------------------------"));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b                                                "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §d           Nyex Gaming - BungeeCord             "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §d                   V 1.0.0                      "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b                                                "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §e ⇝ Loja: §fhttps://nyexgaming.com.br/           "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §e ⇝ Discord: §fhttps://nyexgaming.com.br/discord "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b                                                "));
        ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §b------------------------------------------------"));
    }

    public void reload() {
        if (this.controller != null && this.controller.getTask().isAlive()) {
            this.controller.getTask().interrupt();
        }

        NyexGaming SDK = new NyexGaming(this.getConfig().getString("token-loja"), this.getConfig().getString("token-server"));
        Controller controller = new Controller(SDK);

        try {
            controller.execute(SDK.getTransactions());

            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§9[Nyex Bungee]: §fTodos os modulos foram carregados com sucesso."));
        } catch (NetworkErrorException | RequestFailedException | TokenFailureException e) {
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§4[Nyex Bungee]: §cOcorreu um erro ao comunicar-se com nossos servidores."));
            ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§4[Nyex Bungee]: §c" + e.getMessage()));

            if (e instanceof TokenFailureException) return;
        }

        this.controller = controller;
        this.controller.getTask().start();
    }

    public Controller getController() {
        return controller;
    }

    public Configuration getConfig() {
        try {
            File file = new File(this.getDataFolder(), "config.yml");

            if (!file.exists()) {
                file.getParentFile().mkdirs();

                try {
                    Files.copy(this.getResourceAsStream("config.yml"), file.toPath());
                } catch (IOException ignored) {
                }
            }

            return YamlConfiguration.getProvider(YamlConfiguration.class).load(file);
        } catch (IOException e) {
            return null;
        }
    }
}
