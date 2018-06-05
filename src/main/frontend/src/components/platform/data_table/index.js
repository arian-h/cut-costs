import React, {Component} from 'react';
import TableHeader from './table_header';
import TableRow from './table_row';
import { Table } from 'semantic-ui-react'

class DataTable extends Component {
  render() {
    const { data: entities, columns, rowConfig } = this.props;
    return (
      <Table compact padded singleLine>
        <TableHeader data={ entities } columns={ columns }/>
        <Table.Body>
          { _.map(entities, entity => <TableRow data={ entity } rowConfig={ rowConfig }/>) }
        </Table.Body>
      </Table>
    );
  }
}

export default DataTable;
