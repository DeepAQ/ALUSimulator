import java.util.Random;

/**
 * Created by adn55 on 16/5/21.
 */
public class ALUTest {
    public static void main(String[] args) {
        ALU alu = new ALU();
        Random rand = new Random();
        for (int i = 0; i < 100; i++) {
            int randNum1 = rand.nextInt(65535) - 32768;
            int randNum2 = rand.nextInt(65535) - 32768;
            int result = randNum1 * randNum2;
            String result2 = alu.integerTrueValue(
                    alu.integerMultiplication(
                            alu.integerRepresentation(Integer.toString(randNum1), 16),
                            alu.integerRepresentation(Integer.toString(randNum2), 16),
                            32
                    ).substring(1)
            );
            System.out.println(randNum1 + "*" + randNum2 + "=" + result + " | " + result2 + " | " + Integer.toString(result).equals(result2));
        }
        //System.out.println(alu.adder("01111111", "00000000", '1', 8));
    }
}
