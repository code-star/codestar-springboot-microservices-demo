import {Component, Input, OnInit} from '@angular/core';
import {ApiService} from "../shared/api.service";

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

  constructor(
    private apiService: ApiService
  ) {}

  ngOnInit(): void {
    this.apiService.getUserDetails(this.messageTitle).subscribe({
      next: ans => this.messageTitle = ans.username,
      error: err => console.error(err)
    })
  }

  setText(title: string, body: string): void {
    this.messageTitle = title;
    this.messageBody = body;
  }

}
