// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2017T2, Assignment 2
 * Name: Daniel Satur
 * Username: saturdani
 * ID: 300375193
 */

import ecs100.*;

import javax.swing.*;
import java.util.*;
import java.io.*;

/** 
 * Sokoban
 */

public class Sokoban {

    private Square[][] squares;  // the array representing the warehouse
    private int rows;                  // the height of the warehouse
    private int cols;                   // the width of the warehouse

    private Coord workerPosition;     // the position of the worker
    private String workerDirection = "left"; // the direction the worker is facing

    private final int maxLevels = 4; // maximum number of levels defined
    private int level = 0;                // current level 


    private Map<Character, String> fileCharacterToSquareType;  // character in level file -> square object
    private Map<String, String> directionToWorkerImage;                // worker direction ->  image of worker
    private Map<String, String> keyToAction;                                  // key string -> action to perform

    private Stack <ActionRecord> history = new Stack <ActionRecord>();
    private Stack <ActionRecord> redo = new Stack <ActionRecord>();

    // Constructors
    /** 
     *  Constructs a new Sokoban object
     *  and set up the GUI.
     */
    public Sokoban() {
        UI.addButton("New Level", () -> {level = (level+1)%maxLevels; load(level);});
        UI.addButton("Restart", () -> {load(level);});
        UI.addButton("left", () -> doAction("left"));
        UI.addButton("up", () -> doAction("up"));
        UI.addButton("down", () -> doAction("down"));
        UI.addButton("right", () -> doAction("right"));
        UI.addButton("Undo", this::undo);
        UI.addButton("Redo", this::redo);
        UI.addButton("Quit", UI::quit);

        UI.println("Push the boxes\n to their target postions.");
        UI.println("You may use keys (wasd or ijkl and u)");
        UI.setKeyListener(this::doKey);

        initialiseMappings();
        load(0); // start with level zero
    }

    /** Responds to key actions */
    public void doKey(String key) {
        doAction(keyToAction.get(key));
    }

    public void undo() { //Pop the top ActionRecord off the stack and reverses action.
        if (!history.isEmpty()) {
            ActionRecord temp = this.history.pop();
            if(temp.isMove()){
                this.move(this.oppositeDirection(temp.direction())); //Move in the opposite direction.
                this.redo.push(history.peek()); //Add the undo to the redo stack (for challenge)
                history.remove(history.peek()); //Due to was I implemented the ActionRectords I need to delete the object I made.
            }
            if(temp.isPush()){
                this.pull(this.oppositeDirection(temp.direction())); //Pull a box that was pushed
                this.redo.push(history.peek());
                history.remove(history.peek());
            }
        }
    }

    public void redo(){
        if(!redo.isEmpty()){
            ActionRecord temp2 = this.redo.pop();
            if(temp2.isMove()){
                this.move(this.oppositeDirection(temp2.direction())); //Undo the undo
                //redo.remove(redo.peek());
            }
            else{
                this.push(this.oppositeDirection(temp2.direction())); //Should push things that were pulled, doesn't in reality.
                //redo.remove(redo.peek());
            }
        }
    }

    /*
    public void undo() { //Pop the top ActionRecord off the stack and reverses action.
        if (!history.isEmpty()) {
            ActionRecord temp = this.history.pop();
            if(temp.isMove()){
                this.move(this.oppositeDirection(temp.direction()));
                ActionRecord newMove = new ActionRecord ("move", this.oppositeDirection(temp.direction()));
                this.redo.push(newMove);
            }
            if(temp.isPush()){
                this.pull(temp.direction());
                ActionRecord newPull = new ActionRecord ("pull", this.oppositeDirection(temp.direction()));
                this.redo.push(history.push(newPull));
            }
        }
    }

    public void redo(){
        if(!redo.isEmpty()){
            ActionRecord temp2 = this.redo.pop();
            if(temp2.isMove()){
                this.move(this.oppositeDirection(temp2.direction()));
                //redo.remove(redo.peek());
            }
            else{
                this.push(this.oppositeDirection(temp2.direction()));
                //redo.remove(redo.peek());
            }
        }
    }

     */

