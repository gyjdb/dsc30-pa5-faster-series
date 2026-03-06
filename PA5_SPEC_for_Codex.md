# PA5 Spec for Codex — Faster Series Object (WI26)

This file is a **source-of-truth implementation guide** for Codex.
It is designed to minimize autograder mismatches and manual-grading deductions.
Follow this file **literally**.

---

## 0. Primary goal

Implement PA5 exactly as specified:

- `BST.java`
- `Series.java`
- `SeriesV1.java`
- `SeriesV2.java`
- `LL.java`

The final code must match the assignment specification as closely as possible, including:

- public method names
- public method signatures
- exception types
- exception messages
- printed output formatting
- generic design
- required data structures

Do **not** improvise API changes.
Do **not** add features that were not requested.
Do **not** add imports.

---

## 1. Absolute constraints

### 1.1 Allowed submission files only
The assignment submission consists of exactly:

- `Series.java`
- `SeriesV1.java`
- `SeriesV2.java`
- `BST.java`
- `LL.java`

### 1.2 No additional imports
Do **not** import any package.

### 1.3 Keep public APIs stable
Do **not** rename public classes or public methods.
Do **not** change required public method signatures.
Private helper methods are allowed where the spec says so.

### 1.4 Follow the PA5 spec over old PA4 code
The old `Series.java` from PA4 is only a **baseline reference**.
For PA5:

- `Series.java` must become an **interface**, not a class.
- `SeriesV1.java` is the linked-list implementation.
- `SeriesV2.java` is the linked-list + BST implementation.

### 1.5 Manual grading risk
Manual grading explicitly checks whether `SeriesV2` **really uses BST `searchNode()`** for fast search.
So do not create a fake BST field and continue searching only through the linked list.

---

## 2. Source-of-truth priority when conflicts exist

If any ambiguity appears, use this priority order:

1. **PA5 PDF specification**
2. **Provided starter code for BST.java**
3. **Existing LL.java / old PA4 Series.java as baseline only**
4. This markdown file

Important consequences:

- If the old `Series.java` class conflicts with PA5, **PA5 wins**.
- If current `LL.java` text output still says `print the linked list ...`, PA5 output requirement for Series must be satisfied.
- If any old code prints a slightly different error message than PA5 requires, **PA5 message wins**.

---

## 3. High-level architecture

### 3.1 `Series.java`
This file must define a **generic interface**:

```java
public interface Series<T> {
    public String toString();
    public int getLength();
    public String[] getRowNames();
    public String[] getData();
    public void append(String rn, T d);
    public T loc(String rn);
    public T[] loc(String[] rn);
    public T iloc(int ind);
    public boolean drop(String rn);
    public void fillNull(T value);
}
```

No constructor in the interface.

### 3.2 `SeriesV1.java`
Implements `Series<T>` using:

```java
private LL<T> seriesData;
```

This should behave like the old PA4 linked-list version, except adjusted for PA5 formatting / structure changes.

### 3.3 `SeriesV2.java`
Implements `Series<T>` using:

```java
private LL<T> seriesData;
private BST<String, T> seriesDataBST;
```

`seriesDataBST` must stay synchronized with `seriesData` at all times.
It is mainly used for fast label-based search.

### 3.4 `BST.java`
This is a generic binary search tree.
The index type must support comparison.
The safest design is:

```java
public class BST<I extends Comparable<I>, T> {
    ...
}
```

That way `compareTo()` can be used legally.

---

## 4. Exact deliverable expectations

You must implement:

- Part 1: BST ADT in `BST.java`
- Part 2: Series interface in `Series.java`
- Part 3: `SeriesV1.java` and `SeriesV2.java`

Only `BST.java` was provided as starter for PA5.
For the remaining parts, old `LL.java` and old `Series.java` were supposed to be used as starter references.

---

## 5. `BST.java` full spec

## 5.1 Class signature
Use a comparable bound for the index type.
Recommended:

```java
public class BST<I extends Comparable<I>, T> {
```

## 5.2 Inner class: `BSTNode`

### Required instance variables

```java
private I index;
private T data;
private BSTNode left;
private BSTNode right;
```

### Required constructors

```java
public BSTNode()
```
- Set all instance variables to `null`.

```java
public BSTNode(I _index, T _data)
```
- Set `index = _index`
- Set `data = _data`
- `left = null`
- `right = null`

### Required methods

```java
public I getIndex()
public T getData()
public void setData(T d)
public String toString()
```

