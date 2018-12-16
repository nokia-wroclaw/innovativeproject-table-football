import csv
from matplotlib import pyplot as plt
import numpy
import math


class ProcessingFunctions:
    #funkcje operujące na pojedyńczej próbce
    @staticmethod
    def abs_zet_threshold_exceeded(sample, threshold):
        return abs(sample.data[2]) >= threshold

    @staticmethod
    def abs_vector_length_threshold_exceeded(sample, threshold):
        return math.sqrt(sample.data[0] ** 2 + sample.data[1] ** 2 + sample.data[2] ** 2) >= threshold


class Reading:
    def __init__(self):
        self.timestamp_absolute = None
        self.timestamp = None
        self.data = []


class Buffer:
    size = 32

    def __init__(self):
        self.readings = []


class BufferAnalyzer:
    THRESHOLD = 0.2
    MIN_COUNT = 4

    def __init__(self, func):
        self.processingFunction = func

    def make_decision(self, _buffer):

        above_threshold_count = 0

        for sample in _buffer.readings:
            if self.processingFunction(sample, self.THRESHOLD):
                above_threshold_count += 1

        return above_threshold_count >= self.MIN_COUNT


with open('readings.csv') as readings_file:
    csv_reader = csv.reader(readings_file, delimiter=',')
    line_count = 0

    full_buffers = []
    all_readings = []
    buffer = Buffer()

    for row in csv_reader:
        reading = Reading()
        if line_count >= Buffer.size:
            line_count = 0
            full_buffers.append(buffer)
            buffer = Buffer()

        reading.timestamp_absolute = row[0]
        reading.timestamp = row[1]
        reading.data = [float(row[2]), float(row[3]), float(row[4])]

        all_readings.append(reading)

        line_count += 1

        buffer.readings.append(reading)

#W konstruktorze można zadać własną funkcję do przetwarzania próbek
buffer_analyzer = BufferAnalyzer(ProcessingFunctions.abs_zet_threshold_exceeded)

analyze_result = []

for buf in full_buffers:
    result = buffer_analyzer.make_decision(buf)
    analyze_result.append(result)

#linie pionowe wyznaczające grę
game_timestamps = [0, 325, 900, 1255, 1495, 1710]

time = numpy.asarray([sample.timestamp for sample in all_readings], dtype='uint16')
all_x = numpy.asfarray([sample.data[0] for sample in all_readings], dtype='float32')
all_y = numpy.asfarray([sample.data[1] for sample in all_readings], dtype='float32')
all_z = numpy.asfarray([sample.data[2] for sample in all_readings], dtype='float32')

data = numpy.fabs(all_z)
# data = numpy.asfarray([math.sqrt(val[0] ** 2 + val[1] ** 2 + val[2] ** 2) for val in zip(all_x, all_y, all_z)])

result_count = 0
color = 'r'
#Na zielono zaznacza miejsca uznane za gre, na czerwono - pozostałe
for result in analyze_result:
    lower_index = result_count * Buffer.size
    upper_index = (result_count + 1) * Buffer.size
    if result:
        color = 'g'
    else:
        color = 'r'

    plt.plot(time[lower_index: upper_index], data[lower_index: upper_index], color=color)
    result_count += 1

for stamp in game_timestamps:
    plt.axvline(x=stamp)

plt.show()
