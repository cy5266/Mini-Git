import javax.swing.text.TableView;

/**
 * TableFilter to filter for entries whose two columns match.
 *
 * @author Matthew Owen
 */
public class ColumnMatchFilter extends TableFilter
{

    String colName1;
    String colName2;
    int col1index;
    int col2index;
    Table.TableRow nextRow;


//    call colNameToIndex with colNames and then get the values and compare them in keep

    public ColumnMatchFilter(Table input, String colName1, String colName2) {
        super(input);
        this.colName1 = colName1;
        this.colName2 = colName2;

        this.col1index = input.colNameToIndex(colName1);
        this.col2index = input.colNameToIndex(colName2);
    }

    @Override
    protected boolean keep()
    {
        nextRow = candidateNext();

        if (nextRow.getValue(col1index).equals(nextRow.getValue(col2index))) {
            return true;
        }
        return false;
    }
}
