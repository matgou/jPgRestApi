import { Injectable } from '@angular/core';
import { Http, Headers, Response } from '@angular/http';
import { Observable } from 'rxjs/Observable';

@Injectable()
export class DataService {
  private queryUrl = '/api/data';
  private tablesUrl = '/api/tables';

  constructor(private http: Http) { }

  getHeaders(table_name: string): Observable<Response> {
    return this.http.get(this.tablesUrl + "/" + table_name);
  }
    
  getData(table_name: string): Observable<Response> {
    return this.http.get(this.queryUrl + "/" + table_name);
  }
}
