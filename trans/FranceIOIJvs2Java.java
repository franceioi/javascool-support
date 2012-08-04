// compile with gcj --encoding=utf8 --main=FranceIOIJvs2Java -o FranceIOIJvs2Java FranceIOIJvs2Java.java
import java.io.*;

public class FranceIOIJvs2Java
{
   // Translate a JVS code into a Java code
   public static String translateJvsToJava(String jvsCode)
   {
      // TODO ; Loic : Why ? When does this append ?
      String text = jvsCode.replace((char) 160, ' ');

      // Some verifications
      if (!text.replaceAll("[ \n\r\t]+", " ").matches(".*void[ ]+main[ ]*\\([ ]*\\).*"))
      {
         if (text.replaceAll("[ \n\r\t]+", " ").matches(".*main[ ]*\\([ ]*\\).*")) 
         {
            System.err.println("Attention: il faut mettre \"void\" devant \"main()\" pour que le programme puisque se compiler");
            text = text.replaceFirst("main[ ]*\\([ ]*\\)", "void main()");
         }
         else 
         {
            System.err.println("Attention: il faut un block \"void main()\" pour que le programme puisque se compiler");
            text = "\nvoid main() {\n" + text + "\n}\n";
         }
      }
      
      String[] lines = text.split("\n");
      
      StringBuilder head = new StringBuilder();
      StringBuilder body = new StringBuilder();
      // Here is the translation loop
      {
         int nbRepeat = 0;
         // Copies the user's code
         for (String line : lines) 
         {
            // Try to deal with imports / packages
            if (line.matches("^\\s*(import|package)[^;]*;\\s*$")) 
            {
               head.append(line);
               body.append("//").append(line).append("\n");
               if (line.matches("^\\s*package[^;]*;\\s*$"))
                  System.err.println("Attention : on ne peut normalement pas définir de package Java en JavaScool\n le programme risque de ne pas s'exécuter correctement");
            }
            // The "repeat" structure added to JVS
            else if (line.matches("^\\s*repeat[ ]*\\(\\s*.+\\s*\\).*")) 
            {
               String finalVar = "__final_" + nbRepeat;
               String loopVar = "__loop_" + nbRepeat;
               String replace = "final int " + finalVar + " = ($1);for (int " + loopVar + " = 1 ; " + loopVar + " <= " + finalVar + " ; " + loopVar + "++ ) $2";

               line = line.replaceAll("^\\s*repeat[ ]*\\((.*)\\)\\s*(({.*)?)$", replace);
               body.append(line).append("\n");
               nbRepeat++;
            }
            else
            {
               body.append(line).append("\n");
            }
         }
         String finalBody = body.toString();

         
         // Imports needed Java's packages
         head.append("import static java.util.Arrays.*;\n");
         head.append("import static java.lang.Math.*;\n");

         // Imports Java's Cool static methods
         head.append("import static javascool.Stdout.*;\n");
         head.append("import static javascool.Stdin.*;\n");
         head.append("import static javascool.Misc.*;\n");
         head.append("class Code {\n");
         
         // TODO, temporary : the "robot" library should be automatically added when needed
         head.append("public static void bas()      { System.out.println(\"bas\"); }\n"); 
         head.append("public static void droite()   { System.out.println(\"droite\"); }\n"); 
         head.append("public static void gauche()   { System.out.println(\"gauche\"); }\n"); 
         head.append("public static void haut()     { System.out.println(\"haut\"); }\n"); 
         head.append("public static void deposer()  { System.out.println(\"deposer\"); }\n"); 
         head.append("public static void ramasser() { System.out.println(\"ramasser\"); }\n"); 
         head.append("public static void remplir(int indice)                      { System.out.println(\"remplir\");System.out.println(indice); }\n"); 
         head.append("public static void deplacer(int indiceSrc, int indiceDst)   { System.out.println(\"deplacer\");System.out.println(indiceSrc);System.out.println(indiceDst); }\n"); 
         head.append("public static void transferer(int indiceSrc, int indiceDst) { System.out.println(\"transferer\");System.out.println(indiceSrc);System.out.println(indiceDst); }\n"); 
         head.append("public static void vider(int indice)                        { System.out.println(\"vider\");System.out.println(indice); }\n"); 

         head.append(finalBody);

         head.append("}\n");

         // Declares the proglet's core as a Runnable in the Applet
         head.append("class Main {\n");
         head.append("  public static void main(String[] arg) {\n");
         head.append("   try{ Code c = new Code();c.main(); } catch(Throwable e) { \n");
         head.append("    if (e.toString().matches(\".*Interrupted.*\"))\n");
         head.append("      System.err.println(\"\\n-------------------\\nProggramme arrêté !\\n-------------------\\n\");\n");
         head.append("    else\n");
         head.append("      System.err.println(\"\\n-------------------\\nErreur lors de l'exécution de la proglet\\n\"+e+\"\\n-------------------\\n\");\n");
         head.append("    System.exit(1);\n");
         head.append("   }\n");
         head.append("  }\n");
         head.append("}\n");

         /*
         System.err.println(
                  "\n-------------------\nCode java généré\n-------------------\n" +
                  head.toString().replaceAll("([{;])", "$1\n") + "\n" + finalBody + "}" +
                  "\n----------------------------------------------------------\n"
            );
         */
      }

      return head.toString();
   }

   public static String readFromFile(String fileName)
   {
      String result = "";
      try
      {
         FileReader fstream = new FileReader(fileName);
         BufferedReader reader = new BufferedReader(fstream);
         StringBuilder buffer = new StringBuilder();
         char chars[] = new char[10240];
         while (true)
         {
            int l = reader.read(chars);
            if (l == -1)
               break;
            buffer.append(chars, 0, l);
         }
         result = buffer.toString();
      }
      catch (Exception e)
      {
         System.err.println("Error: " + e.getMessage());
         System.exit(4);
      }
      return result;
   }

   public static void writeToFile(String fileName, String content)
   {
      try
      {
         FileWriter fstream = new FileWriter(fileName);
         BufferedWriter out = new BufferedWriter(fstream);
         out.write(content);
         out.close();
      }
      catch (Exception e)
      {
         System.err.println("Error: " + e.getMessage());
         System.exit(5);
      }
   }

   // @main
   public static void main(String[] args)
   {
      if (args.length != 2)
      {
         System.err.println("The translator need two arguments.");
         System.err.println("Usage :");
         System.err.println("./prog inputJvsFile outputJavaFile");
         System.exit(3);
      }

      String jvsCode = readFromFile(args[0]);
      String javaCode = translateJvsToJava(jvsCode);

      writeToFile(args[1], javaCode);
   }
}
