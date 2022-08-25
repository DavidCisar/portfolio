import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

import { IProject } from 'src/app/model/project';
import { ILanguage }  from 'src/app/model/language';
import { IFramework } from 'src/app/model/framework';
import { ITopic } from 'src/app/model/topic';

@Component({
  selector: 'app-admin',
  templateUrl: './admin.component.html',
  styleUrls: ['./admin.component.css']
})
export class AdminComponent implements OnInit {

  projects: IProject[] = [];
  languages: ILanguage[] = [];
  frameworks: IFramework[] = [];
  topics: ITopic[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {

    this.http.get<IProject[]>('api/v1/projects')
      .subscribe((projects: IProject[]) => this.projects = projects)

    this.http.get<ILanguage[]>('api/v1/languages')
      .subscribe((languages: ILanguage[]) => this.languages = languages)

    this.http.get<IFramework[]>('api/v1/frameworks')
      .subscribe((frameworks: IFramework[]) => this.frameworks = frameworks)

    this.http.get<ITopic[]>('api/v1/topics')
      .subscribe((topics: ITopic[]) => this.topics = topics)
  }

}
