import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteMessage extends Thread{
    @Override
    public void run() {
        while (true){
            String clientMessage;
            try {
                Client.ClientData.setTime(new Date());
                Client.ClientData.setDt1(new SimpleDateFormat("HH:mm:ss"));
                Client.ClientData.setDtime(Client.ClientData.getDt1().format(Client.ClientData.getTime()));
                clientMessage = Client.ClientData.getClientReaderConsole().readLine();

                if (clientMessage.equals("@exit")){
                    Client.ClientData.closeConnection();
                    break;
                }
                if (clientMessage.equals("@menu")){
                    Client.ClientData.menuClient();
                } else {
                    Client.ClientData.getClientWriterSocket().write("[" + Client.ClientData.getDtime() + "] "
                            + Client.ClientData.getNickname() + ": " + clientMessage + "\n");
                }
                Client.ClientData.getClientWriterSocket().flush();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
