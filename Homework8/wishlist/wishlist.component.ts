import {Component, EventEmitter, OnInit, Output, Input, OnChanges, SimpleChange} from '@angular/core';
import { PreComponent} from '../pre-result/pre-result.component';
import { animate, state, style, transition, trigger } from '@angular/animations';

@Component({
  selector: 'app-wishlist',
  templateUrl: './wishlist.component.html',
  styleUrls: ['./wishlist.component.css'],
  animations: [
    trigger('animationThree', [
      state('in', style({
          display: 'block',
          transform: 'translateX(0)'
        })
      ),
      state('out', style({
          display: 'none',
          transform: 'translateX(-100%)'
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

export class WishlistComponent implements OnInit, OnChanges {
  private _products;
  storage;
  wishListArr;
  toDetail;
  // the same like results
  itemId;
  facebookURLW;
  photosW;
  similarProW;
  public _panel = 'result';
  private clearFlag: boolean;

  @Output() singleProductW = new EventEmitter<object>();
  @Output() openDetailsW = new EventEmitter<string>();
  @Output() goToSingleDetailsW = new EventEmitter();
  @Output() singleProductProW = new EventEmitter<object>();
  @Output() panelChange1W = new EventEmitter<string>();
  @Output() progessBarW = new EventEmitter();
  @Output() fbURLW = new EventEmitter<string>();
  @Output() proPhotosW = new EventEmitter<object>();
  @Output() proSimilarsW = new EventEmitter<object>();

  @Input()
  set choosePanelW(panel) {
    this._panel = panel;
  }

  @Input()
  set clearContent(clearFlag) {
    this.clearFlag = clearFlag;
  }

  constructor(private otherURLs: PreComponent) {
    this.storage = window.localStorage;
  }

  ngOnInit() {
    this.wishListArr = new Array(this.storage.length);
    for (let i = 0 ; i < this.storage.length ; i ++) {
      this.wishListArr[i] = JSON.parse(this.storage.getItem(window.localStorage.key(i)));
    }
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

  get wishlist() {
    this.wishListArr = new Array(this.storage.length);
    for (let i = 0 ; i < this.storage.length ; i ++) {
      this.wishListArr[i] = JSON.parse(this.storage.getItem(window.localStorage.key(i)));
    }
    this._products = this.wishListArr;
    return this._products;
  }

  reset() {
    this._panel = 'result';
    this.toDetail = 'no';
    this.itemId = undefined;
    this.facebookURLW = undefined;
    this.photosW = undefined;
    this.similarProW = undefined;
  }

  removeWish(itemid) {
    for (let i = 0 ; i < this.storage.length ; i ++) {
      if ((window.localStorage.key(i)) === itemid) {
        window.localStorage.removeItem(window.localStorage.key(i));
      }
    }
    this.wishListArr = new Array(this.storage.length);
    for (let i = 0 ; i < this.storage.length; i ++) {
      this.wishListArr[i] = JSON.parse(this.storage.getItem(window.localStorage.key(i)));
    }
  }

  computeTotalCost(products) {
    let totalCost = 0.0;
    for (let i = 0 ; i < products.length ; i ++) {
      totalCost += parseFloat(products[i].sellingStatus[0].currentPrice[0].__value__);
    }
    return totalCost;
  }

  stringCut(oneString) {
    if (oneString.length > 35) {
      let newString = oneString.substr(0, 35);
      if (newString.charAt(34) !== ' ') {
        const lastIndex = newString.lastIndexOf(' ');
        newString = newString.substr(0, lastIndex + 1);
      }
      return (newString + '...');
    } else {
      return oneString;
    }
  }

  wishToDetails(itemID, obj) {
    this.itemId = itemID;
    this.toDetail = 'yes';
    this.openDetailsW.emit('openSinglePro');
    this.otherURLs.getSinglePro(this.itemId).subscribe(singleProJSON => {
      this.singleProductW.emit(singleProJSON);
      if (singleProJSON !== undefined) {
        let link = singleProJSON['Item']['ViewItemURLForNaturalSearch'];
        let quoteText = 'Buy ' + singleProJSON['Item']['Title'] + ' at $' + singleProJSON['Item']['CurrentPrice']['Value'];
        quoteText += ' from link below';
        this.facebookURLW = 'https://www.facebook.com/dialog/share?app_id=2264019113814264&display=popup';
        this.facebookURLW += '&href=' + encodeURIComponent(link) + '&quote=' + encodeURIComponent(quoteText);
        // --------- google custom pictures ---------
        this.otherURLs.getProPhotos(singleProJSON['Item']['Title']).subscribe(proPhotosJSON => {
          this.photosW = proPhotosJSON;
          this.fbURLW.emit(this.facebookURLW);
          this.proPhotosW.emit(this.photosW);
        });
      }
    });
    this.goToSingleDetailsW.emit();
    this.singleProductProW.emit(obj);
    this.otherURLs.getSimilars(obj['itemId'][0]).subscribe(similarJSON => {
      this.similarProW = similarJSON;
      this.proSimilarsW.emit(this.similarProW);
    });
    this.progessBarW.emit(true);
  }

  changeSingleW() {
    this.panelChange1W.emit('detail');
  }
}
