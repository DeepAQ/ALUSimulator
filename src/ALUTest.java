import java.util.Random;

/**
 * Created by adn55 on 16/5/21.
 */
public class ALUTest {
    public static void main(String[] args) {
        ALU alu = new ALU();
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            double randNum = rand.nextDouble() * 200.0 - 100.0;
            String result = alu.floatTrueValue(alu.floatRepresentation(Double.toString(randNum), 11, 52), 11, 52);
            System.out.println(alu.floatRepresentation(Double.toString(randNum), 11, 52));
            System.out.println(randNum + " " + result + " " + result.equals(Double.toString(randNum)));
        }
    }
}
