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

  languages: ILanguage[] = [];
  frameworks: IFramework[] = [];
  topics: ITopic[] = [];

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<IFramework[]>("/api/v1/frameworks")
      .subscribe((frameworks: IFramework[]) => this.frameworks = frameworks);
    this.http.get<ILanguage[]>("/api/v1/languages")
      .subscribe((languages: ILanguage[]) => this.languages = languages);
    this.http.get<ITopic[]>("/api/v1/topics")
      .subscribe((topics: ITopic[]) => this.topics = topics);
  }

  concatenateLanguage(language: string) : string {
      return "programming lang-" + language.toLowerCase();
  }

}
