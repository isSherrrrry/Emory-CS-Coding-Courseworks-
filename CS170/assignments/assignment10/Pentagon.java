abstract class GeometricObject{
    abstract double getArea();
    abstract double getPerimeter();
}


public class Pentagon extends GeometricObject implements Comparable<Pentagon>{
    private double side;

    Pentagon(){
        side = 1.0;
    }

    Pentagon(double num){
        side = num;
    }

    public double getArea(){
        double constant = 0.25 * Math.sqrt(5*(5+2*Math.sqrt(5)));
        return constant * side * side;
    }

    public double getPerimeter(){
        return side * 5;
    }

    static public Pentagon max(Pentagon a, Pentagon b){
        if (a.side>b.side) return a;
        return b;
    }

    @Override
    public int compareTo(Pentagon o) {
        if (Math.abs(this.getArea() - o.getArea()) < 0.0001) return 0;
        if (this.getArea() < o.getArea()) return -1;
        return 1;
    }

    @Override
    public boolean equals(Object obj) {
        final Pentagon compare = (Pentagon) obj;
        if (Math.abs(this.getArea() - compare.getArea()) < 0.0001) return true;
        return false;
    }
}
