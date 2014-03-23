/* ListIterator */

package list;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ListIterator implements Iterator {
  private ListNode node;

  ListIterator(List list){
    node = list.front();
  }

  public boolean hasNext(){
    return node.isValidNode();
  }

  public Object next(){
    Object item;
    try{
      item = node.item();
      node = node.next();
    }catch(InvalidNodeException e){
      item = null;
      System.out.println("OH SHI- (invalid node exception in list iterator)");
    }
    return item;
  }
  /**
   *  remove() would remove from the underlying run-length encoding the run
   *  identified by this iterator, but we are NOT implementing it.
   *
   *  DO NOT CHANGE THIS METHOD.
   **/
  public void remove() {
    throw new UnsupportedOperationException();
  }

}
