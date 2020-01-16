#!/bin/bash

if [ ! -e "venv" ]; then
    virtualenv -p /usr/bin/python3 venv
    ./venv/bin/pip install -r ./python_code/web_server/requirements.txt
fi
