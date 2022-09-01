import {Injectable} from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import {Observable} from "rxjs";
import {TokenResponse} from "./model/token-response";
import {UserCredentials} from "../login/model/user-credentials";
import {Message} from "../feed/model/message";
import {UserDetails} from "../feed/model/user-details";
import {UserRequest} from "../register/model/user-request";
import {Content} from "../feed/model/content";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private AUTH_PORT = 8081
  private MESSAGES_PORT = 8082
  private HOST = "http://localhost"
  private API_BASE = "/api"
  private API_VERSION = "/v1"
  private AUTH_URL_BASE = this.HOST + ":" + this.AUTH_PORT + this.API_BASE + this.API_VERSION
  private MESSAGE_URL_BASE = this.HOST + ":" + this.MESSAGES_PORT + this.API_BASE + this.API_VERSION

  private AUTH_URL = this.AUTH_URL_BASE + "/auth"

  private AUTH_USER_URL = this.AUTH_URL + "/user"
  private AUTH_REGISTER_URL = this.AUTH_URL + "/register"
  private AUTH_USER_ALL_URL = this.AUTH_USER_URL + "/all"
  private AUTH_USER_ME_URL = this.AUTH_USER_URL + "/me"

  private MESSAGE_POST_URL = this.MESSAGE_URL_BASE + "/messages"
  private MESSAGE_GET_ALL_URL = this.MESSAGE_URL_BASE + "/messages/all"


  constructor(
    private httpClient: HttpClient
  ) {}

  getAccessToken(credentials: UserCredentials) : Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(this.AUTH_URL, credentials);
  }

  getCurrentUserInfo(accessToken: string) : Observable<UserDetails> {
    return this.httpClient.get<UserDetails>(this.AUTH_USER_ME_URL);
  }

  getAllMessages() : Observable<Message[]> {
    return this.httpClient.get<Message[]>(this.MESSAGE_GET_ALL_URL);
  }

  postMessage(message: Content, accessToken: string) : Observable<Message> {
    return this.httpClient.post<Message>(this.MESSAGE_POST_URL, message);
  }

  getUserDetails(userId?: string) : Observable<UserDetails> {
    return this.httpClient.get<UserDetails>(this.AUTH_USER_URL + "/" + userId);
  }

  registerUser(userRequest: UserRequest): Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(this.AUTH_REGISTER_URL, userRequest);
  }
}
