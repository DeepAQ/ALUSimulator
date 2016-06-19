import static org.junit.Assert.*;

/**
 * Created by adn55 on 16/6/16.
 */
public class ALUTestSimple {
    private ALU alu;

    @org.junit.Before
    public void setUp() throws Exception {
        this.alu = new ALU();
    }

    @org.junit.Test
    public void integerRepresentation() throws Exception {
        assertEquals(alu.integerRepresentation("9", 8), "00001001");
    }

    @org.junit.Test
    public void floatRepresentation() throws Exception {
        assertEquals(alu.floatRepresentation("11.375", 8, 11), "01000001001101100000");
    }

    @org.junit.Test
    public void ieee754() throws Exception {
        assertEquals(alu.ieee754("11.375", 32), "01000001001101100000000000000000");
    }

    @org.junit.Test
    public void integerTrueValue() throws Exception {
        assertEquals(alu.integerTrueValue("00001001"), "9");
    }

    @org.junit.Test
    public void floatTrueValue() throws Exception {
        assertEquals(alu.floatTrueValue("01000001001101100000", 8, 11), "11.375");
    }

    @org.junit.Test
    public void negation() throws Exception {
        assertEquals(alu.negation("00001001"), "11110110");
    }

    @org.junit.Test
    public void leftShift() throws Exception {
        assertEquals(alu.leftShift("00001001", 2), "00100100");
    }

    @org.junit.Test
    public void logRightShift() throws Exception {
        assertEquals(alu.logRightShift("11110110", 2), "00111101");
    }

    @org.junit.Test
    public void ariRightShift() throws Exception {
        assertEquals(alu.ariRightShift("11110110", 2), "11111101");
    }

    @org.junit.Test
    public void fullAdder() throws Exception {
        assertEquals(alu.fullAdder('1', '1', '0'), "10");
    }

    @org.junit.Test
    public void claAdder() throws Exception {
        assertEquals(alu.claAdder("1001", "0001", '1'), "01011");
    }

    @org.junit.Test
    public void oneAdder() throws Exception {
        assertEquals(alu.oneAdder("00001001"), "000001010");
    }

    @org.junit.Test
    public void adder() throws Exception {
        assertEquals(alu.adder("0100", "0011", '0', 8), "000000111");
    }

    @org.junit.Test
    public void integerAddition() throws Exception {
        assertEquals(alu.integerAddition("0100", "0011", 8), "000000111");
    }

    @org.junit.Test
    public void integerSubtraction() throws Exception {
        assertEquals(alu.integerSubtraction("0100", "0011", 8), "000000001");
    }

    @org.junit.Test
    public void integerMultiplication() throws Exception {
        assertEquals(alu.integerMultiplication("0100", "0011", 8), "000001100");
    }

    @org.junit.Test
    public void integerDivision() throws Exception {
        assertEquals(alu.integerDivision("0100", "0011", 8), "00000000100000001");
    }

    @org.junit.Test
    public void signedAddition() throws Exception {
        assertEquals(alu.signedAddition("1100", "1011", 8), "0100000111");
    }

    @org.junit.Test
    public void floatAddition() throws Exception {
        assertEquals(alu.floatAddition("00111111010100000",
                "00111111001000000", 8, 8, 4), "000111111101110000");
    }

    @org.junit.Test
    public void floatSubtraction() throws Exception {
        assertEquals(alu.floatSubtraction("00111111010100000",
                "00111111001000000", 8, 8, 4), "000111110010000000");
    }

    @org.junit.Test
    public void floatMultiplication() throws Exception {
        assertEquals(alu.floatMultiplication("00111110111000000",
                "00111111000000000", 8, 8), "000111110011000000");
    }

    @org.junit.Test
    public void floatDivision() throws Exception {
        assertEquals(alu.floatDivision("00111110111000000",
                "00111111000000000", 8, 8), "000111111011000000");
    }

}