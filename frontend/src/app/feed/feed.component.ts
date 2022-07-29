import {Component, OnInit} from '@angular/core';
import {Message} from "./model/message";
import {ApiService} from "../shared/api.service";

@Component({
  selector: 'app-feed',
  templateUrl: './feed.component.html',
  styleUrls: ['./feed.component.css']
})
export class FeedComponent implements OnInit {
  messages: Message[] = [];

  constructor(
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.getAllMessages();
  }

  getAllMessages() : void {
    this.apiService.getAllMessages().subscribe({
      next: mgs => this.messages = mgs,
      error: err => console.error(err)
    })
  }

}