### Exact `BSTNode.toString()` format
Must return exactly one line in this form:

```text
index: <INDEX>, data: <DATA>\n
```

The assignment says to use `\t` for spaces between words on each line.
Safest interpretation: preserve the exact label words and punctuation, and use tab characters consistently if the starter expects them.
Do not change the textual labels:

- `index:`
- `data:`

Do not omit the trailing newline.

---

## 5.3 BST instance variables

```java
private BSTNode root;
private int size;
```

### Initialization requirements
- `root` is `null` when BST is empty.
- `size` counts valid nodes only.

---

## 5.4 BST required methods

### Constructor

```java
public BST()
```
- Set `root = null`
- Set `size = 0`

### In-order traversal helper

```java
private String inOrderTraversal(BSTNode node)
```
- Traverse recursively in **in-order**.
- Concatenate node information by calling `BSTNode.toString()`.
- Return `""` for null subtree.

### BST `toString()`

```java
public String toString()
```

#### Exact header format
```text
In-order Traversal of the BST ...
==================
```

Then append the in-order traversal string.

#### Example output
```text
In-order Traversal of the BST ...
==================
index: a, data: 2
index: b, data: 3
index: d, data: 1
index: g, data: 4
```

### Size getter

```java
public int getSize()
```
- Return the number of valid BST nodes.

### Add node

```java
public void addNode(I _index, T _data)
```
- Insert according to BST ordering using `_index.compareTo(...)`.
- Indices are assumed unique by the assignment.
- You may use private recursive helpers.
- Increment `size` exactly once when a new node is actually inserted.

### Search node

```java
public BSTNode searchNode(I _index)
```
- Return the node whose index equals `_index`, if present.
- Otherwise return `null`.
- Use BST search logic, not linear scan.

### Remove node

```java
public void removeNode(I _index) throws IllegalArgumentException
```

#### Exact failure message
If `_index` is not found, throw:

```text
removeNode(I _index): No node with an index <THE_INDEX_VALUE> in the BST
```

Replace `<THE_INDEX_VALUE>` with the actual `_index` value.

#### Required behavior
Handle standard BST deletion cases:

1. leaf node
2. one child
3. two children

For the two-child case, use either inorder successor or inorder predecessor consistently.
Decrement `size` exactly once on successful deletion.

### Update node

```java
public void updateNode(I _index, T _newData) throws IllegalArgumentException
```

#### Exact failure message
If `_index` is not found, throw:

```text
updateNode(I _index, T _newData): No node with an index <THE_INDEX_VALUE> in the BST
```

Replace `<THE_INDEX_VALUE>` with the actual `_index` value.

#### Required behavior
- Search for the node.
- Update only the `data` field.
- Do not change tree structure.

### `getIndexArray()` and `getDataArray()`
The spec says these are already provided in starter code and do **not** need to be implemented.
If they are already present in the starter, leave them intact unless the starter is incomplete.

Their output is pre-order-based arrays:

- `getIndexArray()`: index values in pre-order traversal
- `getDataArray()`: data values in pre-order traversal, converted to strings

---

## 6. `Series.java` interface full spec

This file must be an interface, not a class.

```java
public interface Series<T> {
    public String toString();
    public int getLength();
    public String[] getRowNames();
    public String[] getData();
    public void append(String rn, T d);
    public T loc(String rn);
    public T[] loc(String[] rn);
    public T iloc(int ind);
    public boolean drop(String rn);
    public void fillNull(T value);
}
```

Do not put implementation logic here.

---

## 7. `SeriesV1.java` and `SeriesV2.java` shared behavior spec

Both classes must implement the **same observable behavior**.
The only major difference is the internal data structure and search strategy.

## 7.1 Constructor

```java
public SeriesV1(String[] _rowNames, T[] _data)
public SeriesV2(String[] _rowNames, T[] _data)
```

### Input meaning
- `_rowNames`: array of row names
- `_data`: array of T objects

### Row-name immutability requirement
Once row names are loaded into the series, later modification of the external `_rowNames` array must not change the internal series row names.
Strings are immutable, so building nodes from the values is sufficient; do not store the caller's array itself as internal state.

### Exact exceptions for constructor

#### If `_data` is null
Throw:

```text
Series(String[] _index, T[] _data): _data can't be null. Terminating the program
```

#### If `_rowNames.length != _data.length`
Throw:

```text
Series(String[] _index, T[] _data): the length of _index and _data must be the same
```

