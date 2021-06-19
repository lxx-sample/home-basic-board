import { stringify } from '@angular/compiler/src/util';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Observable, of, Subscriber } from 'rxjs';

import { MessageHandlerManager } from './tools/websocket.component'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  uuid: string = "";
  panel: string = "";
  fundTradeHistoryList: Map<string, string>[] = [];

  constructor(private changeDetectorRef: ChangeDetectorRef) {
    this.registerDeviceUuidHandler();
    this.registerPanelChangeHandler();
    this.registerFundTradeHistoryHandler();
  }

  registerPanelChangeHandler() {
    let subject = MessageHandlerManager.getSubject('/system/handler/panel');
    subject?.subscribe(args => {
      this.panel = <string>args.get('panel');
      this.changeDetectorRef.detectChanges();
    })
  }

  registerDeviceUuidHandler() {
    let subject = MessageHandlerManager.getSubject('/system/handler/uuid');
    subject?.subscribe(args => {
      this.uuid = <string>args.get('uuid');
      this.changeDetectorRef.detectChanges();
    })
  }

  registerFundTradeHistoryHandler() {
    let fundTradeHistoryChangeSubject = MessageHandlerManager.getSubject('/fund/trade/helper/calc');
    fundTradeHistoryChangeSubject?.subscribe(args => {

      var validlyTradeList = args.get('validlyTradeList');
      var latestPrice = args.get('latestPrice');
      let helper: Map<string, string>[] = [];

      Object.keys(validlyTradeList).forEach(k => {
        let element = validlyTradeList[k];

        let each = new Map();
        // 最新净值
        each.set("latestPrice", latestPrice);
        // 确认净值
        each.set("price", element['price']);
        // 持有份额
        each.set("tradeNumber", element['tradeNumber']);
        // 持有天数
        each.set("days", element['days']);
        // 收益率
        let _yield = element['yield'];
        each.set("yield", _yield);
        if (parseFloat(_yield) > 0) {
          each.set("bg", "red");
        } else {
          each.set("bg", "green");
        }
        // 交易金额
        let totalAmount = (element['price'] * element['tradeNumber']).toFixed(2);
        each.set("totalAmount", totalAmount);
        

        helper.push(each)
      });

      this.fundTradeHistoryList = helper;
      this.changeDetectorRef.detectChanges();
    })
  }

  background = "lightgreen"
}
