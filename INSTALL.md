# Requirements

- Docker
- Tested on Linux and OSX Macs with Intel CPUs

**Note:** Unfortunately the artifact does not work on Macs with Apple's new M1 line of CPUs (Symbolic Pathfinder is not able to find a Z3 library even when run inside a Docker container).

# Getting Started

Clone the repository:
```
git clone https://github.com/JuanmaCopia/lissa
```
Move to the recently created folder:
```
cd lissa
```

# Install

Build the docker container:
```
docker build -t lissa . 
```
Run the container:
```
docker run -it lissa:latest /bin/bash
```

# Reproducing the experiments

## Running a single experiment

To easily run a single technique over a case study we provide the `run_study_case.sh` script. It takes the following arguments:
```
bash `run_study_case.sh` <class_name> <method_name> <max_scope> <strategy>
```

For example, to analyze `TreeMap`'s `remove` method using `LISSA`, with up to a maximum of `4` nodes in the trees, execute: 
```
bash run_case_study.sh TreeMap remove 4 LISSA
```

The results are shown on the screen, and stored in CSV format in file: ```output/results<CLASS_NAME>-results.csv ```.

## Available classes and techniques

The available classes and their corresponding methods (see Section 4 of the paper) are listed below:
```
TreeMap : [put, remove, containsKey, containsValue]
HashMap : [put, remove, containsKey, containsValue]
TreeSet : [add, remove, contains]
LinkedList : [add, remove, contains]
Schedule : [quantumExpire, addProcess, blockProcess, finishAllProcesses]
TransportStats : [bytesRead, bytesWritten]
DictionaryInfo : [addField, getField] 
CombatantStatistic : [addData, ensureTypExists]
Template : [addParameter, getParameter]
SQLFilterClauses : [put, get]
```

The available techniques (described in more detail in Section 4.1 of the paper) are:

- `LIHYBRID`: Traditional lazy initialization using an automatically derived `hybrid repOK` to identify and prune invalid lazy initialization steps.
- `IFREPOK`: Eagerly concretizes all valid structures (up to the provided bound) by performing symbolic execution of the `repOK` method, before symbolic execution the target method.
- `DRIVER`: Eagerly concretizes all valid structures (up to the provided bound) by performing symbolic execution of selected insertion routines from the target structures, before symbolic execution the target method.
- `LISSA`: Our proposed approach that employs `SymSolve` to identify and prune invalid lazy initialization steps. Note that `LISSA` runs `SymSolve` with symmetry breaking enabled (see Section 3.3 of the paper).
- `LISSAM`: `LISSA` + memoization approach (that caches the results of queries to SymSolve).
- `LISSANOSB`: A `LISSA` version that runs `SymSolve` with symmetry breaking disabled (this configuration is used to answer RQ2 in Section 4.2 of the paper).

## Running all the experiments

To reproduce all the experiments for a specific case study we provide the following scripts: 
```
bash run_treemap_experiments.sh
bash run_hashmap_experiments.sh
bash run_treeset_experiments.sh
bash run_linkedlist_experiments.sh
bash run_schedule_experiments.sh
bash run_transportstats_experiments.sh
bash run_dictionaryinfo_experiments.sh
bash run_combatantstatistic_experiments.sh
bash run_template_experiments.sh
bash run_sqlfilterclauses_experiments.sh
```

**Important:** This might take weeks or months to finish! Each script runs the aforementioned six techniques for all available methods in the case study with scopes between 1 and 50 (with a timeout of 2 hours for each single execution).

To run all experiments execute:
```
bash run_all.sh
```

Tables 1 and 2 in the paper can be generated after processing the results of this script.

**Important:** This might take months to finish!

## Analysis of postconditions

In response to the reviewers feedback, we modified the experiments in Section 4 of the paper to not only run symbolic execution of the method under analysis, but also to check postconditions at the end of the symbolic execution. The camera-ready version of the paper will be updated to reflect these changes.

Intuitively, in each experiment we intend to verify (using one of the symbolic execution approaches described above) the following Hoare's triple:
```
{pre: repOKStructure();}
	P();
{pos: repOKStructure();}
```
where `P` is the method under analysis, and `repOKStructure()` is a Java method that describes the intended structural properties of each data structure (e.g. aciclicity). 

