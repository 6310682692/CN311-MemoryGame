import java.util.*;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    private ServerSocket serverSoc;

    // socket side
    public Server(ServerSocket serverSoc) {
        this.serverSoc = serverSoc;
    }

    public void startserver(String table) {

        try {
            while (!serverSoc.isClosed()) {
                Socket clientsoc = serverSoc.accept();
                System.out.println("A new player has join!");
                ClientHandler ClientHandler = new ClientHandler(clientsoc, table);

                Thread thread = new Thread(ClientHandler);
                thread.start();
            }
        } catch (Exception e) {
            // nothing
        }

    }

    public static void main(String[] args) throws IOException {

        final int HEIGHT = 6;
        final int WIDTH = 6;

        Character[] abc = { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r',
                's', 't', 'v', 'w', 'x', 'y', 'z' };
        String table = "";

        Character[] shuffled = shuffle(abc);

        Character[] duplicated = duplicate(shuffled);

        for (int i = 0; i < 36; i++) {
            // System.out.print(duplicated[i]);
            table += duplicated[i];
        }

        System.out.println(table);

        // for (int y = 0; y < HEIGHT; y++) {
        // for (int x = 0; x < WIDTH; x++) {
        // table += duplicated[(y * 6) + x];
        // }
        // }

        // DrawTable(table, HEIGHT, WIDTH);

        ServerSocket serverSocket = new ServerSocket(1000);
        Server server = new Server(serverSocket);
        server.startserver(table);
    }

    public static Character[] shuffle(Character[] abc) {
        List<Character> abcList = Arrays.asList(abc);
        Collections.shuffle(abcList);
        abcList.toArray(abc);

        return abc;
    }

    public static Character[] duplicate(Character[] shuffled) {
        Character[] duplicate = new Character[36];

        System.arraycopy(shuffled, 0, duplicate, 0, 18);
        System.arraycopy(shuffled, 0, duplicate, 18, 18);

        duplicate = shuffle(duplicate);

        return duplicate;
    }

    public static void DrawTable(Character[][] table, int HEIGHT, int WIDTH) {
        System.out.println("The 2D array is: ");
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(table[i][j] + " ");
            }
            System.out.println();
        }
    }
}