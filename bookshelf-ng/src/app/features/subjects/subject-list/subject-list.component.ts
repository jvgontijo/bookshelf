import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { SubjectService } from '../../../core/services/subject.service';
import { Subject } from '../../../core/models/subject.model';

@Component({
	selector: 'app-subject-list',
	standalone: true,
	imports: [CommonModule, RouterLink],
	templateUrl: './subject-list.component.html',
})
export class SubjectListComponent implements OnInit {
	subjects: Subject[] = [];
	loading = false;
	errorMessage = '';

	constructor(private readonly subjectService: SubjectService) {}

	ngOnInit(): void {
		this.loadSubjects();
	}

	loadSubjects(): void {
		this.loading = true;
		this.errorMessage = '';

		this.subjectService.findAll().subscribe({
			next: (subjects) => {
				this.subjects = subjects;
				this.loading = false;
			},
			error: () => {
				this.errorMessage =
					'Ocorreu um erro ao carregar os assuntos. Tente novamente.';
				this.loading = false;
			},
		});
	}

	onDelete(subject: Subject): void {
		if (!subject.id) {
			return;
		}

		const confirmed = window.confirm(
			`Tem certeza que deseja excluir o assunto "${subject.description}"?`,
		);

		if (!confirmed) {
			return;
		}

		this.subjectService.delete(subject.id).subscribe({
			next: () => this.loadSubjects(),
			error: () => {
				this.errorMessage =
					'Ocorreu um erro ao excluir o assunto. Tente novamente.';
			},
		});
	}
}
