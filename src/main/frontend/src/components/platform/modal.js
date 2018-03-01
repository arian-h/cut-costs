import React, { Component } from 'react';
import ReactDOM from 'react-dom';
import { store } from '../../index';
import { Provider } from 'react-redux';

class Modal extends Component {
  constructor(props) {
    super(props);
    this.container = document.getElementsByClassName("container")[0];
  }

  componentDidMount() {
    this.modalTarget = document.createElement('div');
    this.modalTarget.className = "modalContainer";
    this.container.appendChild(this.modalTarget);
    this._render();
  }

  componentWillUpdate() {
    this._render();
  }

  componentWillUnmount() {
    ReactDOM.unmountComponentAtNode(this.modalTarget);
    this.container.removeChild(this.modalTarget);
  }

  _render() {
    const {content: Content, className: customClassName, onClose} = this.props;
    let className = `${customClassName} modal-container`;
    ReactDOM.render(
      <Provider store={store}>
        <div className={className}>
          <Content {...this.props}/>
        </div>
      </Provider>,
      this.modalTarget
    );
  }

  render() {
    return <noscript />;
  }
}

export default Modal;
