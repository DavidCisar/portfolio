import { Component, OnInit } from '@angular/core';
import { HttpClient } from '@angular/common/http';

import { IRating } from 'src/app/model/rating';

@Component({
  selector: 'app-ratings',
  templateUrl: './ratings.component.html',
  styleUrls: ['./ratings.component.css']
})
export class RatingsComponent implements OnInit {

  ratings: IRating[] = [];
  ratingDTO : IRating = {
    rating : 0,
    message : '',
    userDTO : {
      firstName : '',
      lastName : '',
      username : '',
      email : ''
    }
  }

  constructor(private http: HttpClient) { }

  ngOnInit(): void {
    this.http.get<IRating[]>("/api/v1/ratings")
      .subscribe((ratings: IRating[]) => {
        this.ratings = ratings;
        for (let rating of this.ratings) {
          rating.stars = "✰".repeat(rating.rating);
        }
      });
  }

}
