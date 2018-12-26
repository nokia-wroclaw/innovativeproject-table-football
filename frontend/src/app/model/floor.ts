import { Table } from './table';

export class Floor {
    floorNumber: number;
    tables: Array<Table>;
    visible?: boolean;

    constructor(floorNumber: number) {
        this.floorNumber = floorNumber;
        this.tables = new Array<Table>();
    }

    getVisibleTablesCount() {
        let count = 0;

        this.tables.forEach(element => {
          if (element.visible === true || element.visible === undefined) {
            count++;
          }
        });

        return count;
    }

    getFreeTablesCount() {
      let count = 0;

      this.tables.forEach(element => {
        if (element.occupied === false) {
          count++;
        }
      });

      return count;
    }
}
