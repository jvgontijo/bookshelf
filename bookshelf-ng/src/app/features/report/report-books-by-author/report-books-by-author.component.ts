import { Component } from '@angular/core';

import { ReportService } from '../../../core/services/report.service';
import { CommonModule } from '@angular/common';

@Component({
	selector: 'app-report-books-by-author',
	standalone: true,
	templateUrl: './report-books-by-author.component.html',
	imports: [CommonModule]
})
export class ReportBooksByAuthorComponent {
	constructor(private readonly reportService: ReportService) {}

	onView(): void {
		this.reportService.openBooksByAuthorView();
	}

	onDownload(): void {
		this.reportService.downloadBooksByAuthorPdf();
	}
}
