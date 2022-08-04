package heapsolving.schedule.symsolve;

import java.util.HashSet;
import java.util.Set;

import heapsolving.schedule.Job;
import heapsolving.schedule.List;
import korat.finitization.IFinitization;
import korat.finitization.IObjSet;
import korat.finitization.impl.FinitizationFactory;

public class Schedule {
	
	public final static int MAXPRIO = 3;

	public Job curProc;

	public List prio_1;
	public List prio_2;
	public List prio_3;

	public List blockQueue;

	public static IFinitization finSchedule(int jobsNum) {
		IFinitization f = FinitizationFactory.create(Schedule.class);

		IObjSet jobs = f.createObjSet(Job.class, jobsNum, true);
		f.set(Schedule.class, "curProc", jobs);

		IObjSet lists = f.createObjSet(List.class, 4, true);
		f.set(Schedule.class, "prio_1", lists);
		f.set(Schedule.class, "prio_2", lists);
		f.set(Schedule.class, "prio_3", lists);
		f.set(Schedule.class, "blockQueue", lists);

		f.set(Job.class, "next", jobs);
		f.set(Job.class, "prev", jobs);
		f.set(Job.class, "val", f.createIntSet(0, jobsNum - 1));
		f.set(Job.class, "priority", f.createIntSet(1, MAXPRIO));

		f.set(List.class, "mem_count", f.createIntSet(0, jobsNum));
		f.set(List.class, "first", jobs);
		f.set(List.class, "last", jobs);

		return f;
	}

	public static IFinitization propertyCheckFinSchedule(int jobsNum) {
		return finSchedule(jobsNum + 1);
	}

	public boolean repOK() {
		if (prio_1 == null)
			return false;
		if (prio_2 == null)
			return false;
		if (prio_3 == null)
			return false;
		if (blockQueue == null)
			return false;
		
		HashSet<List> visitedPQ = new HashSet<List>();
		visitedPQ.add(prio_1);
		if (!visitedPQ.add(prio_2))
			return false;
		if (!visitedPQ.add(prio_3))
			return false;
		if (!visitedPQ.add(blockQueue))
			return false;

		
		Set<Job> visitedJobs = new HashSet<Job>();
		if (!isDoublyLinkedList(prio_1, visitedJobs))
			return false;
		if (!isDoublyLinkedList(prio_2, visitedJobs))
			return false;
		if (!isDoublyLinkedList(prio_3, visitedJobs))
			return false;
		if (!isDoublyLinkedList(blockQueue, visitedJobs))
			return false;

		return true;
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

		if (last != current)
			return false;
		return true;
	}
}
