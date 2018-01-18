import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';
import { Table } from './table';

@Injectable()
export class TablesService {
  private queryUrl = '/api/query';

  constructor(private http: Http) { }

  getTatbles(): Observable<Response> {
    const body = {query: 'SELECT table_schema,table_name FROM information_schema.tables ORDER BY table_schema,table_name;'};
    return this.http.post(this.queryUrl, body);
  }
}
