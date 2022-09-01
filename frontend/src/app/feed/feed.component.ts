import {Component, OnInit} from '@angular/core';
import {Message} from "./model/message";
import {ApiService} from "../shared/api.service";
import {ActiveUserService} from "../shared/active-user.service";
import {FeedService} from "./feed.service";

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})
export class FeedComponent implements OnInit {

  messages: Message[] = [];

  constructor(
    public activeUserService: ActiveUserService,
    private apiService: ApiService,
    private feedService: FeedService
  ) {}

  ngOnInit(): void {
    this.feedService.onUpdateFeed().subscribe({
      next: () => this.getAllMessages(),
      error: err => console.error(err)
    })

    this.feedService.updateFeed()
  }

  getAllMessages() : void {
    this.apiService.getAllMessages().subscribe({
      next: mgs => this.messages = mgs,
      error: err => console.error(err)
    })
  }
}
