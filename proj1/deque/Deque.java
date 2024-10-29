package deque;

public interface Deque<Item> {

    public void addFirst(Item t);
    public void addLast(Item t);
    public int size();
    public void printDeque();
    public Item removeFirst();
    public Item removeLast();
    public Item get(int index);
    default public boolean isEmpty(){
        if(this.size() == 0){
            return true;
        }
        return false;
    }
}
