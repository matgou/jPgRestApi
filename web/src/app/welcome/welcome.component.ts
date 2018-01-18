import { Component, OnInit } from '@angular/core';
import { TablesService } from '../tables.service';
import { Table } from '../table';

@Component({
  selector: 'app-welcome',
  templateUrl: './welcome.component.html',
  styleUrls: ['./welcome.component.css']
})
export class WelcomeComponent implements OnInit {
  tables: Table[];
  tablesService: TablesService;
    
  constructor(tablesService: TablesService) {
      this.tablesService = tablesService;
  }

  ngOnInit() {
      this.tablesService.getTatbles().subscribe(res => {
          this.tables = res.json();
          });
  }

}
