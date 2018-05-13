import React, {Component} from 'react';
import TableHeader from './table_header';
import TableRow from './table_row';
import { Table } from 'semantic-ui-react'

class DataTable extends Component {
  render() {
    const { className, configs, data: entities, actions} = this.props;
    return (
      <Table celled compact padded>
        <TableHeader data={configs} actions={actions}/>
        <Table.Body>
          {_.map(entities, entity =>
             <TableRow id={entity.id} actions={actions}
               data={_.map(configs, config =>
                 !_.isUndefined(entity[config.name]) &&
                   {
                     value: entity[config.name],
                     href: config.href && config.href(entity)
                   }
                 )
               }
             />
         )}
        </Table.Body>
      </Table>
    );
  }
}

export default DataTable;
