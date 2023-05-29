import java.util.*;


public class Server {
    public static void main(String[] args) {

        final int HIGHT = 6;
        final int WIDTH = 6;
        
        Character[] abc = {'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'v', 'w', 'x', 'y', 'z'};
        char[][] table = new char[HIGHT][WIDTH];
        
        Character[] shuffled = shuffle(abc);

        
        Character[] duplicated = duplicate(shuffled);

        for (int i = 0; i < duplicated.length; i++) {
            System.out.print(duplicated[i]);
        }

        for (int y = 0; y < HIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                table[y][x] = duplicated[(y * 6) + x];
            }
        }

        DrawTable(table, HIGHT, WIDTH);
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

    public static void DrawTable(char[][] table, int HIGHT, int WIDTH) {
        System.out.println("The 2D array is: ");
        for (int i = 0; i < HIGHT; i++) {
            for (int j = 0; j < WIDTH; j++) {
                System.out.print(table[i][j] + " ");
            }
        System.out.println();
        }
    }
}