    /*Above is the code I tried implementing later because it avoided having to delete an ActionRecord abject after
    calling the move/push/pull methods but in the end I had more issues to fix so I stayed with my version. */

    /** 
     *  Moves the worker in the specified direction, if possible.
     *  If there is box in front of the Worker and a space in front of the box,
     *  then push the box.
     *  Otherwise, if there is anything in front of the Worker, do nothing.
     * @param action the action to perform 
     */
    public void doAction(String action) {
        if (action==null) 
            return;

        workerDirection = action; // action can only be a move; record it.

        Coord nextP = workerPosition.next(workerDirection);  // where the worker would move to
        Coord nextNextP = nextP.next(workerDirection);         // where the worker would move to in two steps

        // is a box push possible?
        if ( squares[nextP.row][nextP.col].hasBox() && squares[nextNextP.row][nextNextP.col].isFree() ) {
            push(workerDirection);

            if (isSolved()) {
                UI.println("\n*** YOU WIN! ***\n");

                // flicker with the boxes to indicate win
                for (int i=0; i<12; i++) {
                    for (int row=0; row<rows; row++)
                        for (int column=0; column<cols; column++) {
                            Square square=squares[row][column];

                            // toggle shelf squares
                            if (square.hasBox()) {square.moveBoxOff(); drawSquare(row, column);}
                            else if (square.isEmptyShelf()) {square.moveBoxOn(); drawSquare(row, column);}
                        }

                    UI.sleep(100);
                }
            }
        }
        else if ( squares[nextP.row][nextP.col].isFree() ) { // can the worker move?
            move(workerDirection);
        }
    }

    /** Moves the worker into the new position (guaranteed to be empty) 
     * @param direction the direction the worker is heading
     */
    public void move(String direction) {
        drawSquare(workerPosition); // display square under worker
        workerPosition = workerPosition.next(direction); // new worker position
        drawWorker();  // display worker at new position

        Trace.println("Move " + direction);
        ActionRecord recordMove = new ActionRecord("move", direction);
        history.push(recordMove); //Add a new ActionRecord object for a move in some direction
    }

    /** Push: Moves the Worker, pushing the box one step 
     *  @param direction the direction the worker is heading
     */
    public void push(String direction) {
        drawSquare(workerPosition); // display square under worker

        workerPosition = workerPosition.next(direction); // new worker position
        Coord boxPosition = workerPosition.next(direction); // this is two steps from the original worker position

        squares[workerPosition.row][workerPosition.col].moveBoxOff(); // remove box from its current position
        drawSquare(workerPosition); // display square without the box
        drawWorker();  // display worker at new position

        squares[boxPosition.row][boxPosition.col].moveBoxOn(); // place box on its new position
        drawSquare(boxPosition);

        Trace.println("Push " + direction);
        ActionRecord recordPush = new ActionRecord("push", direction);
        history.push(recordPush); //Add a new ActionRecord object for a push in some direction
    }

    /** Pull: (useful for undoing a push in the opposite direction)
     *  move the Worker in direction from direction,
     *  pulling the box into the Worker's old position
     */
    public void pull(String direction) {
        String oppositeDir = oppositeDirection(direction);
        Coord boxP = workerPosition.next(oppositeDir);

        squares[boxP.row][boxP.col].moveBoxOff();
        squares[workerPosition.row][workerPosition.col].moveBoxOn();

        drawSquare(boxP);
        drawSquare(workerPosition);

        workerPosition = workerPosition.next(direction);
        workerDirection = oppositeDir;
        drawWorker();

        Trace.println("Pull " + direction);
        ActionRecord recordPull = new ActionRecord("pull", direction);
        history.push(recordPull); //Add a new ActionRecord object for a pull in some direction
    }

