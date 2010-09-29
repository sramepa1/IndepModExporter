package net.sourceforge.phpdt.internal.compiler.parser;

/**
 * The interface that will describe an object that can have children.
 *
 * @author Matthieu Casanova
 */
public interface OutlineableWithChildren extends Outlineable {
  /**
   * Add a children.
   *
   * @param o children the outlineable
   *
   * @return true if it was added
   */
  boolean add(Outlineable o);

  /**
   * Returns the children at index.
   *
   * @param index the index
   *
   * @return the children at index
   */
  Outlineable get(int index);

  /**
   * Returns how many children this item has.
   *
   * @return the children count
   */
  int size();
}
