public class Complex {
    private double a;
    private double b;

    Complex(double a, double b){
        this.a = a;
        this.b = b;
    }

    Complex(double a){
        this.a = a;
        this.b = 0;
    }

    Complex(){
        a = 0;
        b = 0;
    }

    public double getRealPart(){
        return a;
    }

    public double getImaginaryPart(){
        return b;
    }

    @Override
    public String toString(){
        if (Math.abs(b-0) < 0.0001) return "" + a;
        return "" + a + " + " + b + "i";
    }

    public Complex add(Complex obj){
        return new Complex(this.a + obj.a, this.b + obj.b);
    }

    public Complex subtract(Complex obj){
        return new Complex(this.a - obj.a, this.b - obj.b);
    }

    public Complex multiply(Complex obj){
        return new Complex(this.a * obj.a - this.b * obj.b, this.a * obj.b + this.b * obj.a);
    }

    public Complex divide(Complex obj){
        double a1 = this.a;
        double b1 = this.b;
        double c1 = obj.a;
        double d1 = obj.b;
        double divide = c1*c1 + d1 * d1;
        return new Complex((a1*c1+b1*d1)/divide,(b1*c1 - a1*d1)/divide);
    }

    public String abs(){
        return "" + Math.sqrt(Math.pow(a, 2) + Math.pow(b, 2));
    }

}
