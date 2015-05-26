import java.util.AbstractCollection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.Set;

/**
 * <P>
 * The DenseBag class implements a Set-like collection that allows 
 * duplicates (a lot of them).
 * </P>
 * <P>
 * The DenseBag class provides Bag semantics: it represents 
 * a collection with duplicates. The "Dense" part of the class name 
 * comes from the fact that the class needs to efficiently handle 
 * the case where the bag contains 100,000,000
 * copies of a particular item (e.g., don't store 100,000,000 
 * references to the item).
 * </P>
 * <P>
 * In a Bag, removing an item removes a single instance of the item. 
 * For example, a Bag b could contain additional instances of 
 * the String "a" even after calling b.remove("a").
 * </P>
 * <P>
 * The iterator for a dense bag must iterate over all instances, including
 * duplicates. In other words, if a bag contains 5 instances of the 
 * String "a", an iterator will generate the String "a" 5 times.
 * </P>
 * <P>
 * In addition to the methods defined in the Collection interface, 
 * the DenseBag class supports several additional methods: 
 * uniqueElements, getCount, and choose.
 * </P>
 * <P>
 * The class extends AbstractCollection in order to get implementations 
 * of addAll, removeAll, retainAll and containsAll.  
 * (We will not be over-riding those).
 * All other methods defined in he Collection interface will be 
 * implemented here.
 * </P>
 */
public class DenseBag<T> extends AbstractCollection<T> {

    private Map<T, Integer> denseBagMap;
    /* T is the key term and Integer is the value of T */
    private int size;  // Total number of elements in the bag

    /**
     * Initialize a new, empty DenseBag
     */
    public DenseBag() {
        denseBagMap= new HashMap <T, Integer>();
        //Create a new denseBagMap
        size=0; //Initialize size is 0

    };

    /**
     * Generate a String representation of the DenseBag. This will 
     * be useful for your own debugging purposes, but will 
     * not be tested other than to ensure that it does return 
     * a String and that two different DenseBags return two different Strings.
     */
    @Override
    public String toString() {
        String returnString="";//make a new String to store the value
        for (T curr: denseBagMap.keySet()){
            //Iterate through the key set of the BagMap
            returnString=returnString+" "+curr.toString()+
            " "+denseBagMap.get(curr)+"\n";
        }
        return returnString;
    }

