import { NgModule } from '@angular/core';
import { AboutComponent } from './components/about/about.component';
import { HomeComponent } from './components/home/home.component';
import { PortfolioComponent } from './components/portfolio/portfolio.component';
import { SkillsComponent } from './components/skills/skills.component';
import { PrivacyComponent } from './components/privacy/privacy.component';
import { ImprintComponent } from './components/imprint/imprint.component';
import { BtfComponent } from './components/portfolio/btf/btf.component';
import { CdComponent } from './components/portfolio/cd/cd.component';
import { PbtfComponent } from './components/portfolio/pbtf/pbtf.component';
import { PwComponent } from './components/portfolio/pw/pw.component';
import { SrpComponent } from './components/portfolio/srp/srp.component';
import { RouterModule, Routes } from '@angular/router';

const routes: Routes = [
  { path: '', component: HomeComponent, pathMatch: 'full' },
  { path: 'about', component: AboutComponent },
  { path: 'skills', component: SkillsComponent },
  { path: 'imprint', component: ImprintComponent },
  { path: 'privacy', component: PrivacyComponent},
  { path: 'portfolio', component: PortfolioComponent, pathMatch: 'full' },
  { path: 'portfolio/btf', component: BtfComponent },
  { path: 'portfolio/cd', component: CdComponent },
  { path: 'portfolio/pbtf', component: PbtfComponent },
  { path: 'portfolio/pw', component: PwComponent },
  { path: 'portfolio/srp', component: SrpComponent }
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
