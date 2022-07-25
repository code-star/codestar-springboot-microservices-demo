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
  private AUTH_PORT = 8081
  private MESSAGES_PORT = 8082
  private HOST = "http://localhost"
  private API_BASE = "/api"
  private API_VERSION = "/v1"
  private AUTH_URL_BASE = this.HOST + ":" + this.AUTH_PORT + this.API_BASE + this.API_VERSION
  private MESSAGE_URL_BASE = this.HOST + ":" + this.MESSAGES_PORT + this.API_BASE + this.API_VERSION

  private AUTHORIZE_URL = this.AUTH_URL_BASE + "/auth"
  private MESSAGE_POST_URL = this.MESSAGE_URL_BASE + "/messages"
  private MESSAGE_GET_ALL_URL = this.MESSAGE_URL_BASE + "/messages/all"


  constructor(private httpClient: HttpClient) {

  }

  getAccessToken(credentials: UserCredentials) : Observable<TokenResponse> {
    return this.httpClient.post<TokenResponse>(this.AUTHORIZE_URL, credentials);
  }

  getAllMessages() : Observable<Message[]> {
    return this.httpClient.get<Message[]>(this.MESSAGE_GET_ALL_URL);
  }

  postMessage(message: Message, accessToken: string) : Observable<Message> {
    return this.httpClient.post<Message>(this.MESSAGE_POST_URL, message, ApiService.getAuthorizationRequestOptions(accessToken));
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