    /**
     * Tests if two DenseBags are equal. Two DenseBags are considered 
     * equal if they contain the same number of copies of the 
     * same elements. Comparing a DenseBag to an instance of
     * any other class should return false;
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean equals(Object o) {
        boolean returnvalue=true;
        //Check null
        if (o==null){
            return false;
        }
        //Check to make sure they are the same class
        if (o.getClass()!=this.getClass()){
            return false;
        }
        /* Check their hashCode. If they have different hasCode, 
         * they are different*/
        if (o.hashCode()!=this.hashCode()){
            return false;
        }
        for (T curr: denseBagMap.keySet()){
            /* Iterate through the key set
             * and check to see if they are the same
             */
            if (((AbstractCollection<T>) o).contains(curr)==false){
                returnvalue=false;
                break;
            }
        }
        return returnvalue;
    }


    /**
     * Return a hashCode that fulfills the requirements for hashCode 
     * (such as any two equal DenseBags must have the same hashCode)
     * as well as desired properties (two unequal DenseBags will 
     * generally, but not always, have unequal hashCodes).
     */
    @Override
    public int hashCode() {
        int returnvalue=0;
        for (T curr:denseBagMap.keySet()){
            /* Iterate through the key set of the BagMap
             * and take their hasCode multiply with their values.
             * By doing it, we make sure that 2 equal Bags will
             * return the same hashCode */
            returnvalue=returnvalue+curr.hashCode()*denseBagMap.get(curr);

        }
        return returnvalue;
    }

    /**
     * <P>
     * Returns an iterator over the elements in a dense bag. Note that if a
     * Dense bag contains 3 a's, then the iterator must iterator over 3 a's
     * individually.
     * </P>
     * <P>
     * You should use an inner class to define your iterator for DenseBag.
     * (A skeleton version of the class has been provided.)
     * </P>
     * <P>
     * Reminder about the contract for "next": <br \>
     * The user must not call next in the case where hasNext would return 
     * false.<br \>
     * If the user violates this rule, then the call to next should throw
     * a NoSuchElementException.
     * </P>
     * <P>
     * Your "remove" method should simply throw an 
     * UnsupportedOperationException.
     * </P>
     */
    @Override
    public Iterator<T> iterator() {
        return new MyIterator();
        //return MyIterator
    }

    class MyIterator implements Iterator<T> {

        /*Add any instance variables and/or instance methods that you need*/

        Iterator <T> keyIt=denseBagMap.keySet().iterator();
        T next=keyIt.next();
        int totalsize=size; //This variable is used in hasNext method
        int check=0;
        int currcount=0;
        int count=denseBagMap.get(next);
        
        public boolean hasNext() {
            if (check<totalsize){
                /* The number of iterator being returned will be the same 
                with the size of the Bag */
                return true;
            }else {
                return false;
            }
        }

        public T next() {
            if (hasNext()==false){
                throw new NoSuchElementException("Got");
            }
            if (currcount<count){
                //Check to see if it should return this key or next key
                check++; //Variable check moves to the next one
                currcount++; //Variable currcount moves to the next one
                return next; //Return this iterator
            }else{
                next=keyIt.next(); //move to the next iterator
                currcount=1;
                count=denseBagMap.get(next);
                //Variable count will be the value of the next key
                check++;
                return next;
            }
        }

        public void remove() {
            throw new UnsupportedOperationException
            ("Remove is not supported.");
        }
    }


    /**
     * return a Set of the elements in the Bag (since the returned value is a
     * set, it will contain one value for each UNIQUE value in the Bag).
     * 
     * @return A set of elements in the Bag
     */
    public Set<T> uniqueElements() {
        //return the key set of the BagMap only
        return denseBagMap.keySet();
    }

    /**
     * Return the number of instances of a particular object in the bag. 
     * Return 0 if it doesn't exist at all.
     * 
     * @param o
     *            object of interest
     * @return number of times that object occurs in the Bag
     */
    public int getCount(Object currObj) {
        if (denseBagMap.containsKey(currObj)==false){
            //If the bag does not contain the key, return 0
            return 0;
        }else {
            //Otherwise return the value of the key
            return denseBagMap.get(currObj);
        }
    }

    /**
     * Given a random number generator, randomly choose an element
     * from the Bag according to the distribution of objects in 
     * the Bag (e.g., if a Bag contains 7 a's and 3 b's, then 
     * 70% of the time choose should return an a, and 30% of the 
     * time it should return a b.
     * This operation can take time proportional to the number 
     * of unique objects in the Bag, but no more.
     * 
     * This operation should not affect the Bag.
     * 
     * @param r
     *            Random number generator
     * @return randomly chosen element
     */
    public T choose(Random random) {
        int number=random.nextInt(this.size());
        //Create a random number type integer from 0 to the size
        int currposition=0;
        //variable currposition is used to keep track 
        T returnvalue=null;
        for (T curr:denseBagMap.keySet()){
            //Iterate through the key set of the Bag
            currposition=currposition+denseBagMap.get(curr);
            if (number<currposition){
                returnvalue=curr;
                break;
            }
        }
        return returnvalue;
    }

    /**
     * Returns true if the Bag contains one or more instances of o
     */
    @Override
    public boolean contains(Object currObj) {
        boolean returnvalue=false;
        for (T curr: denseBagMap.keySet()){
            //Iterate through the key set to check if it contains the object
            if (currObj.equals(curr)){
                returnvalue=true;
            }
        }
        return returnvalue;
    }

    /**
     * Adds an instance of o to the Bag
     * 
     * @return always returns true, since added an element to a bag always
     *         changes it
     * 
     */
    @Override
    public boolean add(T addObj) {
        if (denseBagMap.containsKey(addObj)){
            //If the denseBag has this object already, add 1 to its value
            denseBagMap.put(addObj, denseBagMap.get(addObj)+1);
            size++; //Increase the size
        }else {
            /*If the denseBag does not have this one, 
            add a new one to the bag with the value 1*/
            denseBagMap.put(addObj, 1);
            size++; //Increase the size
        }
        return true;
    }

    /**
     * Decrements the number of instances of o in the Bag.
     * 
     * @return return true if and only if at least one instance 
     * of o exists in the Bag and was removed.
     */
    @SuppressWarnings("unchecked")
    @Override
    public boolean remove(Object removeObj) {
        boolean returnvalue=false;
        //Case1: the Bag does not have this object
        if (denseBagMap.containsKey(removeObj)==false){
            returnvalue= false;
        }
        //Case2: the Bag has only this object
        if (denseBagMap.containsKey(removeObj)==true
                &&denseBagMap.get(removeObj)==1){
            denseBagMap.remove(removeObj);
            size--; //size decreases due to removing object
            returnvalue= true;
        }
        //Case3: the bag has this one and more
        if (denseBagMap.containsKey(removeObj)==true
                &&denseBagMap.get(removeObj)>1){
            denseBagMap.put((T) removeObj, denseBagMap.get(removeObj)-1);
            size--; //size decreases due to removing object
            returnvalue= true;
        }
        return returnvalue;
    }

    /**
     * Total number of instances of any object in the Bag
     *  (counting duplicates)
     */
    @Override
    public int size() {
        //We keep track the size, so we need to return the size only.
        return size;
    }
}