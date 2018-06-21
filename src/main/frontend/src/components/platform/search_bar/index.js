import React, { Component } from 'react';
import { Search } from 'semantic-ui-react';
import _ from 'lodash';

class SearchBar extends Component {

  constructor(props) {
    super(props);
    this.state = {
      loading: false,
      results: [],
      value: ''
    };
  }

  componentWillMount() {
    this._resetComponent()
  }

  _resetComponent = () => this.setState({ loading: false, results: [], value: '' })

  _handleResultSelect = (e, { result }) => {
    this.setState({ value: result.title });
    this.props.selectItemHandler(result);
  }

  _handleSearchChange = (e, { value }) => {
    if (value.length < 1) return this._resetComponent();
    this.props.searchHandler(value, results => this.setState({ results: this.props.resultFormatter(results), loading: false}));
    this.setState({ loading: true, value });
  }

  _searchCallback = results => {
    this.setState({ results: results });
  }

  render() {
    const { loading, results, value } = this.state;
    const { resultFormatter, searchHandler, selectItemHandler, ...rest } = this.props;
    if (results.length > 0) {
      results.forEach(result => {
        result._id = result.id; // id is overriden by the Search component
      })
    }
    return (<Search
      loading={ loading }
      onResultSelect={ this._handleResultSelect }
      onSearchChange={ _.debounce(this._handleSearchChange, 500, { leading: true }) }
      results={ results }
      value={ value }
      {...rest}
    />);
  }
}

export default SearchBar;
