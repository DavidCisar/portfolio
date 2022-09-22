import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.css']
})
export class LoginComponent implements OnInit {

  selectedPage = 'login';
  loginFormData : any;
  registerFormData : any;

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
  }

  changeCard() {
    if (this.selectedPage === 'login') {
      this.selectedPage = 'register';
    } else {
      this.selectedPage = 'login';
    }
  }

  login() {
    this.http.post('user/login', this.loginFormData)
  }

  register() {
    this.http.post('user/register', this.registerFormData)
  }

}
