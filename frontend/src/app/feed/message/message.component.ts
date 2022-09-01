import {Component, Input, OnInit} from '@angular/core';
import {ApiService} from "../../shared/api.service";

@Component({
  selector: 'app-message',
  templateUrl: './message.component.html',
  styleUrls: ['./message.component.css']
})
export class MessageComponent implements OnInit {

  @Input()
  messageTitle: string = '';
  @Input()
  messageBody: string = '';
  @Input()
  messageCreated: string = '';

  constructor(
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.resolveUserName()
  }

  resolveUserName() {
    this.apiService.getUserDetails(this.messageTitle).subscribe({
      next: value => this.messageTitle = value.username,
      error: err => console.error(err)
    })
  }

  getDateString() {
    const date = new Date(this.messageCreated);
    return date.toLocaleDateString() + " " + date.toLocaleTimeString()
  }

}