Each symbolic execution approach handles postconditions in the most suitable way in accordance with its characteristics. Our approaches `LISSA`, `LISSAM` and `LISSANOSB` employ `SymSolve` to check postconditions over the partially symbolic structures at the end of symbolic execution paths. For `IFREPOK` and `DRIVER` we simply assert that the postcondition holds at the end of symbolic execution paths: ```assert repOKStructure();``` (this is feasible because these approaches do not generate partially symbolic structures). For `LIHYBRID` we don't check postconditions as its incapability of prunning spurious lazy initialization steps would cause postconditions to fail and the symbolic execution to end prematurely (notice that `LIHYBRID` performs very poorly w.r.t. the remaining approaches, and we are only interested in collecting the number of pahts and spurious path generated by this approach to use them as a baseline for the comparison).

The newly added postconditions might slightly change some of the numbers reported in Tables 4.1 and 4.2 in the paper, but they do not affect any of the paper's conclusions. We will include the updated results in the camera-ready version of the paper.

# Executing LISSA on new programs

## Structure of the artifact

All the case studies source code can be found inside the folder: ```src/examples/heapsolving/<class_name>```

For each study case, you can find a configuration file, with the name ```<ClassName>.jpf```. For example, consider the LinkedList configuration file:

```
classpath=${jpf-symbc}/build/examples
sourcepath=${jpf-symbc}/src/examples

# ---------------   Arguments   ---------------

target = heapsolving.linkedlist.METHOD.LinkedListMain
method = METHOD

symbolic.debug = false
symbolic.scope = MAX_SCOPE
symbolic.dp = z3

heapsolving.strategy = HEAP_SOLVING_STRATEGY
heapsolving.symsolve.class = heapsolving.linkedlist.symsolve.LinkedList
heapsolving.symsolve.predicate = repOK

listener = gov.nasa.jpf.symbc.heap.solving.HeapSolvingListener

# ---------------------------------------------
```

The study cases run scripts modify configuration files accordingly. That is, The string ```METHOD``` is replaced by the method under analysis, the ```MAX_SCOPE``` by the intended experiment scope, and ```HEAP_SOLVING_STRATEGY``` by the solving strategy to use (e.g, LISSA, IFREPOK, etc).


The ```target``` configuration indicates the class that contains the ```main``` method (the entry point that calls the method under analysis). Is important to mention that the whole ```main``` method is symbolically executed.

```heapsolving.symsolve.class``` indicates the class that contains the finitization method (the method that defines the scopes for each of the fields of the structure), this method takes as argument the provided scope in the option ```symbolic.scope```. Given that the instrumentation of Java Path Finder and SymSolve may collide and therefore cause errors, we duplicate the class containing the target program. That is, one of the classes is the one used by the main method (```target```) and the other is a duplication that contains the finitization method (```heapsolving.symsolve.class```) passed as parameter to SymSolve.

All the case studies contains a "Harness" class, with the name ```<ClassName>Harness.java```. In this class we simply manage the initial structure according to the specified heap solving technique. For example, if the DRIVER technique is being used, we call the driver's structure generator.

Below there is an example of the ```main``` method to perform symbolic execution of the ```add``` method from LinkedList:

```
public class LinkedListMain {

	public static void main(String[] args) {
		int key = SymHeap.makeSymbolicInteger("INPUT_KEY");

		LinkedList structure = LinkedListHarness.getStructure();
		if (structure != null) {
			try {
				// Call to method under analysis
				structure.add(key);
			} catch (Exception e) {
			}

			SymHeap.countPath();

            // Property Assertion:
			if (SymHeap.usingIfRepOKStrategy() || SymHeap.usingDriverStrategy())
				assert (structure.repOK());
			else if (SymHeap.usingSymSolveBasedStrategy()) {
				// Given that the add method adds a new node, we need to use a finitization with
				// increased size, "propertyCheckFinLinkedList" defined in
				// heapsolving.linkedlist.symsolve.LinkedList
				assert (SymHeap.assertPropertyWithSymSolveUsePropFinitization("repOK", structure));
			}

		}
	}
}
```

Note that after the symbolic execution of the method, we perform the property assertion. In case the technique is based on SymSolve (LISSA, LISSAM and LISSANOSB), we check the property using SymSolve. In this example, as the method add increases the size of the structure by adding a new node, we use a finitization with a the scope increased by one node, to be able to correctly encode the structure. This finitization is defined in the same class as the other, in this case in ```heapsolving.linkedlist.symsolve.LinkedList```.

