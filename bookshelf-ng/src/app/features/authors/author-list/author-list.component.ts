import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { AuthorService } from '../../../core/services/author.service';
import { Author } from '../../../core/models/author.model';

@Component({
	selector: 'app-author-list',
	standalone: true,
	imports: [CommonModule, RouterLink],
	templateUrl: './author-list.component.html',
})
export class AuthorListComponent implements OnInit {
	authors: Author[] = [];
	loading = false;
	errorMessage = '';

	constructor(private readonly authorService: AuthorService) {}

	ngOnInit(): void {
		this.loadAuthors();
	}

	loadAuthors(): void {
		this.loading = true;
		this.errorMessage = '';

		this.authorService.findAll().subscribe({
			next: (authors) => {
				this.authors = authors;
				this.loading = false;
			},
			error: () => {
				this.errorMessage =
					'Ocorreu um erro ao carregar os autores. Tente novamente.';
				this.loading = false;
			},
		});
	}

	onDelete(author: Author): void {
		if (!author.id) {
			return;
		}

		const confirmed = window.confirm(
			`Tem certeza que deseja excluir o autor "${author.name}"?`,
		);

		if (!confirmed) {
			return;
		}

		this.authorService.delete(author.id).subscribe({
			next: () => this.loadAuthors(),
			error: () => {
				this.errorMessage =
					'Ocorreu um erro ao excluir o autor. Tente novamente.';
			},
		});
	}
}
