import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Author } from '../models/author.model';

@Injectable({
	providedIn: 'root'
})
export class AuthorService {

	private readonly baseUrl = 'http://localhost:8080/api/authors';

	constructor(
		private readonly http: HttpClient
	) {}

	findAll(): Observable<Author[]> {
		return this.http.get<Author[]>(this.baseUrl);
	}

	findById(id: number): Observable<Author> {
		return this.http.get<Author>(`${this.baseUrl}/${id}`);
	}

	create(payload: { name: string }): Observable<Author> {
		return this.http.post<Author>(this.baseUrl, payload);
	}

	update(id: number, payload: { name: string }): Observable<Author> {
		return this.http.put<Author>(`${this.baseUrl}/${id}`, payload);
	}

	delete(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}
}
