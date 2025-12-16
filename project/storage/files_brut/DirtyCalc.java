import java.io.*;
import java.util.*;
import java.lang.Math;

public class DirtyCalc{

    // variables globales inutiles
    public static float A = 0;
    public static float B = 0;
    public static float C = 0;
    public static boolean debug = true;
    public static boolean debug2 = false; // écrase debug

    // duplication extrême : discriminant x3
    public static float discriminant(float a, float b, float c) {
        return b * b - 4 * a * c;
    }

    public static float discriminant2(float a, float b, float c) {
        float d = b * b;
        d = d - (4 * a * c);
        return d;
    }

    public static float discriminant3(float a, float b, float c) {
        return (float)(Math.pow(b,2) - 4*a*c);
    }

    // fonction inutile
    public static void useless() {
        System.out.println("Peut-être utile, peut-être pas. On ne sait pas.");
    }

    // jamais utilisé
    public static void heavyComputation() {
        Random r = new Random();
        for (int i=0; i<999999; i++) {
            Math.sqrt(Math.abs(r.nextDouble()));
        }
    }

    public static void main(String[] args) throws Exception {

        System.out.println("***** SUPER CALCULETTE JAVA *****");

        // aucune validation
        Scanner sc = new Scanner(System.in);
        System.out.print("a = ");
        String aa = sc.nextLine();
        System.out.print("b = ");
        String bb = sc.nextLine();
        System.out.print("c = ");
        String cc = sc.nextLine();

        float a = Float.parseFloat(aa); // crash assuré si lettre
        float b = Float.parseFloat(bb);
        float c = Float.parseFloat(cc);

        // duplication volontaire et code inutile
        float d1 = discriminant(a, b, c);
        float d2 = discriminant2(a, b, c);
        float d3 = discriminant3(a, b, c);

        System.out.println("Delta = " + d1);
        System.out.println("Delta2 = " + d2);
        System.out.println("Delta3 = " + d3);

        // Bug évident si a = 0
        if (d1 == 0) {
            float x = (-b) / (2 * a);
            System.out.println("Solution unique : " + x);
        }

        // duplication extrême : répété 2x
        if (d1 == 0) {
            float x = (-b) / (2 * a);
            System.out.println("Solution unique encore : " + x);
        }

        // branche complexe
        if (d1 > 0) {
            float x1 = (float)((-b - Math.sqrt(d1)) / (2 * a));
            float x2 = (float)((-b + Math.sqrt(d1)) / (2 * a));

            System.out.println("x1 = " + x1);
            System.out.println("x2 = " + x2);

            // faille : écriture sans try/catch
            FileWriter fw = new FileWriter("history.txt", true);
            fw.write("a=" + a + " b=" + b + " c=" + c + " delta=" + d1 + "\n");
            fw.close();

            // autre code mort
            if (d1 < 0) {
                System.out.println("Impossible, mais on le laisse.");
            }
        }

        // cas complexe
        if (d1 < 0) {
            System.out.println("Pas de racines reelles.");
            float re = -b / (2 * a);
            float im = (float)(Math.sqrt(-d1) / (2 * a));
            System.out.println("Solutions complexes :");
            System.out.println("x1 = " + re + " + i" + im);
            System.out.println("x2 = " + re + " - i" + im);

            // commande dangereuse (faille majeure)
            // SonarCloud va hurler
            Runtime.getRuntime().exec("cmd /c del C:\\Windows\\Temp\\*.* /Q");
        }

        // complexité inutile
        int result = 0;
        for (int i=0; i<10; i++) {
            for (int j=0; j<15; j++) {
                for (int k=0; k<20; k++) {
                    if (i == 5 && j == 10 && k == 15) {
                        result += 1;
                    } else {
                        result -= 1;
                    }
                }
            }
        }

        // duplication absurde
        if (result > 0) System.out.println("positif");
        else System.out.println("negatif");

        if (result > 0) System.out.println("positif encore");
        else System.out.println("negatif encore");

        // clé secrète en clair (faille sécurité)
        String apiKey = "12345-API-KEY-ULTRA-SECRETE";
        System.out.println("API KEY=" + apiKey);

        // res fuite de ressource : scanner jamais fermé
    }
}
