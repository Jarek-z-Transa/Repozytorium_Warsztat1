package pl.coderslab;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;


public class TaskManager {
    public static final String RED = "\033[0;31m";
    public static final String RESET = "\033[0m";
    public static final String BLUE = "\033[0;34m";

    public static void main(String[] args) throws FileNotFoundException {
        File tasks = new File("tasks.txt");
        String komenda = getData();
        while (!komenda.equals("exit")) {
            switch(komenda) {
                case "add":
                    addTask();
                    komenda = getData();
                    break;
                case "remove":
                    removeTask();
                    komenda = getData();
                    break;
                case "list":
                    listTasks();
                    komenda = getData();
                    break;
            }
        }
        System.out.println(RED+"Bye, bye"+RESET);


    }

    static String getData() {
        String dzialanie = " ";
        Scanner input = new Scanner(System.in);
        System.out.println(BLUE+"Wpisz co mam zrobić"+RESET);
        System.out.println("add");
        System.out.println("remove");
        System.out.println("list");
        System.out.println("exit");

        dzialanie = input.nextLine();
        while (!dzialanie.equals("add") && !dzialanie.equals("remove") && !dzialanie.equals("list") && !dzialanie.equals("exit")) {
            System.out.println("Nie ma takiej opcji, wpisz poprawne działanie");
            System.out.println(BLUE+"Wpisz co mam zrobić"+RESET);
            System.out.println("add");
            System.out.println("remove");
            System.out.println("list");
            System.out.println("exit");
            dzialanie = input.nextLine();
        }
        return dzialanie;
    }

    static void addTask() throws FileNotFoundException {
        File tasks = new File("tasks.txt");
        int numerWiersza = 1;
        int i = 1;
        Scanner input = new Scanner(System.in);
        try {
            Scanner scan = new Scanner(tasks);
            StringBuilder reading = new StringBuilder();
            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                String[] parts = s.split(",");
                numerWiersza = Integer.parseInt(parts[0]);
                if (numerWiersza == i) {
                    i++;
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Brak pliku.");
        }
        String num = Integer.toString(i);
        System.out.println("Wprowadź taska: ");
        String a = input.nextLine();
        System.out.println("Wprowadź datę ");
        String b = input.nextLine();
        boolean dateFormat = dateCheck(b);
        while (dateFormat == false) {
            System.out.println("Wprowadzone dane nia są datą, proszę wprowadź date w formacie rrrr-mm-dd");
            b = input.nextLine();
            dateFormat = dateCheck(b);
        }
        System.out.println("Czy jest task ważny? true/false");
        String c = input.nextLine();
        while (!c.equals("true") && !c.equals("false")) {
            System.out.println("Nieprawidłowa wartość, podaj jeszcze raz");
            c = input.nextLine();
        }
        if (c.equals("true")) {
            c = "ważny";
        } else {
            c = "nieważny";
        }
        String d = String.join(", ",num,a,b,c);
        try (FileWriter fileWriter = new FileWriter(tasks, true)){
            fileWriter.append("\n"+d);
        } catch (IOException ex) {
            System.out.println("Błąd zapisu do pliku.");
        }
        System.out.println("Task został pomyślnie dodany"+"\n");
        regroup();
    }

    static void removeTask() {
        File tasks = new File("tasks.txt");
        Scanner input = new Scanner(System.in);
        System.out.println("Wprowadź numer taska, który ma zostać usunięty");
        int num = input.nextInt();
        int taski = policzTaski();
        while (num < 1 || num > taski) {
            System.out.println("Niepoprawny numer taska, wprowadź jeszcze raz");
            num = input.nextInt();
        }
        String news = "";
        try {
            Scanner scan = new Scanner(tasks);
            StringBuilder reading = new StringBuilder();
            String numm = Integer.toString(num);
            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                String[] parts = s.split(",");
                if (!parts[0].equals(numm)) {
                    reading.append(s+"\n");
                    String f = reading.toString();
                    news = String.join("\n",f);
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Brak pliku.");
        }
        try (FileWriter fileWriter = new FileWriter(tasks, false)){
            fileWriter.append(news);
        } catch (IOException ex) {
            System.out.println("Błąd zapisu do pliku.");
        }
        System.out.println("Task został pomyślnie usunięty"+"\n");
        regroup();

    }

    static void listTasks() {
        File tasks = new File("tasks.txt");
        StringBuilder reading = new StringBuilder();
        try {
            Scanner scan = new Scanner(tasks);
            while (scan.hasNextLine()) {
                reading.append(scan.nextLine() + "\n");
            }
        } catch (FileNotFoundException e) {
            System.out.println("Brak pliku.");
        }
        System.out.println(reading.toString());
    }
    static void regroup() {
        String pogrupowane = "";
        File tasks = new File("tasks.txt");
        StringBuilder reading = new StringBuilder();
        String d = "";
        try {
            Scanner scan = new Scanner(tasks);
            int i = 1;
            while (scan.hasNextLine()) {
                String s = scan.nextLine();
                String[] parts = s.split(",");
                parts[0] = Integer.toString(i);
                d = String.join(",",parts[0],parts[1],parts[2],parts[3]);
                i++;
                reading.append(d);
                if (scan.hasNextLine()) {
                    reading.append("\n");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("Brak pliku.");
        }
        String ops = reading.toString();
        try (FileWriter fileWriter = new FileWriter(tasks, false)){
            fileWriter.append(ops);
        } catch (IOException ex) {
            System.out.println("Błąd zapisu do pliku.");
        }

    }
    static int policzTaski() {
        int iloscTaskow = 0;
        File tasks = new File("tasks.txt");
        StringBuilder reading = new StringBuilder();
        try {
            Scanner scan = new Scanner(tasks);
            while (scan.hasNextLine()) {
                reading.append(scan.nextLine());
                if (scan.hasNextLine()) {
                    reading.append("\n");
                }
                iloscTaskow++;
            }
        } catch (FileNotFoundException e) {
            System.out.println("Brak pliku.");
        }
        return iloscTaskow;
    }
    public static boolean dateCheck(String date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        dateFormat.setLenient(false);
        try {
            dateFormat.parse(date.trim());
        } catch (ParseException pe) {
            return false;
        }
        return true;
    }
}