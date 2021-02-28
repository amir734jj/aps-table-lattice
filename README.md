# aps-table-lattice
APS table lattice test cases

Requirements:
- Scala 2.13
- ScalaTest


Test cases:
```
{A -> {1,2}, B ->{3,4}}  <  {A -> {1,2}, B -> {3,4}, C -> {5,6}}
{A -> {1,2}, B -> {3,4}} <  {A -> {1,2}, B -> {3,4,5}}

{A -> {1,2}, B -> {3,4}} <= {B -> {1,2,3,4}}
```

Implementation:

```scala
// v1 != v2 && v1 <= v2 --> v1 < v2
override val v_compare = (x, y) => {
  (x.keySet subsetOf y.keySet) && v_compare_equal(x, y) && (x.keySet != y.keySet || (x.keySet & y.keySet).exists(k => !_t_ValueType.v_equal(x(k), y(k))))
}

// v1 <= v2 --> v1 <= v2
override val v_compare_equal = (x, y) => {
  (x.keySet & y.keySet).forall(key => _t_ValueType.v_compare_equal(x(key), y(key)))
}
```
