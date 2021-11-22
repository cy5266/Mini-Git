
/** Disjoint sets of contiguous integers that allows (a) finding whether
 *  two integers are in the same set and (b) unioning two sets together.  
 *  At any given time, for a structure partitioning the integers 1 to N, 
 *  into sets, each set is represented by a unique member of that
 *  set, called its representative.
 *  @author
 */
public class UnionFind {

    /** A union-find structure consisting of the sets { 1 }, { 2 }, ... { N }.
     */

    int [] parents;
    int [] sizes; //represents the size of the tree

    public UnionFind(int N) {
        parents = new int[N + 1];
        sizes = new int[N + 1];

        for (int i = 0; i < parents.length; i ++) {
            sizes[i] = i;
            parents[i] = i;
        }
        // FIXME
    }

    /** Return the representative of the set currently containing V.
     *  Assumes V is contained in one of the sets.  */
    public int find(int v) {

        return 0;  // FIXME
    }

    /** Return true iff U and V are in the same set. */
    public boolean samePartition(int u, int v) {
        return find(u) == find(v);
    }

    /** Union U and V into a single set, returning its representative. */
    public int union(int u, int v) {
        return 0;  // FIXME
    }

    //whichever set has the largest size will always be the root node

    //path compression
    // set the vertex parents to the root value


    //algorithm
    // finiding the minimum spanning trees
    // shortest path to traverse all vertices
    // check to see if in the same cycle using SameParition. if not, add to the set

    // FIXME
}
