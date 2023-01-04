import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JwtHelperService, JWT_OPTIONS } from "@auth0/angular-jwt";
import { IUser } from 'src/app/model/user';

@Injectable({
  providedIn: 'root'
})
export class AuthenticationService {

  private token: string;
  private loggedInUsername: string;

  constructor(private http: HttpClient, private jwtHelper: JwtHelperService) { }

  public login(userForm: string) {
    return this.http.post("/user/login", userForm, {observe: 'response'});
  }

  public logout() {
    this.token = '';
    this.loggedInUsername = '';
    localStorage.removeItem('user');
    localStorage.removeItem('token');
  }

  public saveToken(token: string) {
    this.token = token;
    localStorage.setItem('token', token);
  }

  public addUserToLocalCache(user: IUser) {
    localStorage.setItem('user', JSON.stringify(user));
  }

  public getUserFromLocalCache() : IUser {
    return JSON.parse(localStorage.getItem('user') as string);
  }

  public loadToken() {
    this.token = localStorage.getItem('token') as string;
  }

  public getToken() : string {
    return this.token as string;
  }

  public isLoggedIn() : boolean {
    this.loadToken();
    if (this.token != null && this.token !== ''){
      if (this.jwtHelper.decodeToken(this.token).sub != null || '')  {
        if (!this.jwtHelper.isTokenExpired(this.token)) {
          this.loggedInUsername = this.jwtHelper.decodeToken(this.token).sub;
          return true;
        }
      }
    }
    this.logout();
    return false;
  }

  public isAdmin(): boolean {
    this.loadToken();
    if (this.jwtHelper.isTokenExpired(this.token)) {
      return false;
    }
    if (this.jwtHelper.decodeToken(this.token).authorities.includes('admin:all')) {
      return true;
    }
    return false;
  }

}
