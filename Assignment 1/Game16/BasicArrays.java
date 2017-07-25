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
        return new Color ((255/numItems)*value);} //The included code for getting colours for boxes.

    public void doVerticalLine(){
        int sizeOfArray = UI.askInt("Size of Array");
        int list [] = new int [sizeOfArray]; //Make an array of the size the user specifies.
        UI.println(list.length);
        for(int i=0; i<list.length; i++){
            list[i]=list.length-i; //Giving the spaces in the array a number starting from highest one and descending.
        }
        for(int j=0; j<list.length; j++){
            UI.setColor(this.getColor(list.length, list[j]));
            UI.fillRect(this.left, this.top+this.size*j , this.size, this.size); //Fill a rectangle with the colour from the included code.
            UI.setColor(Color.white);
            UI.drawString(list[j]+"", this.left + (size/2), (this.top+this.size*(j+1))-20); //Draw the number in the box in white.
            UI.println("j: " + j);
        }
    }

    public void doBox(){
        int rows = UI.askInt("How many rows? ");
        int cols = UI.askInt("How many columns? ");
        int box [][] = new int [rows][cols];

        int count=1;
        for(int i=0; i<box.length; i++){
            for(int j=0; j<box[i].length; j++){
                box[i][j]=count; //Give each box a number than increases from 1.
                count++;
                Trace.println("("+i+ ", "+j+") => " + box[i][j]); //For debugging the values im getting.
            }
        }

        for (int i= 0; i<box.length; i++){
            for(int j = 0; j<box[i].length; j++){
                UI.setColor(this.getColor(box.length*box[i].length, box[i][j]));
                UI.fillRect(this.left+(this.size*j), this.top+size*i, this.size, this.size); //Fill a rectangle with the colour from the included code.
            }
        }

        UI.setColor(Color.white);
        for(int i=0; i<box.length; i++){
            for(int j=0; j<box[i].length; j++){
                UI.drawString(box[i][j]+"", this.left+(this.size*j)+(this.size/2), (this.top+size*i)+(this.size/2)); //Draw the number in the box in white.
            }
        }
    }

    public void doClear(){
        UI.clearGraphics();
        UI.clearPanes();
    }
}
