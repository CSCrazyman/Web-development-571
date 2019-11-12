import {Component, EventEmitter, Output, OnInit} from '@angular/core';
import {PreComponent} from '../pre-result/pre-result.component';

@Component({
  selector: 'app-search',
  templateUrl: './search.component.html',
  styleUrls: ['./search.component.css']
})

export class SearchComponent implements OnInit {
  currentZip;
  isValid = false;
  // ------------------------
  keyword = '';
  category = '-1';
  // ------ Conditions ------
  newItem = false;
  usedItem = false;
  unspItem = false;
  // ------------------------
  // --- Shipping Options ---
  local = false;
  free = false;
  // ------------------------
  distance = '';
  // --------- zip ----------
  locationZip = 'current';
  zip = '';
  options;
  // ------------------------
  display = false;
  @Output() productsData = new EventEmitter<object>();
  @Output() clearAll = new EventEmitter();
  @Output() closePanel = new EventEmitter();
  @Output() progressBar = new EventEmitter();

  constructor(private allURLs: PreComponent) {}

  // Initialization: get current postal code.
  ngOnInit() {
    const xmlHttp = new XMLHttpRequest();
    xmlHttp.open('GET', 'http://ip-api.com/json', false);
    xmlHttp.send();
    if (xmlHttp.readyState === 4) {
      if (xmlHttp.status === 200) {
        this.currentZip = JSON.parse(xmlHttp.responseText).zip;
      }
    }
  }

  onSubmit() {
    this.progressBar.emit(true);
    const productsURL = this.allURLs.getProductsURL(this.keyword, this.category, this.newItem, this.usedItem,
      this.unspItem, this.local, this.free, this.distance, this.locationZip, this.zip, this.currentZip);
    if (productsURL !== undefined) {
      productsURL.subscribe(productsJSON => {
        this.productsData.emit(productsJSON);
      });
    }
  }

  clearSearch() {
    this.keyword = '';
    this.category = '-1';
    this.newItem = false;
    this.usedItem = false;
    this.unspItem = false;
    this.local = false;
    this.free = false;
    this.distance = '';
    this.locationZip = 'current';
    this.zip = '';
    this.isValid = false;
    this.clearAll.next();
    this.closePanel.emit(this.display);
  }

  chooseNew(e) {
    if (e.target.checked) {
      this.newItem = true;
    } else {
      this.newItem = false;
    }
  }

  chooseUsed(e) {
    if (e.target.checked) {
      this.usedItem = true;
    } else {
      this.usedItem = false;
    }
  }

  chooseUnsp(e) {
    if (e.target.checked) {
      this.unspItem = true;
    } else {
      this.unspItem = false;
    }
  }

  chooseLocal(e) {
    if (e.target.checked) {
      this.local = true;
    } else {
      this.local = false;
    }
  }

  chooseFree(e) {
    if (e.target.checked) {
      this.free = true;
    } else {
      this.free = false;
    }
  }

  autoZipcode(myzip) {
    const tempZip = this.allURLs.getAutoZip(myzip);
    if (tempZip !== undefined) {
      tempZip.subscribe(similarZips => {
        this.options = similarZips['postalCodes'];
      });
    }
  }

  zipValid() {
    const pattern = /^[0-9]{5}$/;
    if (this.locationZip === 'current' || pattern.test(this.zip)) {
      this.isValid = true;
    } else {
        this.isValid = false;
    }
    return this.isValid;
  }

}
