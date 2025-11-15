export interface BookRequest {
	title: string;
	publicationYear: number | null;
	price: number;
	subjectId: number;
	authorIds: number[];
}
