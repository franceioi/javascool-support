package javascool;

public class Misc {
  // @factory
  private Misc() {}

  @Deprecated public static void sleep(int delay) {
    
  }

  public static int random(int min, int max) {
    return randomInteger(min, max);
  }

  public static int randomInteger(int min, int max) {
    return (int) Math.floor(min + (0.99999 + max - min) * Math.random());
  }

  public static double randomDouble(double min, double max) {
    return (Math.random()*(max-min)+min);
  }

  public static boolean equal(String string1, String string2) {
    return string1.equals(string2);
  }

  public static void assertion(boolean condition, String message) {
    if (!condition) {
      System.err.println(message);
      System.exit(1);
    }
  }

}
