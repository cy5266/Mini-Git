/**
 * TableFilter to filter for entries equal to a given string.
 *
 * @author Matthew Owen
 */
public class EqualityFilter extends TableFilter {

    String colName;
    String match;
    int colindex;
    Table.TableRow nextRow;

    public EqualityFilter(Table input, String colName, String match) {
        super(input);
        this.colName = colName;
        this.match = match;
        colindex = input.colNameToIndex(colName);
    }

    @Override
    protected boolean keep() {
        nextRow = candidateNext();
        if (nextRow.getValue(colindex).equals(match)) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
}
