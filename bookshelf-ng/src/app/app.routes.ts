import { Routes } from '@angular/router';
import { BookFormComponent } from './features/book/book-form/book-form.component';
import { BookListComponent } from './features/book/book-list/book-list.component';
import { AuthorFormComponent } from './features/authors/author-form/author-form.component';
import { AuthorListComponent } from './features/authors/author-list/author-list.component';
import { SubjectFormComponent } from './features/subjects/subject-form/subject-form.component';
import { SubjectListComponent } from './features/subjects/subject-list/subject-list.component';
import { ReportBooksByAuthorComponent } from './features/report/report-books-by-author/report-books-by-author.component';


export const routes: Routes = [
	// Redireciona raiz para lista de livros
	{ path: '', redirectTo: 'books', pathMatch: 'full' },

	// LIVROS
	{ path: 'books', component: BookListComponent },
	{ path: 'books/new', component: BookFormComponent },
	{ path: 'books/:id/edit', component: BookFormComponent },

	// AUTORES
	{ path: 'authors', component: AuthorListComponent },
	{ path: 'authors/new', component: AuthorFormComponent },
	{ path: 'authors/:id/edit', component: AuthorFormComponent },

	// ASSUNTOS
	{ path: 'subjects', component: SubjectListComponent },
	{ path: 'subjects/new', component: SubjectFormComponent },
	{ path: 'subjects/:id/edit', component: SubjectFormComponent },

	// RELATÓRIO (Jasper)
	{ path: 'reports/books-by-author', component: ReportBooksByAuthorComponent },

	// Qualquer rota desconhecida volta pra lista de livros
	{ path: '**', redirectTo: 'books' },
];
