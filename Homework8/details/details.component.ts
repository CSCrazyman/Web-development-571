import { Component, EventEmitter, Input, OnChanges, Output, SimpleChange } from '@angular/core';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

function getName(pro: object) {
  let name = pro['title'];
  return name;
}

function getDays(pro: object) {
  let days = pro['timeLeft'].substring(pro['timeLeft'].indexOf('P') + 1, pro['timeLeft'].indexOf('D'));
  return days;
}

function getPrice(pro: object) {
  let price = pro['buyItNowPrice']['__value__'];
  return price;
}

function getShipping(pro: object) {
  let cost = pro['shippingCost']['__value__'];
  return cost;
}

@Component({
  selector: 'app-details',
  templateUrl: './details.component.html',
  styleUrls: ['./details.component.css'],
  animations: [
    trigger('animationTwo', [
      state('in', style({
          display: 'block',
          transform: 'translateX(0)',
        })
      ),
      state('out', style({
          display: 'none',
          transform: 'translateX(100%)'
        })
      ),
      transition(
        'in => out',
        [animate(500)]
      ),
      transition(
        'out => in',
        [animate(500)]
      )
    ])
  ]
})

export class DetailsComponent implements OnChanges {
  // Controls the animation
  public _panel2;
  detailsOpened;
  // Details JSON
  detailsInfo;
  singlePro;
  similarPro;
  // progress bar
  detailsProgressBar = true;
  // storage
  storage;
  wishlistArr;
  // facebook url
  facebookURL;
  // navigation
  navBars = 'product';
  // Background Changers
  productInfoArr;
  shipInfoArr;
  sellerInfoArr;
  // google custom search -> photos
  photos;
  // feedback score
  feedbackScore = 0;
  // sort Selectors
  opt1 = 'default';
  opt2 = 'ascending';
  defaultArr;
  sortedArr;
  // show more or less
  showMoreOrLess = true;
  windowWidth;
  private clearFlag: boolean;
  // panel change
  @Output() panelChange2 = new EventEmitter<string>();

  constructor(private modalService: NgbModal) {
    this.storage = window.localStorage;
    this.windowWidth = window.innerWidth;
  }

  @Input()
  set openSingleProduct(open) {
    this.detailsOpened = open;
  }

  @Input()
  set getSimilars(similars) {
    this.similarPro = similars;
    // --------- similar JSON ---------
    if (this.similarPro !== undefined && this.similarPro['getSimilarItemsResponse']['itemRecommendations']['item'] !== undefined) {
      this.defaultArr = this.similarPro['getSimilarItemsResponse']['itemRecommendations']['item'];
      this.sortedArr = this.defaultArr;
    }
  }

  @Input()
  set getURL(url) {
    this.facebookURL = url;
  }

  @Input()
  set getPhotos(photos) {
    this.photos = photos;
  }

  @Input()
  set choosePanel2(panel) {
    this._panel2 = panel;
  }

  @Input()
  set clearContent(clearFlag) {
    this.clearFlag = clearFlag;
  }

  @Input()
  set getDetailsInfo(detailsInfo) {
    this.getSleep(0);
    this.showMoreOrLess = true;
    this.opt1 = 'default';
    this.opt2 = 'ascending';
    this.detailsInfo = detailsInfo;
    this.navBars = 'product';
    if (this.detailsInfo !== undefined) {
      let infoArrPro = new Array() as Array<any>;
      // --------- product details info array ---------
      if (detailsInfo['Item']['PictureURL'] !== undefined && detailsInfo['Item']['PictureURL'].length !== 0) {
        infoArrPro.push(0);
      }
      if (detailsInfo['Item']['Subtitle'] !== undefined) {
        infoArrPro.push(1);
      }
      if (detailsInfo['Item']['CurrentPrice'] !== undefined) {
        infoArrPro.push(2);
      }
      if (detailsInfo['Item']['Location'] !== undefined) {
        infoArrPro.push(3);
      }
      if (detailsInfo['Item']['ReturnPolicy'] !== undefined) {
        infoArrPro.push(4);
      }
      if (detailsInfo['Item']['ItemSpecifics'] !== undefined) {
        let nameAndVal = detailsInfo['Item']['ItemSpecifics']['NameValueList'];
        for (let i = 0 ; i < nameAndVal.length ; i ++) {
          infoArrPro.push({'name': nameAndVal[i]['Name'], 'value': nameAndVal[i]['Value']});
        }
      }
      this.productInfoArr = infoArrPro;
    }
  }

