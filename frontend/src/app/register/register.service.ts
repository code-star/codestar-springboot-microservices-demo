import { Injectable } from '@angular/core';
import {ApiService} from "../shared/api.service";
import {UserRequest} from "./model/user-request";
import {ActiveUserService} from "../shared/active-user.service";

@Injectable({
  providedIn: 'root'
})
export class RegisterService {

  constructor(
    private apiService: ApiService,
    private activeUserService: ActiveUserService
  ) {}

  doRegister(userRequest: UserRequest) {
    console.log("Registering with", userRequest)

    this.apiService.registerUser(userRequest).subscribe({
      next: value => this.activeUserService.setActiveUser(value),
      error: err => console.error(err)
    })
  }
}
