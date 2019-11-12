import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { MatAutocompleteModule } from '@angular/material/autocomplete';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatTooltipModule } from '@angular/material/tooltip';
import { NgxPaginationModule } from 'ngx-pagination';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { RoundProgressModule } from 'angular-svg-round-progressbar';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
// ---------------------------- declarations ----------------------------
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { ResultsComponent } from './results/results.component';
import { WishlistComponent } from './wishlist/wishlist.component';
import { DetailsComponent } from './details/details.component';
import { SearchComponent } from './search/search.component';
import { PreComponent } from './pre-result/pre-result.component';

@NgModule({
  declarations: [
    AppComponent,
    SearchComponent,
    PreComponent,
    ResultsComponent,
    WishlistComponent,
    DetailsComponent,
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule,
    MatAutocompleteModule,
    BrowserAnimationsModule,
    MatTooltipModule,
    NgxPaginationModule,
    RoundProgressModule,
    NgbModule,
  ],
  providers: [
    PreComponent,
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
