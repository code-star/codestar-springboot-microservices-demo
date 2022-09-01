import {NgModule} from '@angular/core';
import {BrowserModule} from '@angular/platform-browser';
import {RouterModule, Routes} from "@angular/router";
import {FormsModule} from "@angular/forms";
import {HTTP_INTERCEPTORS, HttpClientModule} from "@angular/common/http";

import {AppComponent} from './app.component';
import {NavigationComponent} from './navigation/navigation.component';
import {NotFoundComponent} from './not-found/not-found.component';
import {LoginComponent} from './login/login.component';
import {FeedComponent} from './feed/feed.component';
import {StatusComponent} from './status/status.component';
import {MessageComponent} from './feed/message/message.component';
import {RegisterComponent} from './register/register.component';

import {AuthInterceptor} from "./shared/auth.interceptor";
import { MeComponent } from './me/me.component';
import { PostMessageComponent } from './feed/post-message/post-message.component';

const appRoutes: Routes = [
  {
    path: '',
    component: FeedComponent,
    pathMatch: 'full'
  },
  {
    path: 'login',
    component: LoginComponent
  },
  {
    path: 'register',
    component: RegisterComponent
  },
  {
    path: 'me',
    component: MeComponent
  },
  {
    path: 'status',
    component: StatusComponent
  },
  {
    path: '**',
    component: NotFoundComponent
  }
]

@NgModule({
  declarations: [
    AppComponent,
    NavigationComponent,
    NotFoundComponent,
    LoginComponent,
    FeedComponent,
    StatusComponent,
    MessageComponent,
    RegisterComponent,
    MeComponent,
    PostMessageComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    FormsModule,
    RouterModule.forRoot(appRoutes)
  ],
  providers: [
    { provide: HTTP_INTERCEPTORS, useClass: AuthInterceptor, multi: true },
  ],
  bootstrap: [AppComponent]
})
export class AppModule { }
