import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class Server {

    private static UserInfo info;
    private static Map<String,TaggedConnection> connections = new HashMap<>();

    Map<String,User> credentials = Parser.parse();

    public static void main(String[] args) throws IOException {
        try {
            ServerSocket ss = new ServerSocket(12345);
            info = new UserInfo();

            while (true) {
                System.out.println("À espera de pedidos de clientes...\n");
                Socket socket = ss.accept();
                TaggedConnection c = new TaggedConnection(socket);
                //criação de uma thread de forma a tratar cada um dos clientes que querem fazer pedidos
                Thread t = new Thread(new ServerWorker(socket));
                t.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Server() throws IOException {
    }


    static class ServerWorker implements Runnable {
        private Socket s;

        public ServerWorker(Socket socket) {
            this.s = socket;
        }

        public void run() {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                PrintWriter out = new PrintWriter(s.getOutputStream());

                out.flush();

                s.shutdownOutput();
                s.shutdownInput();
                s.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}