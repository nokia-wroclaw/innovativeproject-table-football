export class AlgorithmParameters {
    window_size: number;
    threshold: number;
    axis: string;
    peaks_count: number;
    max_readings: number;

    constructor() {
        this.window_size = 0;
        this.threshold = 0;
        this.peaks_count = 0;
        this.max_readings = 0;
        this.axis = 'X';
    }
}
