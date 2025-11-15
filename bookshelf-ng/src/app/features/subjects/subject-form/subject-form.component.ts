import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
	FormBuilder,
	FormGroup,
	ReactiveFormsModule,
	Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { SubjectService } from '../../../core/services/subject.service';
import { Subject } from '../../../core/models/subject.model';

@Component({
	selector: 'app-subject-form',
	standalone: true,
	imports: [CommonModule, ReactiveFormsModule, RouterLink],
	templateUrl: './subject-form.component.html',
})
export class SubjectFormComponent implements OnInit {
	form: FormGroup;
	title = 'Novo assunto';
	submitting = false;
	subjectId?: number;

	constructor(
		private readonly fb: FormBuilder,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly subjectService: SubjectService,
	) {
		this.form = this.fb.group({
			description: ['', [Validators.required, Validators.maxLength(100)]],
		});
	}

	ngOnInit(): void {
		const idParam = this.route.snapshot.paramMap.get('id');

		if (idParam) {
			this.subjectId = Number(idParam);

			if (!Number.isNaN(this.subjectId)) {
				this.title = 'Editar assunto';
				this.loadSubject(this.subjectId);
			}
		}
	}

	private loadSubject(id: number): void {
		this.subjectService.findById(id).subscribe({
			next: (subject) => {
				this.form.patchValue({
					description: subject.description,
				});
			},
			error: () => {
				this.router.navigate(['/subjects']);
			},
		});
	}

	onSubmit(): void {
		if (this.form.invalid) {
			this.form.markAllAsTouched();
			return;
		}

		this.submitting = true;

		const payload: Subject = {
			description: this.form.value.description,
		};

		if (this.subjectId) {
			this.subjectService.update(this.subjectId, payload).subscribe({
				next: () => this.onSuccess(),
				error: () => this.onError(),
			});
			return;
		}

		this.subjectService.create(payload).subscribe({
			next: () => this.onSuccess(),
			error: () => this.onError(),
		});
	}

	private onSuccess(): void {
		this.submitting = false;
		this.router.navigate(['/subjects']);
	}

	private onError(): void {
		this.submitting = false;
	}
}
