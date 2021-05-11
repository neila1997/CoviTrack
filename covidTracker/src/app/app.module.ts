import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { FormsModule, ReactiveFormsModule} from '@angular/forms';
import { HttpClientModule } from '@angular/common/http';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { HeaderComponent } from './header/header.component';
import { StateTrackerComponent } from './state-tracker/state-tracker.component';
import { FooterComponent } from './footer/footer.component';
import { VaccineAvailabilityComponent } from './vaccine-availability/vaccine-availability.component';
import { SelfAnalysisComponent } from './self-analysis/self-analysis.component';
import { RegisterAlertsComponent } from './register-alerts/register-alerts.component';

@NgModule({
  declarations: [
    AppComponent,
    HeaderComponent,
    FooterComponent,
    StateTrackerComponent,
    VaccineAvailabilityComponent,
    SelfAnalysisComponent,
    RegisterAlertsComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    HttpClientModule,
    FormsModule,
    ReactiveFormsModule
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
