import {Injectable} from '@angular/core';
import {UserDetails} from "../feed/model/user-details";
import {UserCredentials} from "./model/user-credentials";
import {ApiService} from "../shared/api.service";
import {ActiveUserService} from "../shared/active-user.service";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  userDetails: UserDetails | null = null;

  constructor(
    private apiService: ApiService,
    private activeUserService: ActiveUserService
  ) {}

  doLogin(userCredentials: UserCredentials) {
    console.log("Authorizing with", userCredentials)

    this.apiService.getAccessToken(userCredentials).subscribe({
      next: value => this.activeUserService.setActiveUser(value),
      error: err => console.error(err)
    })
  }
}
