import basic_implicit.{T_SET, T_UNION_LATTICE, t_Boolean}
import org.scalatest.flatspec.AnyFlatSpec
import org.scalatest.matchers.should


class TableLatticeSpec extends AnyFlatSpec with should.Matchers {

  object Testbed {
    import cool_symbol_implicit._
    import table_implicit._;

    val t_Symbols = new M_SET[T_Symbol]("Symbols",t_Symbol)

    type T_Symbols = T_SET[T_Symbol];
    val t_SymbolLattice = new M_UNION_LATTICE[T_Symbol,T_Symbols]("SymbolLattice",t_Symbol,t_Symbols)
      /* dumping traits */
      with C_TYPE[T_Symbols]
      with C_SET[T_Symbols, T_Symbol] {
      override val v_assert = t_Symbols.v_assert;
      override val v_equal = t_Symbols.v_equal;
      override val v_node_equivalent = t_Symbols.v_node_equivalent;
      override val v_string = t_Symbols.v_string;
      override val v_less = t_Symbols.v_less;
      override val v_less_equal = t_Symbols.v_less_equal;
      override val v_none = t_Symbols.v_none;
      override val v_single = t_Symbols.v_single;
      override val v_append = t_Symbols.v_append;
      override val v__op_AC = t_Symbols.v__op_AC;
      override val p__op_AC = t_Symbols.p__op_AC;
      override val v_member = t_Symbols.v_member;
      override val v_union = t_Symbols.v_union;
      override val v_intersect = t_Symbols.v_intersect;
      override val v_difference = t_Symbols.v_difference;
      override val v_combine = t_Symbols.v_combine;
    }

    type T_SymbolLattice = /*TI*/T_UNION_LATTICE[T_Symbol,T_Symbols]
    val t_DeclarationTable = new M_TABLE_LATTICE[T_Symbol,T_SymbolLattice]("DeclarationTable",t_Symbol,t_SymbolLattice)

    type T_DeclarationTable = /*TI*/T_TABLE[T_Symbol,T_SymbolLattice];
  }

  import Testbed._

  "v_compare" should "return true when keys are different" in {
    val table1: Testbed.T_DeclarationTable = tableLattice(Map('A -> toSymbolSet(1, 2), 'B -> toSymbolSet(3, 4)))
    val table2 = tableLattice(Map('A -> toSymbolSet(1, 2), 'B -> toSymbolSet(3, 4), 'C -> toSymbolSet(5, 6)))

    t_DeclarationTable.v_compare(table1, table2) should be (true)
  }

  "v_compare" should "return true when values are different" in {
    val table1: Testbed.T_DeclarationTable = tableLattice(Map('A -> toSymbolSet(1, 2), 'B -> toSymbolSet(3, 4)))
    val table2 = tableLattice(Map('A -> toSymbolSet(1, 2), 'B -> toSymbolSet(3, 4, 5)))

    t_DeclarationTable.v_compare(table1, table2) should be (true)
  }

  "v_compare_equal" should "return true when it is subset" in {
    val table1: Testbed.T_DeclarationTable = tableLattice(Map('A -> toSymbolSet(1, 2), 'B -> toSymbolSet(3, 4)))
    val table2 = tableLattice(Map('B -> toSymbolSet(1, 2, 3, 4)))

    t_DeclarationTable.v_compare_equal(table1, table2) should be (true)
  }

  def toSymbolSet[T](items: T*) = {
    items.map(x => Symbol(x.toString)).toSet
  }

  def symbolLattice(set: Set[Symbol]): T_SymbolLattice = {
    var current = t_SymbolLattice.v_none()
    for (item <- set) {
      current = t_SymbolLattice.v_combine(current, t_SymbolLattice.v_single(item))
    }
    current
  }

  def tableEntry(s: Symbol, set: Set[Symbol]) = {
    t_DeclarationTable.v_table_entry(s, symbolLattice(set))
  }

  def tableLattice(map: Map[Symbol, Set[Symbol]]): T_DeclarationTable = {
    var current = t_DeclarationTable.v_empty_table
    for ((key, values) <- map) {
      current = t_DeclarationTable.v_combine(current, tableEntry(key, values))
    }
    current
  }
}