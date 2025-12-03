package calcul;

public class Calcul {

    private int champA;
    private int champB;
    private int champC;

    public Calcul(int champA, int champB, int champC) {
        this.champA = champA;
        this.champB = champB;
        this.champC = champC;
    }

    public int getA() {
        return champA;
    }

    public void setA(int champA) {
        this.champA = champA;
    }

    public int getB() {
        return champB;
    }

    public void setB(int champB) {
        this.champB = champB;
    }

    public int getC() {
        return champC;
    }

    public void setC(int champC) {
        this.champC = champC;
    }

    public double[] calculSeconddegre() {
        int delta = champB * champB - (4 * champA * champC);

        if (delta > 0) {
            double solution1 = (-champB + Math.sqrt(delta)) / (2.0 * champA);
            double solution2 = (-champB - Math.sqrt(delta)) / (2.0 * champA);
            System.out.println("deux solutions reels : x1 = " + solution1 + ", x2 = " + solution2);
            return new double[] { delta, solution1, solution2 };
        } else if (delta == 0) {
            double solution = -champB / 2.0 * champA;
            System.out.println("une seule solution reelle : x = " + solution);
            return new double[] { delta, solution };
        } else {
            System.out.println("aucune solution (delta < 0)");
            return null;
        }
    }
}