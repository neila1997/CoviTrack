import { HttpClient, HttpResponse } from '@angular/common/http';
import { Injectable } from '@angular/core';

// const baseUrl = "http://localhost:5000"
const baseUrl = "http://covitrack.ap-south-1.elasticbeanstalk.com/"

@Injectable({
  providedIn: 'root'
})

export class ApiService {

  constructor(private http: HttpClient) {

  }

  getData() {
    return this.http.get<Object>("https://api.covid19india.org/state_district_wise.json")
  }

  // CoWin API's

  //1. Fetch list of States 
  fetchStates() {
    return this.http.get<any>("https://cdn-api.co-vin.in/api/v2/admin/location/states")
  }

  //2. Fetch List of Cities
  fetchCities(state){
    return this.http.get<any>("https://cdn-api.co-vin.in/api/v2/admin/location/districts/"+state)
  }

  //3. Check Slot Availability
  checkAvailability(district_id, formatted_date){
    return this.http.get<any>("https://cdn-api.co-vin.in/api/v2/appointment/sessions/public/calendarByDistrict?district_id="+district_id+"&date="+formatted_date)
  }

  //Python API for prediction
// covidPredict(param){
//   return this.http.post("http://127.0.0.1:8000/covidpredictor", param, {responseType: 'text'})
// }

  //Spring Boot API for registration
  register(form){
    console.log(form)
    return this.http.post(baseUrl+"/registerforalerts", form, {responseType: 'text'})
  }

  unsubscribe(id:Number){
    return this.http.get(baseUrl+"/unsubscribe?id="+id, {responseType: 'text'})
  }

}

