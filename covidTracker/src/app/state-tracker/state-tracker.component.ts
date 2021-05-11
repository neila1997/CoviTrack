import { CastExpr } from '@angular/compiler';
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import csc, { ICity, IState } from 'country-state-city'
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-state-tracker',
  templateUrl: './state-tracker.component.html',
  styleUrls: ['./state-tracker.component.css']
})
export class StateTrackerComponent implements OnInit {

  states: string[] = []
  cities: string[] = []
  checkState = ""
  x: Object
  cases = 0
  recovered = 0
  deaths = 0
  totalCases = 0
  totalRecovered = 0
  totalDeaths = 0
  displayFlag = false

  state = "Select a State"
  city = "Select a City"

  constructor(private api:ApiService, private title:Title) { 

    this.title.setTitle("District Tracker | CoviTrack")

    this.api.getData().subscribe(x=>{
      this.x = x
      for (let i of Object.keys(x))
        this.states.push(i)
    })
    // console.log(this.states)
    // this.cities = csc.getCitiesOfState("IN", "GJ")
  }

  ngOnInit(): void {
  }

  updateCity(){
    this.cities = []
    console.log("inside updateCity")
    console.log(this.state)

    console.log("Inside lookForCases")

    for(let i of Object.keys(this.x[this.state].districtData))
      this.cities.push(i)
  }

  lookForCases(){

        // console.log(x[checkState].districtData[this.city].delta)
        this.displayFlag = true
        this.cases = this.x[this.state].districtData[this.city].delta.confirmed
        this.deaths = this.x[this.state].districtData[this.city].delta.deceased
        this.recovered = this.x[this.state].districtData[this.city].delta.recovered
        this.totalCases = this.x[this.state].districtData[this.city].confirmed
        this.totalDeaths = this.x[this.state].districtData[this.city].deceased
        this.totalRecovered = this.x[this.state].districtData[this.city].recovered
      }

  updateData(x){
      console.log(this.state)
        console.log(x[this.state])
  }

}
