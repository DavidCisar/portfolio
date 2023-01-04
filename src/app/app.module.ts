import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';

import { BrowserAnimationsModule } from '@angular/platform-browser/animations';

import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';

import { PortfolioComponent } from './components/portfolio/portfolio.component';
import { SkillsComponent } from './components/skills/skills.component';
import { AboutComponent } from './components/about/about.component';
import { HomeComponent } from './components/home/home.component';
import { RoomComponent } from './components/room/room.component';
import { PrivacyComponent } from './components/privacy/privacy.component';
import { ImprintComponent } from './components/imprint/imprint.component';
import { BtfComponent } from './components/portfolio/btf/btf.component';
import { CdComponent } from './components/portfolio/cd/cd.component';
import { PbtfComponent } from './components/portfolio/pbtf/pbtf.component';
import { PwComponent } from './components/portfolio/pw/pw.component';
import { SrpComponent } from './components/portfolio/srp/srp.component';

@NgModule({
  declarations: [
    AppComponent,
    PortfolioComponent,
    SkillsComponent,
    AboutComponent,
    HomeComponent,
    RoomComponent,
    PrivacyComponent,
    ImprintComponent,
    BtfComponent,
    CdComponent,
    PbtfComponent,
    PwComponent,
    SrpComponent
  ],
  imports: [
    BrowserModule,
    BrowserAnimationsModule,
    AppRoutingModule,
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule {}
