# DAA Assignment 2 – Data Structures

Implementation of **DynamicArray**, **MyLinkedList** and **MinHeap** from scratch (primitive `int`, no `java.util` collections inside the structures).

## Requirements

- Java 17+
- Maven 3.8+

## Project layout

```
src/main/java/com/daa/
  DynamicArray.java
  MyLinkedList.java
  MinHeap.java
  Metrics.java
  Benchmark.java
src/test/java/com/daa/
  DynamicArrayTest.java
  MyLinkedListTest.java
  MinHeapTest.java
results/
  results.csv
  plots/
REPORT.md
README.md
pom.xml
```

## Build

```bash
mvn clean compile
```

## Run tests

```bash
mvn test
```

## Run benchmark (generates results/results.csv)

```bash
mkdir -p results
mvn exec:java -Dexec.mainClass="com.daa.Benchmark"
```

or after packaging:

```bash
mvn package -DskipTests
java -cp target/assignment2-data-structures-1.0.jar com.daa.Benchmark
```

## Git workflow (as required)

- Branches: `main` (tagged `v1.0`), `feature/array`, `feature/list`, `feature/heap`, `feature/metrics`
- Example commits:
  - `feat(array): add dynamic resizing`
  - `feat(list): implement doubly linked list`
  - `feat(heap): bubble-up and bubble-down`
  - `test(heap): check heap property`
  - `feat(metrics): count steps/moves/comparisons`
  - `docs(report): add complexity table and plots`

```bash
git tag v1.0
git push origin main --tags
```
