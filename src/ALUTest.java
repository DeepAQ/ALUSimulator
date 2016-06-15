import java.util.Random;

/**
 * Created by adn55 on 16/5/21.
 */
public class ALUTest {
    public static void main(String[] args) {
        ALU alu = new ALU();
        Random rand = new Random();
        System.out.println(">>> integerRepresentation & integerTrueValue & oneAdder & negation test");
        for (int i = 0; i < 100; i++) {
            long randNum1 = rand.nextLong();
            String result2 = alu.integerTrueValue(
                    alu.oneAdder(alu.negation(alu.integerRepresentation(Long.toString(randNum1), 64))).substring(1)
            );
            boolean correct = result2.equals(Long.toString(-randNum1));
            if (!correct) {
                System.out.println(correct + " | " + randNum1);
            }
        }
        System.out.println(">>> ieee754 & floatRepresentation & floatTrueValue test");
        for (int i = 0; i < 100; i++) {
            double randNum = rand.nextDouble() * 9999 - 5000;
            String result2 = alu.floatTrueValue(alu.ieee754(Double.toString(randNum), 64), 11, 52);
            boolean correct = result2.substring(0, 10).equals(Double.toString(randNum).substring(0, 10));
            if (!correct) {
                System.out.println(correct + " | " + randNum);
            }
        }
        System.out.println(">>> integerDivision & ariRightShift test");
        for (int i = 0; i < 100; i++) {
            long randNum1 = rand.nextLong();
            String rep = alu.integerRepresentation(Long.toString(randNum1), 64);
            String result2 = alu.integerDivision(rep, "0100", 64).substring(1, 65);
            String result = alu.ariRightShift(alu.integerRepresentation(Long.toString(randNum1), 64), 2);
            boolean correct = result2.equals(result) || result2.equals(alu.oneAdder(result).substring(1));
            if (!correct) {
                System.out.println(correct + " | " + randNum1);
            }
        }
        System.out.println(">>> floatSubtraction test");
        for (int i = 0; i < 100; i++) {
            double randNum1 = rand.nextDouble() * rand.nextInt(9999) - 5000;
            double randNum2 = rand.nextDouble() * rand.nextInt(9999) - 5000;
            double result = randNum1 - randNum2;
            String resultString = alu.ieee754(Double.toString(result), 64);
            String result2 = alu.floatSubtraction(
                    alu.ieee754(Double.toString(randNum1), 64),
                    alu.ieee754(Double.toString(randNum2), 64),
                    11, 52, 4
            ).substring(1);
            boolean correct = resultString.substring(0, 50).equals(result2.substring(0, 50));
            if (!correct) {
                System.out.println(correct + " | " + randNum1 + " " + randNum2 + " | " + resultString + " | " + result2);
            }
        }
        System.out.println(">>> floatMultiplication test");
        for (int i = 0; i < 100; i++) {
            double randNum1 = rand.nextDouble() * rand.nextInt(999) - 500;
            double randNum2 = rand.nextDouble() * rand.nextInt(999) - 500;
            double result = randNum1 * randNum2;
            String resultString = alu.ieee754(Double.toString(result), 64);
            String result2 = alu.floatMultiplication(
                    alu.ieee754(Double.toString(randNum1), 64),
                    alu.ieee754(Double.toString(randNum2), 64),
                    11, 52
            ).substring(1);
            boolean correct = resultString.substring(0, 50).equals(result2.substring(0, 50));
            if (!correct) {
                System.out.println(correct + " | " + randNum1 + " " + randNum2 + " | " + resultString + " | " + result2);
            }
        }
        System.out.println(">>> floatDivision test");
        for (int i = 0; i < 100; i++) {
            double randNum1 = rand.nextDouble() * rand.nextInt(9999) - 5000;
            double randNum2 = rand.nextDouble() * rand.nextInt(9999) - 5000;
            double result = randNum1 / randNum2;
            String resultString = alu.ieee754(Double.toString(result), 64);
            String result2 = alu.floatDivision(
                    alu.ieee754(Double.toString(randNum1), 64),
                    alu.ieee754(Double.toString(randNum2), 64),
                    11, 52
            ).substring(1);
            boolean correct = resultString.substring(0, 50).equals(result2.substring(0, 50));
            if (!correct) {
                System.out.println(correct + " | " + randNum1 + " " + randNum2 + " | " + resultString + " | " + result2);
            }
        }
    }
}
