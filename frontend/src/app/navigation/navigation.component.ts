import {Component, OnInit} from '@angular/core';
import {ActiveUserService} from "../shared/active-user.service";

@Component({
  selector: 'app-navigation',
  templateUrl: './navigation.component.html',
  styleUrls: ['./navigation.component.css']
})
export class NavigationComponent implements OnInit {

  navbarTitle = "Microservices Demo";

  constructor(
    public activeUserService: ActiveUserService
  ) {}

  ngOnInit(): void {
  }

}
