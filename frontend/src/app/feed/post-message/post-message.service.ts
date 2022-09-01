import { Injectable } from '@angular/core';
import {ApiService} from "../../shared/api.service";
import {ActiveUserService} from "../../shared/active-user.service";
import { Content } from '../model/content';
import {FeedService} from "../feed.service";

@Injectable({
  providedIn: 'root'
})
export class PostMessageService {

  constructor(
    private apiService: ApiService,
    private activeUserService: ActiveUserService,
    private feedService: FeedService
  ) { }

  postMessage(text: string) {
    console.log("Posting message:", text);
    let message: Content = { content: text }
    let token = this.activeUserService.getToken()
    if (token !== null) {
      this.apiService.postMessage(message, token).subscribe({
        next: () => this.feedService.updateFeed(),
        error: err => console.error(err)
      })
    } else {
      console.error("Token is null, can't post message! Logging out...")
      this.activeUserService.logOut()
    }
  }
}
