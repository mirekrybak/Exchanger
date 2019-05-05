import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class CurrencyExchange {
    public static void main(String[] args) throws MalformedURLException {
        Scanner scanner = new Scanner(System.in);
        String path = enterData(scanner);
        scanner.close();

        List<String> lines = takeTable(path);

        List<Double> buyCourse = createBuyCourseList(lines);
        List<Double> sellCourse = createSellCourseList(lines);

        System.out.println("Średni kurs kupna: " + averageRate(buyCourse));
        System.out.println("Odchylenie standardowe kursów sprzedaży: " + variation(sellCourse));
    }

    private static double variation(List<Double> list) {
        double sum = 0;
        for (double d : list) {
            sum += d;
        }
        double aritmeticAverage = sum / list.size();

        double sumSubtractSquare = 0;
        for (double d : list) {
            sumSubtractSquare += (d - aritmeticAverage) * (d - aritmeticAverage);
        }
        double standardVariation = (double) Math.round(Math.sqrt((double) 1 / list.size() * sumSubtractSquare) * 10000) / 10000;

        return standardVariation;
    }

    private static double averageRate(List<Double> list) {
        double averageBuyingRate = 0;
        for (double d : list) {
            averageBuyingRate += d;
        }
        averageBuyingRate = (double) Math.round(averageBuyingRate / list.size() * 10000) / 10000;

        return averageBuyingRate;
    }

    private static List<Double> createBuyCourseList(List<String> list) {
        List<Double> buy = new LinkedList<>();
        for (String l : list) {
            if (l.substring(0, 4).equals("bid:")) {
                buy.add(Double.valueOf(l.substring(4)));
            }
        }

        return buy;
    }

    private static List<Double> createSellCourseList(List<String> list) {
        List<Double> sell = new LinkedList<>();
        for (String l : list) {
            if (l.substring(0, 4).equals("ask:")) {
                sell.add(Double.valueOf(l.substring(4)));
            }
        }

        return sell;
    }

    private static String enterData(Scanner sc) {
        System.out.println("Podaj poprawne dane wejściowe:");
        System.out.println("kod waluty, data początkowa, data końcowa");
        System.out.println("Format: CCC RRRR-MM-DD RRRR-MM-DD");
        String data = sc.nextLine().replaceAll(" ", "");
        String code = data.substring(0, 3);
        System.out.println("\n<--- WEJŚCIE\nKod waluty: " + code);
        String firstDate = data.substring(3, 13);
        System.out.println("Data początkowa: " + firstDate);
        String lastDate = data.substring(13);
        System.out.println("Data końcowa: " + lastDate);
        System.out.println("\nWYJŚCIE --->");

        String path = "http://api.nbp.pl/api/exchangerates/rates/c/" + code + "/" + firstDate + "/" + lastDate + "/";

        return path;
    }

    private static List<String> takeTable(String path) throws MalformedURLException {
        String[] data;
        data = downloadSource(path).split(",");
        List<String> lines = Arrays.asList(data);

        lines = lines.stream().map(s -> s.
                replaceAll("\"", "").
                replaceAll("\\u007b", "").
                replaceAll("\\u007d", "").
                replaceAll("\\u005b", "").
                replaceAll("\\u005d", "")).
                collect(Collectors.toList());

        return lines;
    }

    private static String downloadSource(String myPath) throws MalformedURLException {
        URL url = new URL(myPath);
        StringBuilder sb = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(
                new InputStreamReader(url.openStream(), "utf-8"))) {
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }
}
