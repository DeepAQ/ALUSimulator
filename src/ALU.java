/**
 * ģ��ALU���������͸���������������
 * @author 151250091_������
 *
 */

public class ALU {

	/**
	 * ����ʮ���������Ķ����Ʋ����ʾ��<br/>
	 * ����integerRepresentation("9", 8)
	 * @param number ʮ������������Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʋ����ʾ�ĳ���
	 * @return number�Ķ����Ʋ����ʾ������Ϊlength
	 */
	public String integerRepresentation (String number, int length) {
		// �жϷ��ţ���Ϊ������ȥ������
		boolean negative = false;
		if (number.charAt(0) == '-') {
			negative = true;
			number = number.substring(1);
		}
		// �ַ���תΪʮ��������
		long num = Long.parseLong(number);
		// ʮ����ת������
		StringBuilder result = new StringBuilder();
		while (num > 0) {
			result.insert(0, num % 2);
			num /= 2;
		}
		// ��λ����������з�����չ
		while (result.length() < length) {
			result.insert(0, "0");
		}
		// ��λ������������
		while (result.length() > length) {
			result.deleteCharAt(0);
		}
		// ������Ϊ������ȡ����1
		if (negative) {
			// ȡ��
			for (int i = result.length() - 1; i >= 0; i--) {
				if (result.charAt(i) == '0') {
					result.setCharAt(i, '1');
				} else {
					result.setCharAt(i, '0');
				}
			}
			// ��1
			for (int i = result.length() - 1; i >= 0; i--) {
				if (result.charAt(i) == '0') {
					result.setCharAt(i, '1');
					break;
				} else {
					result.setCharAt(i, '0');
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * ����ʮ���Ƹ������Ķ����Ʊ�ʾ��
	 * ��Ҫ���� 0������񻯡����������+Inf���͡�-Inf������ NaN�����أ������� IEEE 754��
	 * �������Ϊ��0���롣<br/>
	 * ����floatRepresentation("11.375", 8, 11)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return number�Ķ����Ʊ�ʾ������Ϊ 1+eLength+sLength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		int sign = 0;
		String exponent = "";
		String fraction = "";
		// �жϷ���
		if (number.charAt(0) == '-') {
			sign = 1;
			number = number.substring(1);
		}
		else if (number.charAt(0) == '+') {
			number = number.substring(1);
		}
		// �ж�����
		if (number.equals("Inf")) {
			// ָ��ȫ���Ϊ1
			for (int i = 0; i < eLength; i++) {
				exponent = exponent + "1";
			}
			// β��ȫ���Ϊ0
			for (int i = 0; i < sLength; i++) {
				fraction = fraction + "0";
			}
		}
		// �ж�NaN
		else if (number.equals("NaN")) {
			// ָ��ȫ���Ϊ1
			for (int i = 0; i < eLength; i++) {
				exponent = exponent + "1";
			}
			// β��ȫ���Ϊ1
			for (int i = 0; i < sLength; i++) {
				fraction = fraction + "1";
			}
		}
		// �ж�0
		else if (number.equals("0")) {
			// ָ��ȫ���Ϊ0
			for (int i = 0; i < eLength; i++) {
				exponent = exponent + "0";
			}
			// β��ȫ���Ϊ0
			for (int i = 0; i < sLength; i++) {
				fraction = fraction + "0";
			}
		}
		else {
			// ��������תΪ������
			String intBin = "";
			String decBin = "";
			String[] parts = number.split("\\.");
			long intNum = Long.parseLong(parts[0]);
			double decNum = 0;
			if (parts.length > 1) {
				decNum = Double.parseDouble("0." + parts[1]);
			}
			while (intNum > 0) {
				intBin = (intNum % 2) + intBin;
				intNum /= 2;
			}
			while (decNum > 0) {
				decNum *= 2;
				if (decNum >= 1) {
					decNum -= 1;
					decBin = decBin + "1";
				} else {
					decBin = decBin + "0";
				}
			}
			// ����β��
			long exponentNum = 0;
			if (intBin.indexOf('1') >= 0) {
				// С��������
				fraction = intBin.substring(intBin.indexOf('1') + 1) + decBin;
				exponentNum = intBin.length() - intBin.indexOf('1') - 1;
			}
			else if (decBin.indexOf('1') >= 0) {
				// С��������
				fraction = decBin.substring(decBin.indexOf('1') + 1);
				exponentNum = -1 - decBin.indexOf('1');
			}
			// ����ָ������
			exponentNum += (long) (Math.pow(2, eLength - 1) - 1);
			if (exponentNum > 0) {
				// ���
				exponent = integerRepresentation(Long.toString(exponentNum), eLength);
			} else {
				// �ǹ��
				fraction = "1" + fraction;
				// ָ��ȫ���Ϊ0
				for (int i = 0; i < eLength; i++) {
					exponent = exponent + "0";
				}
				while (exponentNum < 0) {
					// С��������
					fraction = "0" + fraction;
					exponentNum++;
				}
			}
			// ȷ��β��λ��
			while (fraction.length() < sLength) {
				fraction = fraction + "0";
			}
			while (fraction.length() > sLength) {
				// ��0����
				fraction = fraction.substring(0, fraction.length() - 1);
			}
		}
		return sign + exponent + fraction;
	}
	
	/**
	 * ����ʮ���Ƹ�������IEEE 754��ʾ��Ҫ�����{@link #floatRepresentation(String, int, int) floatRepresentation}ʵ�֡�<br/>
	 * ����ieee754("11.375", 32)
	 * @param number ʮ���Ƹ�����������С���㡣��Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 * @param length �����Ʊ�ʾ�ĳ��ȣ�Ϊ32��64
	 * @return number��IEEE 754��ʾ������Ϊlength���������ң�����Ϊ���š�ָ���������ʾ����β������λ���أ�
	 */
	public String ieee754 (String number, int length) {
		if (length == 32) {
			return floatRepresentation(number, 8, 23);
		}
		else if (length == 64) {
			return floatRepresentation(number, 11, 52);
		}
		else {
			return null;
		}
	}
	
	/**
	 * ��������Ʋ����ʾ����������ֵ��<br/>
	 * ����integerTrueValue("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ
	 */
	public String integerTrueValue (String operand) {
		long trueValue = 0;
		// �жϸ���
		if (operand.charAt(0) == '1') {
			trueValue = (long) -Math.pow(2, operand.length() - 1);
		}
		// ת��Ϊʮ����
		for (int i = 1; i < operand.length(); i++) {
			if (operand.charAt(i) == '1') {
				trueValue += (long) Math.pow(2, operand.length() - i - 1);
			}
		}
		return Long.toString(trueValue);
	}
	
	/**
	 * ���������ԭ���ʾ�ĸ���������ֵ��<br/>
	 * ����floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return operand����ֵ����Ϊ���������һλΪ��-������Ϊ������ 0�����޷���λ����������ֱ��ʾΪ��+Inf���͡�-Inf���� NaN��ʾΪ��NaN��
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		// �жϷ���λ
		boolean negative = false;
		if (operand.charAt(0) == '1') {
			negative = true;
		}
		String exponent = operand.substring(1, 1 + eLength);
		String fraction = operand.substring(1 + eLength, 1 + eLength + sLength);
		long expNum = Long.parseLong(integerTrueValue("0" + exponent)); // - (int) Math.pow(2, eLength - 1) + 1;
		long fracNum = Long.parseLong(integerTrueValue("0" + fraction));
		if (expNum == (long) (Math.pow(2, eLength) - 1)) {
			// ����� �� NaN
			if (fracNum == 0) {
				if (negative) return "-Inf";
				else return "+Inf";
			} else {
				return "NaN";
			}
		}
		else if (expNum == 0) {
			// 0 �� �ǹ����
			if (fracNum == 0) {
				return "0";
			} else {
				// �ȼ��� 0.fraction
				double result = fracNum * Math.pow(2, -sLength);
				result = result * Math.pow(2, 2 - Math.pow(2, eLength - 1));
				if (negative) result = -result;
				return Double.toString(result);
			}
		}
		else {
			// �����
			fracNum = Long.parseLong(integerTrueValue("01" + fraction));
			double result = fracNum * Math.pow(2, -sLength);
			result = result * Math.pow(2, expNum - Math.pow(2, eLength - 1) + 1);
			if (negative) result = -result;
			return Double.toString(result);
		}
	}
	
	/**
	 * ��λȡ��������<br/>
	 * ����negation("00001001")
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @return operand��λȡ���Ľ��
	 */
	public String negation (String operand) {
		StringBuilder result = new StringBuilder(operand);
		for (int i = 0; i < result.length(); i++) {
			// ���÷���
			result.setCharAt(i, not(result.charAt(i)));
		}
		return result.toString();
	}

	private char not(char c) {
		if (c == '0') return '1';
		else return '0';
	}
	
	/**
	 * ���Ʋ�����<br/>
	 * ����leftShift("00001001", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand����nλ�Ľ��
	 */
	public String leftShift (String operand, int n) {
		StringBuilder result = new StringBuilder(operand);
		int i = n;
		while (i > 0) {
			result.deleteCharAt(0);
			result.append("0");
			i--;
		}
		return result.toString();
	}
	
	/**
	 * �߼����Ʋ�����<br/>
	 * ����logRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand�߼�����nλ�Ľ��
	 */
	public String logRightShift (String operand, int n) {
		StringBuilder result = new StringBuilder(operand);
		int i = n;
		while (i > 0) {
			result.deleteCharAt(result.length() - 1);
			result.insert(0, '0');
			i--;
		}
		return result.toString();
	}
	
	/**
	 * �������Ʋ�����<br/>
	 * ����ariRightShift("11110110", 2)
	 * @param operand �����Ʊ�ʾ�Ĳ�����
	 * @param n ���Ƶ�λ��
	 * @return operand��������nλ�Ľ��
	 */
	public String ariRightShift (String operand, int n) {
		StringBuilder result = new StringBuilder(operand);
		int i = n;
		char sign = operand.charAt(0);
		while (i > 0) {
			result.deleteCharAt(result.length() - 1);
			result.insert(0, sign);
			i--;
		}
		return result.toString();
	}
	
	/**
	 * ȫ����������λ�Լ���λ���мӷ����㡣<br/>
	 * ����fullAdder('1', '1', '0')
	 * @param x ��������ĳһλ��ȡ0��1
	 * @param y ������ĳһλ��ȡ0��1
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ��ӵĽ�����ó���Ϊ2���ַ�����ʾ����1λ��ʾ��λ����2λ��ʾ��
	 */
	public String fullAdder (char x, char y, char c) {
		char sum = xor(xor(x, y), c);
		char carry = or(or(and(x, c), and(y, c)), and(x, y));
		return new StringBuilder().append(carry).append(sum).toString();
	}

	private char and(char x, char y) {
		if (x == '1' && y == '1') return '1';
		else return '0';
	}

	private char or(char x, char y) {
		if (x == '0' && y == '0') return '0';
		else return '1';
	}

	private char xor(char x, char y) {
		if (x == y) return '0';
		else return '1';
	}
	
	/**
	 * 4λ���н�λ�ӷ�����Ҫ�����{@link #fullAdder(char, char, char) fullAdder}��ʵ��<br/>
	 * ����claAdder("1001", "0001", '1')
	 * @param operand1 4λ�����Ʊ�ʾ�ı�����
	 * @param operand2 4λ�����Ʊ�ʾ�ļ���
	 * @param c ��λ�Ե�ǰλ�Ľ�λ��ȡ0��1
	 * @return ����Ϊ5���ַ�����ʾ�ļ����������е�1λ�����λ��λ����4λ����ӽ�������н�λ��������ѭ�����
	 */
	public String claAdder (String operand1, String operand2, char c) {
		// �ȼ������� Pi, Gi
		char[] p = new char[4];
		char[] g = new char[4];
		for (int i = 0; i < 4; i++) {
			p[i] = or(operand1.charAt(i), operand2.charAt(i));
			g[i] = and(operand1.charAt(i), operand2.charAt(i));
		}
		// �������� Ci
		char[] carry = new char[5];
		carry[4] = c;
		carry[3] = or(g[3], and(p[3], c));
		carry[2] = or(g[2], and(p[2], or(g[3], and(p[3], c))));
		carry[1] = or(g[1], and(p[1], or(g[2], and(p[2], or(g[3], and(p[3], c))))));
		carry[0] = or(g[0], and(p[0], or(g[1], and(p[1], or(g[2], and(p[2], or(g[3], and(p[3], c))))))));
		// ������
		String result = carry[0] + "";
		for (int i = 0; i < 4; i++) {
			result = result + fullAdder(operand1.charAt(i), operand2.charAt(i), carry[i+1]).substring(1);
		}
		return result;
	}
	
	/**
	 * ��һ����ʵ�ֲ�������1�����㡣
	 * ��Ҫ�������š����š�����ŵ�ģ�⣬
	 * ������ֱ�ӵ���{@link #fullAdder(char, char, char) fullAdder}��
	 * {@link #claAdder(String, String, char) claAdder}��
	 * {@link #adder(String, String, char, int) adder}��
	 * {@link #integerAddition(String, String, int) integerAddition}������<br/>
	 * ����oneAdder("00001001")
	 * @param operand �����Ʋ����ʾ�Ĳ�����
	 * @return operand��1�Ľ��������Ϊoperand�ĳ��ȼ�1�����е�1λָʾ�Ƿ���������Ϊ1������Ϊ0��������λΪ��ӽ��
	 */
	public String oneAdder (String operand) {
		String result = "";
		char carry = '0';
		for (int i = operand.length() - 1; i >= 0; i--) {
			char x;
			if (i == operand.length() - 1) {
				x = '1';
			} else {
				x = '0';
			}
			char sum = xor(xor(operand.charAt(i), x), carry);
			carry = or(or(and(operand.charAt(i), carry), and(x, carry)), and(operand.charAt(i), x));
			result = sum + result;
		}
		// �ж��Ƿ����
		char overflow = and(and(not(operand.charAt(0)), '1'), result.charAt(0));
		return overflow + result;
	}
	
	/**
	 * �ӷ�����Ҫ�����{@link #claAdder(String, String, char)}����ʵ�֡�<br/>
	 * ����adder("0100", "0011", '0', 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param c ���λ��λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		// ���������ĳ���С��length����з�����չ
		char sign1 = operand1.charAt(0);
		while (operand1.length() < length) {
			operand1 = sign1 + operand1;
		}
		char sign2 = operand2.charAt(0);
		while (operand2.length() < length) {
			operand2 = sign2 + operand2;
		}
		String result = "";
		char carry = c;
		for (int i = length/4 - 1; i >= 0; i--) {
			String tmpResult = claAdder(operand1.substring(4*i, 4*i+4), operand2.substring(4*i, 4*i+4), carry);
			carry = tmpResult.charAt(0);
			result = tmpResult.substring(1) + result;
		}
		// �ж��Ƿ����
		char overflow = or(and(and(operand1.charAt(0), operand2.charAt(0)), not(result.charAt(0))),
				and(and(not(operand1.charAt(0)), not(operand2.charAt(0))), result.charAt(0)));
		return overflow + result;
	}
	
	/**
	 * �����ӷ���Ҫ�����{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerAddition("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����ӽ��
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1, operand2, '0', length);
	}
	
	/**
	 * �����������ɵ���{@link #adder(String, String, char, int) adder}����ʵ�֡�<br/>
	 * ����integerSubtraction("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ļ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ��������
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		// �Լ���ȡ����һ
		operand2 = oneAdder(negation(operand2)).substring(1);
		return adder(operand1, operand2, '0', length);
	}
	
	/**
	 * �����˷���ʹ��Booth�㷨ʵ�֣��ɵ���{@link #adder(String, String, char, int) adder}�ȷ�����<br/>
	 * ����integerMultiplication("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊlength+1���ַ�����ʾ����˽�������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������lengthλ����˽��
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * �����Ĳ��ָ������������ɵ���{@link #adder(String, String, char, int) adder}�ȷ���ʵ�֡�<br/>
	 * ����integerDivision("0100", "0011", 8)
	 * @param operand1 �����Ʋ����ʾ�ı�����
	 * @param operand2 �����Ʋ����ʾ�ĳ���
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ���ĳ���������ĳ���С��lengthʱ����Ҫ�ڸ�λ������λ
	 * @return ����Ϊ2*length+1���ַ�����ʾ�������������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0�������lengthλΪ�̣����lengthλΪ����
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * �����������ӷ������Ե���{@link #adder(String, String, char, int) adder}�ȷ�����
	 * ������ֱ�ӽ�������ת��Ϊ�����ʹ��{@link #integerAddition(String, String, int) integerAddition}��
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}��ʵ�֡�<br/>
	 * ����signedAddition("1100", "1011", 8)
	 * @param operand1 ������ԭ���ʾ�ı����������е�1λΪ����λ
	 * @param operand2 ������ԭ���ʾ�ļ��������е�1λΪ����λ
	 * @param length ��Ų������ļĴ����ĳ��ȣ�Ϊ4�ı�����length��С�ڲ������ĳ��ȣ����������ţ�����ĳ���������ĳ���С��lengthʱ����Ҫ���䳤����չ��length
	 * @return ����Ϊlength+2���ַ�����ʾ�ļ����������е�1λָʾ�Ƿ���������Ϊ1������Ϊ0������2λΪ����λ����lengthλ����ӽ��
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * �������ӷ����ɵ���{@link #signedAddition(String, String, int) signedAddition}�ȷ���ʵ�֡�<br/>
	 * ����floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����ӽ�������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * �������������ɵ���{@link #floatAddition(String, String, int, int, int) floatAddition}����ʵ�֡�<br/>
	 * ����floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ļ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param gLength ����λ�ĳ���
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ�������������е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * �������˷����ɵ���{@link #integerMultiplication(String, String, int) integerMultiplication}�ȷ���ʵ�֡�<br/>
	 * ����floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * �������������ɵ���{@link #integerDivision(String, String, int) integerDivision}�ȷ���ʵ�֡�<br/>
	 * ����floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 �����Ʊ�ʾ�ı�����
	 * @param operand2 �����Ʊ�ʾ�ĳ���
	 * @param eLength ָ���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @param sLength β���ĳ��ȣ�ȡֵ���ڵ��� 4
	 * @return ����Ϊ2+eLength+sLength���ַ�����ʾ����˽��,���е�1λָʾ�Ƿ�ָ�����磨���Ϊ1������Ϊ0��������λ����������Ϊ���š�ָ���������ʾ����β������λ���أ����������Ϊ��0����
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
}
