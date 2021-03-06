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
		String result = "";
		while (num > 0) {
			result = (num % 2) + result;
			num /= 2;
		}
		// 若位数不足则进行符号扩展
		while (result.length() < length) {
			result = "0" + result;
		}
		// 若位数过多则左移
		while (result.length() > length) {
			result = result.substring(1);
		}
		// 若输入为负数则取反加1
		if (negative) {
			result = oneAdder(negation(result)).substring(1);
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
		char sign = operand.charAt(0);
		while (n > 0) {
			result.deleteCharAt(result.length() - 1);
			result.insert(0, sign);
			n--;
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
		char overflow = '0';
		// 对减数取反加一
		operand2 = oneAdder(negation(operand2));
		if (operand2.charAt(0) == '1') {
			overflow = '1';
		}
		String result = adder(operand1, operand2.substring(1), '0', length);
		if (result.charAt(0) == '1') {
			overflow = '1';
		}
		return overflow + result.substring(1);
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
		// 若操作数的长度小于length则进行符号扩展
		char sign1 = operand1.charAt(0);
		while (operand1.length() < length) {
			operand1 = sign1 + operand1;
		}
		char sign2 = operand2.charAt(0);
		while (operand2.length() < length) {
			operand2 = sign2 + operand2;
		}
		// 填充结果及Y
		char overflow = '0';
		String prod = integerRepresentation("0", length);
		StringBuilder y = new StringBuilder(operand2 + "0");
		for (int i = 0; i < length; i++) {
			switch (y.charAt(y.length() - 1) - y.charAt(y.length() - 2)) {
				case 1:
					prod = integerAddition(prod, operand1, length);
					if (prod.charAt(0) == '1') {
						overflow = '1';
					}
					prod = prod.substring(1);
					break;
				case -1:
					prod = integerSubtraction(prod, operand1, length);
					if (prod.charAt(0) == '1') {
						overflow = '1';
					}
					prod = prod.substring(1);
					break;
			}
			// 右移
			y.deleteCharAt(y.length() - 1);
			y.insert(0, prod.charAt(prod.length() - 1));
			prod = ariRightShift(prod, 1);
		}
		y.deleteCharAt(y.length() - 1);
		// 判断是否溢出
		if (!ariRightShift(prod, length).equals(prod) || prod.charAt(0) != y.charAt(0)) {
			overflow = '1';
		}
		return overflow + y.toString();
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
		String result = "";
		// 预处理
		if (!operand2.contains("1")) {
			return "NaN";
		} else if (!operand1.contains("1")) {
			while (result.length() < 2*length+1) {
				result = result + "0";
			}
		} else {
			// 若操作数的长度小于length则进行符号扩展
			char sign1 = operand1.charAt(0);
			while (operand1.length() < length) {
				operand1 = sign1 + operand1;
			}
			char sign2 = operand2.charAt(0);
			while (operand2.length() < length) {
				operand2 = sign2 + operand2;
			}
			String remainder = "";
			String quotient = operand1;
			while (remainder.length() < length) {
				remainder = remainder + sign1;
			}
			for (int i = 0; i <= length; i++) {
				char oldsign = remainder.charAt(0);
				// 左移
				if (i > 0) {
					remainder = remainder.substring(1) + quotient.charAt(0);
					quotient = quotient.substring(1);
				}
				if (oldsign == operand2.charAt(0)) {
					remainder = integerSubtraction(remainder, operand2, length).substring(1);
				} else {
					remainder = integerAddition(remainder, operand2, length).substring(1);
				}
				// 填充商
				if (remainder.charAt(0) == operand2.charAt(0)) {
					quotient = quotient + "1";
				} else {
					quotient = quotient + "0";
				}

			}
			// 商和余数的修正
			quotient = quotient.substring(1);
			if (quotient.charAt(0) == '1') {
				quotient = oneAdder(quotient).substring(1);
			}
			if (remainder.charAt(0) != operand1.charAt(0)) {
				if (operand1.charAt(0) == operand2.charAt(0)) {
					remainder = integerAddition(remainder, operand2, length).substring(1);
				} else {
					remainder = integerSubtraction(remainder, operand2, length).substring(1);
				}
			}
			// 判断溢出
			if (remainder.equals(operand2)) {
				quotient = oneAdder(quotient).substring(1);
				remainder = integerSubtraction(remainder, operand2, length).substring(1);
				result = "1" + quotient + remainder;
			} else {
				result = "0" + quotient + remainder;
			}
		}
		return result;
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
		// 若操作数的长度小于length则进行右移
		char sign1 = operand1.charAt(0);
		operand1 = operand1.substring(1);
		while (operand1.length() < length + 4) {
			operand1 = "0" + operand1;
		}
		char sign2 = operand2.charAt(0);
		operand2 = operand2.substring(1);
		while (operand2.length() < length + 4) {
			operand2 = "0" + operand2;
		}
		String result;
		char overflow = '0';
		char resultSign;
		if (sign1 == sign2) {
			// 同号相加
			resultSign = sign1;
			result = adder(operand1, operand2, '0', length + 4).substring(1);
			if (result.substring(0, 4).contains("1")) {
				overflow = '1';
			}
			result = result.substring(4);
		} else {
			// 异号相减
			operand2 = operand2.substring(0, 4) + oneAdder(negation(operand2)).substring(5);
			result = adder(operand1, operand2, '0', length + 4).substring(1);
			if (result.substring(0, 4).contains("1")) {
				resultSign = sign1;
			} else {
				resultSign = not(sign1);
				result = oneAdder(negation(result)).substring(1);
			}
			result = result.substring(4);
		}
		return overflow + "" + resultSign + result;
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
		if (!operand1.substring(1).contains("1")) {
			return "0" + operand2; // :)
		} else if (!operand2.substring(1).contains("1")) {
			return "0" + operand1; // :)
		} else {
			char sign1 = operand1.charAt(0);
			char sign2 = operand2.charAt(0);
			String exp1 = operand1.substring(1, 1 + eLength);
			String exp2 = operand2.substring(1, 1 + eLength);
			String frac1 = operand1.substring(1 + eLength);
			String frac2 = operand2.substring(1 + eLength);
			int expNum1 = Integer.parseInt(integerTrueValue("0" + exp1));
			int expNum2 = Integer.parseInt(integerTrueValue("0" + exp2));
			if (expNum1 == 0) {
				expNum1++;
				frac1 = "0" + frac1;
			} else {
				frac1 = "1" + frac1;
			}
			if (expNum2 == 0) {
				expNum2++;
				frac2 = "0" + frac2;
			} else {
				frac2 = "1" + frac2;
			}
			// 补全保护位
			for (int i = 0; i < gLength; i++) {
				frac1 = frac1 + "0";
				frac2 = frac2 + "0";
			}
			int expNumResult = expNum1;
			// 检查指数是否相等
			if (expNum1 < expNum2) {
				frac1 = logRightShift(frac1, expNum2 - expNum1);
				expNumResult = expNum2;
			} else if (expNum2 < expNum1) {
				frac2 = logRightShift(frac2, expNum1 - expNum2);
			}
			if (!frac1.contains("1")) {
				return "0" + operand2; // :)
			} else if (!frac2.contains("1")) {
				return "0" + operand1; // :)
			}
			int newLength = sLength + gLength + 1;
			while (newLength % 4 != 0) {
				newLength++;
			}
			// 分数相加
			String addResult = signedAddition(sign1 + frac1, sign2 + frac2, newLength);
			String fracResult = addResult.substring(addResult.length() - sLength - gLength - 2);
			char signResult = addResult.charAt(1);
			char expOverflow = '0';
			// 判断结果是否为0
			if (!fracResult.contains("1")) {
				return "0" + floatRepresentation("0", eLength, sLength); // :)
			}
			// 判断分数溢出
			if (fracResult.charAt(0) == '1') {
				fracResult = logRightShift(fracResult, 1);
				expNumResult++;
				// 判断指数上溢
				if (expNumResult >= (int) Math.pow(2, eLength) - 1) {
					expOverflow = '1';
				}
			}
			fracResult = fracResult.substring(1);
			// 结果规格化
			expNumResult -= fracResult.indexOf("1");
			if (expNumResult <= 0) {
				// 指数下溢，非规格化
				expNumResult = 0;
			} else {
				// 规格化
				fracResult = leftShift(fracResult, fracResult.indexOf("1"));
			}
			String expResult = integerRepresentation(Integer.toString(expNumResult), eLength);
			// 舍入
			fracResult = fracResult.substring(1, 1 + sLength);
			return expOverflow + "" + signResult + expResult + fracResult; // :)
		}
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
		// 改变减数符号
		operand2 = not(operand2.charAt(0)) + operand2.substring(1);
		return floatAddition(operand1, operand2, eLength, sLength, gLength);
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
		if (!operand1.substring(1).contains("1") || !operand2.substring(1).contains("1")) {
			return "0" + floatRepresentation("0", eLength, sLength);
		} else {
			char signResult = xor(operand1.charAt(0), operand2.charAt(0));
			String exp1 = operand1.substring(1, 1 + eLength);
			String exp2 = operand2.substring(1, 1 + eLength);
			String frac1 = operand1.substring(1 + eLength);
			String frac2 = operand2.substring(1 + eLength);
			int expNum1 = Integer.parseInt(integerTrueValue("0" + exp1));
			int expNum2 = Integer.parseInt(integerTrueValue("0" + exp2));
			if (expNum1 == 0) {
				expNum1++;
				frac1 = "0" + frac1;
			} else {
				frac1 = "1" + frac1;
			}
			if (expNum2 == 0) {
				expNum2++;
				frac2 = "0" + frac2;
			} else {
				frac2 = "1" + frac2;
			}
			int expNumResult = expNum1 + expNum2 - (int) Math.pow(2, eLength - 1) + 1;
			// 分数相乘
			int newLength = 2 * sLength + 2;
			while (newLength % 4 != 0) {
				newLength++;
			}
			String fracResult = integerMultiplication("0" + frac1, "0" + frac2, newLength);
			fracResult = fracResult.substring(fracResult.length() - 2 * sLength - 2);
			char expOverflow = '0';
			// 判断分数溢出
			if (fracResult.charAt(0) == '1') {
				fracResult = logRightShift(fracResult, 1);
				expNumResult++;
				// 判断指数上溢
				if (expNumResult >= (int) Math.pow(2, eLength) - 1) {
					expOverflow = '1';
				}
			}
			fracResult = fracResult.substring(1);
			// 结果规格化
			expNumResult -= fracResult.indexOf("1");
			if (expNumResult <= 0) {
				// 指数下溢，非规格化
				expNumResult = 0;
			} else {
				// 规格化
				fracResult = leftShift(fracResult, fracResult.indexOf("1"));
			}
			String expResult = integerRepresentation(Integer.toString(expNumResult), eLength);
			// 舍入
			fracResult = fracResult.substring(1, 1 + sLength);
			return expOverflow + "" + signResult + expResult + fracResult;
		}
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
		if (!operand1.substring(1).contains("1")) {
			return "0" + floatRepresentation("0", eLength, sLength);
		} else if (!operand2.substring(1).contains("1")) {
			return "0" + floatRepresentation("Inf", eLength, sLength);
		} else {
			char signResult = xor(operand1.charAt(0), operand2.charAt(0));
			String exp1 = operand1.substring(1, 1 + eLength);
			String exp2 = operand2.substring(1, 1 + eLength);
			String frac1 = operand1.substring(1 + eLength);
			String frac2 = operand2.substring(1 + eLength);
			int expNum1 = Integer.parseInt(integerTrueValue("0" + exp1));
			int expNum2 = Integer.parseInt(integerTrueValue("0" + exp2));
			if (expNum1 == 0) {
				expNum1++;
				frac1 = "0" + frac1;
			} else {
				frac1 = "1" + frac1;
			}
			if (expNum2 == 0) {
				expNum2++;
				frac2 = "0" + frac2;
			} else {
				frac2 = "1" + frac2;
			}
			int expNumResult = expNum1 - expNum2 + (int) Math.pow(2, eLength - 1) - 1;
			int newLength = 2 + 2 * sLength;
			while (newLength % 4 != 0) {
				newLength++;
			}
			// 左移被除数分数
			for (int i = 0; i < sLength; i++) {
				frac1 = frac1 + "0";
			}
			String fracResult = integerDivision("0" + frac1, "0" + frac2, newLength).substring(1, 1 + newLength);
			fracResult = fracResult.substring(fracResult.length() - sLength - 1);
			/*for (int i = 0; i < 1 + sLength; i++) {
				if (frac1.charAt(0) == frac2.charAt(0)) {
					frac1 = integerSubtraction("0" + frac1, "0" + frac2, newLength);
					frac1 = frac1.substring(frac1.length() - sLength - 1);
					fracResult = fracResult + "1";
				} else {
					fracResult = fracResult + "0";
				}
				frac1 = frac1.substring(1) + "0";
			}*/
			char expOverflow = '0';
			expNumResult = expNumResult - fracResult.indexOf("1");
			// 判断指数上溢
			if (expNumResult >= (int) Math.pow(2, eLength) - 1) {
				expOverflow = '1';
			}
			if (expNumResult <= 0) {
				// 指数下溢，非规格化
				expNumResult = 0;
			} else {
				// 规格化
				fracResult = leftShift(fracResult, fracResult.indexOf("1"));
			}
			String expResult = integerRepresentation(Integer.toString(expNumResult), eLength);
			// 舍入
			fracResult = fracResult.substring(1, 1 + sLength);
			return expOverflow + "" + signResult + expResult + fracResult;
		}
	}
}