#### If not all row names are valid
Throw:

```text
Series(String[] _index, T[] _data): _rowNames is not valid
```

The old PA4 starter checked for `null` row names. Follow that unless the provided PA5 starter says otherwise.

### Required special handling: `_rowNames == null`
This case must be handled using `try...catch`, not by a separate `if (_rowNames == null)` branch.
If `_rowNames` is null, use default row names:

- first row name: `"0"`
- second row name: `"1"`
- etc.

### Population behavior
Append data in original order.

For `SeriesV1`:
- append to linked list only

For `SeriesV2`:
- append to linked list
- also insert the same `(rowName, data)` into BST

---

## 7.2 `toString()`

```java
public String toString()
```

### Required meaning
Print the current series content.
The PA5 spec says the method should print `seriesData`, using `seriesData.toString()`.

### Exact output header
```text
print the series ...
==================
```

### Example output
```text
print the series ...
==================
null : null
a : 1
b : 2
c : 3
null : null
```

### Important implementation note
The existing old `LL.java` starter may still print:

```text
print the linked list ...
```

That is **not** the PA5 Series header.
So make sure the final output observed from `SeriesV1.toString()` and `SeriesV2.toString()` matches the PA5 series wording.

### Delimiter caution
The old LL starter uses a tab before `:`, e.g. `index + "\t: " + data`.
The PDF examples render as `a : 1`.
Because tabs can render visually like spaces in documents, preserve the exact textual structure expected by the starter/autograder and do not randomly reformat the rows.
The safest path is:

- keep line order exactly the same
- change the header text from linked-list wording to series wording where necessary
- keep dummy head and tail rows visible if the LL design requires them

---

## 7.3 `getLength()`

```java
public int getLength()
```
- Return the number of valid data rows.
- Do not count dummy nodes.

---

## 7.4 `getRowNames()`

```java
public String[] getRowNames()
```
- Return all row names in current order.
- For linked-list-based implementation, use `LL.getIndexArray()`.

---

## 7.5 `getData()`

```java
public String[] getData()
```
- Return a `String[]` of data values.
- Convert `T` values to strings.
- `null` data should become `"null"` if `String.valueOf(...)` is used.

---

## 7.6 `append(String rn, T d)`

```java
public void append(String rn, T d)
```

### Required behavior
- Add a new rowName-data pair to the end of the series.
- Assume there are no duplicate row names.
- If `rn` is null, use the index-number alternative exactly as in the constructor.

### V1 behavior
- Call `seriesData.appendNode(rn, d)`.

### V2 behavior
- Append to linked list.
- Also insert the final resolved row name and data into `seriesDataBST`.
- The row name inserted into BST must match the actual row name stored in LL.

### Important synchronization detail
If `rn` is null or empty and `LL.appendNode()` converts it into a default numeric string using the current list length, the BST must use **that same final row name**, not the original null reference.

---

## 7.7 `loc(String rn)`

```java
public T loc(String rn) throws NullPointerException, IllegalArgumentException
```

### Exact exceptions
If `rn == null`, throw:

```text
loc(String rn): rn can't be null
```

If `rn.length() == 0`, throw:

```text
loc(String rn): rn can't be an empty string
```

### Return behavior
- If row name exists: return its `T` value
- If row name does not exist: return `null`

### V1 behavior
Use linked-list search.

### V2 behavior
Use **BST search**, not LL search.
This is one of the most important grading requirements.

Recommended pattern:

```java
BST<String, T>.BSTNode node = seriesDataBST.searchNode(rn);
```

If inner-class generic access syntax differs depending on implementation, adjust accordingly, but the search must come from the BST.

---

## 7.8 `loc(String[] rn)`

```java
public T[] loc(String[] rn) throws NullPointerException, IllegalArgumentException
```

### Exact exceptions
If `rn == null`, throw:

```text
loc(String[] rn): rn[] can't be null
```

If `rn.length == 0`, throw:

```text
loc(String[] rn): rn[] can't be an empty array
```

### Array creation rule
Do **not** write:

```java
T[] arr = new T[n];
```

Instead use:

```java
T[] arr = (T[]) new Object[n];
```

### Return behavior
- For each label, return the corresponding `T` value
- If a particular label is missing, put `null` at that output position
- Reuse the single-label `loc(String rn)` method

---

## 7.9 `iloc(int ind)`

```java
public T iloc(int ind)
```

