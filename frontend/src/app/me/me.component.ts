import { Component, OnInit } from '@angular/core';
import {ActiveUserService} from "../shared/active-user.service";

@Component({
  selector: 'app-me',
  templateUrl: './me.component.html',
  styleUrls: ['./me.component.css']
})
export class MeComponent implements OnInit {

  constructor(
    public activeUserService: ActiveUserService
  ) {}

  ngOnInit(): void {
  }

}
