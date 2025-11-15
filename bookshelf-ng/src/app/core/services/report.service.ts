import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
	providedIn: 'root',
})
export class ReportService {
	private readonly baseUrl = 'http://localhost:8080/api/reports/books-by-author';

	constructor(private readonly http: HttpClient) {}

	openBooksByAuthorView(): void {
		window.open(`${this.baseUrl}/view`, '_blank');
	}

	downloadBooksByAuthorPdf(): void {
		const url = `${this.baseUrl}/pdf`;

		this.http
			.get(url, { responseType: 'blob' })
			.subscribe((blob) => {
				const objectUrl = URL.createObjectURL(blob);

				const a = document.createElement('a');
				a.href = objectUrl;
				a.download = 'books-by-author.pdf';
				a.click();

				URL.revokeObjectURL(objectUrl);
			});
	}
}
