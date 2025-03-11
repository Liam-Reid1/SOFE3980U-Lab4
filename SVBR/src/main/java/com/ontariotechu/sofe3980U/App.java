package com.ontariotechu.sofe3980U;

import java.io.FileReader;
import java.util.List;
import com.opencsv.*;

/**
 * Evaluate Single Variable Continuous Regression
 *
 */
public class App {
	public static void main(String[] args) {
		String[] filePaths = {"model_1.csv", "model_2.csv", "model_3.csv"};

		for (String filePath : filePaths) {
			System.out.println("Processing file: " + filePath);
			FileReader filereader;
			List<String[]> allData;
			try {
				filereader = new FileReader(filePath);
				CSVReader csvReader = new CSVReaderBuilder(filereader).withSkipLines(1).build();
				allData = csvReader.readAll();
			} catch (Exception e) {
				System.out.println("Error reading the CSV file: " + filePath);
				continue;
			}

			double bce = 0;
			int tp = 0, tn = 0, fp = 0, fn = 0;
			int n = allData.size();

			for (String[] row : allData) {
				int y_true = Integer.parseInt(row[0]);
				float y_predicted = Float.parseFloat(row[1]);
				//System.out.printf("%d \t %.4f\n", y_true, y_predicted);

				// Binary Cross-Entropy Calculation
                bce -= ((y_true * Math.log(y_predicted + 1e-9)) + (1 - y_true) * Math.log(1 - y_predicted + 1e-9));

				// Confusion Matrix Calculation (assuming 0.5 threshold for classification)
				int predicted_class = y_predicted >= 0.5 ? 1 : 0;
				if (y_true == 1 && predicted_class == 1) tp++;
				if (y_true == 0 && predicted_class == 0) tn++;
				if (y_true == 0 && predicted_class == 1) fp++;
				if (y_true == 1 && predicted_class == 0) fn++;

			}

			// Compute Metrics
			bce /= n;
			double accuracy = (tp + tn) / (double) n;
			double precision = tp / (tp + fp + 1e-9);
			double recall = tp / (tp + fn + 1e-9);
			double f1_score = 2 * (precision * recall) / (precision + recall + 1e-9);
			double specificity = tn / (tn + fp + 1e-9);
			double auc_roc = (recall + specificity) / 2; // Simple approximation of AUC-ROC

			System.out.printf("BCE: %.4f\n", bce);
			System.out.printf("Accuracy: %.4f\n", accuracy);
			System.out.printf("Precision: %.4f\n", precision);
			System.out.printf("Recall: %.4f\n", recall);
			System.out.printf("F1 Score: %.4f\n", f1_score);
			System.out.printf("AUC-ROC: %.4f\n", auc_roc);
			System.out.println("Confusion Matrix:");
			System.out.printf("TP: %d  FP: %d\n", tp, fp);
			System.out.printf("FN: %d  TN: %d\n", fn, tn);
			System.out.println("----------------------------------");
		}
	}
}