import { Component, OnInit } from '@angular/core';

import { MessageHandlerManager } from './tools/websocket.component'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  public static uuid: string = '';

  constructor() {
    MessageHandlerManager.registerHandler('/system/handler/uuid', function (args: Map<string, any>) {
      console.log(args);
      AppComponent.uuid = args.get('uuid')
    })
    MessageHandlerManager.registerHandler('/fund/trade/helper/calc', function (args: Map<string, any>) {
      console.log(args);
      // AppComponent.uuid = args.get('uuid')
    })
  }

  get staticUuid() {
    return AppComponent.uuid;
  }

  // uuid = 'xxxx-xxxx-xxxx-xxxxxx';

  background = "lightgreen"
}