## How to Add New Case Studies

Let's try the ```LISSA``` technique and the SymSolve property check.

Create a new folder in ```jpf-symbc/src/examples``` called ```mystudycase```. Inside copy the file ```jpf-symbc/src/examples/heapsolving/linkedlist/LinkedList.java``` and update the package of the copied file to ```package mystudycase;```. The following commands perform the detailed tasks:

```
mkdir src/examples/mystudycase &&
cp src/examples/heapsolving/linkedlist/LinkedList.java src/examples/mystudycase &&
sed -i -E "s/package heapsolving.*/package mystudycase;/g" src/examples/mystudycase/LinkedList.java
```

Now, inside the class ```mystudycase/LinkedList.java```, we will add the following main method:
```
public static void main(String[] args) {
	int key = SymHeap.makeSymbolicInteger("INPUT_KEY");

	LinkedList structure = new LinkedList();
	structure = (LinkedList) SymHeap.makeSymbolicRefThis("linkedlist_0", structure);

	if (structure != null) {
		try {
			// Call to method under analysis
			structure.remove(key);
		} catch (Exception e) {
		}

		SymHeap.countPath();

		// Property Assertion:
		assert (SymHeap.assertPropertyWithSymSolve("repOK", structure));
	}
}
```

To make it easier, we already have this method defined in jpf-symbc/src/examples/exampleMain, we will append it to the end of the file with the following commands:
```
sed -i '$d' src/examples/mystudycase/LinkedList.java &&
sed -i '$d' src/examples/mystudycase/LinkedList.java &&
cat src/examples/exampleMain >> src/examples/mystudycase/LinkedList.java &&
printf "\n}\n" >> src/examples/mystudycase/LinkedList.java
```

This main method creates a symbolic LinkedList, calls the target method ```remove``` and verifies with SymSolve that after the execution of ```remove```, the ```repOK``` holds.

Next, create a new folder inside ```mystudycase``` called ```symsolve```, copy the file ```jpf-symbc/src/examples/linkedlist/symsolve/LinkedList.java``` that defines the finitization method for the LinkedList, and update its package to ```package mystudycase.symsolve;```. We can use the following commands:
```
mkdir src/examples/mystudycase/symsolve &&
cp src/examples/heapsolving/linkedlist/symsolve/LinkedList.java src/examples/mystudycase/symsolve &&
sed -i -E "s/package heapsolving.*/package mystudycase.symsolve;/g" src/examples/mystudycase/symsolve/LinkedList.java
```

Next, create a configuration file called ```LinkedList.jpf``` inside ```mystudycase```, And add the following content to the new file:
```
classpath=${jpf-symbc}/build/examples
sourcepath=${jpf-symbc}/src/examples

# ---------------   Arguments   ---------------

target = mystudycase.LinkedList
method = remove

symbolic.debug = false
symbolic.scope = 4
symbolic.dp = z3

heapsolving.strategy = LISSA
heapsolving.symsolve.class = mystudycase.symsolve.LinkedList
heapsolving.symsolve.predicate = repOK

listener = gov.nasa.jpf.symbc.heap.solving.HeapSolvingListener

# ---------------------------------------------
```

To make it easier, we already have this config defined in jpf-symbc/src/examples/exampleConfig, we will retrieve it with the following command:
```
cat src/examples/exampleConfig > src/examples/mystudycase/LinkedList.jpf &&
tree src/examples/mystudycase
```

The new folder should look as follows:
```
mystudycase
├── LinkedList.java         --> This file contains the main java method from above 
├── LinkedList.jpf          --> This file contains the above configuration
└── symsolve
    └── LinkedList.java     --> This file contains the finitization method

1 directory, 3 files
```

Now, let's compile by running the ```ant build```:
```
ant build
```

After the successful build, you can run the new added study case with:
```
bash run.sh src/examples/mystudycase/LinkedList.jpf   
```

