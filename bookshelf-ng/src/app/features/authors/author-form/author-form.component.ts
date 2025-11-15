import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
	FormBuilder,
	FormGroup,
	ReactiveFormsModule,
	Validators,
} from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AuthorService } from '../../../core/services/author.service';
import { Author } from '../../../core/models/author.model';

@Component({
	selector: 'app-author-form',
	standalone: true,
	imports: [CommonModule, ReactiveFormsModule, RouterLink],
	templateUrl: './author-form.component.html',
})
export class AuthorFormComponent implements OnInit {
	form: FormGroup;
	title = 'Novo autor';
	submitting = false;
	authorId?: number;

	constructor(
		private readonly fb: FormBuilder,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly authorService: AuthorService,
	) {
		this.form = this.fb.group({
			name: ['', [Validators.required, Validators.maxLength(100)]],
		});
	}

	ngOnInit(): void {
		const idParam = this.route.snapshot.paramMap.get('id');

		if (idParam) {
			this.authorId = Number(idParam);

			if (!Number.isNaN(this.authorId)) {
				this.title = 'Editar autor';
				this.loadAuthor(this.authorId);
			}
		}
	}

	private loadAuthor(id: number): void {
		this.authorService.findById(id).subscribe({
			next: (author) => {
				this.form.patchValue({
					name: author.name,
				});
			},
			error: () => {
				// Se der erro ao carregar, volta pra lista
				this.router.navigate(['/authors']);
			},
		});
	}

	onSubmit(): void {
		if (this.form.invalid) {
			this.form.markAllAsTouched();
			return;
		}

		this.submitting = true;

		const payload: Author = {
			name: this.form.value.name,
		};

		// Edição
		if (this.authorId) {
			this.authorService.update(this.authorId, payload).subscribe({
				next: () => this.onSuccess(),
				error: () => this.onError(),
			});
			return;
		}

		// Criação
		this.authorService.create(payload).subscribe({
			next: () => this.onSuccess(),
			error: () => this.onError(),
		});
	}

	private onSuccess(): void {
		this.submitting = false;
		this.router.navigate(['/authors']);
	}

	private onError(): void {
		this.submitting = false;
		// Aqui você pode exibir um toast ou alert, se quiser
	}
}
