import { formatDate } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import { ApiService } from '../service/api.service';

const format = 'dd-MM-yyyy';
const locale = 'en-US';

export class availableSlots {
  name: string
  address: string
  pincode: number
  district: string
  availableSlots: number
  vaccineType: string
  ageLimit: number
}

@Component({
  selector: 'app-vaccine-availability',
  templateUrl: './vaccine-availability.component.html',
  styleUrls: ['./vaccine-availability.component.css']
})

export class VaccineAvailabilityComponent implements OnInit {

  submitDisabled = false
  errorMessage = false
  showSlots = false
  checkMessage = "Check Slot Availability"
  states = new Map<String, number>()
  districts = new Map<String, number>()
  stateList = []
  districtList = []
  slots: availableSlots[] = []
  state = "Select a State"
  city = "Select a City"
  date: number
  dateFormatted: string

  constructor(private api: ApiService, private title: Title) {

    this.title.setTitle("Slot Availability | CoviTrack")

    this.date = Date.now()
    this.dateFormatted = formatDate(this.date, format, locale)
    this.api.fetchStates().subscribe(
      x => {
        // console.log(x)
        for (let state of x.states) {
          // console.log(state.state_name)
          this.states.set(state.state_name, state.state_id)
        }
        // console.log(this.states)
        this.stateList = Array.from(this.states.keys())

        // console.log(this.states.get("Gujarat"))
      }
    )
  }

  fetchCities() {
    this.districts = new Map<string, number>()
    this.districtList = []
    this.city = ""
    this.api.fetchCities(this.states.get(this.state)).subscribe(
      x => {
        // console.log("🪵")
        console.log(x)
        for (let district of x.districts) {
          this.districts.set(district.district_name, district.district_id)
        }
        this.districtList = Array.from(this.districts.keys())
        // console.log(this.districtList)
      }
    )
    console.log(this.dateFormatted)
  }

  checkAvailability() {
    this.showSlots = false
    this.submitDisabled = true
    this.checkMessage = "Checking Availability"
    this.errorMessage = false
    this.slots = []
    this.dateFormatted = formatDate(this.date, format, locale)
    this.api.checkAvailability(this.districts.get(this.city), this.dateFormatted).subscribe(
      x => {
        console.log(this.dateFormatted)
        console.log(x)
        if (x.sessions.length === 0)
          this.errorMessage = true
        else
          this.showSlots = true
        for (var i = 0; i < x.sessions.length; i++) {
          // console.log("Inside")
          // console.log(x.sessions.length)
          this.slots[i] = new availableSlots()
          this.slots[i].name = x.sessions[i].name
          this.slots[i].address = x.sessions[i].address
          this.slots[i].pincode = x.sessions[i].pincode
          this.slots[i].district = x.sessions[i].district_name
          this.slots[i].ageLimit = x.sessions[i].min_age_limit
          this.slots[i].availableSlots = Math.floor(x.sessions[i].available_capacity)
          this.slots[i].vaccineType = x.sessions[i].vaccine
        }
        this.submitDisabled = false
        this.checkMessage = "Check Slot Availability"

        this.slots.sort((a, b) => {
          return a.ageLimit - b.ageLimit
        });
      }
    )
  }

  ngOnInit(): void {
  }

}
