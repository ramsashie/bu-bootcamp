import java.io.*;
import java.util.ArrayList;

public class GradeAnalyzer {

    private static int invalidLineCount = 0;

    /**
     * Main method.
     * Takes input scores.txt and output reports.txt file as arguments.
     * @param args
     *
     * 1. Read data from reports.txt file
     * 2. Checks if the file has contents and filters valid data.
     * 3. Generate statics.
     * 4. Reports the data, prints to the console and saves to reports.txt file.
     * 5. Raises and catches exceptions where necessary.
     *
     */

    public static void main(String[] args) {
        /**
         * Create a project folder and Test data file.
         * Project Folder: Module2
         * Test data file: scores.txt
         *
         */

        if (args.length != 3) {
            System.err.println("Usage: java GradeAnalyzer scores.txt reports.txt true/false.");
            System.err.println("true-static hardcoded data, false- data fetched from reports.txt file");
            System.exit(1); // Exit with error code
        }
        boolean flag = Boolean.parseBoolean(args[2]);
        ArrayList<Integer> scores = new ArrayList<>();
        if (flag) {
            // Testing using hardcoded list:
            scores = testData();
            System.out.println("Average Score from Hardcoded list: " + calculateAverage(testData()));

        } else {
            // Step 3: reading scores from scores.txt file
            scores = readScores(args[0]);
        }

        // Step 4: calculate statistics

        if (scores.isEmpty())
        {
            System.out.println("No valid scores to report.");
            System.out.println("Average score not calcualted : " + GradeAnalyzer.calculateAverage(scores));
        }
        else
        {
            //System.out.println(String.format("Average score from "+args[0]+" file data : %8.2f", GradeAnalyzer.calculateAverage(scores)));
            //Step 5: Find the Highest and Lowest Scores
            int highest = Integer.MIN_VALUE;
            int lowest = Integer.MAX_VALUE;
            for (int score : scores)
                { if (score > highest) { highest = score; }
                if (score < lowest) { lowest = score; } }
            
            //Step 6: Count the Grade Bands
            int[] grades = calculateGradeBrands(scores);
            //Step 7: Implement writeReport
            //Step 7a: Build report lines:
            ArrayList<String> reportLines = new ArrayList<>();
            reportLines = buildReportLines(scores, GradeAnalyzer.calculateAverage(scores), highest, lowest, grades);
            System.out.println("\n\n");
            //Step 7b: print report lines:
            printToConsole(reportLines);
            //Step 7c: write to report.txt file:
            writeReport(reportLines, args[1]);
        }
    }

    // Test Data - hardcoded.
    public static ArrayList<Integer> testData() {
        // ArrayList with initial capacity of 10
        ArrayList<Integer> averagesList = new ArrayList<>(10);
        averagesList.add(99);
        averagesList.add(80);
        averagesList.add(65);
        averagesList.add(53);
        averagesList.add(90);
        averagesList.add(88);
        averagesList.add(58);
        averagesList.add(97);
        averagesList.add(95);
        return averagesList;
    }

    // Returns a list of valid scores read from the file
    //Step 3
    public static ArrayList<Integer> readScores(String filename) {
        ArrayList<Integer> scores = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty()) {
                    continue; // skip blank lines
                }

                try {
                    int score = Integer.parseInt(line);
                    scores.add(score);
                } catch (NumberFormatException e) {
                    System.out.println("Warning: Invalid number, Number format Exception: \"" + line + "\". Ignoring this line.");
                    invalidLineCount++;
                }
            }

        } catch (IOException e) {
            System.out.println("Error reading file: " + e.getMessage());
        }

        return scores;
    }

    // Returns the average of a list of scores, or 0.0 if the list is empty
    //Step 4
    public static double calculateAverage(ArrayList<Integer> scores) {
        // If the list is empty, return 0.0 immediately
        if (scores.isEmpty()) {
            return 0.0;
        }

        // Loop through all scores and accumulate the total in a double
        double total = 0.0;
        for (int score : scores) {
            total += score;
        }

        // Return the total divided by scores.size()
        return total / scores.size();
    }

    public static int[] calculateGradeBrands(ArrayList<Integer> scores) {
        int[] counts = new int[5];
        for (int score : scores) {
            if (score >= 90) {
                counts[0]++; // A
            } else if (score >= 80) {
                counts[1]++; // B
            } else if (score >= 70) {
                counts[2]++; // C
            } else if (score >= 60) {
                counts[3]++; // D
            } else {
                counts[4]++; // F
            }
        }

        return counts;
    }

    public static ArrayList<String> buildReportLines(ArrayList<Integer> scores,
            double avg, int high, int low, int[] grades) {
        ArrayList<String> lines = new ArrayList<>();

        lines.add("");
        lines.add(stringAlignment("=== Grade Analysis Report ===", 60));
        lines.add("");

        lines.add("");
        lines.add(String.format("Total scores processed: %3d", scores.size()));
        lines.add(String.format("Invalid data lines skipped:  %3d", invalidLineCount));
        lines.add("");

        lines.add(stringAlignment("******************************************", 30));

        lines.add(String.format("Average score: %8.2f", avg));
        lines.add(String.format("Highest score: %8d", high));
        lines.add(String.format("Lowest score:  %8d", low));
        lines.add("");
        lines.add(stringAlignment("******************************************", 30));

        lines.add("Grade distribution:");
        lines.add(String.format("  A (90-100):   %d", grades[0]));
        lines.add(String.format("  B (80-89):    %d", grades[1]));
        lines.add(String.format("  C (70-79):    %d", grades[2]));
        lines.add(String.format("  D (60-69):    %d", grades[3]));
        lines.add(String.format("  F (below 60): %d", grades[4]));
        lines.add("");
        return lines;
    }

    // Writes to standard console.
    public static void printToConsole(ArrayList<String> reportLines) {
        for (String line : reportLines) {
            System.out.println(line);
        }
    }

    // Writes the report to report.txt using BufferedWriter/FileWriter.
    public static void writeReport(ArrayList<String> reportLines, String outputFileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFileName))) {
            for (String line : reportLines) {
                writer.write(String.format("%s%n", line));
            }
            System.out.println();
            System.out.println("Report successfully written to " + outputFileName);
        } catch (IOException e) {
            System.out.println("Error writing report: " + e.getMessage());
        }
    }

    public static String stringAlignment(String text, int width) {
        if (text == null) text = "";
        if (width <= text.length()) return text; // No centering if width too small

        int padding = (width - text.length()) / 2;
        StringBuilder sb = new StringBuilder();

        // Add left padding
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);

        // Add right padding
        while (sb.length() < width) {
            sb.append(" ");
        }

        return sb.toString();
    }
}
