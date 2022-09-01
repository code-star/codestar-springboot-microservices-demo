import { Injectable } from '@angular/core';
import {Observable, Subject} from "rxjs";

@Injectable({
  providedIn: 'root'
})
export class FeedService {

  private readonly subject: Subject<void>

  constructor() {
    this.subject = new Subject<void>()
  }

  updateFeed() {
    this.subject.next()
  }

  onUpdateFeed(): Observable<void> {
    return this.subject.asObservable()
  }

}
