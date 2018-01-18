import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { AppComponent } from './app.component';
import { HttpModule } from '@angular/http';
import { WelcomeComponent } from './welcome/welcome.component';
import { TablesService } from './tables.service';
import { DataTableComponent } from './data-table/data-table.component';
import { DataService } from './data.service';
import { KeysPipe } from './keys.pipe';

const appRoutes: Routes = [
  { path: '', component: WelcomeComponent },
  { path: 'data/:table_name', component: DataTableComponent },
];

@NgModule({
  declarations: [
    AppComponent,
    WelcomeComponent,
    DataTableComponent,
    KeysPipe
  ],
  imports: [
    HttpModule,
    BrowserModule,
    RouterModule.forRoot(
      appRoutes,
      { enableTracing: true } // <-- debugging purposes only
    ),
  ],
  providers: [TablesService, DataService],
  bootstrap: [AppComponent]
})
export class AppModule { }
