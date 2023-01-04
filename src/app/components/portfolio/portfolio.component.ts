import { Component, OnInit, Input} from '@angular/core';

import Projects from './Projects';

@Component({
  selector: 'app-portfolio',
  templateUrl: './portfolio.component.html',
  styleUrls: ['./portfolio.component.css']
})
export class PortfolioComponent implements OnInit {

  public projects: any;

  constructor() {
    this.projects = Projects;
  }

  ngOnInit(): void {}

}
