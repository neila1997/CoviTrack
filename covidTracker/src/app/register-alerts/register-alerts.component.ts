import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-register-alerts',
  templateUrl: './register-alerts.component.html',
  styleUrls: ['./register-alerts.component.css']
})
export class RegisterAlertsComponent implements OnInit {

  states = new Map<String, number>()
  districts = new Map<String, number>()
  stateList = []
  districtList = []
  state = "Select a State"
  city = "Select a City"
  email = ""
  modalMessage = ""
  alertForm: FormGroup

  constructor(private api: ApiService, private form: FormBuilder) {
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
    this.api.fetchCities(this.states.get(this.alertForm.get('state').value)).subscribe(
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
  }

  registerForAlerts() {
    console.log("test")
    // console.log(this.alertForm.controls['city'].value)
    this.alertForm.controls['city_id'].setValue(this.districts.get(this.alertForm.controls['city'].value))
    this.alertForm.controls['dose'].setValue(Number(this.alertForm.controls['dose_type'].value))
    this.alertForm.controls['free_dose'].setValue(Number(this.alertForm.controls['free_dose'].value))
    console.log(this.alertForm.value)
    this.api.register(this.alertForm.value).subscribe(
      x => {
        console.log(x)
        this.modalMessage = "You have been successfully registered. We will update you with availability of slots on your registered email id."
      }, y => {
        console.log(y)
        this.modalMessage = "Oops! Something went wrong. Please try again later."
      }
    )
    document.getElementById("alertModalButton").click()
  }

  ngOnInit(): void {
    this.alertForm = this.form.group({ 'name': ['', Validators.required], 'email': ['', Validators.required], 'age': [18, Validators.required], 'state': ['', Validators.required], 'city': ['', Validators.required], 'city_id': [1, Validators.required], 'vaccine': ['', Validators.required], 'dose_type':[0, Validators.required], 'dose':[0, Validators.required], 'free_dose':[0] })
  }

}
