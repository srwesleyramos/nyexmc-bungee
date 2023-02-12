package br.com.nyexgaming.mc.bungee.controller;

import br.com.nyexgaming.sdk.NyexGaming;
import br.com.nyexgaming.sdk.endpoints.products.Product;
import br.com.nyexgaming.sdk.endpoints.transactions.Transaction;
import br.com.nyexgaming.sdk.endpoints.transactions.TransactionStatus;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class Controller {

    private final NyexGaming sdk;
    private final Task task;

    public Controller(NyexGaming sdk) {
        this.sdk = sdk;
        this.task = new Task(this);
    }

    public void execute(Transaction[] transactions) throws NetworkErrorException, RequestFailedException, TokenFailureException {
        for (Transaction transaction : transactions) {
            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(transaction.identificador);

            if (player == null || transaction.getStatus() != TransactionStatus.PAID) continue;

            for (Product product : transaction.produtos) {
                for (String command : product.comandos) {
                    for (int i = 0; i < (command.contains("{{quantidade}}") ? 1 : product.quantidade); i++) {
                        ProxyServer.getInstance().getPluginManager().dispatchCommand(
                                ProxyServer.getInstance().getConsole(),
                                getFormattedText(product, command.replace("{{jogador}}", player.getName()))
                        );
                    }
                }
            }

            this.sdk.update(transaction.status(TransactionStatus.DELIVERED));
        }
    }

    public String getFormattedText(Product product, String text) {
        text = text.replace("{{nome}}", product.nome);
        text = text.replace("{{detalhes}}", product.detalhes);
        text = text.replace("{{quantidade}}", Integer.toString(product.quantidade));
        text = text.replace("{{preco}}", product.preco);

        return text.replace("&", "§");
    }

    public NyexGaming getSDK() {
        return sdk;
    }

    public Task getTask() {
        return task;
    }
}
