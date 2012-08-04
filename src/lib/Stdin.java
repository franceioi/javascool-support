package javascool;

//import java.util.Scanner;
import algorea.Scanner;

/*
A set of utility function to read from stdin :
  readString
  readInteger / readInt
  readBoolean / readBool
  readFloat
  readDouble

TODO : better error handling
*/
public class Stdin {
  // @factory
  private Stdin() {}
  static Scanner sc = new Scanner(System.in);

   // Reads the next line on stdin
  public static String readString() {
    return sc.nextLine();
  }
  @Deprecated public static String readString(String question) {
    return readString();
  }

  // Read the next word on stdin
  public static String readWord() {
    return sc.next();
  }
  @Deprecated public static String readWord(String question) {
    return readWord();
  }

  // Read the next char on stdin
  public static char readChar() {
    return sc.nextChar();
  }
  @Deprecated public static char readChar(String question) {
    return readChar();
  }

  // Reads an integer
  public static int readInt(){
    return sc.nextInt();
  }
  @Deprecated public static int readInt(String question){
    return readInt();
  }
  public static int readInteger(){
    return readInt();
  }
  @Deprecated public static int readInteger(String quest){
    return readInt();
  }

  // Reads a boolean : not supported on France-IOI
  @Deprecated public static boolean readBool() {
    return true;
  }
  @Deprecated public static boolean readBool(String quest) {
    return true;
  }
  @Deprecated public static boolean readBoolean() {
    return true;
  }
  @Deprecated public static boolean readBoolean(String question) {
    return true;
  }

  // Reads a float
  public static float readFloat(){
    return sc.nextFloat();
  }
  @Deprecated public static float readFloat(String question){
    return readFloat();
  }

  // Reads a double
  public static double readDouble(){
    return sc.nextDouble();
  }
  @Deprecated public static double readDouble(String question){
    return readDouble();
  }

  // Reads a double
  public static double readDecimal(){
    return readDouble();
  }
  @Deprecated public static double readDecimal(String question){
    return readDouble();
  }

}
