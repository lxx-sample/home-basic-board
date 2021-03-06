import { stringify } from '@angular/compiler/src/util';
import { ChangeDetectorRef, Component, OnInit } from '@angular/core';
import { Observable, of, Subscriber } from 'rxjs';

import { MessageHandlerManager, WebSocketClient } from './tools/websocket.component'

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  uuid: string = "";
  panel: string = "";
  fundTradeHistoryList: FundTradeHistoryObject[] = [];
  fundTradeHistoryColumns = ['price', 'tradeNumber', 'totalAmount', 'yield'];
  response: any = new Map();
  totalNumber: any = "";
  totalAmount: any = "";
  enableTradeNumber: any = "";

  id = 2;
  latestPrice = 1;
  reckonPrice = 1;

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
      let helper: FundTradeHistoryObject[] = [];

      Object.keys(validlyTradeList).forEach(k => {
        let element = validlyTradeList[k];

        let totalAmount = (element['price'] * element['tradeNumber']).toFixed(2);
        let background = parseFloat(element['yield']) > 0 ? "red" : "green";

        let each = {
          latestPrice: latestPrice, // ????????????
          price: element['price'], // ????????????
          tradeNumber: element['tradeNumber'], // ????????????
          days: element['days'], // ????????????
          yield: element['yield'], // ?????????
          background: background,
          totalAmount: totalAmount // ????????????
        };

        helper.push(each);
      });

      this.fundTradeHistoryList = helper;
      this.changeDetectorRef.detectChanges();

      this.response = args;
      this.totalAmount = args.get('totalAmount');
      this.totalNumber = args.get('totalNumber');
      this.enableTradeNumber = args.get('enableTradeNumber');
      this.changeDetectorRef.detectChanges();
      // console.log(this.response)
    })
  }

  background = "white"

  submit() {
    var message = {
      "path": "/fund/trade/helper/calc",
      "args": { "fundInfoId": this.id, "latestPrice": this.latestPrice, "reckonPrice": this.reckonPrice }
    };
    WebSocketClient.getInstance().send(JSON.stringify(message));
  }
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
