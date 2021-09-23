/**
 * TableFilter to filter for containing substrings.
 *
 * @author Matthew Owen
 */
public class SubstringFilter extends TableFilter
{

    String colName;
    String subStr;
    int colindex;
    Table.TableRow nextRow;

    public SubstringFilter(Table input, String colName, String subStr) {
        super(input);
        this.colName = colName;
        this.subStr = subStr;
        colindex = input.colNameToIndex(colName);
    }

    @Override
    protected boolean keep() {
        nextRow = candidateNext();
        if (nextRow.getValue(colindex).contains(subStr)) {
            return true;
        }
        return false;
    }

    // FIXME: Add instance variables?
}
