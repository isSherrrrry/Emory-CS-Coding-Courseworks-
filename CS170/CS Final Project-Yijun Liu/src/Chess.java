public class Chess {
    private int x;
    private int y;
    private boolean human;

    public Chess(int x, int y, boolean human){
        this.x = x;
        this.y = y;
        this.human = human;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public boolean getStatus(){
        return human;
    }
}
