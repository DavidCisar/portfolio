import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { IReference } from 'src/app/model/reference';

@Component({
  selector: 'app-references',
  templateUrl: './references.component.html',
  styleUrls: ['./references.component.css']
})
export class ReferenceComponent implements OnInit {

  references: IReference[] = [];
  referenceDTO : IReference = {
    message : '',
    name: '',
    link: ''
  }

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<IReference[]>("/api/v1/references")
      .subscribe((references: IReference[]) => this.references = references)
  }

}
