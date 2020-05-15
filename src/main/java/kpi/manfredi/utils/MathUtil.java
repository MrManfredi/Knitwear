package kpi.manfredi.utils;

public class MathUtil {

    /**
     * This method using Euclid's algorithm to find the greatest common divisor of two numbers
     *
     * @param a first number
     * @param b second number
     * @return the greatest common divisor of two numbers
     */
    private static long getGreatestCommonDivisor(long a, long b) {
        while (b > 0) {
            long temp = b;
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
    private static long getGreatestCommonDivisor(long[] input) {
        long result = input[0];
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
    private static long getLeastCommonMultiple(long a, long b) {
        return a * (b / getGreatestCommonDivisor(a, b));
    }

    /**
     * This method is used to obtain the least common multiple of a set of numbers.
     *
     * @param input set of numbers
     * @return the least common multiple
     */
    private static long getLeastCommonMultiple(long[] input) {
        long result = input[0];
        for (int i = 1; i < input.length; i++) result = getLeastCommonMultiple(result, input[i]);
        return result;
    }
}