You should obtain an output like the following:
```
symbolic.min_int=-2147483648
symbolic.min_long=-9223372036854775808
symbolic.min_short=-32768
symbolic.min_byte=-128
symbolic.min_char=0
symbolic.max_int=2147483647
symbolic.max_long=9223372036854775807
symbolic.max_short=32767
symbolic.max_byte=127
symbolic.max_char=65535
symbolic.min_double=4.9E-324
symbolic.max_double=1.7976931348623157E308
JavaPathfinder core system v8.0 - (C) 2005-2014 United States Government. All rights reserved.


====================================================== system under test
mystudycase.LinkedList.main()

====================================================== search started: 8/4/22 3:15 PM

 --------  LISSA Finished for LinkedList.remove Scope 4  --------

    ExecutedPaths:  9
    InvalidPaths:   0
    TotalTime:      0 s.
    SolvingTime:    0 s.

 ------------------------------------------------------------------

====================================================== results
no errors detected

====================================================== statistics
elapsed time:       00:00:00
states:             new=76,visited=0,backtracked=76,end=18
search:             maxDepth=12,constraints=0
choice generators:  thread=1 (signal=0,lock=1,sharedRef=0,threadApi=0,reschedule=0), data=24
heap:               new=384,released=183,maxLive=359,gcCycles=42
instructions:       3581
max memory:         236MB
loaded code:        classes=62,methods=1326
```

Note that no assertions failed, and therefore we verified that for a scope of 4 nodes or less, the remove method holds the repOK.

Now let's induce an error in the remove method to check that the assertion of properties is properly working. Inside the LinkedList implementation, look for the private method ```remove(Entry e)``` that is used by the target public method ```remove(int o)```:
```
private void remove(Entry e) {
	if (e == header) {
		throw new NoSuchElementException();
	}

	e.previous.next = e.next;
	e.next.previous = e.previous;
	size--;
}
```
Instead of correctly setting ```e.next.previous``` to ```e.previous```, we can set it to null.

Run the following command to perform that change:
```
sed -i -E "s/e.next.previous =.*/e.next.previous = null;/g" src/examples/mystudycase/LinkedList.java
```

The resulted method should be the following (without the comment):
```
private void remove(Entry e) {
	if (e == header) {
		throw new NoSuchElementException();
	}

	e.previous.next = e.next;
	e.next.previous = null;     // Induced defect!!
	size--;
}
```

compile again:
```
ant build
```

And run again:
```
bash run.sh src/examples/mystudycase/LinkedList.jpf
```

You should have obtained an output like the following:
```
symbolic.min_int=-2147483648
symbolic.min_long=-9223372036854775808
symbolic.min_short=-32768
symbolic.min_byte=-128
symbolic.min_char=0
symbolic.max_int=2147483647
symbolic.max_long=9223372036854775807
symbolic.max_short=32767
symbolic.max_byte=127
symbolic.max_char=65535
symbolic.min_double=4.9E-324
symbolic.max_double=1.7976931348623157E308
JavaPathfinder core system v8.0 - (C) 2005-2014 United States Government. All rights reserved.


====================================================== system under test
mystudycase.LinkedList.main()

====================================================== search started: 8/4/22 4:09 PM

====================================================== error 1
gov.nasa.jpf.vm.NoUncaughtExceptionsProperty
java.lang.AssertionError
	at mystudycase.LinkedList.main(LinkedList.java:468)


====================================================== snapshot #1
thread java.lang.Thread:{id:0,name:main,status:RUNNING,priority:5,isDaemon:false,lockCount:0,suspendCount:0}
  call stack:
	at mystudycase.LinkedList.main(LinkedList.java:468)


 --------  LISSA Finished for LinkedList.remove Scope 4  --------

    ExecutedPaths:  2
    InvalidPaths:   0
    TotalTime:      0 s.
    SolvingTime:    0 s.

 ------------------------------------------------------------------

====================================================== results
error #1: gov.nasa.jpf.vm.NoUncaughtExceptionsProperty "java.lang.AssertionError  at mystudycase.LinkedLis..."

====================================================== statistics
elapsed time:       00:00:00
states:             new=15,visited=0,backtracked=7,end=2
search:             maxDepth=8,constraints=0
choice generators:  thread=1 (signal=0,lock=1,sharedRef=0,threadApi=0,reschedule=0), data=8
heap:               new=380,released=23,maxLive=357,gcCycles=10
instructions:       3293
max memory:         236MB
loaded code:        classes=66,methods=1367
```
Notice that it indicated the assertion of the repOK in the main method failed. Thus, revealing the error.
