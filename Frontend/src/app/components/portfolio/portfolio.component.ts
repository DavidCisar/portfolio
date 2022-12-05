import { Component, OnInit, Input} from '@angular/core';
import { HttpClient } from '@angular/common/http';

import Projects from './Projects';

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit {

  public projects: any;

  constructor(private http: HttpClient) {
    this.projects = Projects;
  }

  ngOnInit(): void {}

}
