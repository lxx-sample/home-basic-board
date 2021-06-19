import { Component, OnInit } from "@angular/core";
import { Subject } from "rxjs";

(function () {
  console.log(location)
  let url = '/web/socket/dashboard';
  if (location.hostname == 'localhost') {
    url = 'ws://localhost:18080' + url;
  } else {
    url = 'ws://' + location.hostname + ':' + location.port + url;
  }
  let ws = new WebSocket(url);
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

      let path = messageObject['path'];

      let subject = MessageHandlerManager.getSubject(path);
      if (subject) {
        let map = new Map();
        if (messageObject['args']) {
          for (let k of Object.keys(messageObject['args'])) {
            map.set(k, messageObject['args'][k]);
          }
        }

        subject.next(map)
      }
    }

  };

  ws.onclose = function (evt) {
    console.log('Connection closed.');
  };
})();

export class MessageHandlerManager {

  private static MESSAGE_HANDLER_MAPPING: Map<string, Subject<Map<string, any>>> = new Map();

  public static getSubject = function (path: string) {
    if (MessageHandlerManager.MESSAGE_HANDLER_MAPPING.get(path)) {
      return MessageHandlerManager.MESSAGE_HANDLER_MAPPING.get(path);
    }

    let subject = new Subject<Map<string, any>>();
    MessageHandlerManager.MESSAGE_HANDLER_MAPPING.set(path, subject);

    return subject;
  }

}