import { Injectable } from '@angular/core';
import { AuthenticationService } from '../service/authentication.service';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {

  constructor(private authenticationService: AuthenticationService) {}

  intercept(httpRequest: HttpRequest<any>, httpHandler: HttpHandler): Observable<HttpEvent<unknown>> {
    if (httpRequest.url.includes('api/') || httpRequest.url.includes('user/login')) {
      return httpHandler.handle(httpRequest);
    }
    this.authenticationService.loadToken();
    const token = this.authenticationService.getToken();
    const request = httpRequest.clone({setHeaders: { Authorization: `Bearer ${token}` }});
    return httpHandler.handle(request);
  }
}
