import { Table } from './table';

export class Floor {
    floorNumber: number;
    tables: Array<Table>;
    visible?: boolean;

    constructor(floorNumber: number) {
        this.floorNumber = floorNumber;
        this.tables = new Array<Table>();
    }
}
