import { Component, OnInit } from '@angular/core';
import { Observable, Subscriber } from 'rxjs';

import { MessageHandlerManager } from './tools/websocket.component'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  uuid = new Promise((resolve, reject) => {
    MessageHandlerManager.registerHandler('/system/handler/uuid', (args: Map<string, any>) => {
      resolve(args.get('uuid'))
    })
  });

  calc = new Promise<string[]>((resolve, reject) => {
    MessageHandlerManager.registerHandler('/fund/trade/helper/calc', function (args: Map<string, any>) {
      var validlyTradeList = args.get('validlyTradeList');
      var latestPrice = args.get('latestPrice');

      let helper: string[] = [];

      Object.keys(validlyTradeList).forEach(k => {
        let element = validlyTradeList[k];

        let message = '最新净值: ' + latestPrice + ', 确认净值: ' + element['price'] + ', 持有份额: ' + element['tradeNumber'];
        message += ', 持有天数: ' + element['days'] + ', 收益率: ' + element['yield'];

        helper.push(message)
      });

      resolve(helper)
    })

  });

  background = "lightgreen"
}
