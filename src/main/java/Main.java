import static java.lang.Integer.parseInt;
import static java.time.LocalTime.of;
import static java.time.temporal.ChronoUnit.SECONDS;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

public class Main {

  private List<Person> persons;
  private Category categoryJuniors;
  private Category categorySeniors;
  private Category categoryElite;
  private List<Category> categories;

  public static void main(String[] args) throws IOException {
    Main main = new Main();
    main.createCategories();
    main.readPersonData();
    main.readMeasureResults();
    main.removeUnfinishedPersons();
    main.sortPersonsByDuration();
    main.createAllRankings();
    main.createListSortedByName();
  }

  private Main() {
    categoryJuniors = new Category();
    categorySeniors = new Category();
    categoryElite = new Category();
    this.persons = new ArrayList<>();
    this.categories = new ArrayList<>(Arrays.asList(categoryJuniors, categorySeniors, categoryElite));
  }

  private void createCategories() {
    System.out.println("Kategorien werden erstellt");
    this.categoryJuniors.setCategoryNumber(1);
    this.categoryJuniors.setStartTime(of(14, 0));
    this.categoryJuniors.setFileName("junioren.rl.txt");

    this.categorySeniors.setCategoryNumber(2);
    this.categorySeniors.setStartTime(of(14, 15));
    this.categorySeniors.setFileName("senioren.rl.txt");

    this.categoryElite.setCategoryNumber(3);
    this.categoryElite.setStartTime(of(15, 0));
    this.categoryElite.setFileName("elite.rl.txt");
  }

  private void readPersonData() throws IOException {
    System.out.println("Personen werden eingelesen und in Kategorien gespeichert");
    Stream<String> readStartListFile = Files.lines(Paths.get("input/startliste.txt"), StandardCharsets.ISO_8859_1);
    readStartListFile.forEach(line -> {
      if (!line.equals(""))
        line = line.trim();
      String[] data = line.split("\t");
      if (data[2].contains(" ")) {
        data[2] = data[2].substring(0, data[2].length() - 2);
      }
      Person person = new Person(parseInt(data[0]), data[2]);
      this.persons.add(person);
      int categoryNumber = parseInt(data[1]);
      switch (categoryNumber) {
        case 1:
          categoryJuniors.getPersons().add(person);
          person.setCategory(categoryJuniors);
          break;
        case 2:
          categorySeniors.getPersons().add(person);
          person.setCategory(categorySeniors);
          break;
        case 3:
          categoryElite.getPersons().add(person);
          person.setCategory(categoryElite);
          break;
      }
    });
    readStartListFile.close();
  }

  private void readMeasureResults() throws IOException {
    System.out.println("Messresultate werden für die einzelnen Personen berechnet");
    Stream<String> readMeasureResults = Files.lines(Paths.get("input/messresultate.txt"));
    readMeasureResults.forEach(line -> {
      if (!line.equals("")) {
        line = line.trim();
        String[] data = line.split(" ");
        Person searchedPerson = persons.stream()
            .filter(person -> person.getStartNumber() == parseInt(data[0]))
            .findFirst()
            .orElseThrow(NullPointerException::new);
        LocalTime startTime;
        switch (searchedPerson.getCategory().getCategoryNumber()) {
          case 1:
            startTime = categoryJuniors.getStartTime();
            break;
          case 2:
            startTime = categorySeniors.getStartTime();
            break;
          case 3:
            startTime = categoryElite.getStartTime();
            break;
          default:
            throw new NullPointerException("Es konnte keine passende Kategorie gefunden werden");
        }
        long duration = startTime.until(LocalTime.parse(data[1]), SECONDS);
        searchedPerson.setDuration(LocalTime.ofSecondOfDay(duration));
      }
    });
    readMeasureResults.close();
  }

  private void removeUnfinishedPersons() {
    System.out.println("Personen ohne Laufzeit werden gelöscht");
    categories.forEach(category -> category.getPersons().removeIf(person -> person.getDuration() == null));
    persons.removeIf(person -> person.getDuration() == null);
  }

  private void sortPersonsByDuration() {
    System.out.println("Personen werden nach Laufzeiten sortiert");
    categories.forEach(category -> category.getPersons().sort(Comparator.comparing(Person::getDuration)));
  }

  private void createAllRankings() {
    System.out.println("Ranglisten werden erstellt");
    categories.forEach(category -> {
      int rankingNumber = 1;
      PrintWriter printWriter = null;
      try {
        printWriter = new PrintWriter("output/" + category.getFileName());
      } catch (FileNotFoundException e) {
        e.printStackTrace();
      }
      for (Person person : category.getPersons()) {
        if (printWriter != null) {
          printWriter.println(
              rankingNumber + "\t" + person.getStartNumber() + "\t" + String.format("%tT", person.getDuration()) + "\t"
                  + person.getName());
        }
        person.setRanking(rankingNumber);
        rankingNumber++;
      }
      printWriter.flush();
      printWriter.close();
    });
  }

  private void createListSortedByName() throws FileNotFoundException {
    System.out.println("Personen werden nach Namen sortiert");
    persons.sort(Comparator.comparing(Person::getName));
    System.out.println("Namen-Liste wird erstellt");
    PrintWriter printWriter = new PrintWriter("output/namen.ref.txt");
    persons.forEach(person -> {
      printWriter.format("%-10d%-20s%10d%4d%10tT", person.getStartNumber(), person.getName(),
          person.getCategory().getCategoryNumber(), person.getRanking(), person.getDuration());
      printWriter.println();
    });
    printWriter.flush();
    printWriter.close();
  }
}
