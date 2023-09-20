import java.util.*;

public class MathInCharArray {
    public static void main(String[] args) {

        char[] a = {'1', '1','1', '-', '1', '2', '*', '2'};
        String equationLine = "";
        for (int k = 0; k< a.length; k++){
            equationLine += a[k];
        }

        String[] equation = new String[equationLine.length()];

        int i = 0;
        int j = 0;
        int record = 0;

        while(i< a.length){
            if (!Character.isDigit(equationLine.charAt(i))){
                equation[j] = equationLine.substring(record,i);
                equation[j+1] = equationLine.substring(i,i+1);
                record = i+1;
                j += 2;
            }
            i++;
        }
        equation[j] = equationLine.substring(record);

        int equationLength = 0;

        for (int k = 0; k< equation.length; k++){
            if (equation[k] != null){
                equationLength++;
            }
        }

        for (int k = 0; k < equationLength; k++) {
            if(equation[k].equals("*"))
            {
                equation[k+1]= ""+(Double.parseDouble(equation[k-1]) * Double.parseDouble(equation[k+1]));
                equation[k-1]="";
                equation[k]="";

            }else if(equation[k].equals("/"))
            {
                equation[k+1]= ""+(Double.parseDouble(equation[k-1]) / Double.parseDouble(equation[k+1]));
                equation[k-1]="";
                equation[k]="";
            }
            else {
                continue;
            }
        }

        int index = 0;

        for (int k = 0; k < equationLength; k++) {
            index = k;
            if(equation[k].equals("+"))
            {
                while(equation[index++].equals("")) index++;
                equation[index]= ""+(Double.parseDouble(equation[k-1]) + Double.parseDouble(equation[index]));
                equation[k-1]="";
                equation[k]="";

            }else if(equation[k].equals("-"))
            {
                while(equation[++index].equals("")) index++;
                equation[index]= ""+ (Double.parseDouble(equation[k-1]) - Double.parseDouble(equation[index]));
                equation[k-1]="";
                equation[k]="";
            }
            else {
                continue;
            }
        }

        System.out.println(equation[equationLength-1]);

    }

}
