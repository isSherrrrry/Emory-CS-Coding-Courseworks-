import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.Random;

public class Gameboard extends JPanel implements MouseMotionListener, MouseListener {

    //Margins
    private int originX = 20;
    private int originY = 40;

    //Initial Row
    public int rowNum = 3;
    private int cellSide = 680 / rowNum;

    //Game Boards & Arrays to record
    Chess[] chesses = new Chess[300];
    int times;
    char[][] gameBoard = new char[16][16];

    //Status
    private boolean win = false;
    private boolean tie = false;
    private boolean isHuman = true;

    //Initial Computer Piece
    private int compX = -1;
    private int compY = -1;


    public Gameboard(){
        addMouseListener(this);
        addMouseMotionListener(this);
    }


    //Paint the Canvas
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        for (int i = 0; i<rowNum+1; i++){
            g.drawLine(originX,originY+i*cellSide,originX+rowNum*cellSide,originY + i * cellSide);
        }
        for (int i = 0; i<rowNum+1; i++){
            g.drawLine(originX+i*cellSide, originY ,originX+i*cellSide,originY + rowNum * cellSide);
        }

        for (int i = 0; i < times; i += 1) {
            boolean form=chesses[i].getStatus();
            int xPos=chesses[i].getX()*cellSide+originX+cellSide/4;
            int yPos=chesses[i].getY()*cellSide+originY+cellSide/4;

            if(form) {
                g.drawOval(xPos,yPos, cellSide/2, cellSide/2);
            }else {
                g.drawLine(xPos, yPos, xPos+cellSide/2, yPos+ cellSide/2);
                g.drawLine(xPos+cellSide/2, yPos, xPos, yPos+ cellSide/2);
            }
        }
    }

    //Decide where the computer place a piece
    public void computer(){
        int[][] space = new int[300][2];
        int num = 0;
        for (int i = 0; i < rowNum; i++){
            for (int j = 0; j<rowNum; j++){
                if (gameBoard[i][j] != 'O' &&  gameBoard[i][j] != 'X'){
                    space[num][0] = i;
                    space[num][1] = j;
                    num++;
                }
            }
        }
        Random randomGenerator = new Random();
        int randomInt = randomGenerator.nextInt(num);

        compX = space[randomInt][0];
        compY = space[randomInt][1];
    }

    //Mouse
    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent arg0) {
    }

    @Override
    public void mousePressed(MouseEvent arg0) {
        boolean player = isHuman;
        if (win || tie) return;
        int Xindex = (arg0.getX() - originX) / cellSide;
        int Yindex = (arg0.getY() - originY) / cellSide;
        if (Xindex<0 || Yindex<0 || Xindex>rowNum-1 || Yindex>rowNum-1 || isPlaced(Xindex,Yindex)) return;


        if (isHuman){
            Chess oneChess = new Chess(Xindex,Yindex,player);
            chesses[times++] = oneChess;
            gameBoard[Xindex][Yindex] = 'O';
            checkResult();

            if (!win && !tie){
                computer();
                Chess oneChess2 = new Chess(compX,compY,!player);
                chesses[times++] = oneChess2;
                gameBoard[compX][compY] = 'X';
            }
        }


        repaint();

        if (checkResult() == 0){
            String msg = String.format("You Won!");
            JOptionPane.showMessageDialog(this, msg);
            restart(rowNum);
        }
        if (checkResult() == 1){
            String msg = String.format("You Failed");
            JOptionPane.showMessageDialog(this, msg);
            restart(rowNum);
        }
        if (checkResult() == 2){
            String msg = String.format("It's a Tie!");
            JOptionPane.showMessageDialog(this, msg);
            restart(rowNum);
        }
    }

    @Override
    public void mouseExited(MouseEvent arg0) {
    }


    @Override
    public void mouseEntered(MouseEvent arg0) {
    }


    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        int Xindex=(e.getX()-originX)/cellSide;
        int Yindex=(e.getY()-originY)/cellSide;
        if (Xindex<0 || Yindex<0 || Xindex>rowNum-1 || Yindex>rowNum-1 || isPlaced(Xindex,Yindex)){
            setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        }else {
            setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }


    //Decide available space
    private boolean isPlaced(int a, int b){
        for (Chess chess : chesses){
            if (chess != null && chess.getX() == a && chess.getY() == b) return true;
        }
        return false;
    }


    //Check the result
    private int checkResult(){
        boolean checkO = checkRow('O') || checkCol('O') || checkDia('O') || checkAntidia( 'O');
        boolean checkX = checkRow('X') || checkCol('X') || checkDia('X') || checkAntidia('X');
        if (checkO) {
            win = true;
            return 0;
        }

        if (checkX) return 1;
        if (!checkX && !checkO && checkFull()){
            tie = true;
            return 2;
        }
        return 3;
    }

    private boolean checkFull(){
        if(times == rowNum * rowNum) return true;
        return false;
    }

    private boolean checkRow(char inputCha){
        int count;
        for (int i = 0; i<rowNum; i++){
            count = 0;
            for (int j = 0; j<rowNum; j++){
                if (gameBoard[i][j] == inputCha) count++;
                if (count == rowNum) return true;
            }
        }
        return false;
    }

    private boolean checkCol(char inputCha){
        int count;
        for (int i = 0; i<rowNum; i++){
            count = 0;
            for (int j = 0; j<rowNum; j++){
                if (gameBoard[j][i] == inputCha) count++;
                if (count == rowNum) return true;
            }
        }
        return false;
    }

    private boolean checkDia(char inputCha){
        for (int i = 0; i<rowNum; i++){
            if(gameBoard[i][i] != inputCha) break;
            if(i == rowNum-1) return true;
        }
        return false;
    }

    private boolean checkAntidia(char inputCha){
        for (int i = 0; i<rowNum; i++){
            if(gameBoard[i][rowNum-1-i] != inputCha) break;
            if(i == rowNum-1) return true;
        }
        return false;
    }

    //Restart the game
    public void restart(int row){

        for (int i = 0; i<chesses.length; i++){
            chesses[i] = null;
        }

        for (int i = 0; i < rowNum; i++){
            for (int j = 0; j<rowNum; j++){
                gameBoard[i][j] = '*';
            }
        }
        times = 0;
        win = false;
        tie = false;

        compY = -1;
        compX = -1;

        rowNum = row;
        cellSide = 680 / row;

        repaint();
    }
}
