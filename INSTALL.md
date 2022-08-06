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

## Mac OS X on Apple sillicon

**Warning:** For `Symbolic Pathfinder` with the `Z3` solver (and thus `LISSA`) to run in Apple sillicon, a Docker container for the amd64 platform must be created. Thus, the container must be run in emulated mode in the Apple CPU, which might produce in a significant performance hit.

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

# Reproducing the experiments

## Running a single experiment

**PABLO: Hay que cambiar run_study_case por run_case_study, y study case por case study, en todos lados. Estoy casi seguro que study case no existe (o al menos no se usa)**
To easily run a single technique over a case study we provide the `run_study_case.sh` script. It takes the following arguments:
```
bash run_study_case.sh <class_name> <method_name> <max_scope> <strategy>
```

For example, to analyze `LinkedList`'s `remove` method using `LISSA`, with up to a maximum of `4` nodes in the lists, execute: 
```
bash run_study_case.sh LinkedList remove 4 LISSA
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

## Note: Analysis of postconditions

In response to the reviewers feedback, we modified the experiments in Section 4 of the paper (reported in Tables 4.1 and 4.2) to not only run symbolic execution of the method under analysis, but also to check postconditions at the end of the symbolic execution. The camera-ready version of the paper will be updated to reflect these changes.

Intuitively, in each experiment we intend to verify (using one of the symbolic execution approaches described above) the following Hoare's triple:
```
{pre: repOKStructure();}
	P();
{pos: repOKStructure();}
```
where `P` is the method under analysis, and `repOKStructure` is a Java method that describes the intended structural properties of each data structure (e.g. aciclicity). In other words, we verify that the execution of the method under analysis preserves the properties of the structure.

Each symbolic execution approach handles postconditions in the most suitable way in accordance with its characteristics. Our approaches `LISSA`, `LISSAM` and `LISSANOSB` employ `SymSolve` to check postconditions over the partially symbolic structures at the end of symbolic execution paths. For `IFREPOK` and `DRIVER` we simply assert that the postcondition holds at the end of symbolic execution paths: ```assert repOKStructure();``` (this is feasible because these approaches do not generate partially symbolic structures). For `LIHYBRID` we don't check postconditions as its incapability of prunning spurious lazy initialization steps would cause postconditions to fail and the symbolic execution to end prematurely (notice that `LIHYBRID` performs very poorly w.r.t. the remaining approaches, and we are only interested in collecting the number of pahts and spurious path generated by this approach to use them as a baseline for the comparison).

The newly added postconditions might slightly change some of the numbers reported in Tables 4.1 and 4.2 in the paper, but they do not affect any of the paper's conclusions. We will include the updated results in the camera-ready version of the paper.

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

The `run_study_case.sh` script modifies the configuration files accordingly: `METHOD` is replaced by the name of the method to be analyzed, `MAX_SCOPE` by the intended experiment scope, and `HEAP_SOLVING_STRATEGY` by the symbolic execution approach to be employed (e.g. `LISSA`, `IFREPOK`, etc).

The `target` option indicates the class that contains the `main` method, that is, the entry point to perform the symbolic execution of the desired method.

`heapsolving.symsolve.class` indicates the class that contains the finitization method (the method that defines the scopes for each of the classes and fields involved, in the way a finitization is defined by the `Korat` tool). The finitization takes as argument the provided `symbolic.scope`. Given that the instrumentation of `Java PathFinder` and `SymSolve` often collides and causes errors, we create a copy of the class containing the target program. That is, one of the classes is used by the main method (`target`) and the other is a duplicate that contains the finitization method required by `SymSolve` (`heapsolving.symsolve.class`). **PABLO: Are the `heapsolving.symsolve.class` and `heapsolving.symsolve.predicate` needed for the approaches that do not rely on `SymKorat`?**

All the case studies contains a "Harness" class, with the name ```<ClassName>Harness.java```. In this class, we simply manage the initial structure according to the specified heap solving technique. For example, if the DRIVER technique is being used, we call the driver's structure generator. **PABLO: Esto no se entiende. Hasta acá parecía que target tenía el main, y por lo tanto para mi el harness era el main. Hay que explicar mejor la relación entre `target` que dice arriba que tiene el `main` y harness. Ahora que llegue hasta el final me confunde mas esto porque si entendi bien no hay que definir un harness para correr LISSA. Si es para las otras tecnicas solamente hay que aclararlo.**

## Running LISSA on a user provided method

The easiest way to run `LISSA` for a user provided method is to copy the structure of an existing case study from folder `src/examples`. Let's say we want to a analyse the buggy `removeBuggy` method for `LinkedList` shown below:

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

### Step 1: Create a folder for the new case study, and copy the code to be analyzed to the folder

First, we create a new folder for the new case study in `src/examples`, in this case with name `mycasestudy`, and copy the class with the method we want to analyze to it. In this example, we will use the existing `LinkedList` class as a starting point (`src/examples/heapsolving/linkedlist/LinkedList.java`) and copy it into the folder for the new case study (`src/examples/mycasestudy`). We then have to change the package name of the class accordingly (to `package mycasestudy;`). This can be done using the following commands:

```
mkdir src/examples/mycasestudy
cp src/examples/heapsolving/linkedlist/LinkedList.java src/examples/mycasestudy
sed -i -E "s/package heapsolving.*/package mycasestudy;/g" src/examples/mycasestudy/LinkedList.java
```

Then, add the code of the `removeBuggy` methods above to file `src/examples/mycasestudy/LinkedList.java`. **Habria que instalar nano en la VM para poder editar? O lo proveemos al metodo en algun archivo como con el main?**

### Step 2: Add a driver method to symbolically execute the target method using LISSA

The second step is to add a driver (`main`) method to `src/examples/mycasestudy/LinkedList.java` symbolically execute `removeBuggy` using `LISSA`. An example `main` is shown below:

```
public static void main(String[] args) {

	int key = SymHeap.makeSymbolicInteger("INPUT_KEY");
	LinkedList structure = new LinkedList();
	structure = (LinkedList) SymHeap.makeSymbolicRefThis("linkedlist_0", structure);

	if (structure != null) {
		try {
			// Execute method under analysis with symbolic input structure `structure` and symbolic integer `key`
			structure.removeBuggy(key);
		} catch (Exception e) {
		}

		// PABLO: The precondition is assumed to always be repOK? If true this must be stated in a comment of this method.

		// Call this method if you want LISSA to report the number of explored paths
		SymHeap.countPath();

		// Assert postcondition:
		assert (SymHeap.assertPropertyWithSymSolve("repOK", structure));
	}
}
```

`main` above creates a symbolic list `structure` and a symbolic integer `key`, calls the target method `removeBuggy` with the symbolic inputs, and verifies that `repOK` holds after the execution of `removeBuggy` using `SymSolve`. **PABLO: Falta algo de la pre**

To make it easier for the user, the `main` above is already provided in file `src/examples/exampleMain`, and we will append it to the end of `src/examples/mystudycase/LinkedList.java` with the following commands:

```
sed -i '$d' src/examples/mycasestudy/LinkedList.java
sed -i '$d' src/examples/mycasestudy/LinkedList.java PABLO: Cual es el proposito de estos comandos sed? Habria que aclararlo en un comentario porque no me parece que sean evidentes (con los de abajo me parece que alcanzaria). Estan duplicados por alguna razon?
cat src/examples/exampleMain >> src/examples/mycasestudy/LinkedList.java
printf "\n}\n" >> src/examples/mycasestudy/LinkedList.java
```

### Step 3: Create a finitization to define the scopes of the analysis

For this step we use the existing `LinkedList` finitization defined in `src/examples/linkedlist/symsolve/LinkedList.java`. To learn to define your own finitization check `Korat`'s documentation **PABLO: Agregar un link a la documentacion o al codigo de korat.**. We'll put the finitization in a `symsolve` folder inside `src/examples/mycasestudy`. We then change the package of the newly created finitization to `package mystudycase.symsolve;`. This step can be done using the following commands:

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

target = mystudycase.LinkedList
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

To make it easier, we provide this config file in `jpf-symbc/src/examples/exampleConfig`. We'll copy it with the following command:
```
cat src/examples/exampleConfig > src/examples/mycasestudy/LinkedList.jpf
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

