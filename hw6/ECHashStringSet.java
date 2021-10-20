import java.util.ArrayList;
import java.util.List;

/** A set of String values.
 *  @author Cindy Yang
 */
class ECHashStringSet implements StringSet {

    private ArrayList<String>[] buckets; //array of arraylists
    private int numBucketSize = 10;
    private int numItems;

    public ECHashStringSet() {

        numItems = 0;
//        numBucketSize = 10;
        //(ArrayList<String>[])
        buckets = (ArrayList<String>[]) new ArrayList[numBucketSize];

        for (int i = 0; i <buckets.length; i++) {
            buckets[i] = new ArrayList<String>();
        }
    }

    @Override
    public void put(String s) {
        //if we have reached the load limit, we should resize
        // if (number of items / number of buckets) > 5

        if (numItems / numBucketSize > 5) {
            resize();
        }

        //figure out which index i bucket to go to
        //whichBucket

        int index = whichBucket(s);
        ArrayList<String> test = buckets[index];
        test.add(s);
        numItems += 1;
        buckets[index] = test;

        // get arraylist at bucket[i]
        //add s to the linkedlist
        // FIXME
    }

    private void resize() {
        ArrayList<String>[] buckets2 =  new ArrayList[numBucketSize*2];

        for (int i = 0; i <buckets.length; i++) {
            buckets[i] = new ArrayList<String>();
        }

        // take all of the elements from the hashset we already have and figure out
        // which bucket to put them in the new list

        for (int i = 0; i < buckets.length; i ++) {
            for (String element: buckets[i]) {
                int bucketIndex = whichBucket(element);
                buckets2[bucketIndex].add(element);
            }
        }

        buckets = buckets2;
        //make a new array to move everything to
        // fill the array with arraylists
        // take all of the elements from the hashset we already have and figure out
        // which bucket to put them in the new list
    }

    @Override
    public boolean contains(String s) {

        int index = whichBucket(s);

        if (buckets[index].contains(s)) {
            return true;
        }
        else {
            return false;
        }
        //first figure out which index i bucket s would be in
        //then get the arraylist at the bucket, and see if the arraylist contains S
//        return false; // FIXME
    }

    private int whichBucket(String s) {
        int hashCode = s.hashCode();
        if (hashCode < 0) {
            hashCode = (s.hashCode() & 0x7fffffff) % numBucketSize;
        }
        else {
            hashCode = hashCode % numBucketSize;
        }
        return hashCode;
        //returns which bucket i the string should be in
        //call the default string hashcode, as in s.hashCode()
        //figure out a way to make that default hashcode wrap or fit within the range 0-numbuckets-1
    }
    @Override
    public List<String> asList() {
        return null; // FIXME
    }
}
