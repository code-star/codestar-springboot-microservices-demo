import { Injectable } from '@angular/core';
import {
  HttpRequest,
  HttpHandler,
  HttpEvent,
  HttpInterceptor
} from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor() {}

  intercept(request: HttpRequest<unknown>, next: HttpHandler): Observable<HttpEvent<unknown>> {
    const token = localStorage.getItem('access-token');

    if (!token) {
      return next.handle(request);
    }

    const authReq = request.clone({ setHeaders: { Authorization: `Bearer ${token}` } });

    return next.handle(authReq);
  }
}
