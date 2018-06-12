import React, {Component} from 'react';
import TableHeader from './table_header';
import TableRow from './table_row';
import { Table, Menu, Icon } from 'semantic-ui-react'

class DataTable extends Component {

  constructor(props) {
    super(props);
    this.state = {
      pageIndex: 0
    };
  }

  _setPage(pageIndex) {
    this.setState({ pageIndex: pageIndex });
  }

  _nextPage() {
    this._setPage(Math.min(this._getLastPageIndex(), this.state.pageIndex + 1));
  }

  _previousPage() {
    this._setPage(Math.max(0, this.state.pageIndex - 1));
  }

  _getLastPageIndex() {
    return Math.ceil(this.props.data.length / this.props.pageSize) - 1;
  }

  render() {
    const {
      data: entities,
      columns,
      rowConfig,
      paginated,
      pageSize
    } = this.props;
    let tableBody = [], paginator;
    if (paginated && (entities.length > pageSize)) {
      const { pageIndex } = this.state;
      for (let index = pageSize * pageIndex; index < Math.min(pageSize * (pageIndex + 1), entities.length); index++) {
        tableBody.push(<TableRow data={ entities[index] } rowConfig={ rowConfig }/>);
      }
      paginator =
        <Table.Footer>
          <Table.Row>
            <Table.HeaderCell colSpan={ columns.length }>
              <Menu floated='right' pagination>
                <Menu.Item as='a' icon
                  onClick={ this._previousPage.bind(this) }
                >
                  <Icon name='chevron left' />
                </Menu.Item>
                {
                  _.map(Array.from(Array(this._getLastPageIndex() + 1).keys()), index =>
                    <Menu.Item as='a'
                      active={ pageIndex === index }
                      onClick={this._setPage.bind(this, index)}
                    >
                      { index + 1 }
                    </Menu.Item>
                  )
                }
                <Menu.Item as='a' icon
                  onClick={ this._nextPage.bind(this) }
                >
                  <Icon name='chevron right' />
                </Menu.Item>
              </Menu>
            </Table.HeaderCell>
          </Table.Row>
        </Table.Footer>;
    } else {
      //whatever number of rows to show up
      tableBody = _.map(entities, entity => <TableRow data={ entity } rowConfig={ rowConfig }/>);
    }
    return (
      <Table singleLine columns={ columns.length }>
        <TableHeader data={ entities } columns={ columns }/>
        <Table.Body>
          { tableBody }
        </Table.Body>
        { paginator }
      </Table>
    );
  }
}

export default DataTable;
