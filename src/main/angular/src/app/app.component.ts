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
  fundTradeHistoryList: FundTradeHistoryObject[] = [];
  fundTradeHistoryColumns = ['latestPrice', 'price', 'tradeNumber', 'days', 'totalAmount', 'yield']

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
      // let helper: Map<string, string>[] = [];
      let helper: FundTradeHistoryObject[] = [];

      Object.keys(validlyTradeList).forEach(k => {
        let element = validlyTradeList[k];

        let totalAmount = (element['price'] * element['tradeNumber']).toFixed(2);
        let background = parseFloat(element['yield']) > 0 ? "red" : "green";

        let each = {
          latestPrice: latestPrice, // 最新净值
          price: element['price'], // 确认净值
          tradeNumber: element['tradeNumber'], // 持有份额
          days: element['days'], // 持有天数
          yield: element['yield'], // 收益率
          background: background,
          totalAmount: totalAmount // 交易金额
        };

        helper.push(each);
      });

      this.fundTradeHistoryList = helper;
      this.changeDetectorRef.detectChanges();
    })
  }

  background = "lightgreen"
}

export interface FundTradeHistoryObject {
  latestPrice: string,
  price: string,
  tradeNumber: string
  days: string,
  yield: string,
  background: string,
  totalAmount: string
}