import { Component } from '@angular/core';
import { JobOfferService } from '../../services/job-offer.service';
import { JobOffer } from '../../models/job-offer.model'; // Adjust the import path if necessary
import { CommonModule } from '@angular/common'
import { FormsModule } from '@angular/forms';

@Component({
selector: 'app-job-list',
standalone: true,
templateUrl: './job-list.component.html',
imports: [CommonModule, FormsModule],
providers: [JobOfferService],
styleUrls: ['./job-list.component.css']
})
export class JobListComponent {
jobOffers: JobOffer[] = [];
search: string = '';
skills: string = '';
selectedSkills: string[] = [];
sortAscending: boolean = true; // Property to toggle sorting order

  constructor(private jobOfferService: JobOfferService) {}

fetchJobOffers(): void {
if (!this.search.trim()) {
alert("Search parameter is required!");
return;
}

this.jobOfferService.getJobOffers(this.search).subscribe((data: JobOffer[]) => {
this.jobOffers = data;
this.sortJobOffers(); // Ensure job offers are sorted when fetched
      console.log('Job offers loaded:', this.jobOffers);
});
}

fetchJobOffersBySkills(): void {
if (!this.search.trim()) {
alert("Search parameter is required!");
return;
}

const skillArray = this.skills.split(',').map(skill => skill.trim()); // Convert input to array

    this.jobOfferService.getJobOffersBySkills(this.search, skillArray).subscribe((data: JobOffer[]) => {
this.jobOffers = data;
this.sortJobOffers(); // Ensure job offers are sorted when fetched
      console.log('Job offers by skills loaded:', this.jobOffers);
});
}

// Method to sort job offers based on salary
  sortJobOffers(): void {
this.jobOffers.sort((a, b) => {
return this.sortAscending
? Number(a.salary) - Number(b.salary)
: Number(b.salary) - Number(a.salary);
});
}

// Method to toggle the sorting order
  toggleSortOrder(): void {
this.sortAscending = !this.sortAscending;
this.sortJobOffers(); // Re-sort the job offers when the order is toggled
  }

// New method to process skills
  getProcessedSkills(skills: string): string[] {
return skills.split(',').map(skill => skill.trim());
}
}
