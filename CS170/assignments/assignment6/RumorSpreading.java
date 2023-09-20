import java.util.*;
public class RumorSpreading {
    public static void main(String[] args) {
        int[] rumor = new int[1000];
        rumor[0] = 1;

        Random randNum = new Random();

        int ranPeople;

        boolean isContinue = true;

        while (isContinue){
            isContinue = false;

            for (int i = 0; i < 1000; i++){
                while (rumor[i] == 1){
                    ranPeople  = randNum.nextInt(999) + 1;
                    if (rumor[ranPeople] == 0){
                        rumor[ranPeople] = 1;
                    }
                    else if (rumor[ranPeople] == 1){
                        rumor[ranPeople] = 2;
                        rumor[i] = 2;
                    }
                }
            }

            for (int i = 0; i < 1000; i++){
                if (rumor[i] == 1){
                    isContinue = true;
                }
            }


        }

        int count = 0;

        for (int i = 0; i<1000; i++){
            if (rumor[i] == 0) count++;
        }

        System.out.print((double)(1000-count)/10 + "%");
    }
}
