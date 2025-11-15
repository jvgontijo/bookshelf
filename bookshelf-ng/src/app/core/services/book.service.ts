import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Book } from '../models/book.model';
import { BookRequest } from '../models/book-request.model';

@Injectable({
	providedIn: 'root'
})
export class BookService {

	private readonly baseUrl = 'http://localhost:8080/api/books';

	constructor(
		private readonly http: HttpClient
	) {}

	findAll(): Observable<Book[]> {
		return this.http.get<Book[]>(this.baseUrl);
	}

	findById(id: number): Observable<Book> {
		return this.http.get<Book>(`${this.baseUrl}/${id}`);
	}

	create(payload: BookRequest): Observable<Book> {
		return this.http.post<Book>(this.baseUrl, payload);
	}

	update(id: number, payload: BookRequest): Observable<Book> {
		return this.http.put<Book>(`${this.baseUrl}/${id}`, payload);
	}

	delete(id: number): Observable<void> {
		return this.http.delete<void>(`${this.baseUrl}/${id}`);
	}
}
