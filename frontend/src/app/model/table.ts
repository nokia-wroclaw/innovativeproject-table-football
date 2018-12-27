export interface Table {
    id: string;
    occupied: boolean;
    online: boolean;
    lastNotificationDate: Date;
    floor: number;
    room: number;
    visible?: boolean;
}
