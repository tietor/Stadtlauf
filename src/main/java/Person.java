import java.time.LocalTime;

public class Person {
  private int startNumber;
  private String name;
  private LocalTime duration;
  private Category category;
  private int ranking;

  public Person(int startNumber, String name) {
    this.startNumber = startNumber;
    this.name = name;
  }

  public int getStartNumber() {
    return startNumber;
  }

  public void setStartNumber(int startNumber) {
    this.startNumber = startNumber;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public LocalTime getDuration() {
    return duration;
  }

  public void setDuration(LocalTime duration) {
    this.duration = duration;
  }

  public Category getCategory() {
    return category;
  }

  public void setCategory(Category category) {
    this.category = category;
  }

  public int getRanking() {
    return ranking;
  }

  public void setRanking(int ranking) {
    this.ranking = ranking;
  }
}
