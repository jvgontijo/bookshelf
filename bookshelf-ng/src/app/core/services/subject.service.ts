import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Subject } from '../models/subject.model';

@Injectable({
	providedIn: 'root'
})
export class SubjectService {

	private readonly baseUrl = 'http://localhost:8080/api/subjects';

	constructor(
		private readonly http: HttpClient
	) {}

	findAll(): Observable<Subject[]> {
		return this.http.get<Subject[]>(this.baseUrl);
	}

	findById(id: number): Observable<Subject> {
		return this.http.get<Subject>(`${this.baseUrl}/${id}`);
	}

	create(payload: { description: string }): Observable<Subject> {
		return this.http.post<Subject>(this.baseUrl, payload);
	}

	update(id: number, payload: { description: string }): Observable<Subject> {
		return this.http.put<Subject>(`${this.baseUrl}/${id}`, payload);
	}

	delete(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}
}
