package com.suryadev.basicminicalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class BasicCalculator {

	private static List<String> operators;
	private static Map<String, Integer> opValues;

	private BasicCalculator() {
	};

	public static void addOperators(String op, int weight) {

		opValues.put(op, weight);
		operators.add(op);

	}

	static {

		opValues = new HashMap<String, Integer>();
		opValues.put("+", 2);
		opValues.put("-", 1);
		opValues.put("x", 3);
		opValues.put("/", 4);
		opValues.put("%", 4);

		operators = new ArrayList<String>();
		operators.add("+");
		operators.add("-");
		operators.add("x");
		operators.add("/");
		operators.add("%");

	}

	private static Double compute(String op, Double valueA, Double valueB) {

		Double result = null;

		switch (op) {

		case "+":
			result = valueB + valueA;
			break;
		case "-":
			result = valueB - valueA;
			break;
		case "/":
			result = valueB / valueA;
			break;
		case "x":
			result = valueB * valueA;
			break;
		case "%":
			result = valueB % valueA;

		}

		return result;

	}

	private static List<String> parseExpression(String expression) {

		List<String> orList = new ArrayList<String>();

		char[] values = expression.toCharArray();

		String noBuffer = "";

		for (int i = 0; i < values.length; i++) {

			if (operators.contains(String.valueOf(values[i]))) {
              /**  Special Case for (-) Hold The Previous result value **/
				if(i == 0){
					noBuffer += values[i];
				}else {
					/** Special Case for (-) Hold The Previous result value **/

					/**
					 * Flush the noBuffer to odList
					 */
					orList.add(noBuffer);
					orList.add(String.valueOf(values[i]));
					noBuffer = "";
				}
			} else {

				noBuffer += values[i];

				if (i == (values.length - 1)) {
					/**
					 * Flush the last no value.
					 */
					orList.add(noBuffer);
				}
			}

		}

		return orList;
	}

	private static int compareOp(String op1, String op2) {

		if (opValues.get(op1) > opValues.get(op2)) {
			return 1;
		} else if (opValues.get(op1) == opValues.get(op2)) {
			return 0;
		} else {
			return -1;
		}
	}

	public static boolean isOperator(String str) {
       return operators.contains(str);
	}

	private static boolean isNumber(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (NumberFormatException e) {

			try {
				Double.valueOf(str);
				return true;
			} catch (NumberFormatException e2) {
				return false;
			}
		}
	}

	public static Double calculate(String expression) throws InvalidExpression {
		try {
			Stack<String> left = new Stack<String>();
			Stack<Double> right = new Stack<Double>();
			List<String> expList = parseExpression(expression);

			for (int i = 0; i < expList.size(); i++) {

				String value = expList.get(i);

				if (isNumber(value)) {
					right.push(Double.valueOf(value));

				} else if (operators.contains(value)) {

					if (right.size() > 1) {

						String tempOpValue = left.peek();

						if (compareOp(value, tempOpValue) == 1 || compareOp(value, tempOpValue) == 0) {
							/**
							 * If greater than existing operator no problem
							 */
							left.push(value);

						} else if (compareOp(value, tempOpValue) == -1) {
							/**
							 * if lesser than existing operator perform those operators first
							 */

							int leftBukketSize = left.size();

							for (int j = 0; j < leftBukketSize; j++) {

								String tempOpValue2 = left.peek();

								if (compareOp(value, tempOpValue2) == -1) {
									right.push(
											compute(left.pop(), Double.valueOf(right.pop()), Double.valueOf(right.pop())));

								} else {
									left.push(value);
								}

							}

							if (left.isEmpty()) {
								left.push(value);
							}
						}

					} else {
						left.push(value);
					}

				}

				/**
				 * Check if it is last element
				 */
				if (i == expList.size() - 1) {

					int leftBucketSize = left.size();

					for (int j = 0; j < leftBucketSize; j++) {

						right.push(compute(left.pop(), Double.valueOf(right.pop()), Double.valueOf(right.pop())));
					}
				}

			}

			return right.pop();

		}catch(Exception e){
			throw new InvalidExpression();
		}

	}

}
