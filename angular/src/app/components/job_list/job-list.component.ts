import { Component } from '@angular/core';
import { JobOfferService } from '../../services/job-offer.service';

@Component({
  selector: 'app-job-list',
  templateUrl: './job-list.component.html',
  styleUrls: ['./job-list.component.css']
})
export class JobListComponent {
  jobOffers: any[] = [];
  search: string = '';
  skills: string = ''; // Comma-separated input (e.g., "Java,Angular")

  constructor(private jobOfferService: JobOfferService) {}

  fetchJobOffers(): void {
    if (!this.search.trim()) {
      alert("Search parameter is required!");
      return;
    }

    this.jobOfferService.getJobOffers(this.search).subscribe(data => {
      this.jobOffers = data;
      console.log('Job offers loaded:', this.jobOffers);
    });
  }

  fetchJobOffersBySkills(): void {
    if (!this.search.trim()) {
      alert("Search parameter is required!");
      return;
    }

    const skillArray = this.skills.split(',').map(skill => skill.trim()); // Convert input to array

    this.jobOfferService.getJobOffersBySkills(this.search, skillArray).subscribe(data => {
      this.jobOffers = data;
      console.log('Job offers by skills loaded:', this.jobOffers);
    });
  }
}