====================================================== search started: 8/4/22 4:09 PM

====================================================== error 1
gov.nasa.jpf.vm.NoUncaughtExceptionsProperty
java.lang.AssertionError
	at mycasestudy.LinkedList.main(LinkedList.java:468)


====================================================== snapshot #1
thread java.lang.Thread:{id:0,name:main,status:RUNNING,priority:5,isDaemon:false,lockCount:0,suspendCount:0}
  call stack:
	at mycasestudy.LinkedList.main(LinkedList.java:468)


 --------  LISSA Finished for LinkedList.removeBuggy Scope 4  --------

    ExecutedPaths:  2
    InvalidPaths:   0
    TotalTime:      0 s.
    SolvingTime:    0 s.

 ------------------------------------------------------------------

====================================================== results
error #1: gov.nasa.jpf.vm.NoUncaughtExceptionsProperty "java.lang.AssertionError  at mycasestudy.LinkedLis..."

====================================================== statistics
elapsed time:       00:00:00
```
** PABLO: Actualizar con el output real, no lo puedo correr facilmente en mi PC nueva**

The output indicates that the assertion of `repOK` (postcondition) in the `main` method fails, thus revealing the error in `removeBuggy`.


# Implementation details of related approaches

**Pablo: Lo de LISSA y SymSolve ya está explicado arriba en detalle. Acá habría que quitar todo el texto que habla de LISSA y SymSolve y dar un ejemplo de un main de un IFREPOK, un DRIVER y quizás hasta un LIHYBRID, y explicar que significa cada cosa en los mains. Esto lo ponemos al final porque los reviewers que no estén interesados lo pueden ignorar, y sólo evaluar reproducibilidad y usabilidad de lissa para poner las badges (creo que la mayoría va a hacer eso). O directamente se puede omitir esta parte si no llegamos con el tiempo.

Below there is an example of the `main` method to perform symbolic execution of the `add` method from `LinkedList`:

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

Note that after the symbolic execution of the method, we perform the property assertion. ~~In case the technique is based on SymSolve (LISSA, LISSAM and LISSANOSB), we check the property using SymSolve. In this example, as the method add increases the size of the structure by adding a new node, we use a finitization with a the scope increased by one node, to be able to correctly encode the structure. This finitization is defined in the same class as the other, in this case in ```heapsolving.linkedlist.symsolve.LinkedList```. ~~.


