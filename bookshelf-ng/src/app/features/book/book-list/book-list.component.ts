import { Component, OnInit } from '@angular/core';
import { Router } from '@angular/router';
import { CommonModule } from '@angular/common';

import { BookService } from '../../../core/services/book.service';
import { Book } from '../../../core/models/book.model';

@Component({
	selector: 'app-book-list',
	standalone: true,
	imports: [CommonModule],
	templateUrl: './book-list.component.html'
})
export class BookListComponent implements OnInit {

	books: Book[] = [];
	loading = false;
	errorMessage = '';

	constructor(
		private readonly bookService: BookService,
		private readonly router: Router
	) {}

	ngOnInit(): void {
		this.loadBooks();
	}

	loadBooks(): void {
		this.loading = true;
		this.errorMessage = '';

		this.bookService.findAll().subscribe({
			next: books => {
				this.books = books;
				this.loading = false;
			},
			error: () => {
				this.errorMessage = 'Erro ao carregar livros.';
				this.loading = false;
			}
		});
	}

	onNew(): void {
		this.router.navigate(['/books/new']);
	}

	onEdit(book: Book): void {
		this.router.navigate(['/books', book.id, 'edit']);
	}

	onDelete(book: Book): void {
		const confirmed = window.confirm(`Deseja realmente excluir o livro "${book.title}"?`);
		if (!confirmed) {
			return;
		}

		this.bookService.delete(book.id).subscribe({
			next: () => this.loadBooks(),
			error: () => window.alert('Erro ao excluir livro.')
		});
	}
}
