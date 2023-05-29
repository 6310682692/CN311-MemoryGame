import java.util.Scanner;
import java.io.*;
import java.net.Socket;
import java.net.UnknownHostException;

public class Client {
    private Socket socket;
    private BufferedReader input;
    private BufferedWriter output;
    private String username;

    public Client(Socket socket, String username) {
        try {
            this.socket = socket;
            this.output = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            this.input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            this.username = username;

        } catch (Exception e) {

        }
    }

    public void checkForAnotherWinner() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String winner;

                while (socket.isConnected()) {
                    try {
                        winner = input.readLine();
                        System.out.println("The winner is: " + winner);
                    } catch (IOException e) {

                    }
                }
            }
        }).start();
    }

    public String getTable() {
        String table = "";

        try {
            table = input.readLine();

            return table;
        } catch (IOException e) {

        }
        return table;
    }

    public static void main(String[] args) throws UnknownHostException, IOException {
        final int HEIGHT = 6;
        final int WIDTH = 6;
        // Character[][] table;

        Boolean[][] done = new Boolean[HEIGHT][WIDTH];
        Integer[] memory = { null, null };
        boolean winCodition = false;

        Scanner sc = new Scanner(System.in);

        // create done table
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                done[i][j] = true;
            }
        }

        done[5][5] = false;

        System.out.print("Enter your username: ");
        String username = sc.nextLine();

        Socket socket = new Socket("localhost", 1000);
        Client client = new Client(socket, username);
        client.output.write(username);
        client.output.newLine();
        client.output.flush();
        // client.sendIndex();
        String gettable = client.getTable();
        // System.out.println(gettable);
        Character[][] table = genTable(gettable, HEIGHT, WIDTH);

        // the table sent from sever delare server here
        // Character[][] table = {
        // { 'a', 'a', 'j', 'r', 'f', 'r' },
        // { 'j', 'z', 'z', 'f', 'l', 'l' }
        // };

        DrawTable(table, done, HEIGHT, WIDTH);

        winCodition = winCodition(done, HEIGHT, WIDTH);

        while (!winCodition) {
            // This is the place server sent if another player is the winner

            String input = sc.nextLine();

            if (ValidAns(input)) {
                String[] arrOfinput = input.split("");
                arrOfinput[0] = arrOfinput[0].toLowerCase();
                int x = arrOfinput[0].charAt(0) - 97; // Minus 97 because ASCII
                int y = Integer.parseInt(arrOfinput[1]) - 1;

                if (done[x][y]) { // Check if player already play the card
                    System.out.println("You have already play this spot");
                } else {
                    done[x][y] = true;

                    if (memory[0] == null && memory[1] == null) { // Check if there any in memory
                        memory[0] = x;
                        memory[1] = y;
                        DrawTable(table, done, HEIGHT, WIDTH);

                    } else if (table[x][y] == table[memory[0]][memory[1]]) { // Check if the pair is the same char
                        memory[0] = null;
                        memory[1] = null;
                        DrawTable(table, done, HEIGHT, WIDTH);

                        System.out.println("Correct!");
                    } else { // The pair is not the same char
                        DrawTable(table, done, HEIGHT, WIDTH);
                        done[x][y] = false;
                        done[memory[0]][memory[1]] = false;
                        memory[0] = null;
                        memory[1] = null;

                        System.out.println("Wrong!");
                    }

                    winCodition = winCodition(done, HEIGHT, WIDTH);
                }
            } else {
                System.out.println("Invalid input plase try again.");
                System.out.println();
            }

        }

        // This is the place that the client sent that its the winner
        client.output.write("true");
        client.output.newLine();
        client.output.flush();
        String winner = client.input.readLine();
        System.out.println(winner);

        sc.close();
    }

    public static void DrawTable(Character[][] table, Boolean[][] done, int HEIGHT, int WIDTH) {
        System.out.println("The current table is: ");
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                if (done[i][j] == false) {
                    System.out.print("?" + " ");
                } else {
                    System.out.print(table[i][j] + " ");
                }
            }
            System.out.println();
        }
    }

    public static boolean ValidAns(String inpuString) {
        if (inpuString.length() != 2) { // Check the input lenght
            return false;
        }
        String[] arrOfinput = inpuString.split("");

        if (arrOfinput[0].matches("[a-fA-F]+") && arrOfinput[1].matches("[0-6]")) { // Check if the input in range and
                                                                                    // correct
            return true;
        } else {
            return false;
        }
    }

    public static boolean winCodition(Boolean[][] done, int HEIGHT, int WIDTH) {
        boolean winCodition = true;
        for (int i = 0; i < HEIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                winCodition &= done[i][j];
                // System.out.print(done[i][j] + " ");
            }
            // System.out.println();
        }
        return winCodition;
    }

    public static Character[][] genTable(String strtable, int HEIGHT, int WIDTH) {
        Character[][] table = new Character[HEIGHT][WIDTH];

        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                table[y][x] = strtable.charAt((y * 6) + x);
                // System.out.println(strtable.charAt((y * 6) + x));
            }
        }
        return table;
    }
}
