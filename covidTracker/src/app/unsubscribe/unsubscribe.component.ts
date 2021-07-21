import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ApiService } from '../service/api.service';

@Component({
  selector: 'app-unsubscribe',
  templateUrl: './unsubscribe.component.html',
  styleUrls: ['./unsubscribe.component.css']
})
export class UnsubscribeComponent implements OnInit {

  id :Number
  constructor(private route:ActivatedRoute, private api:ApiService) {
    this.id = this.route.snapshot.params['id']
    console.log(this.id)
    this.api.unsubscribe(this.id).subscribe()
   }

  ngOnInit(): void {
  }

}
