package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Model for Multi-Class Classification Metrics (Cross-Entropy and Confusion Matrix)
 *
 */
public class App {
    public static void main(String[] args) {
        String filePath = "model.csv";  // Single model file

        FileReader filereader;
        List<String[]> allData;
        try {
            filereader = new FileReader(filePath);
            CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
            allData = csvReader.readAll();
        } catch (Exception e) {
            System.out.println("Error reading the CSV file: " + filePath);
            return;
        }

        double ce = 0;  // Cross-Entropy
        int[][] confusionMatrix = new int[5][5];  // 5x5 confusion matrix for 5 classes
        int n = allData.size();

        // Process each row in the CSV file
        for (String[] row : allData) {
            int y_true = Integer.parseInt(row[0]);  // Actual class (1-5)
            float[] y_predicted = new float[5];

            // Extract predicted probabilities for each class
            for (int i = 0; i < 5; i++) {
                y_predicted[i] = Float.parseFloat(row[i + 1]);
            }

            // Cross-Entropy Calculation
            ce -= Math.log(y_predicted[y_true - 1] + 1e-9);  // Correct class is y_true - 1 (0-indexed)

            // Confusion Matrix Calculation
            int predicted_class = getMaxIndex(y_predicted) + 1;  // Find the predicted class (1-5)
            confusionMatrix[y_true - 1][predicted_class - 1]++;  // Increment the confusion matrix for the true vs predicted class
        }

        // Calculate Average Cross-Entropy
        ce /= n;

        // Display Results
        System.out.printf("Cross-Entropy (CE): %.4f\n", ce);
        System.out.println("Confusion Matrix:");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                System.out.printf("%d ", confusionMatrix[i][j]);
            }
            System.out.println();
        }
    }

    // Helper function to get the index of the max value in the array (for classification)
    private static int getMaxIndex(float[] array) {
        int maxIndex = 0;
        for (int i = 1; i < array.length; i++) {
            if (array[i] > array[maxIndex]) {
                maxIndex = i;
            }
        }
        return maxIndex;
    }
}
