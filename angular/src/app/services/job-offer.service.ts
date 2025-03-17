import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { JobOffer } from '../models/job-offer.model';

@Injectable({
providedIn: 'root'
})
export class JobOfferService {
private baseUrl = 'http://localhost:8080/joblist/offers';

  constructor(private http: HttpClient) {}

// Get job offers with required search parameter
  getJobOffers(search: string): Observable<JobOffer[]> {
return this.http.get<JobOffer[]>(`${this.baseUrl}?search=${search}`);
}

// Get job offers by skills and search term
  getJobOffersBySkills(search: string, skills: string[]): Observable<JobOffer[]> {
return this.http.get<JobOffer[]>(`${this.baseUrl}/skills/${skills.join(',')}?search=${search}`);
}
}
