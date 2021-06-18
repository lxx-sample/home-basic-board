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

  panel = new Observable<string>(observer => {
    // setInterval(() => observer.next(new Date().toString()), 1000);

    MessageHandlerManager.registerHandler('/system/handler/panel', (args: Map<string, any>) => {
      // resolve(args.get('panel'))
      observer.next(args.get('panel'));
    })

  });

  calc = new Promise<Map<string, string>[]>((resolve, reject) => {
    MessageHandlerManager.registerHandler('/fund/trade/helper/calc', function (args: Map<string, any>) {
      var validlyTradeList = args.get('validlyTradeList');
      var latestPrice = args.get('latestPrice');

      let helper: Map<string, string>[] = [];

      Object.keys(validlyTradeList).forEach(k => {
        let element = validlyTradeList[k];

        let message = '最新净值: ' + latestPrice + ', 确认净值: ' + element['price'] + ', 持有份额: ' + element['tradeNumber'];
        message += ', 持有天数: ' + element['days'] + ', 收益率: ' + element['yield'];

        let each = new Map();
        each.set("latestPrice", latestPrice);
        each.set("price", element['price']);
        each.set("tradeNumber", element['tradeNumber']);
        each.set("days", element['days']);
        each.set("yield", element['yield']);
        each.set("totalAmount", (element['price'] * element['tradeNumber']).toFixed(2));

        helper.push(each)
      });

      resolve(helper)
    })

  });

  background = "lightgreen"
}
