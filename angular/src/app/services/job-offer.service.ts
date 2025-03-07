import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class JobOfferService {
  private baseUrl = 'http://localhost:8080/api/offers';

  constructor(private http: HttpClient) {}

  // Get job offers with required search parameter
  getJobOffers(search: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}?search=${search}`);
  }

  // Get job offers by skills and search term
  getJobOffersBySkills(search: string, skills: string[]): Observable<any[]> {
    return this.http.get<any[]>(`${this.baseUrl}/skills/${skills.join(',')}?search=${search}`);
  }
}

