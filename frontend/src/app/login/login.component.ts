import {Component, OnInit} from '@angular/core';
import {UserCredentials} from "./model/user-credentials";
import {LoginService} from "./login.service";

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  userCredentials: UserCredentials = {
    username: '',
    password: ''
  }

  constructor(private loginService: LoginService) {}

  onSubmit(): void {
    this.loginService.doLogin(this.userCredentials);
  }

  ngOnInit(): void {}
}
