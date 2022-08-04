package heapsolving.schedule;

import java.util.HashSet;
import java.util.Set;

public class Schedule {

	public final static int MAXPRIO = 3;

	public Job curProc;

	public List prio_1;
	public List prio_2;
	public List prio_3;

	public List blockQueue;

	// Init the queues with no processes
	public Schedule() {
		initialize();
		initPrioQueue(3, 0);
		initPrioQueue(2, 0);
		initPrioQueue(1, 0);
	}

	public Schedule(int numProc3, int numProc2, int numProc1) {
		initialize();
		initPrioQueue(3, numProc3);
		initPrioQueue(2, numProc2);
		initPrioQueue(1, numProc1);
	}

	private List appendEle(List aList, Job aEle) {
		if (aList == null) {
			aList = new List();/* make list without compare function */
		}

		aEle.setPrev(aList.getLast()); /* insert at the tail */
		if (aList.getLast() != null) {
			aList.getLast().setNext(aEle);
		} else {
			aList.setFirst(aEle);
		}
		aList.setLast(aEle);
		aEle.setNext(null);
		aList.setMemCount(aList.getMemCount() + 1);
		return aList;
	}

	private Job findNth(List fList, int n) {
		Job fEle;

		if (fList == null) {
			return null;
		}
		fEle = fList.getFirst();
		for (int i = 1; fEle != null && i < n; i++) {
			fEle = fEle.getNext();
		}
		return fEle;
	}

	private List delEle(List dList, Job dEle) {
		if (dList == null || dEle == null) {
			return null;
		}

		if (dEle.getNext() != null) {
			dEle.getNext().setPrev(dEle.getPrev());
		} else {
			dList.setLast(dEle.getPrev());
		}
		if (dEle.getPrev() != null) {
			dEle.getPrev().setNext(dEle.getNext());
		} else {
			dList.setFirst(dEle.getNext());
		}
		/* KEEP d_ele's data & pointers intact!! */
		dList.setMemCount(dList.getMemCount() - 1);
		return dList;
	}

	public void finishProcess() {
		schedule();
		curProc = null;
	}

	public void finishAllProcesses() {
		schedule();
		while (curProc != null)
			finishProcess();
	}

	private void schedule() {
		curProc = null;
		for (int i = MAXPRIO; i > 0; i--) {
			if (getPrioQueue(i).getMemCount() > 0) {
				curProc = getPrioQueue(i).getFirst();
				List list = delEle(getPrioQueue(i), curProc);
				if (list != null)
					setPrioQueue(i, list);
				return;
			}
		}
	}

	public void upgradeProcessPrio(int prio, float ratio) {
		if (prio < 1 || prio > MAXPRIO)
			throw new IllegalArgumentException();
		if (ratio < 0.0 || ratio > 1.0)
			throw new IllegalArgumentException();

		int count;
		int n;
		Job proc;
		List srcQueue, destQueue;

		if (prio >= MAXPRIO) {
			return;
		}
		srcQueue = getPrioQueue(prio);
		destQueue = getPrioQueue(prio + 1);
		count = srcQueue.getMemCount();

		if (count > 0) {
			n = (int) (count * ratio + 1);
			proc = findNth(srcQueue, n);
			if (proc != null) {
				srcQueue = delEle(srcQueue, proc);
				/* append to appropriate prio queue */
				proc.setPriority(prio + 1);
				destQueue = appendEle(destQueue, proc);
			}
		}
	}

	public void unblockProcess(float ratio) {
		if (ratio < 0.0 || ratio > 1.0)
			throw new IllegalArgumentException();

		int count;
		int n;
		Job proc;
		int prio;
		if (blockQueue != null) {
			count = blockQueue.getMemCount();
			n = (int) (count * ratio + 1);
			proc = findNth(blockQueue, n);
			if (proc != null) {
				blockQueue = delEle(blockQueue, proc);
				/* append to appropriate prio queue */
				prio = proc.getPriority();
				setPrioQueue(prio, appendEle(getPrioQueue(prio), proc));
			}
		}
	}

	public void quantumExpire() {
		int prio;
		schedule();
		if (curProc != null) {
			prio = curProc.getPriority();
			setPrioQueue(prio, appendEle(getPrioQueue(prio), curProc));
		}
	}

	public void blockProcess() {
		schedule();
		if (curProc != null) {
			blockQueue = appendEle(blockQueue, curProc);
		}
	}

	private Job newProcess(int prio) {
		if (prio < 1 || prio > MAXPRIO)
			throw new IllegalArgumentException();
		Job proc = new Job();
		proc.setPriority(prio);
		return proc;
	}

	public void addProcess(int prio) {
		if (prio < 1 || prio > MAXPRIO)
			throw new IllegalArgumentException();
		Job proc;
		proc = newProcess(prio);

		setPrioQueue(prio, appendEle(getPrioQueue(prio), proc));
	}

	public void initPrioQueue(int prio, int numProc) {
		if (prio < 1 || prio > MAXPRIO)
			throw new IllegalArgumentException();
		List queue;
		Job proc;

		queue = new List();
		for (int i = 0; i < numProc; i++) {
			proc = newProcess(prio);
			queue = appendEle(queue, proc);
		}

		setPrioQueue(prio, queue);
	}

	private void setPrioQueue(int prio, List queue) {
		switch (prio) {
		case 1:
			prio_1 = queue;
			break;
		case 2:
			prio_2 = queue;
			break;
		case 3:
			prio_3 = queue;
			break;
		default:
			throw new IllegalArgumentException();
		}
	}

	private List getPrioQueue(int prio) {
		switch (prio) {
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

	private void initialize() {
		blockQueue = new List();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("Schedule = {");
		buf.append(" prioQueue = { ");
		for (int i = 1; i <= MAXPRIO; i++) {
			List proc = getPrioQueue(i);
			buf.append(proc.toString());
		}
		buf.append(" }");
		buf.append(" , ");
		buf.append(" blockQueue = { ");
		buf.append(blockQueue.toString());
		buf.append(" }");
		buf.append(" currProc = { ");
		buf.append(curProc == null ? "null" : curProc.toString());
		buf.append(" }");
		buf.append(" }");
		return buf.toString();
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