### Required behavior
- Read value by integer position (0-based)
- If valid, return the corresponding `T`
- If index is invalid, handle `IndexOutOfBoundsException` via `try...catch`

### Exact printed message on invalid index
Print exactly:

```text
the index <IND> is not valid.. returning null
```

Note carefully: there are **two periods** after `valid`.

Then return `null`.

### Safe implementation strategy
Because LL is singly linked and the old starter may not provide direct integer-node access, a safe approach is:

1. call `getRowNames()`
2. read `rowNames[ind]`
3. call `loc(targetRowName)`
4. catch `IndexOutOfBoundsException`

This is acceptable.

---

## 7.10 `drop(String rn)`

```java
public boolean drop(String rn) throws NullPointerException, IllegalArgumentException
```

### Exact exceptions
If `rn == null`, throw:

```text
drop(String rn): rn can't be null
```

If `rn.length() == 0`, throw:

```text
drop(String rn): rn can't be an empty String
```

### Return behavior
- Return `true` if deletion succeeded
- Return `false` if row name was not found

### V1 behavior
Use LL removal.

### V2 behavior
Must keep LL and BST synchronized.
A safe strategy is:

1. validate `rn`
2. check whether the row exists using `seriesDataBST.searchNode(rn)`
3. if not found, return `false`
4. if found, remove from LL and remove from BST
5. return `true`

Do **not** remove from only one structure.

---

## 7.11 `fillNull(T value)`

```java
public void fillNull(T value) throws IllegalArgumentException
```

### Exact exception
If `value == null`, throw:

```text
fillNull(T value): value can't be null
```

### Required behavior
Replace every row whose data is currently `null` with `value`.

### V1 behavior
Update the linked list.

### V2 behavior
Update **both**:

- the linked list node data
- the BST node data

### Important synchronization rule
In V2, the BST stores rowName-data pairs too. So if you update only LL and not BST, then later BST-based `loc()` will return stale values. That is incorrect.

Recommended approach:

1. get row names in order
2. for each row name, check current value
3. if null:
   - `seriesData.updateNode(rowName, value)`
   - `seriesDataBST.updateNode(rowName, value)`

---

## 8. `SeriesV1` vs `SeriesV2` exact internal requirements

## 8.1 `SeriesV1`
Use only:

```java
private LL<T> seriesData;
```

Operations should delegate to LL whenever appropriate.

## 8.2 `SeriesV2`
Use:

```java
private LL<T> seriesData;
private BST<String, T> seriesDataBST;
```

### Mandatory synchronization points
These operations must keep BST updated:

- constructor initialization
- append
- drop
- fillNull
- any future data update behavior

### Mandatory BST usage points
At minimum, the following must use BST search logic:

- `loc(String rn)`
- any internal existence-check logic in V2 where row-name search is needed

If you search only through LL in V2, that will likely lose manual-grading points.

---

## 9. Existing starter-code adaptation notes

## 9.1 Existing `Series.java` file is obsolete for PA5 structure
The uploaded old file currently defines:

```java
public class Series<T>
```

That is **wrong for PA5**.
It must become:

```java
public interface Series<T>
```

and the old implementation logic should move into:

- `SeriesV1.java`
- `SeriesV2.java`

## 9.2 Existing `LL.java` likely needs output-header adjustment
The uploaded old LL starter prints linked-list wording.
PA5 Series output requires series wording.
If `Series.toString()` simply returns `seriesData.toString()`, then the underlying LL output must reflect PA5 formatting expectations.

## 9.3 Existing `iloc` message in old code may be wrong
Do **not** trust the old implementation's invalid-index print text.
The PA5 spec requires:

```text
the index <IND> is not valid.. returning null
```

with **two periods** before `returning`.

## 9.4 Existing LL exception messages
The old LL helper methods may already throw messages like:

- `removeNode(String _index): No node with an index ... in the list`
- `updateNode(String _index, T value): No node with an index ... in the list`

Those are helper-level LL messages and are fine if internally used.
But the public `SeriesV1`/`SeriesV2` behavior must still match the PA5 Series specification.

---

## 10. Autograder-sensitive strings

These strings are high-risk and should be copied literally.

### Constructor
```text
Series(String[] _index, T[] _data): _data can't be null. Terminating the program
Series(String[] _index, T[] _data): the length of _index and _data must be the same
Series(String[] _index, T[] _data): _rowNames is not valid
```

### `loc(String rn)`
```text
loc(String rn): rn can't be null
loc(String rn): rn can't be an empty string
```

