import { Component} from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-pre-result',
  templateUrl: './pre-result.component.html',
  styleUrls: ['./pre-result.component.css']
})

export class PreComponent {

  constructor(private getHttp: HttpClient) {}

  getProductsURL(keyword, categoryId, isNew, isUsed, isUnsp, isLocal, isFree, distance, mode, zip, current) {
    // ---- Get URL ----
    let productsURL = 'http://productsearch-rl.us-east-2.elasticbeanstalk.com/' + keyword;
    if (mode === 'current') {
      productsURL += '/t/' + current;
    } else {
      productsURL += '/t/' + zip;
    }
    productsURL += '/' + categoryId;
    if (distance === '') {
      productsURL += '/t/' + 10;
    } else {
      productsURL += '/t/' + distance;
    }
    if (isFree) {
      productsURL += '/t';
    } else {
      productsURL += '/f';
    }
    if (isLocal) {
      productsURL += '/t';
    } else {
      productsURL += '/f';
    }
    productsURL += '/t';
    if (isNew) {
      productsURL += '/t';
    } else {
      productsURL += '/f';
    }
    if (isUsed) {
      productsURL += '/t';
    } else {
      productsURL += '/f';
    }
    if (isUnsp) {
      productsURL += '/t';
    } else {
      productsURL += '/f';
    }
    return this.getHttp.get(encodeURI(productsURL));
  }

  getSinglePro(itemid) {
    return this.getHttp.get('http://productsearch-rl.us-east-2.elasticbeanstalk.com/itemF/' + itemid);
  }

  getSimilars(itemid) {
    return this.getHttp.get('http://productsearch-rl.us-east-2.elasticbeanstalk.com/s1/s2/s3/' + itemid);
  }

  getAutoZip(zipcode) {
    if (zipcode.length === 3) {
      return this.getHttp.get('http://productsearch-rl.us-east-2.elasticbeanstalk.com/' + zipcode);
    }
  }

  getProPhotos(itemTitle) {
    return this.getHttp.get('http://productsearch-rl.us-east-2.elasticbeanstalk.com/g1/g2/' + encodeURIComponent(itemTitle));
  }
}
