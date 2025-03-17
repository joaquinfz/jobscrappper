import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import { JobListComponent } from './components/job-list/job-list.component';
import { HttpClientModule } from '@angular/common/http';

@Component({
selector: 'app-root',
standalone: true, // Ensure this is a standalone component
  imports: [HttpClientModule, JobListComponent],
template: `<app-job-list></app-job-list>`, // Remove templateUrl to avoid conflict
  styleUrls: ['./app.component.css'] // Fix styleUrl -> styleUrls
})
export class AppComponent {
  title = 'angular';
}
