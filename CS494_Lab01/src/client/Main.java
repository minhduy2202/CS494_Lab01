package client;

import ui.LoginUI;

import javax.swing.*;

import static utils.Constants.PORT;
import static utils.Constants.SERVER_IP;

public class Main {
        static ClientNetwork clientNetwork = new ClientNetwork(SERVER_IP, PORT);

    private static final LoginUI loginUI = new LoginUI(clientNetwork);


    public static void main(String[] args) {
        startClientConnection();
        clientNetwork.getClientHandlerTmp().addPropertyChangeListener(loginUI);
        runUI();
    }

    private static void startClientConnection(){
        new Thread(clientNetwork).start();
    }

    public static void runUI(){
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                loginUI.getLoginUI();
            }
        });
    }
}


