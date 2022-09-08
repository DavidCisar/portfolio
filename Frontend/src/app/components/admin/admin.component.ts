import { Component, OnInit, Input} from '@angular/core';
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

  edit: boolean = false;
  create: boolean = false;
  updateFrameworks: boolean = false;
  updateLanguages: boolean = false;
  updateTopics: boolean = false;
  updateLanguage: boolean = false;
  updateTopic: boolean = false;
  projectDTO: IProject;
  languageDTO: ILanguage;
  frameworkDTO: IFramework;
  topicDTO: ITopic;

  @Input()
  content: string [] = ['Projects', 'Languages', 'Frameworks', 'Topics', 'Tools']
  selectedContent: string = "Projects";

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<IProject[]>('/api/v1/projects')
          .subscribe((projects: IProject[]) => this.projects = projects)

        this.http.get<ILanguage[]>('/api/v1/languages')
          .subscribe((languages: ILanguage[]) => this.languages = languages)

        this.http.get<IFramework[]>('/api/v1/frameworks')
          .subscribe((frameworks: IFramework[]) => this.frameworks = frameworks)

        this.http.get<ITopic[]>('/api/v1/topics')
          .subscribe((topics: ITopic[]) => this.topics = topics)
  }

  createContent() {
    this.edit = false;
    this.create = true;
    this.projectDTO = {
      name : "",
      description : "",
      projectContext : "",
      website : "",
      languagesInProject : [],
      frameworksInProject : [],
      topicsInProject : []
    };
    this.languageDTO = {
      name : "",
      description : "",
      version : "",
      documentation : ""
    };
    this.frameworkDTO = {
      name : "",
      description : "",
      version : "",
      documentation : "",
      languageDTO : {
        name : "",
        description : "",
        version : "",
        documentation : ""
      }
    };
    this.topicDTO = {
      name : ""
    };
  }

  editProject(project: IProject) {
    this.edit = true;
    this.create = false;
    this.projectDTO = project;
  }

  editLanguage(language: ILanguage) {
    this.edit = true;
    this.create = false;
    this.languageDTO = language;
  }

  editFramework(framework: IFramework) {
      this.edit = true;
      this.create = false;
      this.frameworkDTO = framework;
  }

  editTopic(topic: ITopic) {
    this.edit = true;
    this.create = false;
    this.topicDTO = topic;
  }

  updateFrameworksInProject() {
    this.updateFrameworks = !this.updateFrameworks;
  }

  updateLanguagesInProject() {
      this.updateLanguages = !this.updateLanguages;
  }

  updateTopicsInProject() {
      this.updateTopics = !this.updateTopics;
  }

  updateLanguageInFramework() {
    this.updateLanguage = !this.updateLanguage;
  }

  addFrameworkToProject(framework: IFramework) {
    this.projectDTO.frameworksInProject!.push(framework);
  }

  addLanguageToProject(language: ILanguage) {
      this.projectDTO.languagesInProject!.push(language);
  }

  addTopicToProject(topic: ITopic) {
        this.projectDTO.topicsInProject!.push(topic);
  }

  setLanguageInFramework(language: ILanguage) {
    this.frameworkDTO.languageDTO = language;
  }

  dropFramework(frameworkToBeRemoved: IFramework) {
      this.projectDTO.frameworksInProject = this.projectDTO.frameworksInProject!.filter(framework => framework != frameworkToBeRemoved);
  }

  dropLanguage(languageToBeRemoved: ILanguage) {
      this.projectDTO.languagesInProject = this.projectDTO.languagesInProject!.filter(language => language != languageToBeRemoved);
  }

  dropTopic(topicToBeRemoved: ITopic) {
        this.projectDTO.topicsInProject = this.projectDTO.topicsInProject!.filter(topic => topic != topicToBeRemoved);
  }

  deleteProject(project: IProject) {
    let url = "/admin/deleteProject/" + project.id;
    this.http.delete(url, {responseType: 'text'})
      .subscribe(() => {
        this.http.get<IProject[]>("/api/v1/projects")
          .subscribe((projects: IProject[]) => this.projects = projects)
        })
  }

  deleteLanguage(language: ILanguage) {
      let url = "/admin/deleteLanguage/" + language.id;
      this.http.delete(url, {responseType: 'text'})
        .subscribe(() => {
          this.http.get<IProject[]>("/api/v1/projects")
            .subscribe((projects: IProject[]) => this.projects = projects);

          this.http.get<ILanguage[]>("/api/v1/languages")
            .subscribe((languages: ILanguage[]) => this.languages = languages);
          })
  }

  deleteFramework(framework: IFramework) {
        let url = "/admin/deleteFramework/" + framework.id;
        this.http.delete(url, {responseType: 'text'})
          .subscribe(() => {
            this.http.get<IProject[]>("/api/v1/projects")
              .subscribe((projects: IProject[]) => this.projects = projects);

            this.http.get<IFramework[]>("/api/v1/frameworks")
              .subscribe((frameworks: IFramework[]) => this.frameworks = frameworks);
            })
  }

  deleteTopic(topic: ITopic) {
          let url = "/admin/deleteTopic/" + topic.id;
          this.http.delete(url, {responseType: 'text'})
            .subscribe(() => {
              this.http.get<IProject[]>("/api/v1/projects")
                .subscribe((projects: IProject[]) => this.projects = projects);

              this.http.get<ITopic[]>("/api/v1/topics")
                .subscribe((topics: ITopic[]) => this.topics = topics);
              })
    }

  close() {
    this.edit = false;
    this.create = false;
    this.updateFrameworks = false;
    this.updateLanguage = false;
    this.projectDTO = {
          name : "",
          description : "",
          projectContext : "",
          website : "",
          languagesInProject : [],
          frameworksInProject : [],
          topicsInProject : []
    };
    this.languageDTO = {
          name : "",
          description : "",
          version : "",
          documentation : ""
    };
    this.frameworkDTO = {
          name : "",
          description : "",
          version : "",
          documentation : "",
          languageDTO : {
            name : "",
            description : "",
            version : "",
            documentation : ""
          }
    };
    this.topicDTO = {
          name : ""
    };
  }

  saveProject() {
    if (this.edit) {
      this.edit = false;
      this.updateFrameworks = false;
      this.http.put("/admin/updateProject", this.projectDTO, {responseType: 'text'})
        .subscribe( () => {
          this.close();
          this.http.get<IProject[]>("/api/v1/projects")
            .subscribe((projects: IProject[]) => this.projects = projects)
          }
        )
    } else {
      this.create = false;
      this.updateFrameworks = false;
      console.log("Project gets created....")
      this.http.post("admin/createProject", this.projectDTO, {responseType: 'text'})
        .subscribe(() => {
          this.close();
          this.http.get<IProject[]>("/api/v1/projects")
            .subscribe((projects: IProject[]) => this.projects = projects)
        })
    }

  }

  saveLanguage() {
      if (this.edit) {
        this.edit = false;
        this.http.put("/admin/updateLanguage", this.languageDTO, {responseType: 'text'})
          .subscribe( () => {
            this.close();
            this.http.get<ILanguage[]>("/api/v1/languages")
              .subscribe((languages: ILanguage[]) => this.languages = languages)
            }
          )
      } else {
        this.create = false;
        this.http.post("admin/createLanguage", this.languageDTO, {responseType: 'text'})
          .subscribe(() => {
            this.close();
            this.http.get<ILanguage[]>("/api/v1/languages")
              .subscribe((languages: ILanguage[]) => this.languages = languages)
          })
      }

  }

  saveFramework() {
        if (this.frameworkDTO.languageDTO.name === '') {
          console.error('Please provide a name for the language!');
        } else if (this.edit) {
          this.edit = false;
          this.updateLanguage = false;
          this.http.put("/admin/updateFramework", this.frameworkDTO, {responseType: 'text'})
            .subscribe( () => {
              this.close();
              this.http.get<IFramework[]>("/api/v1/frameworks")
                .subscribe((frameworks: IFramework[]) => this.frameworks = frameworks)
              }
            )
        } else {
          this.create = false;
          this.updateLanguage = false;
          this.http.post("admin/createFramework", this.frameworkDTO, {responseType: 'text'})
            .subscribe( () => {
              this.close();
              this.http.get<IFramework[]>("/api/v1/frameworks")
                .subscribe((frameworks: IFramework[]) => this.frameworks = frameworks)
              }
            )
        }

    }

    saveTopic() {
            if (this.edit) {
              this.edit = false;
              this.updateTopic = false;
              this.http.put("/admin/updateTopic", this.topicDTO, {responseType: 'text'})
                .subscribe( () => {
                  this.close();
                  this.http.get<ITopic[]>("/api/v1/topics")
                    .subscribe((topics: ITopic[]) => this.topics = topics)
                  }
                )
            } else {
              this.create = false;
              this.updateTopic = false;
              this.http.post("admin/createTopic", this.topicDTO, {responseType: 'text'})
                .subscribe( () => {
                  this.close();
                  this.http.get<ITopic[]>("/api/v1/topics")
                    .subscribe((topics: ITopic[]) => this.topics = topics)
                  }
                )
            }

        }

}
