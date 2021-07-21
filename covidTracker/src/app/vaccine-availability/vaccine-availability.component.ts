import { formatDate } from '@angular/common';
import { HttpClient } from '@angular/common/http';
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
  doseOneSlots: number
  doseTwoSlots: number
  vaccineType: string
  ageLimit: number
  fees: number
  date: string
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
  state = "Select a State"
  city = "Select a City"
  date: number
  dateFormatted: string
  slots = new Map<string, availableSlots[]>()

  constructor(private api: ApiService, private title: Title, private http: HttpClient) {

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
        // console.log(x)
        for (let district of x.districts) {
          this.districts.set(district.district_name, district.district_id)
        }
        this.districtList = Array.from(this.districts.keys())
        // console.log(this.districtList)
      }
    )
  }

  checkAvailability() {
    this.showSlots = false
    this.submitDisabled = true
    this.checkMessage = "Checking Availability"
    this.errorMessage = false
    this.slots = new Map<string, availableSlots[]>()
    this.dateFormatted = formatDate(this.date, format, locale)
      
    this.api.checkAvailability(this.districts.get(this.city), this.dateFormatted).subscribe(
      x => {
        console.log(x)
        console.log(x.centers.length)
        for (var i = 0; i < x.centers.length; i++) {
          for (var j = 0; j < x.centers[i].sessions.length; j++) {
            var slot = new availableSlots()
            slot.fees = (x.centers[i].fee_type.toUpperCase() === 'FREE') ? 0 : 1
            slot.name = x.centers[i].name
            slot.address = x.centers[i].address
            slot.district = x.centers[i].district_name
            slot.pincode = x.centers[i].pincode
            slot.ageLimit = x.centers[i].sessions[j].min_age_limit
            slot.availableSlots = x.centers[i].sessions[j].available_capacity
            slot.doseOneSlots = x.centers[i].sessions[j].available_capacity_dose1
            slot.doseTwoSlots = x.centers[i].sessions[j].available_capacity_dose2
            slot.vaccineType = x.centers[i].sessions[j].vaccine
            slot.date = x.centers[i].sessions[j].date

            if (slot.fees === 1)
              for (var k = 0; k < x.centers[i].vaccine_fees.length; k++)
                if (slot.vaccineType === x.centers[i].vaccine_fees[k].vaccine)
                  slot.fees = x.centers[i].vaccine_fees[k].fee
            if (!this.slots.has(slot.date)) {
              // console.log("Test")
              this.slots.set(slot.date, [])
            }
            if (slot.availableSlots > 0)
              this.slots.get(slot.date).push(slot)
          }
        }

        for (let key of this.slots.keys()){
          console.log(this.slots.get(key))
          if (this.slots.get(key) === undefined)
            this.slots.delete(key)
          else if (this.slots.get(key).length === 0)
            this.slots.delete(key)
        }
        for (let key of this.slots.keys()){
          var array = this.slots.get(key)
          if (array.length != 0)
            array.sort((a, b)=>{
              return a.ageLimit - b.ageLimit || a.fees - b.fees 
            })
          this.slots.set(key, array)
        }
        // for (let key of this.slots.keys()){
        //   var array = this.slots.get(key)
        //   if (array.length != 0)
        //     array.sort((a, b)=>{
        //       if (a.ageLimit == b.ageLimit)
        //         if (a.fees.localeCompare('FREE')) return -1
        //         else if(b.fees.localeCompare('FREE')) return 1
        //     })
        //   this.slots.set(key, array)
        // }
        this.slots.size > 0 ? this.showSlots = true : this.errorMessage = true
        this.submitDisabled = false
      }
    )
  }

  ngOnInit(): void {
  }

}
