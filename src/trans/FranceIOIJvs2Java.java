// compile with gcj --encoding=utf8 --main=FranceIOIJvs2Java -o FranceIOIJvs2Java FranceIOIJvs2Java.java
import java.io.*;
import java.util.*;

public class FranceIOIJvs2Java
{
   public static String translateJvsToJava(String jvsCode, List<String> lib) 
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
         /*
         head.append("import static javascool.Stdout.*;\n");
         head.append("import static javascool.Stdin.*;\n");
         head.append("import static javascool.Misc.*;\n");
         */

         // Add libs
         for(String libname : lib)
         {
            // FIXME BUG WARNING ERROR Scanner is a special one, don't do anything about it
            if (libname.equals("Scanner"))
               continue;
            // javascools standard library
            String prefix;
            if(libname.equals("Stdin") || libname.equals("Stdout") || libname.equals("Misc"))
               prefix = "javascool";
            else
               prefix = "algorea";
            head.append("import static " + prefix + "." + libname + ".*;\n");
         }

         head.append("class Code {\n");
         head.append(finalBody);
         head.append("}\n");

         // Declares the proglet's core as a Runnable in the Applet
         head.append("class Main {\n");
         head.append("  public static void main(String[] arg) {\n");
         head.append("    try{ Code c = new Code();c.main(); } catch(Throwable e) { \n");
         head.append("      if (e.toString().matches(\".*Interrupted.*\"))\n");
         head.append("        System.err.println(\"\\n-------------------\\nProggramme arrêté !\\n-------------------\\n\");\n");
         head.append("      else\n");
         head.append("        System.err.println(\"\\n-------------------\\nErreur lors de l'exécution de la proglet\\n\"+e+\"\\n-------------------\\n\");\n");
         head.append("      System.exit(1);\n");
         head.append("    }\n");
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

   public static String ExtractLibName(String file)
   {
      String libname = file;
      int idx = libname.lastIndexOf('/');
      if(idx != -1)
         libname = libname.substring(idx + 1);
      idx = libname.lastIndexOf('.');
      if(idx != -1)
         libname = libname.substring(0, idx);
      //System.err.println("Debug: extract lib name from " + file + ": " + libname);
      return libname;
   }

   // @main
   public static void main(String[] args)
   {
      System.out.println("");
      if (args.length < 2)
      {
         System.err.println("The translator need at least two arguments.");
         System.err.println("Usage :");
         System.err.println("./prog inputJvsFile outputJavaFile lib1 lib2...");
         System.exit(3);
      }

      List<String> lib = new ArrayList<String>();
      for(int i = 2; i < args.length; i++)
         lib.add(ExtractLibName(args[i]));

      String jvsCode = readFromFile(args[0]);
      String javaCode = translateJvsToJava(jvsCode, lib);

      writeToFile(args[1], javaCode);
   }
}
