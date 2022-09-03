# Requirements

- Docker

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

## Recommended setup: Linux and Mac OS X on Intel sillicon

Build the docker container:
```
docker build -t lissa . 
```
Run the container:
```
docker run -it lissa:latest /bin/bash
```
**Note:** The experiments in the paper were run on Intel sillicon.

## Setup for Mac OS X on Apple sillicon

**Warning:** For `Symbolic Pathfinder` with the `Z3` solver (and thus `LISSA`) to run in Apple sillicon, a Docker container for the amd64 platform must be created (an arm64 container did not work for me). Thus, the container must be run in emulated mode in the Apple CPU, which might produce in a significant performance hit and some issues (see below).

Build the docker container (using `buildx` [0]):
```
docker buildx create --name amd64builder
docker buildx use amd64builder
docker buildx build --platform linux/amd64 -t lissa . --load
```

**Warning:** The creation of the container might hang. It worked for me after restarting the process a few times.

Run the container:
```
docker run --platform linux/amd64 -it lissa:latest /bin/bash
```

[0] https://docs.docker.com/desktop/multi-arch/


# Folder structure

The `jpf-symbc` folder contains a modified version of Symbolic PathFinder in which we implement our approach.

The `jpf-core` folder includes the version of Java PathFinder Model Checker [1] in which Symbolic PathFinder is based upon.

The folder `symsolve` contains the source files of `SymSolve`, our solver for partially symbolic structures.

[1] https://github.com/corinus/jpf-core

## Folder structure of a case study

The source files of the case studies can be found in: `jpf-symbc/src/examples/heapsolving`. Each case study have the following files and folders:

```
├── <ClassName>.java             --> The java class that contains the methods under test
├── <ClassName>.jpf              --> The configuration file
├── <ClassName>Harness.java      --> A Harness necessary to run techniques that require perform previous actions before SUT's execution (IFREPOK and DRIVER).
├── <method 1>                   --> A folder for each of the SUTs, containing the main entry for the SUT
│   └── <ClassName>Main.java     --> A java class that contains the main entry that calls SUT
├── <method 2>
│   └── <ClassName>Main.java
├── <method 3>
│   └── <ClassName>Main.java
└── symsolve
    └── <ClassName>.java        --> A java class containing the finitization method 
```

# Reproducing the experiments

## Running a single experiment

To easily run a single technique over a case study we provide the `run_case_study.sh` script. It takes the following arguments:
```
bash run_case_study.sh <class_name> <method_name> <max_scope> <strategy>
```

For example, to analyze `LinkedList`'s `remove` method using `LISSA`, with up to a maximum of `4` nodes in the lists, execute: 
```
bash run_case_study.sh LinkedList remove 4 LISSA
```

The results are shown on the screen, and stored in CSV format in file: ```output/results/<CLASS_NAME>-results.csv ```.

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

## Understanding the output of the analysis

When the analysis of a program finishes, the metrics of that execution are stored under `jpf-symbc/output/results/<CLASS_NAME>-results.csv`, where `<CLASS_NAME>` is the name of the class containing the method under analysis.
Each run appends a new line to the `.csv` file, with the following data:

```
<MethodName>, <Technique>, <Scope>, <TotalTime>, <SolvingTime>, <NumberOfPaths>, <SpuriousPaths>, <CacheHits>
```

For example, The following `.csv` file shows the results of the analysis of the remove method from `LinkedList` for all techniques with a maximum scope of 3: 
```
remove, LIHYBRID, 1, 0, 0, 1, 0, 0
remove, LIHYBRID, 2, 0, 0, 4, 1, 0
remove, LIHYBRID, 3, 1, 0, 23, 17, 0
remove, DRIVER, 1, 0, 0, 3, 0, 0
remove, DRIVER, 2, 0, 0, 6, 0, 0
remove, DRIVER, 3, 0, 0, 10, 0, 0
remove, IFREPOK, 1, 0, 0, 1, 0, 0
remove, IFREPOK, 2, 0, 0, 3, 0, 0
remove, IFREPOK, 3, 0, 0, 6, 0, 0
remove, LISSA, 1, 0, 0, 1, 0, 0
remove, LISSA, 2, 0, 0, 3, 0, 0
remove, LISSA, 3, 0, 0, 6, 0, 0
remove, LISSAM, 1, 0, 0, 1, 0, 0
remove, LISSAM, 2, 0, 0, 3, 0, 0
remove, LISSAM, 3, 0, 0, 6, 0, 0
remove, LISSANOSB, 1, 0, 0, 1, 0, 0
remove, LISSANOSB, 2, 0, 0, 3, 0, 0
remove, LISSANOSB, 3, 0, 0, 6, 0, 0
```

