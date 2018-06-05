import React, {Component} from 'react';
import _ from 'lodash';
import { Link } from 'react-router-dom';
import { Table } from 'semantic-ui-react'

class TableRow extends Component {
  render() {
    const { data, rowConfig } = this.props;
    return (
      <Table.Row>
        {
          _.map(rowConfig, cellConfig => {
            return <Table.Cell>{ cellConfig(data) }</Table.Cell>;
          })
        }
      </Table.Row>
    );
  }
}

export default TableRow;
