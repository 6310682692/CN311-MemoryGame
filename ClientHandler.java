import java.io.*;
import java.net.Socket;
import java.util.ArrayList;

public class ClientHandler implements Runnable {

    public static ArrayList<ClientHandler> clientHandlers = new ArrayList<>();
    private Socket socket;
    private BufferedWriter output;
    private BufferedReader input;
    private String playerUsername;
    private String table;

    public ClientHandler(Socket socket, String table) {
        try {
            this.socket = socket;
            this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.playerUsername = input.readLine();
            System.out.println(this.playerUsername);
            this.output.write(table);
            this.output.newLine();
            this.output.flush();
            System.out.println("The table has been sent");
            clientHandlers.add(this);

        } catch (Exception e) {
            e.printStackTrace();
            terminateAll(socket, input, output);
        }
    }

    @Override
    public void run() {
        String statusPlayer;

        while (socket.isConnected()) {
            try {
                statusPlayer = input.readLine();
                if (statusPlayer.equals("true")) {
                    broadcastWinner(this.playerUsername, this.table);
                    System.out.println("TEST 3");
                    System.out.println(statusPlayer);
                }
            } catch (Exception e) {
                e.printStackTrace();
                terminateAll(socket, input, output);
                break;
            }
        }
    }

    public void broadcastWinner(String winner, String table) {
        for (ClientHandler clientHandler : clientHandlers) {
            try {
                if (!clientHandler.playerUsername.equals(playerUsername)) {
                    clientHandler.output.write("The winner is " + winner);
                    clientHandler.output.newLine();
                    clientHandler.output.flush();
                } else {
                    clientHandler.output.write("Congratulations you is the winner!");
                    clientHandler.output.newLine();
                    clientHandler.output.flush();
                }
            } catch (Exception e) {
                terminateAll(socket, input, output);
            }
        }
    }

    public void terminateAll(Socket socker, BufferedReader input, BufferedWriter output) {
        clientHandlers.remove(this);
        try {
            input.close();
            output.close();
            // if (socket != null) {
            socket.close();
            // }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
