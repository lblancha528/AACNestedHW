package edu.grinnell.csc207.util;

import static java.lang.reflect.Array.newInstance;

/**
 * A basic implementation of Associative Arrays with keys of type K and values of type V.
 * Associative Arrays store key/value pairs and permit you to look up values by key.
 *
 * @param <K> the key type
 * @param <V> the value type
 *
 * @author Lily Blanchard
 * @author Samuel A. Rebelsky
 */
public class AssociativeArray<K, V> {
  // +-----------+---------------------------------------------------
  // | Constants |
  // +-----------+

  /**
   * The default capacity of the initial array.
   */
  static final int DEFAULT_CAPACITY = 16;

  // +--------+------------------------------------------------------
  // | Fields |
  // +--------+

  /**
   * The size of the associative array (the number of key/value pairs).
   */
  int size;

  /**
   * The array of key/value pairs.
   */
  KVPair<K, V>[] pairs;

  // +--------------+------------------------------------------------
  // | Constructors |
  // +--------------+

  /**
   * Create a new, empty associative array.
   */
  @SuppressWarnings({"unchecked"})
  public AssociativeArray() {
    // Creating new arrays is sometimes a PITN.
    this.pairs = (KVPair<K, V>[]) newInstance((new KVPair<K, V>()).getClass(), DEFAULT_CAPACITY);
    this.size = 0;
  } // AssociativeArray()

  // +------------------+--------------------------------------------
  // | Standard Methods |
  // +------------------+

  /**
   * Create a copy of this AssociativeArray.
   *
   * @return a new copy of the array
   */
  public AssociativeArray<K, V> clone() {
    AssociativeArray<K, V> cloneArr = new AssociativeArray<K, V>();
    while (cloneArr.pairs.length < this.pairs.length) {
      // if original array has been expanded, expand clone until big enough
      cloneArr.expand();
    } // while
    for (KVPair<K, V> pair : this.pairs) {
      try {
        cloneArr.set(pair.key, pair.val);
      } catch (Exception NullKeyException) {
      } // try/catch
    } // for
    return cloneArr;
  } // clone()

  /**
   * Convert the array to a string.
   *
   * @return a string of the form "{Key0:Value0, Key1:Value1, ... KeyN:ValueN}"
   */
  public String toString() {
    String arrStr = new String("{");
    int count = 0;
    for (KVPair<K, V> pair : pairs) {
      if (pair == null) {
        continue;
      } // if
      if (count < this.size - 1) {
        arrStr += pair.toString() + ", ";
        count++;
      } else {
        arrStr += pair.toString();
      } // if
    } // for
    arrStr += "}";
    return arrStr;
  } // toString()

  // +----------------+----------------------------------------------
  // | Public Methods |
  // +----------------+

  /**
   * Set the value associated with key to value. Future calls to get(key) will return value.
   *
   * @param key The key whose value we are seeting.
   * @param value The value of that key.
   *
   * @throws NullKeyException If the client provides a null key.
   */
  public void set(K key, V value) throws NullKeyException {
    if (key == null) {
      throw new NullKeyException();
    } else if (this.hasKey(key)) {
      try {
        int i = find(key);
        pairs[i].val = value;
      } catch (Exception KeyNotFoundException) {
      } // try/catch
    } else if (this.size == this.pairs.length) {
      this.expand();
      this.pairs[this.size + 1] = new KVPair<K, V>(key, value);
      this.size += 1;
    } else {
      // must not exist and must be room
      try {
        int i = 0;
        // KVPair<K ,V> nullPair = new KVPair<K ,V>();
        for (; i < this.pairs.length; i++) {
          if (this.pairs[i] == null) {
            break;
          } // if
        } // for
        this.pairs[i] = new KVPair<K, V>(key, value);
        this.size += 1;
      } catch (Exception KeyNotFoundException) {
        System.err.println("Fail to set pair.");
      } // try/catch
    } // if
    return;
  } // set(K,V)

  /**
   * Get the value associated with key.
   *
   * @param key A key
   *
   * @throws KeyNotFoundException when the key is null or does not appear in the associative array.
   *
   * @return the value of given key
   */
  public V get(K key) throws KeyNotFoundException {
    if (key == null) {
      throw new KeyNotFoundException();
    } // if key is null
    int i = find(key);
    return this.pairs[i].val;
  } // get(K)

  /**
   * Determine if key appears in the associative array. Should return false for the null key.
   *
   * @param key A key
   *
   * @return true if key exists in array, false if not
   */
  public boolean hasKey(K key) {
    try {
      find(key);
      return true;
    } catch (Exception KeyNotFoundException) {
      return false;
    } // try/catch
  } // hasKey(K)

  /**
   * Remove the key/value pair associated with a key. Future calls to get(key) will throw an
   * exception. If the key does not appear in the associative array, does nothing.
   *
   * @param key A key
   */
  public void remove(K key) {
    try {
      int i = find(key);
      this.pairs[i].key = null;
      this.pairs[i].val = null;
      size -= 1;
    } catch (Exception KeyNotFoundException) {
    } // try/catch

    return;
  } // remove(K)

  /**
   * Determine how many key/value pairs are in the associative array.
   *
   * @return the number of keys
   */
  public int size() {
    return this.size;
  } // size()

  // +-----------------+---------------------------------------------
  // | Private Methods |
  // +-----------------+

  /**
   * Expand the underlying array.
   */
  void expand() {
    this.pairs = java.util.Arrays.copyOf(this.pairs, this.pairs.length * 2);
  } // expand()

  /**
   * Find the index of the first entry in `pairs` that contains key. If no such entry is found,
   * throws an exception.
   * Will 'skip' null key entries aka blank spaces in the array.
   *
   * @param key The key of the entry.
   *
   * @throws KeyNotFoundException If the key does not appear in the associative array.
   *
   * @return the index of the found key
   */
  int find(K key) throws KeyNotFoundException {
    for (int i = 0; i < this.pairs.length; i++) {
      if (this.pairs[i] == null || this.pairs[i].key == null) {
        continue;
      } // if key is null, skip loop
      if (this.pairs[i].key.equals(key)) {
        return i;
      } // if
    } // for
    throw new KeyNotFoundException();
  } // find(K)

} // class AssociativeArray