### `loc(String[] rn)`
```text
loc(String[] rn): rn[] can't be null
loc(String[] rn): rn[] can't be an empty array
```

### `iloc(int ind)` printed output
```text
the index <IND> is not valid.. returning null
```

### `drop(String rn)`
```text
drop(String rn): rn can't be null
drop(String rn): rn can't be an empty String
```

### `fillNull(T value)`
```text
fillNull(T value): value can't be null
```

### BST exceptions
```text
removeNode(I _index): No node with an index <THE_INDEX_VALUE> in the BST
updateNode(I _index, T _newData): No node with an index <THE_INDEX_VALUE> in the BST
```

---

## 11. Exact output-format checklist

## 11.1 BST `toString()`
Must start with:

```text
In-order Traversal of the BST ...
==================
```

Then one node per line in in-order traversal.

## 11.2 Series `toString()`
Must start with:

```text
print the series ...
==================
```

Then print the linked-list-backed content in order, including dummy endpoints if the LL design includes them and PA4 formatting expects them.

## 11.3 Newlines
Do not accidentally remove trailing `\n` from node/string builders if the old design depended on them.
String-format mismatches are common autograder failures.

---

## 12. Recommended implementation order for Codex

1. Convert `Series.java` into the interface.
2. Implement/fix `BST.java` first.
3. Implement `SeriesV1.java` by adapting old PA4 Series logic.
4. Implement `SeriesV2.java` using both LL and BST.
5. Adjust `LL.java` only as needed for PA5-compatible Series output and compatibility.
6. Re-check all literal strings.

---

## 13. Required sanity-check scenarios before finalizing code

Codex should mentally verify these cases:

### Constructor
- `_data == null` throws exact NPE message
- `_rowNames == null` uses `"0"`, `"1"`, ... via `try...catch`
- length mismatch throws exact IAE message
- row name array containing invalid entry throws exact IAE message

### `append`
- append with normal row name
- append with `null` row name uses numeric default row name
- in V2, BST gets the final actual row name too

### `loc`
- valid label returns data
- missing label returns `null`
- `null` label throws exact NPE
- empty label throws exact IAE
- V2 uses BST search, not LL search

### `loc(String[])`
- returns `T[]`
- missing labels map to `null`
- null array throws exact NPE
- empty array throws exact IAE

### `iloc`
- valid 0-based index returns correct data
- invalid index prints exact message with two periods and returns `null`

### `drop`
- valid existing row returns `true`
- missing row returns `false`
- null row name throws exact NPE
- empty row name throws exact IAE
- V2 removes from both LL and BST

### `fillNull`
- null replacement value throws exact IAE
- only null data fields get replaced
- V2 updates both LL and BST

### BST
- empty BST string header works
- search returns null when absent
- remove absent node throws exact message
- update absent node throws exact message
- remove handles leaf / one-child / two-child correctly

---

## 14. Codex instructions: what NOT to do

Do **not**:

- keep `Series.java` as a class
- add imports
- rewrite the project into a totally different design
- replace LL-based storage in V1
- use LL search in `SeriesV2.loc(String rn)`
- forget to synchronize BST in append/drop/fillNull
- change exception wording
- "clean up" punctuation in printed messages
- change `not valid.. returning null` to one dot
- change `empty String` to `empty string` in `drop`

---

## 15. Suggested prompt to give Codex

You can paste the following prompt to Codex after adding this file to the repo:

```text
Read PA5_SPEC_for_Codex.md first and treat it as the implementation checklist.

Task:
1. Implement/fix BST.java exactly according to the spec.
2. Replace Series.java with a generic interface.
3. Create SeriesV1.java as the linked-list implementation.
4. Create SeriesV2.java as the linked-list + BST implementation.
5. Keep BST synchronized with LL in SeriesV2.
6. Use BST searchNode() for label-based search in SeriesV2.
7. Do not add imports.
8. Do not change required public method signatures.
9. Match all exception messages and output formatting literally.
10. Before writing code, output a checklist of all required methods and exact-risk strings from PA5_SPEC_for_Codex.md. Then implement.
```

---

## 16. Final reminder

The biggest autograder / manual-grading risks are:

1. `Series.java` not converted into an interface
2. `SeriesV2` not actually using BST search
3. BST and LL getting out of sync in `SeriesV2`
4. wrong output header strings
5. wrong exception messages
6. wrong `iloc` invalid-index print message punctuation

Implement conservatively and literally.
