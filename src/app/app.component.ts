import { Component, AfterViewInit } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.css']
})
export class AppComponent {

  public arrow: HTMLElement;

  constructor() {}

  ngAfterViewInit() {
    this.arrow = document.getElementById('arrow') as HTMLElement;
    window.addEventListener('scroll', () => {
      let y = window.scrollY;
      if (y > 100 && this.arrow != null) {
        this.arrow.classList.add('disappear');
      } else {
        this.arrow.classList.remove('disappear');
      }
    })
  }

  scrollTo(element: any): void {
    (document.getElementById(element) as HTMLElement).scrollIntoView({behavior: "smooth", block: "start", inline: "nearest"});
  }

}
