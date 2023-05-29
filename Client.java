import java.util.Scanner;

public class Client {
    public static void main(String[] args) {
        final int HIGHT = 2;
        final int WIDTH = 6;
        
        Boolean[][] done = new Boolean[HIGHT][WIDTH];
        Integer[] memory = {null, null};
        boolean winCodition = false;
        
        Scanner sc = new Scanner(System.in);

        // create done table
        for (int i = 0; i < HIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                done[i][j] = false;
            }
        }
        
        // the table sent from sever delare server here
        Character[][] table = {
            {'a', 'a', 'j', 'r', 'f', 'r'},
            {'j', 'z', 'z', 'f', 'l', 'l'}
        };        

        DrawTable(table, done, HIGHT, WIDTH);

        while(!winCodition) {
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
                    
                    if (memory[0]== null && memory[1] == null) { // Check if there any in memory
                        memory[0] = x;
                        memory[1] = y;
                        DrawTable(table, done, 2, 6);         
                        
                    } else if (table[x][y] == table[memory[0]][memory[1]]) { // Check if the pair is the same char
                        memory[0] = null;
                        memory[1] = null;
                        DrawTable(table, done, 2, 6);
                        
                        System.out.println("Correct!");
                    } else { // The pair is not the same char
                        DrawTable(table, done, 2, 6);            
                        done[x][y] = false;
                        done[memory[0]][memory[1]] = false;
                        memory[0] = null;
                        memory[1] = null;
                        
                        System.out.println("Wrong!");
                    }
                    
                    winCodition = winCodition(done, HIGHT, WIDTH);                    
                }
            } else {
                System.out.println("Invalid input plase try again.");
                System.out.println();
            }
            
        }
        sc.close();
        
        // This is the place that the client sent that its the winner
        System.out.println("Congratulations you is the winner!");
    }

    public static void DrawTable(Character[][] table, Boolean[][] done, int HIGHT, int WIDTH) {
        System.out.println("The current table is: ");
        for (int i = 0; i < HIGHT; i++) {
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

        if (arrOfinput[0].matches("[a-fA-F]+") && arrOfinput[1].matches("[0-6]")) { // Check if the input in range and correct
            return true;            
        } else {
            return false;
        }
    }

    public static boolean winCodition(Boolean[][] done, int HIGHT, int WIDTH) {
        boolean winCodition = true;
        for (int i = 0; i < HIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                winCodition &= done[i][j];
                // System.out.print(done[i][j] + " ");
            }
            // System.out.println();
        }
        return winCodition;
    }
}
