

## Getting Started

Clone the repository:
```
git clone https://github.com/JuanmaCopia/lissa
```

Move to the cloned repository:
```
cd lissa
```

## Installation

Build the docker container:
```
docker build -t lissa . 
```

After the docker is build, run the docker container:
```
docker run -it lissa:latest /bin/bash
```


## Replicating the experiments


To run specific experiments, run the run_study_case.sh script, with the following arguments:
```
bash run_study_case.sh <class_name> <method_name> <max_scope> <strategy>
```

For example: 
```
bash run_case_study.sh TreeMap remove 4 LISSA
```

The available methods per class are:
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

The available techniques are:

- LIHYBRID : It uses the automatically derived hybrid repOK to prune invalid lazy initialization steps.
- IFREPOK : Eagerly concretizes all valid structures up to the provided bound by performing symbolic execution (with lazy initialization) of the repOK method before executing the target program.
- DRIVER: Eagerly concretizes instances by performing symbolic execution (without lazy initialization) of insertion routines of the target structures.
- LISSA: Our proposed solution that uses our solver SymSolve to prune invalid lazy initialization steps. It also has ENABLED the proposed symmetry breaking approach.
- LISSAM: Same as LISSA, but using memoization (caching queries to SymSolve).
- LISSANOSB: It uses SymSolve but with the proposed symmetry break approach DISABLED.



To reproduce all the experiments for a specific case study one can run the following scripts: 
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


To run all experiments one can run:
```
bash run_all.sh
```

## Results

The experiments results can be found in the folder ```jpf-symbc/output/results``` in a file with the following format: ``` <CLASS_NAME>-results.csv ``` and the standard output of the executions can be found at ```jpf-symbc/output/results```.

## Assertion of Postconditions

In response to the reviewers feedback, we added assertions of postconditions to each experiment.

WARNING: Given the separation of the path condition (contains the constraints over the symbolic values of primitive type) and the symbolic heap (shape of the structure), the postconditions can only refer to "structural properties", i.e. properties involving only reference types (e.g. user defined classes). Therefore, we treat
each target program P as the following Hoare's triple:

```
{pre: repOKStructure();}
	P();
{pos: repOKStructure();}
```

The only exception being the cases using ```TreeMap``` implementations, given that the ```java.util.TreeMap``` manages the "balancing" property of red-black-trees using boolean values (the RED and BLACK colors of the nodes). In those cases, the following Hoare's triple is being checked:

```
{pre: repOKStructureAndColors();}
	P();
{pos: repOKStructure();}
```

The assertion of the postcondition is made according to the technique. That is, for our proposed techniques, we check the postcondition using our solver SymSolve. For the LIHYBRID technique we don't check postconditions given that its incapability of prunning spurious lazy initialization steps cause the postcondition to fail. For the other two techniques (IFREPOK and DRIVER) we simply execute the assertion with the corresponding method call, e.g. ```assert (repOKStructure());```

This new added assertion of postconditions might slightly affect the numbers of the case studies, but they do not affect any of the paper's conclusions. These new results will be reported in the camera-ready version of the paper.

## Source Code of the Case Studies

All the case studies source code can be found inside the folder: ```jpf-symbc/src/examples/heapsolving/<class_name>```

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