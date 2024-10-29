package deque;

import java.util.Comparator;

public class MaxArrayDeque<Item> extends ArrayDeque<Item> {
    //私有比较器属性
    private Comparator<Item> cpt;

    public MaxArrayDeque(Comparator<Item> c) {
        cpt = c;
    }

    public Item max() {
        if (this.isEmpty()) {
            return null;
        }
        Item themax = this.get(0);
        for (Item i : this) {
            if (cpt.compare(i, themax) > 0) {
                themax = i;
            }
        }
        return themax;
    }

    public Item max(Comparator<Item> c) {
        if (this.isEmpty()) {
            return null;
        }
        Item themax = this.get(0);
        for (Item i : this) {
            if (c.compare(i, themax) > 0) {
                themax = i;
            }
        }
        return themax;
    }

}
