import { Component, EventEmitter, Input, Output, OnChanges, SimpleChange } from '@angular/core';
import { animate, state, style, transition, trigger } from '@angular/animations';
import { PreComponent} from '../pre-result/pre-result.component';

@Component({
  selector: 'app-results',
  templateUrl: './results.component.html',
  styleUrls: ['./results.component.css'],
  animations: [
    trigger('animationOne', [
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

export class ResultsComponent implements OnChanges {
  products;
  productsValid = false;
  progress = false;
  showError = false;
  toDetail;
  itemId;
  storage;
  wishlistArr;
  facebookURL;
  photos;
  similarPro;

  public _panel = 'result';
  private clearFlag: boolean;

  constructor(private otherURLs: PreComponent) {
    this.storage = window.localStorage;
  }

  @Output() singleProduct = new EventEmitter<object>();
  @Output() openDetails = new EventEmitter<string>();
  @Output() goToSingleDetails = new EventEmitter();
  @Output() panelChange1 = new EventEmitter<string>();
  @Output() singleProductPro = new EventEmitter<object>();
  @Output() fbURL = new EventEmitter<string>();
  @Output() proPhotos = new EventEmitter<object>();
  @Output() proSimilars = new EventEmitter<object>();

  @Input()
  set progressBar(showBar) {
    this.progress = showBar;
    this.showError = false;
    this.productsValid = false;
  }

  @Input()
  set clearContent(clearFlag) {
    this.clearFlag = clearFlag;
  }

  @Input()
  set data(productsInfo) {
    if (productsInfo !== undefined) {
      this.products = productsInfo.findItemsAdvancedResponse[0].searchResult[0].item;
      this.productsValid = true;
    }
    if (this.products === undefined && this.productsValid === true) {
      this.progress = false;
      this.showError = true;
    }
  }

  @Input()
  set choosePanel(panel) {
    this._panel = panel;
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
    this.products = undefined;
    this._panel = 'result';
    this.toDetail = 'no';
    this.showError = false;
    this.productsValid = false;
    this.facebookURL = undefined;
    this.photos = undefined;
    this.similarPro = undefined;
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

  computeIndex(x: number, y: number) {
    if (x === undefined) {
      return y;
    } else {
      return (x - 1) * 10 + y;
    }
  }

  // --------------------relates to details --------------------
  goToDetails(itemid) {
    this.itemId = itemid;
    this.toDetail = 'yes';
    this._panel = 'detail';
    this.openDetails.emit('openSinglePro');
    this.otherURLs.getSinglePro(this.itemId).subscribe(singleProJSON => {
      this.singleProduct.emit(singleProJSON);
      if (singleProJSON !== undefined) {
        let link = singleProJSON['Item']['ViewItemURLForNaturalSearch'];
        let quoteText = 'Buy ' + singleProJSON['Item']['Title'] + ' at $' + singleProJSON['Item']['CurrentPrice']['Value'];
        quoteText += ' from link below';
        this.facebookURL = 'https://www.facebook.com/dialog/share?app_id=2264019113814264&display=popup';
        this.facebookURL += '&href=' + encodeURIComponent(link) + '&quote=' + encodeURIComponent(quoteText);
        // --------- google custom pictures ---------
        this.otherURLs.getProPhotos(singleProJSON['Item']['Title']).subscribe(proPhotosJSON => {
          this.photos = proPhotosJSON;
          this.fbURL.emit(this.facebookURL);
          this.proPhotos.emit(this.photos);
        });
      }
    });
    this.goToSingleDetails.emit();
    for (let i = 0 ; i < this.products.length ; i ++) {
      if (this.itemId === this.products[i]['itemId']) {
        this.singleProductPro.emit(this.products[i]);
        this.otherURLs.getSimilars(this.products[i]['itemId'][0]).subscribe(similarJSON => {
          this.similarPro = similarJSON;
          this.proSimilars.emit(this.similarPro);
        });
      }
    }
  }

  changeSingle() {
    this.panelChange1.emit('detail');
  }
}
