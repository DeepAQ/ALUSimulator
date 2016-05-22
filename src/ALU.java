/**
 * 模拟ALU进行整数和浮点数的四则运算
 * @author 151250091_梁家铭
 *
 */

public class ALU {

	/**
	 * 生成十进制整数的二进制补码表示。<br/>
	 * 例：integerRepresentation("9", 8)
	 * @param number 十进制整数。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制补码表示的长度
	 * @return number的二进制补码表示，长度为length
	 */
	public String integerRepresentation (String number, int length) {
		// 判断符号，若为负数则去除符号
		boolean negative = false;
		if (number.charAt(0) == '-') {
			negative = true;
			number = number.substring(1);
		}
		// 字符串转为十进制整数
		long num = Long.parseLong(number);
		// 十进制转二进制
		StringBuilder result = new StringBuilder();
		while (num > 0) {
			result.insert(0, num % 2);
			num /= 2;
		}
		// 若位数不足则进行符号扩展
		while (result.length() < length) {
			result.insert(0, "0");
		}
		// 若位数过多则左移
		while (result.length() > length) {
			result.deleteCharAt(0);
		}
		// 若输入为负数则取反加1
		if (negative) {
			// 取反
			for (int i = result.length() - 1; i >= 0; i--) {
				if (result.charAt(i) == '0') {
					result.setCharAt(i, '1');
				} else {
					result.setCharAt(i, '0');
				}
			}
			// 加1
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
	 * 生成十进制浮点数的二进制表示。
	 * 需要考虑 0、反规格化、正负无穷（“+Inf”和“-Inf”）、 NaN等因素，具体借鉴 IEEE 754。
	 * 舍入策略为向0舍入。<br/>
	 * 例：floatRepresentation("11.375", 8, 11)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return number的二进制表示，长度为 1+eLength+sLength。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
	 */
	public String floatRepresentation (String number, int eLength, int sLength) {
		int sign = 0;
		String exponent = "";
		String fraction = "";
		// 判断符号
		if (number.charAt(0) == '-') {
			sign = 1;
			number = number.substring(1);
		}
		else if (number.charAt(0) == '+') {
			number = number.substring(1);
		}
		// 判断无穷
		if (number.equals("Inf")) {
			// 指数全填充为1
			for (int i = 0; i < eLength; i++) {
				exponent = exponent + "1";
			}
			// 尾数全填充为0
			for (int i = 0; i < sLength; i++) {
				fraction = fraction + "0";
			}
		}
		// 判断NaN
		else if (number.equals("NaN")) {
			// 指数全填充为1
			for (int i = 0; i < eLength; i++) {
				exponent = exponent + "1";
			}
			// 尾数全填充为1
			for (int i = 0; i < sLength; i++) {
				fraction = fraction + "1";
			}
		}
		// 判断0
		else if (number.equals("0")) {
			// 指数全填充为0
			for (int i = 0; i < eLength; i++) {
				exponent = exponent + "0";
			}
			// 尾数全填充为0
			for (int i = 0; i < sLength; i++) {
				fraction = fraction + "0";
			}
		}
		else {
			// 将浮点数转为二进制
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
			// 计算尾数
			long exponentNum = 0;
			if (intBin.indexOf('1') >= 0) {
				// 小数点左移
				fraction = intBin.substring(intBin.indexOf('1') + 1) + decBin;
				exponentNum = intBin.length() - intBin.indexOf('1') - 1;
			}
			else if (decBin.indexOf('1') >= 0) {
				// 小数点右移
				fraction = decBin.substring(decBin.indexOf('1') + 1);
				exponentNum = -1 - decBin.indexOf('1');
			}
			// 计算指数移码
			exponentNum += (long) (Math.pow(2, eLength - 1) - 1);
			if (exponentNum > 0) {
				// 规格化
				exponent = integerRepresentation(Long.toString(exponentNum), eLength);
			} else {
				// 非规格化
				fraction = "1" + fraction;
				// 指数全填充为0
				for (int i = 0; i < eLength; i++) {
					exponent = exponent + "0";
				}
				while (exponentNum < 0) {
					// 小数点左移
					fraction = "0" + fraction;
					exponentNum++;
				}
			}
			// 确定尾数位数
			while (fraction.length() < sLength) {
				fraction = fraction + "0";
			}
			while (fraction.length() > sLength) {
				// 向0舍入
				fraction = fraction.substring(0, fraction.length() - 1);
			}
		}
		return sign + exponent + fraction;
	}
	
	/**
	 * 生成十进制浮点数的IEEE 754表示，要求调用{@link #floatRepresentation(String, int, int) floatRepresentation}实现。<br/>
	 * 例：ieee754("11.375", 32)
	 * @param number 十进制浮点数，包含小数点。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 * @param length 二进制表示的长度，为32或64
	 * @return number的IEEE 754表示，长度为length。从左向右，依次为符号、指数（移码表示）、尾数（首位隐藏）
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
	 * 计算二进制补码表示的整数的真值。<br/>
	 * 例：integerTrueValue("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位
	 */
	public String integerTrueValue (String operand) {
		long trueValue = 0;
		// 判断负数
		if (operand.charAt(0) == '1') {
			trueValue = (long) -Math.pow(2, operand.length() - 1);
		}
		// 转换为十进制
		for (int i = 1; i < operand.length(); i++) {
			if (operand.charAt(i) == '1') {
				trueValue += (long) Math.pow(2, operand.length() - i - 1);
			}
		}
		return Long.toString(trueValue);
	}
	
	/**
	 * 计算二进制原码表示的浮点数的真值。<br/>
	 * 例：floatTrueValue("01000001001101100000", 8, 11)
	 * @param operand 二进制表示的操作数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return operand的真值。若为负数；则第一位为“-”；若为正数或 0，则无符号位。正负无穷分别表示为“+Inf”和“-Inf”， NaN表示为“NaN”
	 */
	public String floatTrueValue (String operand, int eLength, int sLength) {
		// 判断符号位
		boolean negative = false;
		if (operand.charAt(0) == '1') {
			negative = true;
		}
		String exponent = operand.substring(1, 1 + eLength);
		String fraction = operand.substring(1 + eLength, 1 + eLength + sLength);
		long expNum = Long.parseLong(integerTrueValue("0" + exponent)); // - (int) Math.pow(2, eLength - 1) + 1;
		long fracNum = Long.parseLong(integerTrueValue("0" + fraction));
		if (expNum == (long) (Math.pow(2, eLength) - 1)) {
			// 无穷大 或 NaN
			if (fracNum == 0) {
				if (negative) return "-Inf";
				else return "+Inf";
			} else {
				return "NaN";
			}
		}
		else if (expNum == 0) {
			// 0 或 非规格化数
			if (fracNum == 0) {
				return "0";
			} else {
				// 先计算 0.fraction
				double result = fracNum * Math.pow(2, -sLength);
				result = result * Math.pow(2, 2 - Math.pow(2, eLength - 1));
				if (negative) result = -result;
				return Double.toString(result);
			}
		}
		else {
			// 规格化数
			fracNum = Long.parseLong(integerTrueValue("01" + fraction));
			double result = fracNum * Math.pow(2, -sLength);
			result = result * Math.pow(2, expNum - Math.pow(2, eLength - 1) + 1);
			if (negative) result = -result;
			return Double.toString(result);
		}
	}
	
	/**
	 * 按位取反操作。<br/>
	 * 例：negation("00001001")
	 * @param operand 二进制表示的操作数
	 * @return operand按位取反的结果
	 */
	public String negation (String operand) {
		StringBuilder result = new StringBuilder(operand);
		for (int i = 0; i < result.length(); i++) {
			// 调用非门
			result.setCharAt(i, not(result.charAt(i)));
		}
		return result.toString();
	}

	private char not(char c) {
		if (c == '0') return '1';
		else return '0';
	}
	
	/**
	 * 左移操作。<br/>
	 * 例：leftShift("00001001", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 左移的位数
	 * @return operand左移n位的结果
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
	 * 逻辑右移操作。<br/>
	 * 例：logRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand逻辑右移n位的结果
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
	 * 算术右移操作。<br/>
	 * 例：ariRightShift("11110110", 2)
	 * @param operand 二进制表示的操作数
	 * @param n 右移的位数
	 * @return operand算术右移n位的结果
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
	 * 全加器，对两位以及进位进行加法运算。<br/>
	 * 例：fullAdder('1', '1', '0')
	 * @param x 被加数的某一位，取0或1
	 * @param y 加数的某一位，取0或1
	 * @param c 低位对当前位的进位，取0或1
	 * @return 相加的结果，用长度为2的字符串表示，第1位表示进位，第2位表示和
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
	 * 4位先行进位加法器。要求采用{@link #fullAdder(char, char, char) fullAdder}来实现<br/>
	 * 例：claAdder("1001", "0001", '1')
	 * @param operand1 4位二进制表示的被加数
	 * @param operand2 4位二进制表示的加数
	 * @param c 低位对当前位的进位，取0或1
	 * @return 长度为5的字符串表示的计算结果，其中第1位是最高位进位，后4位是相加结果，其中进位不可以由循环获得
	 */
	public String claAdder (String operand1, String operand2, char c) {
		// 先计算所有 Pi, Gi
		char[] p = new char[4];
		char[] g = new char[4];
		for (int i = 0; i < 4; i++) {
			p[i] = or(operand1.charAt(i), operand2.charAt(i));
			g[i] = and(operand1.charAt(i), operand2.charAt(i));
		}
		// 计算所有 Ci
		char[] carry = new char[5];
		carry[4] = c;
		carry[3] = or(g[3], and(p[3], c));
		carry[2] = or(g[2], and(p[2], or(g[3], and(p[3], c))));
		carry[1] = or(g[1], and(p[1], or(g[2], and(p[2], or(g[3], and(p[3], c))))));
		carry[0] = or(g[0], and(p[0], or(g[1], and(p[1], or(g[2], and(p[2], or(g[3], and(p[3], c))))))));
		// 计算结果
		String result = carry[0] + "";
		for (int i = 0; i < 4; i++) {
			result = result + fullAdder(operand1.charAt(i), operand2.charAt(i), carry[i+1]).substring(1);
		}
		return result;
	}
	
	/**
	 * 加一器，实现操作数加1的运算。
	 * 需要采用与门、或门、异或门等模拟，
	 * 不可以直接调用{@link #fullAdder(char, char, char) fullAdder}、
	 * {@link #claAdder(String, String, char) claAdder}、
	 * {@link #adder(String, String, char, int) adder}、
	 * {@link #integerAddition(String, String, int) integerAddition}方法。<br/>
	 * 例：oneAdder("00001001")
	 * @param operand 二进制补码表示的操作数
	 * @return operand加1的结果，长度为operand的长度加1，其中第1位指示是否溢出（溢出为1，否则为0），其余位为相加结果
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
		// 判断是否溢出
		char overflow = and(and(not(operand.charAt(0)), '1'), result.charAt(0));
		return overflow + result;
	}
	
	/**
	 * 加法器，要求调用{@link #claAdder(String, String, char)}方法实现。<br/>
	 * 例：adder("0100", "0011", '0', 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param c 最低位进位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String adder (String operand1, String operand2, char c, int length) {
		// 若操作数的长度小于length则进行符号扩展
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
		// 判断是否溢出
		char overflow = or(and(and(operand1.charAt(0), operand2.charAt(0)), not(result.charAt(0))),
				and(and(not(operand1.charAt(0)), not(operand2.charAt(0))), result.charAt(0)));
		return overflow + result;
	}
	
	/**
	 * 整数加法，要求调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerAddition("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被加数
	 * @param operand2 二进制补码表示的加数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相加结果
	 */
	public String integerAddition (String operand1, String operand2, int length) {
		return adder(operand1, operand2, '0', length);
	}
	
	/**
	 * 整数减法，可调用{@link #adder(String, String, char, int) adder}方法实现。<br/>
	 * 例：integerSubtraction("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被减数
	 * @param operand2 二进制补码表示的减数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相减结果
	 */
	public String integerSubtraction (String operand1, String operand2, int length) {
		// 对减数取反加一
		operand2 = oneAdder(negation(operand2)).substring(1);
		return adder(operand1, operand2, '0', length);
	}
	
	/**
	 * 整数乘法，使用Booth算法实现，可调用{@link #adder(String, String, char, int) adder}等方法。<br/>
	 * 例：integerMultiplication("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被乘数
	 * @param operand2 二进制补码表示的乘数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为length+1的字符串表示的相乘结果，其中第1位指示是否溢出（溢出为1，否则为0），后length位是相乘结果
	 */
	public String integerMultiplication (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 整数的不恢复余数除法，可调用{@link #adder(String, String, char, int) adder}等方法实现。<br/>
	 * 例：integerDivision("0100", "0011", 8)
	 * @param operand1 二进制补码表示的被除数
	 * @param operand2 二进制补码表示的除数
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度，当某个操作数的长度小于length时，需要在高位补符号位
	 * @return 长度为2*length+1的字符串表示的相除结果，其中第1位指示是否溢出（溢出为1，否则为0），其后length位为商，最后length位为余数
	 */
	public String integerDivision (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 带符号整数加法，可以调用{@link #adder(String, String, char, int) adder}等方法，
	 * 但不能直接将操作数转换为补码后使用{@link #integerAddition(String, String, int) integerAddition}、
	 * {@link #integerSubtraction(String, String, int) integerSubtraction}来实现。<br/>
	 * 例：signedAddition("1100", "1011", 8)
	 * @param operand1 二进制原码表示的被加数，其中第1位为符号位
	 * @param operand2 二进制原码表示的加数，其中第1位为符号位
	 * @param length 存放操作数的寄存器的长度，为4的倍数。length不小于操作数的长度（不包含符号），当某个操作数的长度小于length时，需要将其长度扩展到length
	 * @return 长度为length+2的字符串表示的计算结果，其中第1位指示是否溢出（溢出为1，否则为0），第2位为符号位，后length位是相加结果
	 */
	public String signedAddition (String operand1, String operand2, int length) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数加法，可调用{@link #signedAddition(String, String, int) signedAddition}等方法实现。<br/>
	 * 例：floatAddition("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被加数
	 * @param operand2 二进制表示的加数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相加结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatAddition (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数减法，可调用{@link #floatAddition(String, String, int, int, int) floatAddition}方法实现。<br/>
	 * 例：floatSubtraction("00111111010100000", "00111111001000000", 8, 8, 8)
	 * @param operand1 二进制表示的被减数
	 * @param operand2 二进制表示的减数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @param gLength 保护位的长度
	 * @return 长度为2+eLength+sLength的字符串表示的相减结果，其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatSubtraction (String operand1, String operand2, int eLength, int sLength, int gLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数乘法，可调用{@link #integerMultiplication(String, String, int) integerMultiplication}等方法实现。<br/>
	 * 例：floatMultiplication("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被乘数
	 * @param operand2 二进制表示的乘数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatMultiplication (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
	
	/**
	 * 浮点数除法，可调用{@link #integerDivision(String, String, int) integerDivision}等方法实现。<br/>
	 * 例：floatDivision("00111110111000000", "00111111000000000", 8, 8)
	 * @param operand1 二进制表示的被除数
	 * @param operand2 二进制表示的除数
	 * @param eLength 指数的长度，取值大于等于 4
	 * @param sLength 尾数的长度，取值大于等于 4
	 * @return 长度为2+eLength+sLength的字符串表示的相乘结果,其中第1位指示是否指数上溢（溢出为1，否则为0），其余位从左到右依次为符号、指数（移码表示）、尾数（首位隐藏）。舍入策略为向0舍入
	 */
	public String floatDivision (String operand1, String operand2, int eLength, int sLength) {
		// TODO YOUR CODE HERE.
		return null;
	}
}
