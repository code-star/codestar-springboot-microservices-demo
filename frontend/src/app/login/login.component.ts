import {Component, OnInit} from '@angular/core';
import {HttpClient} from "@angular/common/http";
import {UserCredentials} from "./model/user-credentials";
import {TokenResponse} from "./model/token-response";
import {ApiService} from "../shared/api.service";

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

  constructor(private apiService: ApiService) {}

  onSubmit(): void {
    console.log("Authorizing with", this.userCredentials)

    this.apiService.getAccessToken(this.userCredentials).subscribe({
        next: res => localStorage.setItem('access-token', res.token),
        error: err => console.error(err)
    })

  }

  ngOnInit(): void {}
}
