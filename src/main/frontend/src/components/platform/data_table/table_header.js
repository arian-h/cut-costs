import React, {Component} from 'react';
import _ from 'lodash';
import { Table } from 'semantic-ui-react'

class TableHeader extends Component {
  render() {
    const { data, columns } = this.props;
    return (
      <Table.Header>
        <Table.Row>
          {
            _.map(columns, column => {
              return <Table.HeaderCell>{ column(data) }</Table.HeaderCell>;
            })
          }
        </Table.Row>
      </Table.Header>
    );
  }
}

export default TableHeader;