The complete output for the `LinkedList` experiments can be found in `jpf-symbc/example-output/LinkedList-results.csv`.

# Symbolic execution of user programs with LISSA

Read below if you want to run `LISSA` on your own program.

## Structure of the artifact

For case studies in the `LISSA` evaluation, the source code for `<class_name>` (where `<class_name>` is one of the options above) is inside the folder: ```src/examples/heapsolving/<class_name>```.

In the folder, you can find a `Symbolic PathFinder`'s configuration file with name ```<ClassName>.jpf```. For example, consider the `LinkedList` configuration file shown below:

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

The `run_case_study.sh` script modifies the configuration files accordingly: `METHOD` is replaced by the name of the method to be analyzed, `MAX_SCOPE` by the intended experiment scope, and `HEAP_SOLVING_STRATEGY` by the symbolic execution approach to be employed (e.g. `LISSA`, `IFREPOK`, etc).

The `target` option indicates the class that contains the `main` method, that is, the entry point to perform the symbolic execution of the desired method.

`heapsolving.symsolve.class` indicates the class that contains the finitization method (the method that defines the scopes for each of the classes and fields involved, in the way a finitization is defined by the `Korat` tool). The finitization takes as argument the provided `symbolic.scope`. Given that the instrumentation of `Java PathFinder` and `SymSolve` often collides and causes errors, we create a copy of the class containing the target program. That is, one of the classes is used by the main method (`target`) and the other is a duplicate that contains the finitization method required by `SymSolve` (`heapsolving.symsolve.class`).

`heapsolving.symsolve.predicate` indicates the name of the method to be used as precondition for the SymSolve based techniques. Both `heapsolving.symsolve.class` and `heapsolving.symsolve.predicate` are needed for all lazy initialization based techniques. That is, all the assessed techniques except for `DRIVER`. This is because we use SymSolve's finitization method to define the bounds for the Lazy Initialization steps.

