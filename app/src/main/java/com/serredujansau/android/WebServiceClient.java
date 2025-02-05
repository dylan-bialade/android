package com.serredujansau.android;

import org.apache.xmlrpc.client.XmlRpcClient;
import org.apache.xmlrpc.client.XmlRpcClientConfigImpl;
import java.net.URL;
import java.util.Vector;

public class WebServiceClient {
    private final XmlRpcClient client;

    public WebServiceClient(String ipAddress, int port) throws Exception {
        XmlRpcClientConfigImpl config = new XmlRpcClientConfigImpl();
        config.setServerURL(new URL("http://" + ipAddress + ":" + port + "/RPC2"));
        client = new XmlRpcClient();
        client.setConfig(config);
    }

    public boolean testServerConnection() {
        try {
            Vector<Object> params = new Vector<>();
            params.add("test_connection");
            Object result = client.execute("system.listMethods", params);
            return result != null;
        } catch (Exception e) {
            return false;
        }
    }
}
