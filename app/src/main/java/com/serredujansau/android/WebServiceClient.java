package com.serredujansau.android;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;
import java.security.MessageDigest;
import java.util.Vector;

public class WebServiceClient {
    private final XmlRpcClient client;

    public WebServiceClient(String ipAddress, int port) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://" + ipAddress + ":" + port + "/"));
        client = new XmlRpcClient();
        client.setConfig(config);
    }

    // 🔹 Tester la connexion au serveur en appelant mg.version
    public boolean testServerConnection(String username, String password) {
        try {
            Vector<Object> params = new Vector<>();
            params.add(username);
            params.add(hashPassword(password)); // On envoie le hash du mot de passe
            params.add(true); // Paramètre boolean

            Object result = client.execute("mg.version", params);

            return result != null;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 🔹 Fonction pour hasher le mot de passe en MD5 (comme mgpda)
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : messageDigest) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
