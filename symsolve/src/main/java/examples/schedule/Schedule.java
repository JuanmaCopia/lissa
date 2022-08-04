package examples.schedule;

import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

public class Schedule {
    /**
     * A job descriptor.
     */

    private final static int MAXPRIO = 3;

    public int allocProcNum;

    public int numProcesses;

    public Job curProc;

    /**
     * 0th element unused
     */
    public List prio_0;
    public List prio_1;
    public List prio_2;
    public List prio_3;

    public List blockQueue;

    public static IFinitization finSchedule(int jobsNum) {
        IFinitization f = FinitizationFactory.create(Schedule.class);

        IObjSet jobs = f.createObjSet(Job.class, jobsNum, true);
        f.set(Schedule.class, "allocProcNum", f.createIntSet(0, jobsNum + 1));
        f.set(Schedule.class, "numProcesses", f.createIntSet(0, jobsNum));
        f.set(Schedule.class, "curProc", jobs);

        IObjSet lists = f.createObjSet(List.class, 4, true);
        f.set(Schedule.class, "prio_0", lists);
        f.set(Schedule.class, "prio_1", lists);
        f.set(Schedule.class, "prio_2", lists);
        f.set(Schedule.class, "prio_3", lists);
        f.set(Schedule.class, "blockQueue", lists);

        f.set(Job.class, "next", jobs);
        f.set(Job.class, "prev", jobs);
        f.set(Job.class, "val", f.createIntSet(0, jobsNum - 1));
        f.set(Job.class, "priority", f.createIntSet(0, MAXPRIO));

        f.set(List.class, "mem_count", f.createIntSet(0, jobsNum));
        f.set(List.class, "first", jobs);
        f.set(List.class, "last", jobs);

        return f;
    }


    public boolean repOK() {
        if (!structureOK())
            return false;
        return primitivesOK();
    }

    public boolean structureOK() {
        if (blockQueue == null || prio_0 != null || prio_1 == null || prio_2 == null || prio_3 == null)
            return false;

        HashSet<List> listSet = new HashSet<List>();
        if (!listSet.add(prio_1) || !listSet.add(prio_2) || !listSet.add(prio_3) || !listSet.add(blockQueue))
            return false;

        Set<Job> visited = new HashSet<Job>();

        if (!isDoublyLinkedList(prio_1, visited))
            return false;

        if (!isDoublyLinkedList(prio_2, visited))
            return false;

        if (!isDoublyLinkedList(prio_3, visited))
            return false;

        if (!isDoublyLinkedList(blockQueue, visited))
            return false;

        if (numProcesses != visited.size())
            return false;

        return allocProcNum == visited.size();
    }

    private boolean isDoublyLinkedList(List list, Set<Job> visited) {
        Job current = list.getFirst();
        Job last = list.getLast();

        if (current == null)
            return last == null;

        if (current.getPrev() != null)
            return false;

        if (!visited.add(current))
            return false;

        while (true) {

            Job next = current.getNext();
            if (next == null)
                break;
            if (next.getPrev() != current)
                return false;
            if (!visited.add(next))
                return false;

            current = next;
        }

        return last == current;
    }

    private boolean primitivesOK() {
        TreeSet<Integer> visitedIds = new TreeSet<Integer>();
        for (int i = 1; i <= MAXPRIO; i++) {
            if (!isPriorityOfListOK(i, visitedIds))
                return false;
        }

        return isBlockQueueOK(visitedIds);
    }

    private boolean isPriorityOfListOK(int priority, Set<Integer> visitedIds) {
        List list = getPrioQueue(priority);
        Job current = list.getFirst();
        int size = 0;

        while (current != null) {
            if (current.val >= this.numProcesses || current.val < 0)
                return false;
            if (!visitedIds.add(current.val))
                return false;
            if (current.priority != priority)
                return false;
            size++;
            current = current.getNext();
        }
        return size == list.getMemCount();
    }

    private List getPrioQueue(int prio) {
        switch (prio) {
            case 0:
                return prio_0;
            case 1:
                return prio_1;
            case 2:
                return prio_2;
            case 3:
                return prio_3;
            default:
                throw new IllegalArgumentException();
        }
    }

    private boolean isBlockQueueOK(Set<Integer> visitedIds) {
        Job current = blockQueue.getFirst();
        int size = 0;

        while (current != null) {
            if (current.val >= this.numProcesses || current.val < 0)
                return false;
            if (!visitedIds.add(current.val))
                return false;
            size++;
            current = current.getNext();
        }
        return size == blockQueue.getMemCount();
    }

}
