import { Component, OnInit } from "@angular/core";

(function () {
  let ws = new WebSocket('ws://localhost:18080/web/socket/dashboard');
  ws.onopen = function (evt) {
    // console.log('Connection open ...');

    // TODO 就是个Demo而已
    // var message = {
    //     "path": "/fund/trade/helper/echo",
    //     "args": { "timestamp": new Date().getTime() }
    // };
    // ws.send(JSON.stringify(message));
    var message = {
      "path": "/fund/trade/helper/calc",
      "args": { "fundInfoId": "1" }
    };
    ws.send(JSON.stringify(message));
  };

  ws.onmessage = function (evt) {
    let messageObject = JSON.parse(evt.data);

    if (messageObject && messageObject['path']) {

      let handler = messageObject['path'];

      let action = MessageHandlerManager.get(handler);
      if (action) {
        let map = new Map();
        if (messageObject['args']) {
          for (let k of Object.keys(messageObject['args'])) {
            map.set(k,messageObject['args'][k]);
          }
        }

        action(map);
      }
    }

  };

  ws.onclose = function (evt) {
    console.log('Connection closed.');
  };
})();

export class MessageHandlerManager {

  private static MESSAGE_HANDLER_MAPPING: Map<string, Function> = new Map();

  public static registerHandler = function (handler: string, action: Function) {
    if (MessageHandlerManager.MESSAGE_HANDLER_MAPPING.get(handler)) {
      throw 'handler ' + handler + ' already exists';
    }

    MessageHandlerManager.MESSAGE_HANDLER_MAPPING.set(handler, action);
  }

  public static get = function (handler: string) {
    if (MessageHandlerManager.MESSAGE_HANDLER_MAPPING.get(handler)) {
      return MessageHandlerManager.MESSAGE_HANDLER_MAPPING.get(handler);
    }

    throw 'no handler ' + handler + ' exists';
  }

}