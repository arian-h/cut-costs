import React, {Component} from 'react';
import _ from 'lodash';
import { Link } from 'react-router-dom';
import { Table, Dropdown, Image } from 'semantic-ui-react'

class TableRow extends Component {
  render() {
    const { data, actions, id } = this.props;
    let actionMenu;
    if (actions && actions.length) {
      let actionItems = [];
      for (var i = 0; i < actions.length; i++) {
        if (actions[i].isEnabled(id)) {
          actionItems.push(<Dropdown.Item icon={ actions[i].icon } text={ actions[i].label } onClick={ actions[i].action.bind(this, id) }/>);
        }
      }
      actionMenu =
          <Dropdown icon='chevron down'>
            <Dropdown.Menu>
              { actionItems }
            </Dropdown.Menu>
          </Dropdown>;
    }
    return (
      <Table.Row>
          {_.map(data, cellData => {
              let element;
              if (cellData.avatar) {
                element =
                  <Table.Cell>
                      <Image src={ cellData.avatar } avatar />
                      { cellData.value }
                  </Table.Cell>;
              } else {
                element = <Table.Cell>{ cellData.value }</Table.Cell>;
              }
              if (cellData.href) {
                element = <Table.Cell><Link to={ cellData.href }>{ cellData.value }</Link></Table.Cell>;
              }
              return element;
          })
        }
        <Table.Cell>{ actionMenu }</Table.Cell>
      </Table.Row>
    );
  }
}

export default TableRow;