  @Input()
  set singleWhole(singleProType) {
    this.singlePro = singleProType;
    if (this.singlePro !== undefined) {
      let infoArrShip = new Array() as Array<number>;
      let infoArrSeller = new Array() as Array<number>;
      // --------- shipping details info array ---------
      if (this.singlePro['shippingInfo'] !== undefined) {
        if (this.singlePro['shippingInfo'][0]['shippingServiceCost'] !== undefined) {
          infoArrShip.push(0);
        }
        if (this.singlePro['shippingInfo'][0]['shipToLocations'] !== undefined) {
          infoArrShip.push(1);
        }
        if (this.singlePro['shippingInfo'][0]['handlingTime'] !== undefined) {
          infoArrShip.push(2);
        }
        if (this.singlePro['shippingInfo'][0]['expeditedShipping'] !== undefined) {
          infoArrShip.push(3);
        }
        if (this.singlePro['shippingInfo'][0]['oneDayShippingAvailable'] !== undefined) {
          infoArrShip.push(4);
        }
      }
      if (this.singlePro['returnsAccepted'] !== undefined) {
        infoArrShip.push(5);
      }
      this.shipInfoArr = infoArrShip;
      // --------- seller details info array ---------
      if (this.singlePro['sellerInfo'] !== undefined) {
        if (this.singlePro['sellerInfo'][0]['sellerUserName'] !== undefined) {
          infoArrSeller.push(0);
        }
        if (this.singlePro['sellerInfo'][0]['feedbackScore'] !== undefined) {
          infoArrSeller.push(1);
          this.feedbackScore = parseInt(this.singlePro['sellerInfo'][0]['feedbackScore'][0], 10);
        }
        if (this.singlePro['sellerInfo'][0]['positiveFeedbackPercent'] !== undefined) {
          infoArrSeller.push(2);
        }
        if (this.singlePro['sellerInfo'][0]['feedbackRatingStar'] !== undefined) {
          infoArrSeller.push(3);
        }
        if (this.singlePro['sellerInfo'][0]['topRatedSeller'] !== undefined) {
          infoArrSeller.push(4);
        }
      }
      if (this.singlePro['storeInfo'] !== undefined) {
        if (this.singlePro['storeInfo'][0]['storeName'] !== undefined) {
          infoArrSeller.push(5);
        }
        if (this.singlePro['storeInfo'][0]['storeURL'] !== undefined) {
          infoArrSeller.push(6);
        }
      }
      this.sellerInfoArr = infoArrSeller;
    }
  }

  @Input()
  set detailProgress(pro) {
    this.detailsProgressBar = pro;
  }

  ngOnChanges(changes: {[propKey: string]: SimpleChange}) {
    for (const propName in changes) {
      if (propName === 'clearContent') {
        if (this.clearFlag === true) {
          this.reset();
        }
      }
    }
  }

  reset() {
    this.similarPro = undefined;
    this.detailsInfo = undefined;
    this.singlePro = undefined;
    this.showMoreOrLess = true;
    this.opt1 = 'default';
    this.opt2 = 'ascending';
  }

  goToResult() {
    this.detailsOpened = 'other';
    this.panelChange2.emit('result');
  }

  changeToWish(itemid, product) {
    if (window.localStorage.getItem(itemid) === null) {
      this.storage.setItem(itemid, JSON.stringify(product));
      this.wishlistArr = new Array(this.storage.length);
      for (let i = 0 ; i < this.storage.length ; i ++) {
        this.wishlistArr[i] = JSON.parse(this.storage.getItem(window.localStorage.key(i)));
      }
    } else {
      window.localStorage.removeItem(itemid);
    }
  }

  checkWish(itemid) {
    if ((window.localStorage.getItem(itemid)) === null) {
      return false;
    } else {
      return true;
    }
  }

  specifyValuePro(valueArr) {
    let valueContent = '';
    for (let i = 0 ; i < valueArr.length ; i ++) {
      valueContent += valueArr[i];
      if (i < valueArr.length - 1) {
        valueContent += ', ';
      }
    }
    return valueContent;
  }

  removeSpace(oneString) {
    const spaceExp = /\s/g;
    return oneString.replace(spaceExp, '');
  }

