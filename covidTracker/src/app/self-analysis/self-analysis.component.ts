import { Component, OnInit } from '@angular/core';
import { Title } from '@angular/platform-browser';
import * as $ from 'jquery';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-self-analysis',
  templateUrl: './self-analysis.component.html',
  styleUrls: ['./self-analysis.component.css']
})
export class SelfAnalysisComponent implements OnInit {

  fever = false
  cough = false
  cold = false
  taste = false
  breath = false
  fatigue = false
  message = ""

  constructor(private title: Title, private api: ApiService) {
    this.title.setTitle("Self-Analysis | CoviTrack")
  }

  ngOnInit(): void {
  }

  checkForSymptoms() {
    // console.log("TEST")
    console.log(this.fever)
    var a = 1
    if (this.fever === false)
      a = 0
    console.log($("input[name='contact']:checked").val())
    // this.api.covidPredict(a).subscribe(
    //   x => {
    //     console.log(x)
    //     if (x === "True")
    //       this.message = "You may be suffering from Covid-19. Please get yourself tested. "
    //     else
    //       this.message = "You seem fine niggah!"

    //     document.getElementById("toggleModal").click()
    //   }
    // )
  }

}
