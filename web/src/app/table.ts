export class Table {
    table_schema: string;
    table_name: string;
    
    public toString() {
        return this.table_schema + '.' + this.table_name;
    }
}
