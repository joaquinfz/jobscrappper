import { AppRoutingModule } from './app-routing.module';
import  { JobListComponent } from './components/job-list/job-list.component';
import { NgModule } from '@angular/core';
import { BrowserModule } from '@angular/platform-browser';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';

@NgModule({
declarations: [
AppComponent,
JobListComponent
],
imports: [
AppRoutingModule,
HttpClientModule,
FormsModule
  ],
bootstrap: [AppComponent]
})
export class AppModule {}
