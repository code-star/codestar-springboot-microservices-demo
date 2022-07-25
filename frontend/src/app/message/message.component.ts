import {Component, Input, OnInit} from '@angular/core';

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

  constructor() {
  }

  ngOnInit(): void {}

  setText(title: string, body: string): void {
    this.messageTitle = title;
    this.messageBody = body;
  }

}
