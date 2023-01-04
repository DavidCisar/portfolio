import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { ITopic } from 'src/app/model/topic';
import { IFramework } from 'src/app/model/framework';
import { ILanguage } from 'src/app/model/language';

@Component({
  selector: 'app-skills',
  templateUrl: './skills.component.html',
  styleUrls: ['./skills.component.css']
})
export class SkillsComponent implements OnInit {

  constructor(private http: HttpClient) { }

  ngOnInit(): void {}

}
