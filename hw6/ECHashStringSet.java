import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.List;

/** A set of String values.
 *  @author Cindy Yang
 */
class ECHashStringSet implements StringSet {

    private ArrayList<String>[] buckets; //array of arraylists
    private int numBucketSize = 10;
    private int numItems = 0;

    public ECHashStringSet() {

//        numItems = 0;
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

        if (s != null) {
            if (numItems / numBucketSize > 5) {
                resize();
            }

            int index = whichBucket(s);

//            buckets[index] = test;
            if (!buckets[index].contains(s)) {
                buckets[index].add(s);
                numItems += 1;
            }

//            if (!buckets[index].contains(s)) {
//            System.out.println(index);
//            System.out.println(buckets.length);
//            numItems += 1;
//            buckets[index].add(s);

//            }
            // get arraylist at bucket[i]
            //add s to the linkedlist
            // FIXME
        }
    }

    private void resize() {
//        int newNumBucketSize = numBucketSize*2;
//        ECHashStringSet buckets2 =  new ECHashStringSet();
//        buckets2.numBucketSize = newNumBucketSize;
////
//        for (int i = 0; i <buckets.length; i++) {
//            for (String element: buckets[i]) {
//                buckets2.put(element);
//            }
////            buckets[i] = new ArrayList<String>();
//        }

        // take all of the elements from the hashset we already have and figure out
        // which bucket to put them in the new list
//
//        for (int i = 0; i < buckets.length; i ++) {
//            for (String element: buckets[i]) {
//                int bucketIndex = whichBucket(element);
//                buckets2[bucketIndex].add(element);
//            }
//        }
//
//        buckets = buckets2.buckets;
//        numBucketSize = newNumBucketSize;
        //make a new array to move everything to
        // fill the array with arraylists
        // take all of the elements from the hashset we already have and figure out
        // which bucket to put them in the new list


        ArrayList<String>[] buckets2 =  (ArrayList<String>[]) new ArrayList[numBucketSize * 2];

        for (int i = 0; i <buckets2.length; i++) {
            buckets2[i] = new ArrayList<String>();
        }

        // take all of the elements from the hashset we already have and figure out
        // which bucket to put them in the new list

        ArrayList<String>[] forN = buckets;
        buckets = buckets2;

        numBucketSize = numBucketSize * 2;

        for (int i = 0; i < forN.length; i ++) {
            for (String element: forN[i]) {
                int bucketIndex = whichBucket(element);
                buckets[bucketIndex].add(element);
            }
        }


    }

    @Override
    public boolean contains(String s) {

        if (s == null) {
            return false;
        }
//        if (numItems / numBucketSize > 5) {
//            resize();
//        }

        int index = whichBucket(s);

        if (buckets[index].contains(s)) {
            return true;
        } else {
            return false;
        }
        //first figure out which index i bucket s would be in
        //then get the arraylist at the bucket, and see if the arraylist contains S
//        return false; // FIXME
    }

    private int whichBucket(String s) {
//        int hashCode = s.hashCode();
//        if (hashCode < 0) {
//            hashCode = (s.hashCode() & 0x7fffffff) % numBucketSize;
//        }
//        else {
//            hashCode = hashCode % numBucketSize;
//        }
//        s.hashCode() % numBucketSize;
//
        return (s.hashCode() & 0x7fffffff) % numBucketSize;
        //returns which bucket i the string should be in
        //call the default string hashcode, as in s.hashCode()
        //figure out a way to make that default hashcode wrap or fit within the range 0-numbuckets-1
    }
    @Override
    public List<String> asList() {

        ArrayList<String> asList = new ArrayList<String>();

        for (int i = 0; i < buckets.length; i ++) {
            if (buckets[i] != null) {
                for (String element: buckets[i]) {
                    asList.add(element);
                }
            }
        }
        return asList; // FIXME
    }
}
