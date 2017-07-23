/**
 * Created by Daniel on 23/07/2017.
 */
import ecs100.*;
import java.awt.Color;

public class BasicArrays {

    public static void main(String[] args) {
        new BasicArrays();
    }

    public double size = 40;
    public double top = 20;
    public double left = 50;

    public BasicArrays(){
        UI.addButton("Vertical Line", this::doVerticalLine);
        UI.addButton("Box", this::doBox);
        //UI.addButton("Vertical Line", this::doSpiral);
        UI.addButton("Clear", this::doClear);
        UI.addButton("Quit", UI::quit);
        UI.setDivider(0.4);
    }

    private Color getColor(int numItems, int value){
        return new Color ((255/numItems)*value);
    }

    public void doVerticalLine(){
        int sizeOfArray = UI.askInt("Size of Array");
        int list [] = new int [sizeOfArray];
        UI.println(list.length);
        for(int i=0; i<list.length; i++){
            list[i]=list.length-i;
        }
        for(int j=0; j<list.length; j++){
            UI.setColor(this.getColor(list.length, list[j]));
            UI.fillRect(this.left, this.top+this.size*j , this.size, this.size);
            UI.setColor(Color.white);
            UI.drawString(list[j]+"", this.left + (size/2), (this.top+this.size*(j+1))-20);
            UI.println("j: " + j);
        }
    }

    public void doBox(){
        int rows = UI.askInt("How many rows? ");
        int cols = UI.askInt("How many columns? ");
        int box [][] = new int [rows][cols];
        UI.setColor(Color.black);
        for (int i= 0; i<box.length; i++){
            for(int j = 0; j<box[i].length; j++){
                UI.drawRect(this.left+(this.size*j), this.top+size*i, this.size, this.size);
            }
        }
    }

    public void doClear(){
        UI.clearGraphics();
        UI.clearPanes();
    }
}