    /** Load a grid of squares (and Worker position) from a file */
    public void load(int level) {

        for(int i=0; i<history.size(); i++){ //Iterate through history and delete all movements for new game.
            history.pop();
        }

        File f = new File("warehouse" + level + ".txt");

        if (f.exists()) {
            List<String> lines = new ArrayList<String>();

            try {
                Scanner sc = new Scanner(f);

                while (sc.hasNext())
                    lines.add(sc.nextLine());

                sc.close();
            } catch(IOException e) {
                Trace.println("File error: " + e);
            }

            rows = lines.size();
            cols = lines.get(0).length();

            squares = new Square[rows][cols];

            for(int row = 0; row < rows; row++) {
                String line = lines.get(row);

                for(int col = 0; col < cols; col++) {

                    if (col>=line.length())
                        squares[row][col] = new Square("empty");
                    else {
                        char ch = line.charAt(col);

                        if ( fileCharacterToSquareType.containsKey(ch) )
                            squares[row][col] = new Square(fileCharacterToSquareType.get(ch));
                        else {
                            squares[row][col] = new Square("empty");
                            UI.printf("Invalid char: (%d, %d) = %c \n", row, col, ch);
                        }

                        if (ch=='A')
                            workerPosition = new Coord(row,col);
                    }
                }
            }
            draw();

        }
    }

    // Drawing 

    private static final int leftMargin = 40;
    private static final int topMargin = 40;
    private static final int squareSize = 25;

    /** Draw the grid of squares on the screen, and the Worker */
    public void draw() {
        UI.clearGraphics();
        // draw squares
        for(int row = 0; row<rows; row++)
            for(int col = 0; col<cols; col++)
                drawSquare(row, col);

        drawWorker();
    }

    private void drawWorker() {
        UI.drawImage(directionToWorkerImage.get(workerDirection),
            leftMargin+(squareSize* workerPosition.col),
            topMargin+(squareSize* workerPosition.row),
            squareSize, squareSize);
    }

    private void drawSquare(Coord pos) {
        drawSquare(pos.row, pos.col);
    }

    private void drawSquare(int row, int col) {
        String imageName = squares[row][col].imageName();

        if (imageName != ".gif")
            UI.drawImage(imageName,
                leftMargin+(squareSize* col),
                topMargin+(squareSize* row),
                squareSize, squareSize);
    }

    /** 
     *  @returns true, if the warehouse is solved, i.e.,  
     *  all the shelves have boxes on them 
     */
    public boolean isSolved() {
        for(int row = 0; row<rows; row++) {
            for(int col = 0; col<cols; col++)
                if(squares[row][col].isEmptyShelf())
                    return  false;
        }

        return true;
    }

    /** 
     * @return the direction that is opposite of the parameter 
     */
    public String oppositeDirection(String direction) {
        if ( direction.equalsIgnoreCase("right")) return "left";
        if ( direction.equalsIgnoreCase("left"))  return "right";
        if ( direction.equalsIgnoreCase("up"))    return "down";
        if ( direction.equalsIgnoreCase("down"))  return "up";
        return direction;
    }

    private void initialiseMappings() {
        // character in level file -> square type
        fileCharacterToSquareType = new HashMap<Character, String>();
        fileCharacterToSquareType.put('.',  "empty");
        fileCharacterToSquareType.put('A', "empty");  // initial position of worker is an empty square beneath
        fileCharacterToSquareType.put('#',  "wall");
        fileCharacterToSquareType.put('S', "emptyShelf");
        fileCharacterToSquareType.put('B',  "box");

        // worker direction ->  image of worker
        directionToWorkerImage = new HashMap<String, String>();
        directionToWorkerImage.put("up", "worker-up.gif");
        directionToWorkerImage.put("down", "worker-down.gif");
        directionToWorkerImage.put("left", "worker-left.gif");
        directionToWorkerImage.put("right", "worker-right.gif");

        // key string -> action to perform
        keyToAction = new HashMap<String,String>();
        keyToAction.put("i", "up");     keyToAction.put("I", "up");   
        keyToAction.put("k", "down");   keyToAction.put("K", "down"); 
        keyToAction.put("j", "left");   keyToAction.put("J", "left"); 
        keyToAction.put("l", "right");  keyToAction.put("L", "right");

        keyToAction.put("w", "up");     keyToAction.put("W", "up");   
        keyToAction.put("s", "down");   keyToAction.put("S", "down"); 
        keyToAction.put("a", "left");   keyToAction.put("A", "left"); 
        keyToAction.put("d", "right");  keyToAction.put("D", "right");
    }

    public static void main(String[] args) {
        new Sokoban();
    }
}
