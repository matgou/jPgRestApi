import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { DataService } from '../data.service';

@Component({
  selector: 'app-data-table',
  templateUrl: './data-table.component.html',
  styleUrls: ['./data-table.component.css']
})
export class DataTableComponent implements OnInit {
  private table_name:string
  private sub: any;
  data:any;
  dataHeaders:any;
    
  constructor(private route: ActivatedRoute, private dataService: DataService) { }

  ngOnInit() {
      this.sub = this.route.params.subscribe(params => {
       this.table_name = params['table_name'];
       this.dataService.getData(this.table_name).subscribe(res => {
          this.data = res.json();
          });
       
       this.dataService.getHeaders(this.table_name).subscribe(res => {
          this.dataHeaders = res.json();
          });
    });
  }

}
