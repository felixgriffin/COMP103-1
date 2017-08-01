// This program is copyright VUW.
// You are granted permission to use it to construct your answer to a COMP103 assignment.
// You may not distribute it in any other way without permission.

/* Code for COMP103 - 2017T2, Assignment 2
 * Name:
 * Username:
 * ID:
 */

import ecs100.*;
import java.util.*;

/** The FastFood game involves customers who generate orders, and the player
 *  who has to fulfill the orders by assembling the right collection of food items.
 *  The goal of the game is to make as much money as possible before
 *  the player gets too far behind the customers and is forced to give up.
 *
 *  The game presents the player with a queue of orders in a fast food outlet.
 *  The player has to fullfill the customer orders by adding the correct items to
 *  the order at the head of the queue.  
 *  When the order is ready, the player can deliver the order, which will
 *  take it off the queue, and will add the price of the order to the balance.
 *  Whenever the player adds an item to the order that doesn't belong in the
 *  order, the price of the item is subtracted from the balance.
 *  The player can practice by generating orders using the Practice button.
 *  Once the game is started, the orders are generated automatically.
 */
public class FastFood{

    private Queue<Order> orders;
    private double balance;
    public static Map <String, Double> prices;


    public FastFood() {
        orders = new ArrayDeque<Order>();

        prices = new HashMap<String, Double>();
        prices.put("Fish", 2.50);
        prices.put("Chips", 1.50);
        prices.put("Burger", 5.00);

        UI.addButton("Practice Order", () -> {generateOrder(); drawOrders();});
        UI.addButton("Add Fish",       () -> {addItem("Fish"); drawOrders();});
        UI.addButton("Add Chips",      () -> {addItem("Chips"); drawOrders();});
        UI.addButton("Add Burger",     () -> {addItem("Burger"); drawOrders();});
        UI.addButton("Deliver Order",  () -> {deliverOrder(); drawOrders();});
        UI.addButton("Start Game",     () -> {startGame(); drawOrders();});

        drawOrders();
        this.run();

    }

    /** Creates a new order and puts it on the queue to be processed */
    public void generateOrder() {
        Order o = new Order();
        orders.offer(o);
    }

    /** As long as there is an order in the queue, adds the specified
     *  item to the order at the head of the queue,
     *  If adding the item fails (i.e., it isn't one of the items
     *  that are wanted by the order) then the price
     *  of the item is deducted from the current balance.
     */
    public void addItem(String item) {
        if(!orders.isEmpty()){
            if(orders.peek().addItemToOrder(item)) {
            }
            else{
                balance-=orders.peek().getPrice();
                UI.println("$"+orders.peek().getPrice() + " has been deducted from your balance.");
            }
        }
    }

    /** As long as there is an order at the front of the queue and it is ready,
     *  take the first order off the queue, compute the price of the order,
     *  and update the total balance by adding the order price.
     *  If there is not a ready order on the queue, it prints a warning message.
     */
    public void deliverOrder() {
        if(!orders.isEmpty()) {
            if (orders.peek().isReady()) {
                this.balance += (orders.poll().getPrice());
            }
            else {
                UI.println("The order is not ready");
            }
        }
    }

    /** Draws the queue of orders on the Graphics pane.
     *  Also draws the current balance in the top left corner
     */
    public void drawOrders() {
        UI.clearGraphics();
        if(!orders.isEmpty()){
            int count=0;
            for(Order o : orders){
                o.draw(10+(count*50));
                count++;
            }
        }
        UI.drawString("$"+this.balance, 10, 10);
    }

    // In the game version, the orders must be automatically generated.
    // The methods below will reset the queue and the current balance,
    // and will then set the gameRunning field to true. This will make
    // the run method start generating orders.
    // The run method is called from the main method, and therefore is in the main
    // thread, which executes concurrently with all the GUI buttons.
    // run  does nothing until the gameRunning field is set to be true
    // Once the gameRunning field is true, then it will generate orders automatically,
    // every timeBetweenOrders milliseconds. It will also makde the games speed up
    // gradually, by steadily reducing the timeBetweenOrders.
    // You do not need to write these methods code.

    private boolean gameRunning = false;
    private long timeBetweenOrders = 5000;

    private void startGame(){
        UI.clearGraphics();
        UI.clearText();
        orders.clear();
        balance = 0;
        timeBetweenOrders = 5000;
        gameRunning = true;
    }

    public void run() {
        long timeBetweenSpeedups = 2000;
        long timeNextOrder = 0;
        long timeNextSpeedup = 0;
        
        while (true) { // forever...
            UI.sleep(100); // Wait at least 100 milliseconds between actions.
            long now = System.currentTimeMillis();
            
            if (!gameRunning) 
                continue;  // if gameRunning is false, then don't generate orders
                
            // add another order, if the time has come
            if (now >= timeNextOrder) {
                timeNextOrder = now + timeBetweenOrders;
                generateOrder();
                drawOrders();
            }
            
            // speed up order generation, if the time has come
            if (now >= timeNextSpeedup) {   
                if (timeBetweenOrders> 200)    // as long as maximum speed has not been reached...
                    timeBetweenOrders -= 100;  // reduce interval
                    
                timeNextSpeedup = now + timeBetweenSpeedups;
            }
            
            if (orders.size() > 20) {
                UI.println("Oh no! You have too many orders waiting! Game over...");
                orders.clear();
                gameRunning = false;
                break;
            }
        }
    }

    public static void main(String args[]) {
        FastFood ff = new FastFood();
    }
}
