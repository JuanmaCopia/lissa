

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
each target program P as the following Hoare's triple: {pre: repOKStructure();} P(); {pos: repOKStructure();}. The only exception being the cases using TreeMap implementations, given that the java.util.TreeMap manages the "balancing" property of red-black-trees using boolean values (the RED and BLACK colors of the nodes). In those cases, the following Hoare's triple is being checked: {pre: repOKStructureAndColors();} P(); {pos: repOKStructure();}.

The assertion of the postcondition is made according to the technique. That is, for our proposed techniques, we check the postcondition using our solver SymSolve. For the LIHYBRID technique we don't check postconditions given that its incapability of prunning spurious lazy initialization steps cause the postcondition to fail. For the other two techniques (IFREPOK and DRIVER) we simply execute the assertion with the corresponding method call, e.g. assert (repOKStructure());

This new added assertion of postconditions might slightly affect the numbers of the case studies, but they do not affect any of the paper's conclusions. These new results will be reported in the camera-ready version of the paper.

## Source Code of the Case Studies

All the case studies source code can be found inside the folder: ```jpf-symbc/src/examples/heapsolving/<class_name>```

...

## How to Add New Case Studies

...






