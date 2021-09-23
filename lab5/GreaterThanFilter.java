/**
 * TableFilter to filter for entries greater than a given string.
 *
 * @author Matthew Owen
 */
public class GreaterThanFilter extends TableFilter
{

    String colName;
    String ref;
    int colindex;
    Table.TableRow nextRow;

    public GreaterThanFilter(Table input, String colName, String ref) {
        super(input);
        this.colName = colName;
        this.ref = ref;
        colindex = input.colNameToIndex(colName);
    }

    @Override
    protected boolean keep() {
        nextRow = candidateNext();
        if (nextRow.getValue(colindex).compareTo(ref) > 0) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
}
