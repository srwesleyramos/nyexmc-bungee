package br.com.nyexgaming.mc.bungee.controller.tasks;

import br.com.nyexgaming.mc.bungee.controller.Controller;
import br.com.nyexgaming.sdk.http.exceptions.NetworkErrorException;
import br.com.nyexgaming.sdk.http.exceptions.RequestFailedException;
import br.com.nyexgaming.sdk.http.exceptions.TokenFailureException;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.chat.TextComponent;

public class ActivateTask extends Thread {

    private final Controller controller;

    public ActivateTask(Controller controller) {
        super("NyexTask #1");

        this.controller = controller;
    }

    @Override
    public void run() {
        while (this.controller.getSDK() != null && this.isAlive()) {
            try {
                Thread.sleep(60000L);
            } catch (InterruptedException ignored) {
            }

            try {
                this.controller.execute(this.controller.getSDK().getTransactions());
            } catch (NetworkErrorException e) {
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§4[NyexGaming] [ERROR]: §cNão foi possível conectar a API, verifique a sua conexão."));
            } catch (RequestFailedException e) {
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§4[NyexGaming] [ERROR]: §cOps... Aparentemente o seu plugin está com uma versão antiga, verifique se há atualizações!"));
            } catch (TokenFailureException e) {
                ProxyServer.getInstance().getConsole().sendMessage(new TextComponent("§4[NyexGaming] [ERROR]: §cEita! As credenciais de autenticação que você forneceu são inválidas, verifique e recarregue o plugin!"));
                break;
            }
        }
    }
}
