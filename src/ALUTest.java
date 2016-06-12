import java.util.Random;

/**
 * Created by adn55 on 16/5/21.
 */
public class ALUTest {
    public static void main(String[] args) {
        ALU alu = new ALU();
        Random rand = new Random();
        /*for (int i = 0; i < 100; i++) {
            long randNum1 = rand.nextLong();
            long randNum2 = rand.nextInt();
            if (randNum2 == 0) continue;
            long q1 = randNum1 / randNum2;
            long r1 = randNum1 % randNum2;
            String result2 = alu.integerDivision(
                    alu.integerRepresentation(Long.toString(randNum1), 64),
                    alu.integerRepresentation(Long.toString(randNum2), 64),
                    64
            ).substring(1);
            String q2 = alu.integerTrueValue(result2.substring(0, 64));
            String r2 = alu.integerTrueValue(result2.substring(64));
            System.out.println(randNum1 + "/" + randNum2 + " = " + q1 + "..." + r1 + " | " + q2 + "..." + r2
                    + " | " + (Long.toString(q1).equals(q2)&&Long.toString(r1).equals(r2)));
        }*/
        //System.out.println(alu.integerDivision(alu.integerRepresentation("-8", 4), alu.integerRepresentation("-1", 4), 4));
        for (int i = 0; i < 100; i++) {
            double randNum1 = rand.nextDouble() * rand.nextInt(999);
            double randNum2 = rand.nextDouble() * rand.nextInt(999);
            double result = randNum1 - randNum2;
            String resultString = alu.ieee754(Double.toString(result), 64);
            String result2 = alu.floatSubtraction(
                    alu.ieee754(Double.toString(randNum1), 64),
                    alu.ieee754(Double.toString(randNum2), 64),
                    11, 52, 4
            ).substring(1);
            System.out.println(resultString.substring(0, 50).equals(result2.substring(0, 50)) + " | " + randNum1 + " " + randNum2 + " | " + resultString + " | " + result2);
        }

    }
}
