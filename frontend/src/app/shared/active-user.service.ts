import { Injectable } from '@angular/core';
import {TokenResponse} from "./model/token-response";
import {UserDetails} from "../feed/model/user-details";
import {ApiService} from "./api.service";
import {Router} from "@angular/router";

@Injectable({
  providedIn: 'root'
})
export class ActiveUserService {

  userDetails?: UserDetails

  private readonly TOKEN_KEY = 'access-token'
  private readonly USER_ID_KEY = 'user-id'

  constructor(
    private apiService: ApiService,
    private router: Router
  ) {
    let token = this.getToken()
    let userId = this.getUserId()

    if (token !== null && userId !== null) {
      console.log("User already logged in");
      this.getUserInformation()
    } else {
      console.log("User not logged in");
    }
  }

  setActiveUser(tokenResponse: TokenResponse) {
    console.log("Setting active user", tokenResponse);
    this.setToken(tokenResponse.token)
    this.setUserId(tokenResponse.userId)
    this.getUserInformation()
  }

  isUserLoggedIn() {
    return this.userDetails !== undefined;
  }

  getUsername() {
    return this.userDetails ? this.userDetails.username : ''
  }

  getEmail() {
    return this.userDetails ? this.userDetails.email : ''
  }

  getUserId() {
    return localStorage.getItem(this.USER_ID_KEY)
  }

  setUserId(id: string) {
    localStorage.setItem(this.USER_ID_KEY, id)
  }

  getToken() {
    return localStorage.getItem(this.TOKEN_KEY)
  }

  setToken(token: string) {
    console.log("Setting token", token);
    localStorage.setItem(this.TOKEN_KEY, token)
  }

  logOut() {
    localStorage.removeItem(this.TOKEN_KEY)
    localStorage.removeItem(this.USER_ID_KEY)
    this.userDetails = undefined;
    this.navigateToHome()
  }

  private getUserInformation() {
    let userId = this.getUserId()
    if (!userId) return
    this.apiService.getUserDetails(userId).subscribe({
      next: value => {
        this.userDetails = value
        this.navigateToHome()
      },
      error: err => console.error(err)
    })
  }

  private navigateToHome() {
    this.router.navigate(["/"])
  }
}
