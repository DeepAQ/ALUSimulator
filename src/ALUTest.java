import java.util.Random;

/**
 * Created by adn55 on 16/5/21.
 */
public class ALUTest {
    public static void main(String[] args) {
        ALU alu = new ALU();
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int randInt = rand.nextInt(256) - 128;
            System.out.println(randInt + " " + alu.integerTrueValue(alu.integerRepresentation(Integer.toString(randInt), 8)).equals(Integer.toString(randInt)));
        }
    }
}
