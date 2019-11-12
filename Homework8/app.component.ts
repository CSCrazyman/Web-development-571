import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})

export class AppComponent {
  // ------- products information (JSON obj) -------
  productInfo;
  proToDetails;
  singlePro;
  // ------- 1st progress bar -------
  resultProgress;
  detailProgress = false;
  // ------- Controls which panel to display -------
  display = false;
  // ------- Changes the styles of two buttons -------
  resultChosen = true;
  wishChosen = false;
  // ------- Controls details and results to display -------
  panel1 = 'result';
  panel2;
  toDetails;
  productPhotos;
  fbURL;
  similars;
  // ------- clear signal -------
  clearFlag = false;
  // -----------------------------------------------
  // ------- products information wish list (JSON obj) -------
  proToDetailsW;
  singleProW;
  detailProgressW;
  // ------- Controls details and results to display -------
  panel1W = 'result';
  panel2W;
  toDetailsW;
  productPhotosW;
  fbURLW;
  similarsW;

  // ------------------ Finished: get data and progress info ------------------
  getProInfo(productInfo) {
    this.productInfo = productInfo;
    this.clearFlag = false;
  }
  getProgressBar(x) {
    this.resultProgress = x;
    this.detailProgress = x;
  }
  // ------------------ Unfinished: clear and close ------------------
  closeResult(x) {
    this.display = x;
    this.resultChosen = true;
    this.wishChosen = false;
  }
  clearPanel() {
    this.clearFlag = true;

    this.panel1 = 'result';
    this.detailProgress = false;
    this.resultProgress = false;
    this.detailProgressW = false;
    this.panel2 = undefined;
    this.toDetails = undefined;
    this.panel1W = 'result';
    this.panel2W = undefined;
    this.toDetailsW = undefined;
  }
  // ------------------ change button styles ------------------
  changeToResult() {
    this.display = false;
    this.resultChosen = true;
    this.wishChosen = false;
  }
  changeToWish() {
    this.display = true;
    this.resultChosen = false;
    this.wishChosen = true;
  }
  // ------------------ relate to details ------------------
  getProDetails(proToDetails) {
    this.proToDetails = proToDetails;
  }

  goToOpenDetails(x) {
    this.toDetails = x;
  }

  changeToDetails() {
    this.panel1 = 'detail';
    this.panel2 = 'detail';
  }

  changeReToDe(x) {
    this.panel1 = 'detail';
    this.panel2 = x;
  }

  changeDeToRe(x) {
    this.panel1 = x;
    this.panel2 = 'result';
  }

  getProDePro(x) {
    this.singlePro = x;
  }

  getPhotos(x) {
    this.productPhotos = x;
  }

  getFbURL(x) {
    this.fbURL = x;
  }

  getSimilars(x) {
    this.similars = x;
  }
  // ------------------ relate to wishlist ------------------
  getProDetailsW(proToDetails) {
    this.proToDetailsW = proToDetails;
  }

  goToOpenDetailsW(x) {
    this.toDetailsW = x;
  }

  changeToDetailsW() {
    this.panel1W = 'detail';
    this.panel2W = 'detail';
  }

  changeReToDeW(x) {
    this.panel1W = 'detail';
    this.panel2W = x;
  }

  changeDeToReW(x) {
    this.panel1W = x;
    this.panel2W = 'result';
  }

  getProDeProW(x) {
    this.singleProW = x;
  }

  getProgressBarW(x) {
    this.detailProgressW = x;
  }

  getPhotosW(x) {
    this.productPhotosW = x;
  }

  getFbURLW(x) {
    this.fbURLW = x;
  }

  getSimilarsW(x) {
    this.similarsW = x;
  }
}
