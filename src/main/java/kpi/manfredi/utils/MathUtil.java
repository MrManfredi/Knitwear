package kpi.manfredi.utils;

import java.util.List;

public class MathUtil {

    /**
     * This method using Euclid's algorithm to find the greatest common divisor of two numbers
     *
     * @param a first number
     * @param b second number
     * @return the greatest common divisor of two numbers
     */
    public static Integer getGreatestCommonDivisor(Integer a, Integer b) {
        while (b > 0) {
            Integer temp = b;
            b = a % b; // % is remainder
            a = temp;
        }
        return a;
    }

    /**
     * This method is used to obtain the greatest common divisor of a set of numbers.
     *
     * @param input set of numbers
     * @return the greatest common divisor
     */
    public static Integer getGreatestCommonDivisor(Integer[] input) {
        Integer result = input[0];
        for (int i = 1; i < input.length; i++) result = getGreatestCommonDivisor(result, input[i]);
        return result;
    }

    /**
     * This method is used to obtain the least common multiple of two numbers
     *
     * @param a first number
     * @param b second number
     * @return the least common multiple
     */
    public static Integer getLeastCommonMultiple(Integer a, Integer b) {
        return a * (b / getGreatestCommonDivisor(a, b));
    }

    /**
     * This method is used to obtain the least common multiple of a set of numbers.
     *
     * @param input set of numbers
     * @return the least common multiple
     */
    public static Integer getLeastCommonMultiple(List<Integer> input) {
        Integer result = input.get(0);
        for (int i = 1; i < input.size(); i++) {
            result = getLeastCommonMultiple(result, input.get(i));
        }
        return result;
    }
}
