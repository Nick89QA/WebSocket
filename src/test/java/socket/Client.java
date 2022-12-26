package socket;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class Client extends WebSocketClient {
    private final SocketContext context;

    public Client(URI serverUri, SocketContext context) throws URISyntaxException {
        super(new URI(context.getURI()));
        this.context = context;
    }

    /**
     * Open connection
     */
    @Override
    public void onOpen(ServerHandshake handshakedata) {
        System.out.println("Opened connection " + context.getURI());
    }

    /**
     * Send massage
     */
    @Override
    public void onMessage(String message) {
        System.out.println("Received new message " + message);
        context.getMessageList().add(message);//сохраняем все новые сообщения в список
        if (message.equals(context.getExpectedMessage())) {//завершаем подключение к сокету если ожидаемое сообщение пришло
            closeConnection(1000, "Received expected message");
        }
    }

    /**
     * Close connection
     */
    @Override
    public void onClose(int code, String reason, boolean remote) {
        System.out.println("Close socket with code " + code + "reason is " + reason);
        context.setStatusCode(code);
    }

    /**
     * Exceptions
     */

    @Override
    public void onError(Exception ex) {
        System.out.println("Message error");
    }
}
