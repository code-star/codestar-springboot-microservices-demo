import { Component, OnInit } from '@angular/core';
import {UserRequest} from "./model/user-request";
import {RegisterService} from "./register.service";

@Component({
  selector: 'app-register',
  templateUrl: './register.component.html',
  styleUrls: ['./register.component.css']
})
export class RegisterComponent implements OnInit {

  userRequest: UserRequest = {
    username: '',
    password: '',
    email: ''
  }

  constructor(private registerService: RegisterService) { }

  onSubmit(): void {
    this.registerService.doRegister(this.userRequest);
  }

  ngOnInit(): void {
  }

}
