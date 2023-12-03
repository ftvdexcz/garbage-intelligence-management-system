export interface Pagination<T> {
  totals: number;
  pages: number;
  page: number;
  size: number;
  results: T[];
}
