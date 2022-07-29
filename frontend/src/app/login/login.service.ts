import {Injectable} from '@angular/core';
import {UserDetails} from "../feed/model/user-details";
import {UserCredentials} from "./model/user-credentials";
import {ApiService} from "../shared/api.service";

@Injectable({
  providedIn: 'root'
})
export class LoginService {

  userDetails: UserDetails | null = null;

  constructor(
    private apiService: ApiService
  ) {}

  doLogin(userCredentials: UserCredentials) {
    console.log("Authorizing with", userCredentials)

    this.apiService.getAccessToken(userCredentials).subscribe({
      next: res => {
        localStorage.setItem('access-token', res.token)
        this.userDetails = {
          userId: res.userId,
          username: "",
          email: ""
        }
      },
      error: err => console.error(err)
    })
  }

  private aboutMe() {
    let token = localStorage.getItem('access-token');
    if (token == null) {
      console.error("User not logged in.")
      return;
    }

  }

}
