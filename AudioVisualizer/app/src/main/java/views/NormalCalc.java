package views;

public class NormalCalc {
    public static double phi(double x) {
        return Math.exp(((-x) * x) / 2.0d) / Math.sqrt(6.283185307179586d);
    }

    public static double phi(double x, double mu, double sigma) {
        return phi((x - mu) / sigma) / sigma;
    }

    public static double Phi(double z) {
        if (z < -8.0d) {
            return 0.0d;
        }
        if (z > 8.0d) {
            return 1.0d;
        }
        double sum = 0.0d;
        double term = z;
        int i = 3;
        while (sum + term != sum) {
            sum += term;
            term = ((term * z) * z) / ((double) i);
            i += 2;
        }
        return 0.5d + (phi(z) * sum);
    }

    public static double Phi(double z, double mu, double sigma) {
        return Phi((z - mu) / sigma);
    }

    public static double PhiInverse(double y) {
        return PhiInverse(y, 1.0E-8d, -8.0d, 8.0d);
    }

    private static double PhiInverse(double y, double delta, double lo, double hi) {
        double mid = lo + ((hi - lo) / 2.0d);
        if (hi - lo < delta) {
            return mid;
        }
        if (Phi(mid) > y) {
            return PhiInverse(y, delta, lo, mid);
        }
        return PhiInverse(y, delta, mid, hi);
    }

    public static double PhiInverse2(double y) {
        return PhiInverse2(y, 1.0E-8d, -8.0d, 8.0d);
    }

    private static double PhiInverse2(double y, double delta, double lo, double hi) {
        double mid = lo + ((hi - lo) / 2.0d);
        if (hi - lo < delta) {
            return mid;
        }
        if (Phi(mid) - Phi(-mid) > y) {
            return PhiInverse2(y, delta, lo, mid);
        }
        return PhiInverse2(y, delta, mid, hi);
    }
}
