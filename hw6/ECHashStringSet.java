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
        buckets = (ArrayList<String>[]) new ArrayList[numBucketSize];

        for (int i = 0; i <buckets.length; i++) {
            buckets[i] = new ArrayList<String>();
        }
    }

    @Override
    public void put(String s) {
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
        }
    }

    private void resize() {

        ArrayList<String>[] buckets2 =  (ArrayList<String>[]) new ArrayList[numBucketSize * 2];

        for (int i = 0; i <buckets2.length; i++) {
            buckets2[i] = new ArrayList<String>();
        }

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
        int index = whichBucket(s);

        if (buckets[index].contains(s)) {
            return true;
        } else {
            return false;
        }
    }

    private int whichBucket(String s) {
        return (s.hashCode() & 0x7fffffff) % numBucketSize;
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
