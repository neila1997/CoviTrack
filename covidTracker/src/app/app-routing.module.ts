import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { RegisterAlertsComponent } from './register-alerts/register-alerts.component';
import { SelfAnalysisComponent } from './self-analysis/self-analysis.component';
import { StateTrackerComponent } from './state-tracker/state-tracker.component';
import { UnsubscribeComponent } from './unsubscribe/unsubscribe.component';
import { VaccineAvailabilityComponent } from './vaccine-availability/vaccine-availability.component';

const routes: Routes = [
  {path:'statetracker', component:StateTrackerComponent},
  {path:'home', component:VaccineAvailabilityComponent},
  {path:'vaccine', component:VaccineAvailabilityComponent},
  {path:'analysis', component:SelfAnalysisComponent},
  {path:'registerforalerts', component:RegisterAlertsComponent},
  {path:'unsubscribealerts/:id', component:UnsubscribeComponent},
  {path:'', component:VaccineAvailabilityComponent}
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
