import { Component, OnInit } from '@angular/core';
import {PostMessageService} from "./post-message.service";

@Component({
  selector: 'app-post-message',
  templateUrl: './post-message.component.html',
  styleUrls: ['./post-message.component.css']
})
export class PostMessageComponent implements OnInit {

  text: string = ''

  constructor(
    private postMessageService: PostMessageService
  ) { }

  ngOnInit(): void {
  }

  onSubmit() {
    this.postMessageService.postMessage(this.text)
    this.text = ''
  }

}
