package org.springframework;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        List<Integer> numberList = new ArrayList<>();

        System.out.println("Enter numbers to calculate the average (type 'done' to finish):");

        try (Scanner scanner = new Scanner(System.in)) {
            // ユーザーからの入力を受け取る
            while (scanner.hasNext()) {
                String input = scanner.next();
                if (input.equalsIgnoreCase("done")) {
                    break;
                }
                try {
                    int number = Integer.parseInt(input);
                    numberList.add(number);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter an integer or 'done' to finish.");
                }
            }
        } catch (Exception e) {
            System.err.println("An unexpected error occurred: " + e.getMessage());
            return;
        }

        // 入力された数値を配列に変換
        int[] numbers = numberList.stream().mapToInt(i -> i).toArray();

        try {
            // CalculationTest クラスの calculateAverage メソッドを使用して平均値を計算
            double average = CalculationTest.calculateAverage(numbers);

            // 計算結果を出力
            System.out.println("The average is: " + average);
        } catch (IllegalArgumentException e) {
            // 配列が空の場合のエラーメッセージを出力
            System.err.println("Error: " + e.getMessage());
        }
    }
}