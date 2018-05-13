import React, {Component} from 'react';
import _ from 'lodash';
import { Table } from 'semantic-ui-react'

class TableHeader extends Component {
  render() {
    const { data, actions } = this.props;
    return (
      <Table.Header>
        <Table.Row>
          {_.map(data, cellData => <Table.HeaderCell>{ cellData.label }</Table.HeaderCell>)}
          { actions && actions.length && <Table.HeaderCell></Table.HeaderCell> }
        </Table.Row>
      </Table.Header>
    );
  }
}

export default TableHeader;