Given that the techniques `DRIVER` and `IFREPOK` require a special harness to be provided, e.g, `DRIVER` needs a method building structures using insertion routines, all the case studies contains a "Harness" class, with the name `<ClassName>Harness.java`. The purpose of this class is to execute the appropriate harness according to the technique. (The other studied heap solving strategies don't require any harness, just a symbolic instance of the input).

## Running LISSA on a user provided method

The easiest way to run `LISSA` for a user provided method is to copy the structure of an existing case study from folder `src/examples`. Let's say we want to analyze the buggy `removeBuggy` method for `LinkedList` shown below:

```
public boolean removeBuggy(int o) {
	for (Entry e = header.next; e != header; e = e.next) {
		if (o == e.element) {
			removeBuggy(e);
			return true;
		}
	}
	return false;
}

private void removeBuggy(Entry e) {
	if (e == header) {
		throw new NoSuchElementException();
	}

	e.previous.next = e.next;
	// e.next.previous = e.previous;
	e.next.previous = null;     // Induced defect!!
	size--;
}
```

Let's call the new case study `mycasestudy`. In the following, we describe the five steps that need to be performed to analyze this code with `LISSA`. 

### Step 1: Create a folder for the new case study, and copy the target class

First, we create a new folder for the new case study in `src/examples`, in this case with name `mycasestudy`, and copy the class we want to analyze to it. In this example, we will use the existing `LinkedList` class as a starting point (`src/examples/heapsolving/linkedlist/LinkedList.java`) and copy it into the folder for the new case study (`src/examples/mycasestudy`). We then have to change the package name of the class accordingly (to `package mycasestudy;`). This can be done using the following commands:

```
mkdir src/examples/mycasestudy
cp src/examples/heapsolving/linkedlist/LinkedList.java src/examples/mycasestudy
sed -i -E "s/package heapsolving.*/package mycasestudy;/g" src/examples/mycasestudy/LinkedList.java
```

### Step 2: Add the target program and the main entry point

The second step is to add target program `removeBuggy` and the main entry point (`main`) method to `src/examples/mycasestudy/LinkedList.java` to symbolically execute `removeBuggy` using `LISSA`. An example `main` is shown below:

```
public static void main(String[] args) {

	int key = SymHeap.makeSymbolicInteger("INPUT_KEY");
	LinkedList structure = new LinkedList();
	structure = (LinkedList) SymHeap.makeSymbolicRefThis("linkedlist_0", structure);

	if (structure != null) {
		try {
			// Execute method under analysis with symbolic input structure `structure` and symbolic integer `key`
			// The assumed precondition is the one defined in the configuration: heapsolving.symsolve.predicate
			// in this case, "repOK".
			structure.removeBuggy(key);
		} catch (Exception e) {
		}

		// Call this method if you want LISSA to report the number of explored paths
		SymHeap.countPath();

		// Assert postcondition:
		assert (structure.repOK());
	}
}
```

`main` above creates a symbolic list `structure` and a symbolic integer `key`, calls the target method `removeBuggy` with the symbolic inputs, and verifies that `repOK` holds after the execution of `removeBuggy` using `SymSolve`. During the symbolic execution, the predicate defined in the configuration `heapsolving.symsolve.predicate` will be assumed as precondition, in our example, the `repOK`. This means that `SymSolve` will prune all inputs violating the `repOK`.

To make it easier for the user, the `main` from above along with the program under analysis `removeBuggy` are already provided in the file `src/examples/snippets/completeSnippet`, and we will append it to the end of `src/examples/mycasestudy/LinkedList.java`.

First, remove the last closing bracket `}` of the `LinkedList` class to append the code inside the class:
```
head -n -2 src/examples/mycasestudy/LinkedList.java > tmp
mv tmp src/examples/mycasestudy/LinkedList.java
```

Then, append the provided code snippet:
```
cat src/examples/snippets/completeSnippet >> src/examples/mycasestudy/LinkedList.java
```

### Step 3: Create a finitization to define the scopes of the analysis

For this step we use the existing `LinkedList` finitization defined in `src/examples/linkedlist/symsolve/LinkedList.java`. We'll put the finitization in a `symsolve` folder inside `src/examples/mycasestudy`. We then change the package of the newly created finitization to `package mycasestudy.symsolve;`. This step can be done using the following commands:

```
mkdir src/examples/mycasestudy/symsolve
cp src/examples/heapsolving/linkedlist/symsolve/LinkedList.java src/examples/mycasestudy/symsolve
sed -i -E "s/package heapsolving.*/package mycasestudy.symsolve;/g" src/examples/mycasestudy/symsolve/LinkedList.java
```

### Step 4: Create a .jpf configuration file

Finally, create a configuration file called `LinkedList.jpf` in `src/examples/mycasestudy` with the following content:
```
classpath=${jpf-symbc}/build/examples
sourcepath=${jpf-symbc}/src/examples

# ---------------   Arguments   ---------------

target = mycasestudy.LinkedList
method = removeBuggy

symbolic.debug = false
symbolic.scope = 4
symbolic.dp = z3

heapsolving.strategy = LISSA
heapsolving.symsolve.class = mycasestudy.symsolve.LinkedList
heapsolving.symsolve.predicate = repOK

listener = gov.nasa.jpf.symbc.heap.solving.HeapSolvingListener

# ---------------------------------------------
```

To make it easier, we provide this config file in `jpf-symbc/src/examples/snippets/exampleConfig`. We'll copy it with the following command:
```
cat src/examples/snippets/exampleConfig > src/examples/mycasestudy/LinkedList.jpf
```

The folder for the case study should look as follows:
```
tree src/examples/mycasestudy
mycasestudy
├── LinkedList.java         --> This file contains the main java method from above 
├── LinkedList.jpf          --> This file contains the above configuration
└── symsolve
    └── LinkedList.java     --> This file contains the finitization method

1 directory, 3 files
```

### Step 5: Compile the code and run LISSA

Compile the code with:
```
ant build
```

After the build finishes you can run the newly added study case with:
```
bash run.sh src/examples/mycasestudy/LinkedList.jpf   
```

Below is an excerpt with the relevant parts of `LISSA`'s output:
```
====================================================== system under test
mycasestudy.LinkedList.main()

====================================================== search started: 8/7/22 1:21 PM

====================================================== error 1
gov.nasa.jpf.vm.NoUncaughtExceptionsProperty
java.lang.AssertionError
	at mycasestudy.LinkedList.main(LinkedList.java:492)


====================================================== snapshot #1
thread java.lang.Thread:{id:0,name:main,status:RUNNING,priority:5,isDaemon:false,lockCount:0,suspendCount:0}
  call stack:
	at mycasestudy.LinkedList.main(LinkedList.java:492)


 --------  LISSA Finished for LinkedList.removeBuggy Scope 4  --------

    ExecutedPaths:  2
    InvalidPaths:   0
    TotalTime:      0 s.
    SolvingTime:    0 s.

 ------------------------------------------------------------------

====================================================== results
error #1: gov.nasa.jpf.vm.NoUncaughtExceptionsProperty "java.lang.AssertionError  at mycasestudy.LinkedLis..."
```

The output indicates that the assertion of `repOK` (postcondition) in the `main` method fails, thus revealing the error in `removeBuggy`.
