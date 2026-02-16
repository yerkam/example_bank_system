package src;

/**
 * TransactionList class represents a linked list of transactions. 
 * Each transaction is represented by a TransactionNode, which contains the transaction ID, amount, and references to the previous and next nodes in the list.
 * The TransactionList class provides methods to add transactions and convert the list to a 2D array for easier access.
 */
public class TransactionList {
    private TransactionNode head;
    private TransactionNode tail;
    private int size;

    public TransactionList(){
        head = null;
        tail = null;
        size = 0;
    }

    public void add(long ID, int amount, String date){
        TransactionNode newNode = new TransactionNode(ID, amount, date);
        if(head == null && tail == null){
            head = newNode;
            tail = newNode;
        }else{
            newNode.setPrev(tail);
            tail.setNext(newNode);
            tail = newNode;
        }
        size++;
    }

    public String[][] toArray(){
        String[][] arr = new String[size][3];
        TransactionNode current = head;
        int index = 0;
        while(current != null){
            arr[index][0] = String.valueOf(current.getID());
            arr[index][1] = String.valueOf(current.getamount());
            arr[index][2] = current.getDate();
            current = current.getNext();
            index++;
        }
        return arr;
    } 
}

/**
 * TransactionNode class represents a single transaction in the TransactionList.
 * It contains the transaction ID, amount, date, and references to the previous and next nodes in the list.
 */
class TransactionNode {
    private TransactionNode prev;
    private long ID;       
    private int amount; 
    private String date;
    private TransactionNode next; 

    public TransactionNode(long ID, int amount, String date) {
        this.prev = null;
        this.ID = ID;
        this.amount = amount;
        this.date = date;
        this.next = null;
    }

    public TransactionNode getPrev() {
        return prev;
    }

    public void setPrev(TransactionNode prev) {
        this.prev = prev;
    }

    public TransactionNode getNext() {
        return next;
    }

    public void setNext(TransactionNode next) {
        this.next = next;
    }

    public long getID() {
        return ID;
    }

    public int getamount() {
        return amount;
    }

    public String getDate() {
        return date;
    }
}
