import java.io.IOException;

public class ReadMessage extends Thread {
    @Override
    public void run() {
        String str;
        try {
            while (true) {
                str = Client.ClientData.getClientReaderSocket().readLine();
                if (str != null) {
                    if (str.equals("@exit")) {
                        Client.ClientData.closeConnection();
                        break;
                    } else {
                        System.out.println(str);
                    }
                }
            }
        } catch (IOException ignored) {
        }
    }
}
