import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class Category {

  private int categoryNumber;
  private List<Person> persons;
  private LocalTime startTime;
  private String fileName;

  public Category() {
    this.persons = new ArrayList<>();
  }

  public int getCategoryNumber() {
    return categoryNumber;
  }

  public void setCategoryNumber(int categoryNumber) {
    this.categoryNumber = categoryNumber;
  }

  public List<Person> getPersons() {
    return persons;
  }

  public void setPersons(List<Person> persons) {
    this.persons = persons;
  }

  public LocalTime getStartTime() {
    return startTime;
  }

  public void setStartTime(LocalTime startTime) {
    this.startTime = startTime;
  }

  public String getFileName() {
    return fileName;
  }

  public void setFileName(String fileName) {
    this.fileName = fileName;
  }
}
