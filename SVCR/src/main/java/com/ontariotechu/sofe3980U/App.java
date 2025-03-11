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
			double mse = 0;
			double mae = 0;
			double mare = 0;
			int n = allData.size();

			for (String[] row : allData) {
				float y_true = Float.parseFloat(row[0]);
				float y_predicted = Float.parseFloat(row[1]);
				//System.out.printf("%.4f \t %.4f\n", y_true, y_predicted);

				double error = y_true - y_predicted;
				mse += Math.pow(error, 2);
				mae += Math.abs(error);
				mare += Math.abs(error / y_true);

			}

			// Compute the averages
			mse /= n;
			mae /= n;
			mare = (mare / n) * 100; // Convert to percentage

			System.out.printf("MSE: %.4f\n", mse);
			System.out.printf("MAE: %.4f\n", mae);
			System.out.printf("MARE: %.2f%%\n", mare);
			System.out.println("----------------------------------");
		}
	}
}

