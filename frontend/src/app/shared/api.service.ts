import { Injectable } from '@angular/core';
import {HttpClient, HttpHeaders} from "@angular/common/http";
import { Observable } from "rxjs";
import { TokenResponse } from "../login/model/token-response";
import { UserCredentials } from "../login/model/user-credentials";
import {Message} from "../feed/model/message";

@Injectable({
  providedIn: 'root'
})
export class ApiService {
  private PORT = 8081
  private HOST = "http://localhost"
  private API_BASE = "/api"
  private API_VERSION = "/v1"
  private URL_BASE = this.HOST + ":" + this.PORT + this.API_BASE + this.API_VERSION

  private AUTH_BASE = this.URL_BASE + "/auth"
  private MESSAGE_BASE = this.URL_BASE + "/messages"
  private MESSAGE_GET_ALL_URL = this.URL_BASE + "/messages/all"


  constructor(private httpClient: HttpClient) {

  }

  getAccessToken(credentials: UserCredentials) : Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(this.AUTH_BASE, credentials);
  }

  getAllMessages() : Observable<Message[]> {
    return this.httpClient.get<Message[]>(this.MESSAGE_GET_ALL_URL);
  }

  postMessage(message: Message, accessToken: string) : Observable<Message> {
    return this.httpClient.post<Message>(this.MESSAGE_BASE, message, ApiService.getAuthorizationRequestOptions(accessToken));
  }

  private static getAuthorizationHeader(token: string) : object {
    return new HttpHeaders({
      'Content-Type': 'application/json',
      'Authorization': `Bearer ${token}`
    });
  }

  private static getAuthorizationRequestOptions(token: string) : object {
    const header = {headers: ApiService.getAuthorizationHeader(token)};
    return {headers: header};
  }
}
