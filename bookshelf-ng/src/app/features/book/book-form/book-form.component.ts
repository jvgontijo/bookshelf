import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { ActivatedRoute, Router } from '@angular/router';

import { BookService } from '../../../core/services/book.service';
import { SubjectService } from '../../../core/services/subject.service';
import { AuthorService } from '../../../core/services/author.service';

import { Subject } from '../../../core/models/subject.model';
import { Author } from '../../../core/models/author.model';
import { Book } from '../../../core/models/book.model';
import { BookRequest } from '../../../core/models/book-request.model';

@Component({
	selector: 'app-book-form',
	standalone: true,
	imports: [
		CommonModule,
		ReactiveFormsModule
	],
	templateUrl: './book-form.component.html'
})
export class BookFormComponent implements OnInit {

	form!: FormGroup;
	subjects: Subject[] = [];
	authors: Author[] = [];
	bookId: number | null = null;
	title = 'Novo livro';
	submitted = false;

	constructor(
		private readonly fb: FormBuilder,
		private readonly route: ActivatedRoute,
		private readonly router: Router,
		private readonly bookService: BookService,
		private readonly subjectService: SubjectService,
		private readonly authorService: AuthorService
	) {}

	ngOnInit(): void {
		this.buildForm();
		this.loadSubjects();
		this.loadAuthors();

		const idParam = this.route.snapshot.paramMap.get('id');
		if (idParam) {
			this.bookId = Number(idParam);
			this.title = 'Editar livro';
			this.loadBook(this.bookId);
		}
	}

	private buildForm(): void {
		this.form = this.fb.group({
			title: ['', [Validators.required, Validators.maxLength(255)]],
			publicationYear: [undefined],
			price: [undefined, [Validators.required, Validators.min(0)]],
			subjectId: [undefined, Validators.required],
			authorIds: [[], Validators.required]
		});
	}

	private loadSubjects(): void {
		this.subjectService.findAll().subscribe({
			next: subjects => this.subjects = subjects,
			error: () => window.alert('Erro ao carregar assuntos.')
		});
	}

	private loadAuthors(): void {
		this.authorService.findAll().subscribe({
			next: authors => this.authors = authors,
			error: () => window.alert('Erro ao carregar autores.')
		});
	}

	private loadBook(id: number): void {
		this.bookService.findById(id).subscribe({
			next: (book: Book) => {
				this.form.patchValue({
					title: book.title,
					publicationYear: book.publicationYear,
					price: book.price,
					subjectId: book.subject.id,
					authorIds: book.authors.map(a => a.id)
				});
			},
			error: () => window.alert('Erro ao carregar livro.')
		});
	}

	onSubmit(): void {
		this.submitted = true;

		if (this.form.invalid) {
			return;
		}

		const value = this.form.value as BookRequest;

		const payload: BookRequest = {
			title: value.title,
			publicationYear: value.publicationYear ?? null,
			price: value.price,
			subjectId: value.subjectId,
			authorIds: value.authorIds
		};

		if (this.bookId !== null) {
			this.bookService.update(this.bookId, payload).subscribe({
				next: () => this.router.navigate(['/books']),
				error: () => window.alert('Erro ao atualizar livro.')
			});
		} else {
			this.bookService.create(payload).subscribe({
				next: () => this.router.navigate(['/books']),
				error: () => window.alert('Erro ao criar livro.')
			});
		}
	}

	onCancel(): void {
		this.router.navigate(['/books']);
	}

	hasError(field: string, error: string): boolean {
		const control = this.form.get(field);
		if (!control) {
			return false;
		}
		return Boolean(control.errors?.[error] && (control.dirty || control.touched || this.submitted));
	}
}