  sortSimilar1(sortIndex) {
    const tempArr = new Array(this.defaultArr.length);
    for (let i = 0 ; i < this.defaultArr.length ; i ++) {
      tempArr[i] = this.defaultArr[i];
    }
    this.opt1 = sortIndex;
    if (tempArr !== undefined) {
      if (this.opt2 === 'ascending') {
        if (this.opt1 === 'default') {
          this.sortedArr = tempArr;
        } else if (this.opt1 === 'name') {
          this.sortedArr = tempArr.sort((a, b) => {
            if (getName(a) < getName(b)) {
              return -1;
            }
            if (getName(a) > getName(b)) {
              return 1;
            }
            return 0;
          });
        } else if (this.opt1 === 'days') {
          this.sortedArr = tempArr.sort((a, b) => {
            return parseInt(getDays(a), 10) - parseInt(getDays(b), 10);
          });
        } else if (this.opt1 === 'price') {
          this.sortedArr = tempArr.sort((a, b) => {
            return parseFloat(getPrice(a)) - parseFloat(getPrice(b));
          });
        } else if (this.opt1 === 'shipping') {
          this.sortedArr = tempArr.sort((a, b) => {
            return parseFloat(getShipping(a)) - parseFloat(getShipping(b));
          });
        }
      } else if (this.opt2 === 'descending') {
        if (this.opt1 === 'default') {
          this.sortedArr = tempArr;
        } else if (this.opt1 === 'name') {
          this.sortedArr = tempArr.sort((b, a) => {
            if (getName(a) < getName(b)) {
              return -1;
            }
            if (getName(a) > getName(b)) {
              return 1;
            }
            return 0;
          });
        } else if (this.opt1 === 'days') {
          this.sortedArr = tempArr.sort((b, a) => {
            return parseInt(getDays(a), 10) - parseInt(getDays(b), 10);
          });
        } else if (this.opt1 === 'price') {
          this.sortedArr = tempArr.sort((b, a) => {
            return parseFloat(getPrice(a)) - parseFloat(getPrice(b));
          });
        } else if (this.opt1 === 'shipping') {
          this.sortedArr = tempArr.sort((b, a) => {
            return parseFloat(getShipping(a)) - parseFloat(getShipping(b));
          });
        }
      }
    }
  }

  sortSimilar2(sortIndex) {
    const tempArr = new Array(this.defaultArr.length);
    for (let i = 0 ; i < this.defaultArr.length ; i ++) {
      tempArr[i] = this.defaultArr[i];
    }
    this.opt2 = sortIndex;
    if (tempArr !== undefined) {
      if (this.opt2 === 'ascending') {
        if (this.opt1 === 'default') {
          this.sortedArr = tempArr;
        } else if (this.opt1 === 'name') {
          this.sortedArr = tempArr.sort((a, b) => {
            if (getName(a) < getName(b)) {
              return -1;
            }
            if (getName(a) > getName(b)) {
              return 1;
            }
            return 0;
          });
        } else if (this.opt1 === 'days') {
          this.sortedArr = tempArr.sort((a, b) => {
            return parseInt(getDays(a), 10) - parseInt(getDays(b), 10);
          });
        } else if (this.opt1 === 'price') {
          this.sortedArr = tempArr.sort((a, b) => {
            return parseFloat(getPrice(a)) - parseFloat(getPrice(b));
          });
        } else if (this.opt1 === 'shipping') {
          this.sortedArr = tempArr.sort((a, b) => {
            return parseFloat(getShipping(a)) - parseFloat(getShipping(b));
          });
        }
      } else if (this.opt2 === 'descending') {
        if (this.opt1 === 'default') {
          this.sortedArr = tempArr;
        } else if (this.opt1 === 'name') {
          this.sortedArr = tempArr.sort((b, a) => {
            if (getName(a) < getName(b)) {
              return -1;
            }
            if (getName(a) > getName(b)) {
              return 1;
            }
            return 0;
          });
        } else if (this.opt1 === 'days') {
          this.sortedArr = tempArr.sort((b, a) => {
            return parseInt(getDays(a), 10) - parseInt(getDays(b), 10);
          });
        } else if (this.opt1 === 'price') {
          this.sortedArr = tempArr.sort((b, a) => {
            return parseFloat(getPrice(a)) - parseFloat(getPrice(b));
          });
        } else if (this.opt1 === 'shipping') {
          this.sortedArr = tempArr.sort((b, a) => {
            return parseFloat(getShipping(a)) - parseFloat(getShipping(b));
          });
        }
      }
    }
  }

  getLeftDay(oneString) {
    return oneString.substring(oneString.indexOf('P') + 1, oneString.indexOf('D'));
  }

  changeMoreAndLess() {
    this.showMoreOrLess = !this.showMoreOrLess;
  }

  private getSleep(sleepTime: number = 0): void {
    const t = Date.now();
    while (Date.now() - t <= sleepTime) {}
  }

  open(content) {
    this.modalService.open(content);
  }
}
