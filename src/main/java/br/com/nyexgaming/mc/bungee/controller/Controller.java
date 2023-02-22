package br.com.nyexgaming.mc.bungee.controller;

import br.com.nyexgaming.mc.bungee.NyexPlugin;
import br.com.nyexgaming.mc.bungee.controller.tasks.ActivateTask;
import br.com.nyexgaming.sdk.NyexGaming;
import br.com.nyexgaming.sdk.endpoints.products.Product;
import br.com.nyexgaming.sdk.endpoints.products.ProductCommand;
import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Controller {

    private NyexGaming sdk;
    private ActivateTask task;

    public Controller() {
    }

    public void execute(Transaction[] transactions) throws NetworkErrorException, RequestFailedException, TokenFailureException {
        boolean bungeecordCommands = NyexPlugin.getConfig().getBoolean("service.bungeecord-commands");

        for (Transaction transaction : transactions) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(transaction.identificador);

            if (player == null || transaction.status != TransactionStatus.PAID.statusCode) continue;

            transaction.entregue = true;
            this.sdk.update(transaction);

            for (Product product : transaction.produtos) {
                for (int i = 0; i < product.quantidade; i++) {
                    for (ProductCommand command : product.comandos) {
                        if (!command.console && !bungeecordCommands) {
                            player.chat("/" + command.cmd.replace("<jogador>", player.getName()));
                            continue;
                        }

                        ProxyServer.getInstance().getPluginManager().dispatchCommand(
                                command.console ? ProxyServer.getInstance().getConsole() : player,
                                command.cmd.replace("<jogador>", player.getName())
                        );
                    }
                }
            }
        }
    }

    public ActivateTask getTask() {
        return task;
    }

    public void setTask(ActivateTask task) {
        this.task = task;
    }

    public NyexGaming getSDK() {
        return sdk;
    }

    public void setSDK(NyexGaming sdk) {
        this.sdk = sdk;
    }
}
