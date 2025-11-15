import { Author } from './author.model';
import { Subject } from './subject.model';

export interface Book {
	id: number;
	title: string;
	publicationYear: number | null;
	price: number;
	subject: Subject;
	authors: Author[];
}
