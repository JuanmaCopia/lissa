package examples.schedule;

public class List {

    public int mem_count;

    public Job first;

    public Job last;

    public List() {
        first = null;
        last = null;
        mem_count = 0;
    }

    public Job getFirst() {
        return first;
    }

    public void setFirst(Job newFirst) {
        first = newFirst;
    }

    public Job getLast() {
        return last;
    }

    public void setLast(Job newLast) {
        last = newLast;
    }

    public String toString() {
        Job curr = this.first;
        final StringBuilder buf = new StringBuilder(2 * this.getMemCount());
        buf.append('{');
        for (int i = 0; i < this.getMemCount(); i++) {
            buf.append(curr.toString());
            curr = curr.getNext();
            if (i < this.getMemCount() - 1)
                buf.append(",");
        }
        buf.append('}');
        return buf.toString();
    }

    public int getMemCount() {
        return mem_count;
    }

    public void setMemCount(int newCount) {
        mem_count = newCount;
    }
}