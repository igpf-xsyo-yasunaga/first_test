package org.springframework;

public class CalculationTest {

    /**
     * 数値の配列の平均を計算します。
     * @param numbers 数値の配列
     * @return 平均値
     * @throws IllegalArgumentException 配列が空の場合
     */
    public static double calculateAverage(int[] numbers) {
        if (numbers == null || numbers.length == 0) {
            throw new IllegalArgumentException("The array is empty. Cannot calculate average.");
        }
        int sum = 0;
        for (int number : numbers) {
            sum += number;
        }
        return (double) sum / numbers.length;
    }
}