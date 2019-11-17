import http.client
import json
import random
import ssl
import threading
import os.path
from base64 import b64encode
import argparse
import requests
import time

HOST = 'https://localhost'
PORT = 18443
ENDPOINT = 'sensor'
MAC_ADDRESS = '00:00:00:00:00:00'
USERNAME = 'sensor'
PASSWORD = 'sensor'


def set_axis(axis):
    if axis in ('x', 'y', 'z'):
        parameters['axis'] = axis
        return
    print(f"ERROR: {axis} is not valid axis")


def set_values_range(lower, upper):
    parameters['values_range'] = (lower, upper)


def set_buffer_size(buffer_size):
    parameters['buffer_size'] = buffer_size


def set_delay_s(delay):
    parameters['delay_s'] = delay


def print_help(command=None):
    print("This is helpful")


def _build_message():
    message = {}
    buffer_size = parameters['buffer_size']
    for axis in ('x', 'y', 'z'):
        if axis == parameters['axis']:
            axes_buffer = [random.random() for _ in range(buffer_size)]
        else:
            lower, upper = parameters['values_range']
            axes_buffer = [random.uniform(lower, upper) for _ in range(buffer_size)]
        message[axis] = axes_buffer
    message['id'] = MAC_ADDRESS
    message['vcc'] = parameters['vcc']
    return message


def send_buffer(client_key, client_cert):
    time.sleep(parameters['delay_s'])
    while True:
        msg = _build_message()
        response = requests.post(f'{HOST}:{PORT}/{ENDPOINT}/', cert=(client_cert, client_key), auth=(USERNAME, PASSWORD),
                                 json=msg, verify=False)
        if response.status_code != 200:
            print(f"Response failed with code {response.status_code} and message {response.content}")
        else:
            print("request sent")

        time.sleep(parameters['delay_s'])


parameters = {
    'axis': 'x',
    'values_range': (0.0, 1.0),
    'buffer_size': 32,
    'delay_s': 30,
    'vcc': 540

}

COMMANDS_HANDLERS = {
    'axis': set_axis,
    'range': set_values_range,
    'buffer': set_buffer_size,
    'delay': set_delay_s,
    'help': print_help,
}

parser = argparse.ArgumentParser()
parser.add_argument('host')
parser.add_argument('port', type=int)
parser.add_argument('username')
parser.add_argument('password')

if __name__ == '__main__':
    args = parser.parse_args()
    HOST = args.host
    PORT = args.port
    USERNAME = args.username
    PASSWORD = args.password
    user_input = ''

    threading.Thread(target=send_buffer, args=(
        os.path.abspath('client_key.pem'),
        os.path.abspath('client_cert.pem')
    ), daemon=True).start()

    while True:
        user_input = input("Enter command: ")
        base, *flags = user_input.split()
        if base not in COMMANDS_HANDLERS:
            print(f'command {base} is not valid, use help for more information')
        else:
            handler = COMMANDS_HANDLERS[base]
            try:
                handler(*flags)
            except TypeError:
                print(f'Invalid arguments {flags} for command {base}')
